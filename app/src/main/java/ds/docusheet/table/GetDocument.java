package ds.docusheet.table;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.javascript.Scriptable;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import ds.docusheet.table.database.ColumnDetailsEntity;
import ds.docusheet.table.database.DatabaseClass;
import ds.docusheet.table.database.SavedDataEntity;

import static android.view.View.GONE;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;


public class GetDocument extends AppCompatActivity implements TextWatcher,DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {
    private static final String TAG = "GetDocument";
    static List<cell> cells,cells2,cells3,cells4;
    List<cell> combined_List;
    final int[] row_idabovebelow = new int[2];
    public static int[] arr_column_id;
    public static int[] total_col;
    public static String[] formula;
    protected  int colIndex=1;
    static int rows=2,column=5;
    int col_num,setting=0,mHour,mMinute,h=-1,m=-1,pos=0,datacell_num=0,column_id=0,posii=-1;
    Button rowbut,colbut;
    ImageView savebut;
    String t_row;
    EditText et;
    String id,doc_name,format,update="n";
    RecyclerView recyclerView,recyclerView4;
    RelativeLayout relativeLayout;
    Adapter_row_dataCombined adapter;
    AdapterTotal adapter4;
    ArrayList<dataCell> dataCellArrayList;
    protected static List<cellColumn> cellColumnArrayList;
    ProgressDialog progressDialog;
    GetDocument instance;
    static List<cell> cells5=new ArrayList<>();
    cell cells_temp;
    Toolbar toolbar2,toolbar1;
    Calendar now = Calendar.getInstance();
    TimePickerDialog tpd;
    DatePickerDialog dpd;


    DatabaseClass databaseData,databaseColumns;

    List<SavedDataEntity> listData=new ArrayList<>();
    List<ColumnDetailsEntity> listColumn=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_document);
        instance = this;
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(this,"0b359b9b0d40bb01e6381e29c23f1310");
        mixpanel.track("On Resume",null);
        mixpanel.flush();

        id = getIntent().getStringExtra("doc_id");
        doc_name = getIntent().getStringExtra("doc_name");
        col_num = getIntent().getIntExtra("col_num", -1);
        setting = getIntent().getIntExtra("setting", 0);
        update = getIntent().getStringExtra("update_document");


        toolbar2=findViewById(R.id.toolbar2);
        toolbar1=findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar1);

        toolbar1.setNavigationOnClickListener(view -> {
            finish();
            onBackPressed();
        });
        Objects.requireNonNull(getSupportActionBar()).setTitle(doc_name);

        rowbut = findViewById(R.id.rowbut);
        colbut = findViewById(R.id.colbut);
        savebut = findViewById(R.id.savebut);
        recyclerView = findViewById(R.id.recycle1);
        recyclerView4 = findViewById(R.id.recycler4);

        et = findViewById(R.id.edittext5);
        relativeLayout = findViewById(R.id.vislay);
        cells = new ArrayList<>();
        cells2 = new ArrayList<>();
        cells3 = new ArrayList<>();
        cells4 = new ArrayList<>();
        arr_column_id=new int[20];
        total_col=new int[20];
        formula=new String[20];
        for(int i=0;i<20;i++) {
            formula[i]="0";
        }

        now=Calendar.getInstance();
        dpd = new DatePickerDialog(this, this,now.get(YEAR),now.get(MONTH),now.get(DAY_OF_MONTH));

        tpd= new TimePickerDialog(this, this,now.get(Calendar.HOUR_OF_DAY),now.get(Calendar.MINUTE),false);


        if(update!=null) {
            String No="no";
            if (No.equals(update)) {
                Log.d("babbar","update");
                check();
            }
        }

        databaseColumns = DatabaseClass.getDatabaseColumns(this);
        databaseData = DatabaseClass.getDatabaseData(this);
        extract();
        adapter4 = new AdapterTotal(getApplicationContext(),cells4);

        rowbut.setOnClickListener(view -> {
            final int[] row_id = new int[3];

            String rowurl="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/add-three-row/"+id;
            RequestQueue queue=Volley.newRequestQueue(getApplicationContext());
            JSONObject jsonObject=new JSONObject();
            try {
                JSONArray jsonArray1=new JSONArray();
                for(int i=1;i<=column;i++){
                    jsonArray1.put("");
                }
                jsonObject.put("data",jsonArray1);
                JSONArray jsonArray2=new JSONArray();
                for(int i=1;i<=column;i++){
                    jsonArray2.put("10");
                }
                jsonObject.put("width",jsonArray2);
                JSONArray jsonArray3=new JSONArray();
                for(int i=1;i<=column;i++){
                    jsonArray3.put("10");
                }
                jsonObject.put("height",jsonArray3);
                jsonObject.put("cols_num",column);
                jsonObject.put("rows_num",rows);
                jsonObject.put("current_row_number",rows+1);

                JsonObjectRequest objectRequest=new JsonObjectRequest(Request.Method.POST, rowurl, jsonObject, response -> {
                    try {
                        JSONArray jsonArray=response.getJSONArray("row_id");
                        row_id[0] =Integer.parseInt(jsonArray.getString(2));
                        row_id[1]= Integer.parseInt(jsonArray.getString(1));
                        row_id[2]= Integer.parseInt(jsonArray.getString(0));

                        int temp_row_no=rows;
                        for(int i=0;i<3;i++){
                            SavedDataEntity sd=new SavedDataEntity();
                            sd.setId(row_id[i]);
                            for(int j=1;j<=20;j++){
                                sd.setColumn("",j);
                            }
                            sd.setDoc_id(id);
                            temp_row_no++;
                            sd.setSerial_no(String.valueOf(temp_row_no));
                            databaseData.getSavedDataDao().insert(sd);
                        }

                        dataCellArrayList.add(new dataCell(row_id[0]));
                        dataCellArrayList.get(rows).setSerialno(rows+1);
                        for(int i=1;i<=(column);i++){
                            cells.add(new cell(""));
                        }
                        cells3.add(new cell(""+(rows+1)));
                        rows++;
                        dataCellArrayList.add(new dataCell(row_id[1]));
                        dataCellArrayList.get(rows).setSerialno(rows+1);
                        for(int i=1;i<=(column);i++){
                            cells.add(new cell(""));
                        }
                        cells3.add(new cell(""+(rows+1)));
                        rows++;
                        dataCellArrayList.add(new dataCell(row_id[2]));
                        dataCellArrayList.get(rows).setSerialno(rows+1);
                        for(int i=1;i<=(column);i++){
                            cells.add(new cell(""));
                        }
                        cells3.add(new cell(""+(rows+1)));
                        rows++;
                        forming_adapter();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("exception row",e.toString());
                    }
                }, error -> Log.e("error a gya",error.toString()));

                queue.add(objectRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

        colbut.setOnClickListener(view -> {
            if(column>=20){
                Toast.makeText(GetDocument.this, "Cannot exceed more than 20", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                String url="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/add-column/"+id;
                RequestQueue queue=Volley.newRequestQueue(getApplicationContext());
                JSONObject object=new JSONObject();
                object.put("row_nums",String.valueOf(rows));
                object.put("column_nums",String.valueOf(column));
                object.put("column_type","text");
                object.put("column_names","Column "+(column+1));

                listData = databaseData.getSavedDataDao().getDocumentSerial(Integer.parseInt(id));
                for(int i=0;i<listData.size();i++){
                    SavedDataEntity sde = new SavedDataEntity();
                    for(int j=1;j<=20;j++){
                        sde.setColumn(listData.get(i).getColumn(j),j);
                    }
                    sde.setColumn("",column+1);
                    databaseData.getSavedDataDao().updateSinleData(sde.getColumn1(),sde.getColumn2(),sde.getColumn3(),sde.getColumn4(),sde.getColumn5(),
                            sde.getColumn6(),sde.getColumn7(),sde.getColumn8(),sde.getColumn9(),sde.getColumn10(),sde.getColumn11(),sde.getColumn12(),
                            sde.getColumn13(),sde.getColumn14(),sde.getColumn15(),sde.getColumn16(),sde.getColumn17(),sde.getColumn18(),sde.getColumn19(),
                            sde.getColumn20(),listData.get(i).getHeight(),listData.get(i).getWidth(),listData.get(i).getId());
                }

                JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, url, object, response -> {
                    try {

                        int col_id=Integer.parseInt(response.getString("column_id"));
                        cellColumnArrayList.add(new cellColumn(col_id,"Column "+(column+1),"text",colIndex++));
                        ColumnDetailsEntity cellcol = new ColumnDetailsEntity(col_id,
                                "Column "+(column+1),
                                String.valueOf(column+1),
                                "text",
                                id,
                                "0",
                                "0");

                        databaseColumns.getColumnDetails().insert(cellcol);
                        List<ColumnDetailsEntity> list= databaseColumns.getColumnDetails().getAllDetails(Integer.parseInt(id));
                        for(int i=0;i<list.size();i++){
                            Log.d("Keshav",list.get(i).getColumn_names()+" "+list.get(i).getColumn_nums()
                            +" "+list.get(i).getCol_id()+" "+list.get(i).getFormula());
                        }
                        List<cell> temp=new ArrayList<>();
                        for(int j=0;j<rows*column;j++){
                            temp.add(new cell(cells.get(j).getData()));
                        }
                        cells.clear();

                        for(int i=0;i<column;i++)cells.add(new cell(temp.get(i).getData()));
                        for(int i=column,k=column,j=column;i<rows*(column+1);i++){
                            if(k==(column)){
                                cells.add(new cell(""));
                                k=0;
                            }
                            else {
                                if(j<rows*column) cells.add(new cell(temp.get(j).getData()));
                                j++;
                                k++;
                            }
                        }
                        column=column+1;
                        cells2.add(new cell("Column "+column));
                        forming_adapter();
                        if(recyclerView4.getVisibility()==View.VISIBLE){
                            cells4.add(new cell(""));
                            recyclerView4.setLayoutManager(new GridLayoutManager(getApplicationContext(),column));
                            adapter4.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {

                });
                queue.add(request);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

    }
    public void check(){
        String url = "http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/update-document-serial/" + id;
        RequestQueue Queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, null, response -> {

        }, error -> {

        });
        Queue.add(jsonObjectRequest);
    }
    public void click(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        adapter.setOnItemClickListenerCell((position, v) -> {
            MenuItem menuItem = toolbar1.getMenu().findItem(R.id.autoSaving);
            menuItem.setVisible(true);
            toolbar1.setVisibility(View.VISIBLE);
            toolbar2.setVisibility(GONE);
            setSupportActionBar(toolbar1);
            toolbar1.setNavigationOnClickListener(view -> {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                finish();
                onBackPressed();
            });
            posii=-1;
            invalidateOptionsMenu();
            ConstraintLayout constraintLayout;
            constraintLayout = findViewById(R.id.layout);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(R.id.nest,ConstraintSet.TOP,R.id.toolbar1,ConstraintSet.BOTTOM,65);
            constraintSet.applyTo(constraintLayout);
            Objects.requireNonNull(getSupportActionBar()).setTitle(doc_name);
            cells_temp = combined_List.get(position);
            datacell_num = (position/(column+1))-1;
            column_id = position%(column+1);
            Log.d("Keshav", datacell_num +" "+position + " " + column+" "+column_id);

            pos=position;

            switch (cellColumnArrayList.get(column_id).getColumn_type()) {
                case "text":
                    relativeLayout.setVisibility(View.VISIBLE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    et.requestFocus();
                    et.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                    et.addTextChangedListener(GetDocument.this);
                    et.setText(cells_temp.getData());
                    et.setSelection(et.getText().length());
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                    savebut.setOnClickListener(view -> {
                        relativeLayout.setVisibility(GONE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    });
                    break;
                case "Reminder":

                    if (cells.get(position).getData().trim().equals("")) {
                        dpd.show();
                    } else {
                        AlertDialog dialog;
                        AlertDialog.Builder builder = new AlertDialog.Builder(GetDocument.this);
                        builder.setTitle("Reminder Settings\n\n");
                        builder.setPositiveButton("Edit Reminder", (dialog1, which) -> dpd.show());
                        builder.setNegativeButton("Delete Reminder", (dialog12, which) -> {
                            cells.get(position).setData("");
                            forming_adapter();
                            postRequest(datacell_num);
                        });

                        dialog = builder.create();
                        dialog.setCancelable(false);
                        dialog.show();
                        dialog.setIcon(R.drawable.alarm);
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GREEN);
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
                    }
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    et.requestFocus();
                    break;
                case "number":
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    relativeLayout.setVisibility(View.VISIBLE);
                    et.requestFocus();
                    et.setText(cells_temp.getData());
                    et.setSelection(et.getText().length());
                    et.addTextChangedListener(GetDocument.this);
                    et.setInputType(InputType.TYPE_CLASS_NUMBER);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    savebut.setOnClickListener(view -> {
                        relativeLayout.setVisibility(GONE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    });
                    break;
                case "time":
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    h = -1;
                    m = -1;
                    Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    format = "AM";
                    TimePickerDialog timePickerDialog = new TimePickerDialog(GetDocument.this, (timePicker, i, i1) -> {
                        Log.e("Time", i + " " + i1);

                        if (i == 0) i += 12;
                        else if (i == 12) format = "PM";
                        else if (i > 12) {
                            i -= 12;
                            format = "PM";
                        }
                        h = i;
                        m = i1;

                    }, mHour, mMinute, false);
                    timePickerDialog.setTitle("Set Time");
                    timePickerDialog.show();
                    if (h == -1 && m == -1) {
                        relativeLayout.setVisibility(View.VISIBLE);
                        et.requestFocus();
                        et.setText(cells_temp.getData());
                        et.setSelection(et.getText().length());
                        et.addTextChangedListener(GetDocument.this);
                        et.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_TIME);
                        savebut.setOnClickListener(view -> {
                            relativeLayout.setVisibility(GONE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        });
                    } else {
                        if (m / 10 == 0) cells_temp.setData(h + ":0" + m + " " + format);
                        else cells_temp.setData(h + ":" + m + " " + format);
                        postRequest(datacell_num);
                        adapter.notifyItemChanged(position);
                    }
                    break;
                case "date":
                    relativeLayout.setVisibility(GONE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    Calendar calendar = Calendar.getInstance();
                    int mYear = calendar.get(YEAR);
                    int mMonth = calendar.get(MONTH);
                    int mDate = calendar.get(DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(GetDocument.this, (datePicker, i, i1, i2) -> {
                        cells_temp.setData(i2 + "/" + (i1 + 1) + "/" + i);
                        postRequest(datacell_num);
                        adapter.notifyItemChanged(position);
                    }, mYear, mMonth, mDate);
                    datePickerDialog.show();
                    break;
                case "rupees":
                    relativeLayout.setVisibility(View.VISIBLE);
                    et.requestFocus();
                    et.setText(cells_temp.getData());
                    et.setSelection(et.getText().length());
                    et.addTextChangedListener(GetDocument.this);
                    et.setInputType(InputType.TYPE_CLASS_NUMBER);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    savebut.setOnClickListener(view -> {
                        relativeLayout.setVisibility(GONE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    });
                    break;
                case "phone":
                    relativeLayout.setVisibility(View.VISIBLE);
                    et.requestFocus();

                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);

                    startActivityForResult(intent, 1);
                    et.setText(cells_temp.getData());
                    et.setSelection(et.getText().length());
                    et.setInputType(InputType.TYPE_CLASS_PHONE);
                    et.addTextChangedListener(GetDocument.this);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    savebut.setOnClickListener(view -> {
                        relativeLayout.setVisibility(GONE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        forming_adapter();
                    });
                    break;
                case "toggle":
                case "checkbox":
                    Toast.makeText(GetDocument.this, "This feature will be added soon", Toast.LENGTH_SHORT).show();
                    break;
            }
        });

        adapter.setOnItemClickListener((position, view) -> {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            Intent intent=new Intent(GetDocument.this, columnClicked.class);
            intent.putExtra("col_id",cellColumnArrayList.get(position).getCol_id());
            intent.putExtra("col_name",cellColumnArrayList.get(position).getColumn_name());
            intent.putExtra("col_type",cellColumnArrayList.get(position).getColumn_type());
            intent.putExtra("column_num",String.valueOf(position));
            intent.putExtra("total_col",String.valueOf(column));
            intent.putExtra("doc_id",id);
            intent.putExtra("rows",t_row);
            intent.putExtra("doc_name",doc_name);
            startActivity(intent);
        });
        adapter.setOnLongItemClick((position, v) -> {
            posii=position;
            datacell_num=(position/(column+1))-1;
            toolbar1.setVisibility(GONE);
            toolbar2.setVisibility(View.VISIBLE);
            setSupportActionBar(toolbar2);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            ConstraintLayout constraintLayout;
            constraintLayout=findViewById(R.id.layout);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(R.id.nest,ConstraintSet.TOP,R.id.toolbar2,ConstraintSet.BOTTOM,65);
            constraintSet.applyTo(constraintLayout);
           getSupportActionBar().setTitle("");
           invalidateOptionsMenu();

            relativeLayout.setVisibility(GONE);
            toolbar2.setNavigationOnClickListener(view -> {
                toolbar1.setVisibility(View.VISIBLE);
                toolbar2.setVisibility(GONE);
                setSupportActionBar(toolbar1);
                toolbar1.setNavigationOnClickListener(view1 -> {
                    finish();
                    onBackPressed();
                });
                posii=-1;
                invalidateOptionsMenu();
                ConstraintLayout constraintLayout1;
                constraintLayout1 =findViewById(R.id.layout);
                ConstraintSet constraintSet1 = new ConstraintSet();
                constraintSet1.clone(constraintLayout1);
                constraintSet1.connect(R.id.nest,ConstraintSet.TOP,R.id.toolbar1,ConstraintSet.BOTTOM,65);
                constraintSet1.applyTo(constraintLayout1);
                getSupportActionBar().setTitle(doc_name);
            });
        });

    }
    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(posii!=-1){
            getMenuInflater().inflate(R.menu.cell_props, menu);

            if(menu instanceof MenuBuilder){
                MenuBuilder m = (MenuBuilder) menu;
                m.setOptionalIconsVisible(true);
            }
            return true;
        }
        else{
            getMenuInflater().inflate(R.menu.get_document_menu,menu);
        }
        return true;
    }
    public void makePdf(MenuItem Item) {
        Log.d(TAG, "makePdf: " + "Called");
        long queueid = 0;
        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        int check = 1;
        LoadingBar loadingBar = new LoadingBar(GetDocument.this);
        int finalCheck = check;
        DownloadManager finalDm = dm;
        long finalQueueid = queueid;
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action=intent.getAction();
                if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)){

                    DownloadManager.Query query=new DownloadManager.Query();
                    query.setFilterById(finalQueueid);
                    Cursor c = finalDm.query(query);
                    if(c.moveToFirst()){
                        int col=c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if(DownloadManager.STATUS_SUCCESSFUL==c.getInt(col)) {

                            if (finalCheck == 1) {
                                try {
                                    if (Build.VERSION.SDK_INT >= 24) {
                                        try {
                                            Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                                            m.invoke(null);
                                        } catch (Exception e) {
                                        }
                                    }
                                    try {
                                        String filename = doc_name + id + ".pdf";
                                        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + filename);
                                        Intent target = new Intent(Intent.ACTION_VIEW);
                                        Uri fileUri = null;
                                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
                                            fileUri = Uri.fromFile(folder);
                                        }
                                        else{
                                            fileUri = FileProvider.getUriForFile(GetDocument.this,BuildConfig.APPLICATION_ID + ".provider",folder);
                                        }
                                        target.setDataAndType(fileUri, "application/pdf");
                                        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        Intent i = Intent.createChooser(target, "Open File");
                                        try {
                                            startActivity(i);
                                        } catch (Exception e) {
                                        }
                                        Log.d(TAG, "onReceive: " + "#1");
                                        loadingBar.dismissDialog();
                                        Log.d(TAG, "onReceive: " + "#2");
                                        Toast.makeText(GetDocument.this,"Download Complete",Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "onReceive: " + "#3");
                                    } catch (Exception e) {
                                        Log.d("pdf3", e.toString());
                                    }
                                }
                                catch (Exception e)
                                {
                                    Log.d("pdf3",e.toString());
                                }
                            }
                            else if(finalCheck ==2)
                            {
                                try {
                                    if (Build.VERSION.SDK_INT >= 24) {
                                        try {
                                            Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                                            m.invoke(null);
                                        } catch (Exception e) {
                                        }
                                    }
                                    try {
                                        String filename = doc_name + id + ".pdf";
                                        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + filename);
                                        //Intent target=new Intent(Intent.ACTION_VIEW);
                                        //target.setDataAndType(Uri.fromFile(folder),"application/pdf");
                                        // target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY| Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        // Intent intent=Intent.createChooser(target,"Open File");
                                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                        Uri screenshotUri = Uri.fromFile(folder);
                                        sharingIntent.setType("application/pdf");
                                        //screenshotUri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", folder);
                                        sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                                        try {
                                            //startActivity(intent);
                                            loadingBar.dismissDialog();
                                            startActivity(sharingIntent);
                                        } catch (Exception e) {
                                            Log.d("pdf4",e.toString());
                                        }
                                    }
                                    catch (Exception e)
                                    {
                                        Log.d("pdf3",e.toString());
                                    }
                                }
                                catch (Exception e)
                                {
                                    Log.d("pdf2",e.toString());
                                }
                            }
                        }
                    }
                }
            }

        };

        try {
            if (isStoragePermissionGranted()) {
                loadingBar.startLoading();
                String directory_path = Environment.getExternalStorageDirectory().getPath() + "/Download/" + doc_name + id + ".pdf";
                File file = new File(directory_path);
                if (file.exists()) {
                    file.delete();
                }
                check = 1;
                dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/create-pdf/" + id));
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI)
                        .setAllowedOverRoaming(false).setTitle("").setDescription("").setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, doc_name + id + ".pdf");
                queueid = dm.enqueue(request);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String filename = doc_name + id + ".pdf";
                            File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + filename);
                            Intent target = new Intent(Intent.ACTION_VIEW);
                            Uri fileUri = null;
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
                                fileUri = Uri.fromFile(folder);
                            }
                            else{
                                fileUri = FileProvider.getUriForFile(GetDocument.this,BuildConfig.APPLICATION_ID + ".provider",folder);
                            }
                            target.setDataAndType(fileUri, "application/pdf");
                            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            Intent i = Intent.createChooser(target, "Open File");
                            try {
                                Toast.makeText(GetDocument.this,"Download complete",Toast.LENGTH_LONG).show();
                                startActivity(i);
                            } catch (Exception e) {
                                Toast.makeText(GetDocument.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                            loadingBar.dismissDialog();
                        } catch (Exception e) {
                            Log.d("pdf3", e.toString());
                        }
                    }
                },3000);

            } else {

            }
        }
        catch (Exception e)
        {
            Log.d("not downloaded",e.toString());
        }
    }
    public boolean isStoragePermissionGranted() {
        String Tag = "Permission Granted";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v(Tag, "Permission is granted");
                return true;
            } else {
                Log.v(Tag, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            Log.v(Tag, "Permission is granted");
            return true;
        }
    }
    public String getTotal(){
        if(cells4.size()==0) {
            for (int i = 1; i <= column; i++) {
                cells4.add(new cell(""));
            }
        }

        // babbar debug


        // babbar debug end
        //Toast.makeText(GetDocument.this," "+cells5.size(),Toast.LENGTH_SHORT).show();
        for (int j = 0; j < cells5.size(); j++) {
            int col_num = Integer.parseInt(cells5.get(j).getData());
            if(Integer.valueOf(id)==cells5.get(j).doc_id) {
                //babbar
                //Toast.makeText(this, " "+col_num, Toast.LENGTH_SHORT).show();

                int can = 0;
                Double total = 0.0;

                String data = null;
                for (int i = col_num; i < rows * column; i += column) {
                    data = cells.get(i).getData().trim();
                    try {
                        if (data.equals("")) continue;
                        else if (data.startsWith("\u20B9")) {
                            if(data.length()>2)
                                total += Double.parseDouble(data.substring(2));
                            //total += Double.parseDouble(data.substring(2));
                        } else total += Double.parseDouble(data);
                    } catch (NumberFormatException e) {
                        can = -1;
                        break;
                    }
                }
                if (can == -1) {
                    Toast.makeText(this, " Cannot be converted as numbers", Toast.LENGTH_SHORT).show();
                    break;
                }
//                    else
//                        return String.valueOf(total);
                else {
                    if (cellColumnArrayList.get(col_num).getColumn_type().equals("rupees"))
                        cells4.get(col_num).setData("\u20B9 " + String.valueOf(total));
                    else cells4.get(col_num).setData(String.valueOf(total));
                    //adapter4.notifyItemChanged(col_num);
                }
            }
        }

        adapter4 = new AdapterTotal(getApplicationContext(), cells4);
        recyclerView4.setLayoutManager(new GridLayoutManager(getApplicationContext(), column+1));
        recyclerView4.setVisibility(View.VISIBLE);
        recyclerView4.setAdapter(adapter4);
        ViewCompat.setNestedScrollingEnabled(recyclerView4, false);
        //(can==0) return String.valueOf(total);
        return "";
    }
    public void postRequest(int datacell_num){
        String url="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/update-single-data/"+id;
        try {

            dataCell tempdatacell = dataCellArrayList.get(datacell_num);
            Log.d("babbar size1",String.valueOf(dataCellArrayList.size()));

            int row_id=tempdatacell.getRow_id();
            String row=String.valueOf(row_id);

            RequestQueue queue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("rows_nums", rows);
            jsonBody.put("cols_nums", column);
            jsonBody.put("id",row);
            jsonBody.put("serialno",tempdatacell.getSerialno());
            JSONArray jsonArray = new JSONArray();
            for (int i = (datacell_num)*column; i < ((datacell_num)*column)+column; i++) {
                String string = cells.get(i).getData();
                StringBuilder temp = new StringBuilder();
                for (int j = 0; j < string.length(); j++) {
                    if (string.charAt(j) != '\u20B9'){
                        temp.append(string.charAt(j));
                    }
                }
                jsonArray.put(temp.toString());
            }
            jsonBody.put("data",jsonArray);
            JSONArray json1 = new JSONArray();
            for(int i=1;i<=column;i++){
                json1.put("10");
            }
            jsonBody.put("width",json1);
            JSONArray json2 = new JSONArray();
            for(int i=1;i<=column;i++){
                json2.put("10");
            }
            jsonBody.put("height",json2);
            String[][] rcdata =new String[40][20];

            int col=column;
            long id=Long.parseLong(row);
            int x=0;
            int z;
            for(z=0;z<col;z++)
            {
                rcdata[0][z]=jsonArray.getString(x);
                x++;
            }

            x=0;
            SavedDataEntity data1=new SavedDataEntity();

            data1.setColumn1(rcdata[0][0]);
            data1.setColumn2(rcdata[0][1]);
            data1.setColumn3(rcdata[0][2]);
            data1.setColumn4(rcdata[0][3]);
            data1.setColumn5(rcdata[0][4]);
            data1.setColumn6(rcdata[0][5]);
            data1.setColumn7(rcdata[0][6]);
            data1.setColumn8(rcdata[0][7]);
            data1.setColumn9(rcdata[0][8]);
            data1.setColumn10(rcdata[0][9]);
            data1.setColumn11(rcdata[0][10]);
            data1.setColumn12(rcdata[0][11]);
            data1.setColumn13(rcdata[0][12]);
            data1.setColumn14(rcdata[0][13]);
            data1.setColumn15(rcdata[0][14]);
            data1.setColumn16(rcdata[0][15]);
            data1.setColumn17(rcdata[0][16]);
            data1.setColumn18(rcdata[0][17]);
            data1.setColumn19(rcdata[0][18]);
            data1.setColumn20(rcdata[0][19]);
            data1.setId(Long.parseLong(row));
            data1.setDoc_id(String.valueOf(id));
            data1.setHeight(json1.getString(x));
            data1.setWidth(json2.getString(x));
            data1.setSerial_no(String.valueOf(tempdatacell.getSerialno()));
            databaseData.getSavedDataDao().updateSinleData(data1.getColumn1(),data1.getColumn2(),data1.getColumn3(),data1.getColumn4(),data1.getColumn5(),
                    data1.getColumn6(),data1.getColumn7(),data1.getColumn8(),data1.getColumn9(),data1.getColumn10(),data1.getColumn11(),data1.getColumn12(),
                    data1.getColumn13(),data1.getColumn14(),data1.getColumn15(),data1.getColumn16(),data1.getColumn17(),data1.getColumn18(),data1.getColumn19(),
                    data1.getColumn20(),data1.getHeight(),data1.getWidth(),data1.getId());

            JsonObjectRequest request=new JsonObjectRequest(Request.Method.PUT, url,jsonBody, response -> {

            }, error -> {
                Log.d(TAG, "onErrorResponse: " + error.getMessage());
                if (error.getMessage().contains("true")) {
                    MenuItem menuItem = toolbar1.getMenu().findItem(R.id.autoSaving);
                    new Handler().postDelayed(() -> menuItem.setIcon(R.drawable.ic_baseline_cloud_done_saved_24), 4000);
                }
            });
            queue.add(request);

            try {
                for (int i = 1; i <= column; i++) {

                    if (!formula[i - 1].equals("0")) {
                        for (int j = 1; j <= rows; j++) {
                            String evaluate = "";
                            for (int k = 0; k < formula[i - 1].length(); k++) {
                                if (formula[i - 1].charAt(k) == '<') {
                                    k++;
                                    StringBuilder tcol = new StringBuilder();
                                    while (formula[i - 1].charAt(k) != '>') {
                                        tcol.append(formula[i - 1].charAt(k));
                                        k++;
                                    }
                                    int ecol = Integer.parseInt(tcol.toString());
                                    Log.d("col number", tcol.toString());

                                    //(column*(j-1))+ecol-1

                                     if(!cells.get((column*(j-1))+ecol-1).getData().trim().equals(""))
                                     {
                                         if(cellColumnArrayList.get(i-1).getColumn_type().equals("rupees")&&cells.get((column * (j - 1)) + ecol - 1).getData().length()>2)
                                             evaluate = evaluate + cells.get((column * (j - 1)) + ecol - 1).getData().substring(1);
                                         else
                                             evaluate = evaluate + cells.get((column * (j - 1)) + ecol - 1).getData();
                                     }
                                    else
                                        evaluate=evaluate+"0";
                                    //continue;
                                } else
                                    evaluate = evaluate + formula[i - 1].charAt(k);
                            }

                            Log.d("evaluate ", evaluate);
                            org.mozilla.javascript.Context rhino = org.mozilla.javascript.Context.enter();
                            rhino.setLanguageVersion(org.mozilla.javascript.Context.VERSION_1_2);
                            rhino.setOptimizationLevel(-1);
                            Scriptable scope = rhino.initStandardObjects();

                            Object result = rhino.evaluateString(scope, evaluate, "test", 1, null);
                            if(cellColumnArrayList.get(i-1).column_type.equals("rupees"))
                                cells.get(column * (j - 1) + i - 1).setData("\u20B9"+result.toString());
                            else
                            cells.get(column * (j - 1) + i - 1).setData(result.toString());
                            //adapter.notifyDataSetChanged();
                            forming_adapter();
                        }
                    }
                }
            }
            catch (Exception e)
            {
                Log.d("add col exception",e.toString());
            }

            // formula deb end


            if(recyclerView4.getVisibility()==View.VISIBLE){
                for (int j = 0; j < cells5.size(); j++) {

                    int col_num = Integer.parseInt(cells5.get(j).getData());

                    //Toast.makeText(this, " "+col_num, Toast.LENGTH_SHORT).show();
                    double total = 0.0;
                    int can = 0;
                    String data = null;
                    for (int i = col_num; i < rows * column; i += column) {
                        data = cells.get(i).getData().trim();
                        try {
                            if (data.equals("")) continue;
                            else if (data.startsWith("\u20B9")) {
                                if(data.length()>2)
                                    total += Double.parseDouble(data.substring(2));

                            } else total += Double.parseDouble(data);
                        } catch (NumberFormatException e) {
                            can = -1;
                            int cnt=0;
                            for(int k=0;k<data.length();k++)
                                if(data.charAt(k)==' ');
                            cnt++;
                            Log.d("babbar number",String.valueOf(data)+" "+cnt);

                        }
                    }
                    if (can == -1) {
                        Toast.makeText(this, " Cannot be converted as numbers", Toast.LENGTH_SHORT).show();
                        break;
                    } else {
                        if (cellColumnArrayList.get(col_num).getColumn_type().equals("rupees"))
                            cells4.get(col_num).setData("\u20B9 " + total);
                        else cells4.get(col_num).setData(String.valueOf(total));

                        //update_total(col_num);
                        //adapter4.notifyItemChanged(col_num);
                    }
                }
                adapter4.notifyDataSetChanged();
            }

            update_row(datacell_num);
            // update_total();

        }
        catch (JSONException e){
            Toast.makeText(GetDocument.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    public void update_row(int datacell_num) {
        String url="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/update-single-data/"+id;
        try {

            dataCell tempdatacell=dataCellArrayList.get(datacell_num);
            Log.d("babbar size1",String.valueOf(dataCellArrayList.size()));

            int row_id=tempdatacell.getRow_id();
            String row=String.valueOf(row_id);

            RequestQueue queue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("rows_nums", rows);
            jsonBody.put("cols_nums", column);
            jsonBody.put("id",row);
            jsonBody.put("serialno",tempdatacell.getSerialno());
            JSONArray jsonArray = new JSONArray();
            for (int i = (datacell_num)*column; i < ((datacell_num)*column)+column; i++) {
                cell tempCell = cells.get(i);
                String string = tempCell.getData();
                String temp="";
                for(int j=0;j<string.length();j++)
                {
                    if(string.charAt(j)!='\u20B9')
                        temp=temp+string.charAt(j);
                }
                jsonArray.put(temp);
            }
            jsonBody.put("data",jsonArray);
            JSONArray json1 = new JSONArray();
            for(int i=1;i<=column;i++){
                json1.put("10");
            }
            jsonBody.put("width",json1);
            JSONArray json2 = new JSONArray();
            for(int i=1;i<=column;i++){
                json2.put("10");
            }
            jsonBody.put("height",json2);
            //final String requestBody = jsonBody.toString();


            JsonObjectRequest request=new JsonObjectRequest(Request.Method.PUT, url,jsonBody, response -> {
                //Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
            }, error -> {
                //Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            });
            queue.add(request);
        }
        catch (JSONException e){
            Toast.makeText(GetDocument.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    public void extract(){
        cells.clear();
        cells5.clear();

        listColumn = databaseColumns.getColumnDetails().getAllDetails(Long.parseLong(id));
        listData = databaseData.getSavedDataDao().getDocumentSerial(Long.parseLong(id));
        column = listColumn.size();
        Log.d("Keshav",String.valueOf(listData.size()));

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        rowbut.setVisibility(GONE);
        colbut.setVisibility(GONE);

        dataCellArrayList=new ArrayList<>();
        for(int i=0;i<listData.size();i++) {
            try {
                dataCell datacell = new dataCell((int) listData.get(i).getId(),
                        listData.get(i).getColumn1(),
                        listData.get(i).getColumn2(),
                        listData.get(i).getColumn3(),
                        listData.get(i).getColumn4(),
                        listData.get(i).getColumn5(),
                        listData.get(i).getColumn6(),
                        listData.get(i).getColumn7(),
                        listData.get(i).getColumn8(),
                        listData.get(i).getColumn9(),
                        listData.get(i).getColumn10(),
                        listData.get(i).getColumn11(),
                        listData.get(i).getColumn12(),
                        listData.get(i).getColumn13(),
                        listData.get(i).getColumn14(),
                        listData.get(i).getColumn15(),
                        listData.get(i).getColumn16(),
                        listData.get(i).getColumn17(),
                        listData.get(i).getColumn18(),
                        listData.get(i).getColumn19(),
                        listData.get(i).getColumn20(),
                        listData.get(i).getHeight(),
                        listData.get(i).getWidth(),
                        Integer.parseInt(listData.get(i).getSerial_no()));
                dataCellArrayList.add(datacell);
                String[] data = {datacell.getColumn1(), datacell.getColumn2(), datacell.getColumn3(),
                        datacell.getColumn4(), datacell.getColumn5(), datacell.getColumn6(),
                        datacell.getColumn7(), datacell.getColumn8(), datacell.getColumn9(),
                        datacell.getColumn10(), datacell.getColumn11(), datacell.getColumn12(),
                        datacell.getColumn13(), datacell.getColumn14(), datacell.getColumn15(),
                        datacell.getColumn16(), datacell.getColumn17(), datacell.getColumn18(),
                        datacell.getColumn19(), datacell.getColumn20()};

                for (int j = 0; j < column; j++) {
                    if(data[j] != null){
                        if(data[j].equals("null")){
                            cells.add(new cell(""));
                        }else {
                            cells.add(new cell(data[j]));
                        }
                    }
                }
            }
            catch (Exception e){
                Toast.makeText(GetDocument.this,"data error "+e,Toast.LENGTH_SHORT).show();
            }
        }
        cellColumnArrayList=new ArrayList<>();
        cellColumnArrayList.add(new cellColumn(-1,"null","null",0));
        String row = String.valueOf(listData.size());
        t_row = row;
        rows = Integer.parseInt(row);
        column = listColumn.size();

        for (int i = 0; i <listColumn.size(); i++) {

            try{
                cellColumn cellcol = new cellColumn((int) listColumn.get(i).getCol_id(),
                        listColumn.get(i).getColumn_names(),
                        listColumn.get(i).getColumn_type(),
                        colIndex++);
                cellColumnArrayList.add(cellcol);
                arr_column_id[i] = (int) listColumn.get(i).getCol_id();
                String total_column = String.valueOf(listColumn.get(i).getTotal());
                formula[i] = listColumn.get(i).getFormula();
                cells2.add(new cell(cellcol.getColumn_name()));
                if (!total_column.equals("0"))
                    cells5.add(new cell(String.valueOf(i), Integer.parseInt(id)));

            }
            catch(Exception e){
                Toast.makeText(GetDocument.this,"list column*****"+e,Toast.LENGTH_SHORT).show();
            }
        }
        try{
            for(int i=0;i<column;i++)
            {
                StringBuilder temp_formula= new StringBuilder();
                for(int k=0;k<formula[i].length();k++)
                {
                    if(formula[i].charAt(k)=='<')
                    {
                        k++;
                        StringBuilder tcol= new StringBuilder();
                        while(formula[i].charAt(k)!='>')
                        {
                            tcol.append(formula[i].charAt(k));
                            k++;
                        }

                        int tcol_no=Integer.parseInt(tcol.toString());
                        temp_formula.append(cellColumnArrayList.get(tcol_no - 1).getColumn_name());
                    }
                    else
                        temp_formula.append(formula[i].charAt(k));
                }
                if(!formula[i].equals("0"))
                    cells2.get(i).setData(cells2.get(i).getData()+"\n= "+temp_formula);

            }

            int mod=cellColumnArrayList.size();
            for(int i=0;i<cells.size();i++)
            {
                if(cellColumnArrayList.get(i%mod).getColumn_type().equals("rupees")&&cells.get(i).getData().trim().length()>0&&!cells.get(i).getData().contains("\u20B9"))
                    cells.get(i).setData('\u20B9'+cells.get(i).getData());
            }
            cells3.add(new cell(""));
            for(int i=1;i<=(rows);i++){
                cells3.add(new cell(""+i));
            }
            progressDialog.dismiss();
            rowbut.setVisibility(View.VISIBLE);
            colbut.setVisibility(View.VISIBLE);
            forming_adapter();
            click();
            if(setting==-1){

                cells5.add(new cell(String.valueOf(col_num),Integer.parseInt(id)));
                getTotal();
                update_total(col_num);
            }
        } catch (Exception e){
            Toast.makeText(GetDocument.this, "column"+e, Toast.LENGTH_SHORT).show();
        }



        /*String JsonURL="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/get-document-serial/"+id;
        Log.d("Babbar id",String.valueOf(id));
        RequestQueue requestQueue=Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest=new JsonObjectRequest(Request.Method.GET, JsonURL, null, response -> {
            try {

                JSONArray array = response.getJSONArray("data");
                String urlcol="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/get-column-data/"+id;
                RequestQueue Queue=Volley.newRequestQueue(getApplicationContext());
                cellColumnArrayList=new ArrayList<>();
                dataCellArrayList=new ArrayList<>();
                JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, urlcol, null, response1 -> {
                    try {
                        JSONArray arraycol= response1.getJSONArray("data");
                        JSONObject jsonObject1=arraycol.getJSONObject(0);
                        //jsonObject1.getString("id");
                        String row=jsonObject1.getString("row_nums");
                        t_row=row;
                        rows=Integer.parseInt(row);
                        String c=jsonObject1.getString("column_nums");
                        column=Integer.parseInt(c);

                        for(int i=0;i<array.length();i++) {
                            Log.d("Babbar array length", String.valueOf(array.length()));
                            JSONObject jsonObject = array.getJSONObject(i);
                            dataCell datacell = new dataCell(jsonObject.getInt("id"),
                                    jsonObject.getString("column1"),
                                    jsonObject.getString("column2"),
                                    jsonObject.getString("column3"),
                                    jsonObject.getString("column4"),
                                    jsonObject.getString("column5"),
                                    jsonObject.getString("column6"),
                                    jsonObject.getString("column7"),
                                    jsonObject.getString("column8"),
                                    jsonObject.getString("column9"),
                                    jsonObject.getString("column10"),
                                    jsonObject.getString("column11"),
                                    jsonObject.getString("column12"),
                                    jsonObject.getString("column13"),
                                    jsonObject.getString("column14"),
                                    jsonObject.getString("column15"),
                                    jsonObject.getString("column16"),
                                    jsonObject.getString("column17"),
                                    jsonObject.getString("column18"),
                                    jsonObject.getString("column19"),
                                    jsonObject.getString("column20"),
                                    jsonObject.getString("height"),
                                    jsonObject.getString("width"),
                                    jsonObject.getInt("serialno"));
                            dataCellArrayList.add(datacell);
                            String[] data = {datacell.getColumn1(), datacell.getColumn2(), datacell.getColumn3(),
                                    datacell.getColumn4(), datacell.getColumn5(), datacell.getColumn6(),
                                    datacell.getColumn7(), datacell.getColumn8(), datacell.getColumn9(),
                                    datacell.getColumn10(), datacell.getColumn11(), datacell.getColumn12(),
                                    datacell.getColumn13(), datacell.getColumn14(), datacell.getColumn15(),
                                    datacell.getColumn16(), datacell.getColumn17(), datacell.getColumn18(),
                                    datacell.getColumn19(), datacell.getColumn20()};

                            for (int j = 0; j < column; j++) {
                                if(!data[j].equals("null")){
                                    cells.add(new cell(data[j]));
                                }
                            }
                        }
                        boolean flag = false;
                        for (int i=0;i<cells.size();i++){
                            if (!cells.get(i).getData().equals("")){
                                flag = true;
                                break;
                            }
                        }
                        if (!flag){
                            MenuItem menuItem = toolbar1.getMenu().findItem(R.id.autoSaving);
                            menuItem.setVisible(false);
                        }
                        cellColumnArrayList.add(new cellColumn(-1,"null","null",0));
                        for(int i=0;i<arraycol.length();i++) {
                            JSONObject jsonObjectcol = arraycol.getJSONObject(i);
                            cellColumn cellcol=new cellColumn(jsonObjectcol.getInt("id"),jsonObjectcol.getString("column_names"),jsonObjectcol.getString("column_type"),colIndex++);
                            cellColumnArrayList.add(cellcol);
                            arr_column_id[i]=jsonObjectcol.getInt("id");
                            String total_column= jsonObjectcol.getString("total");
                            formula[i]=jsonObjectcol.getString("formula");
                            Log.d("Babbar formula",formula[i]+" "+i);
                            cells2.add(new cell(cellcol.getColumn_name()));
                            if(!total_column.equals("0"))
                                cells5.add(new cell(String.valueOf(i),Integer.parseInt(id)));
                        }


                        for(int i=0;i<column;i++)
                        {
                            String temp_formula="";
                            for(int k=0;k<formula[i].length();k++)
                            {
                                if(formula[i].charAt(k)=='<')
                                {
                                    k++;
                                    StringBuilder tcol= new StringBuilder();
                                    while(formula[i].charAt(k)!='>')
                                    {
                                        tcol.append(formula[i].charAt(k));
                                        k++;
                                    }

                                    int tcol_no=Integer.parseInt(tcol.toString());
                                    temp_formula = temp_formula+cellColumnArrayList.get(tcol_no-1).getColumn_name();

                                }
                                else
                                    temp_formula=temp_formula+formula[i].charAt(k);

                            }
                            if(!formula[i].equals("0"))
                                cells2.get(i).setData(cells2.get(i).getData()+"\n= "+temp_formula);
                        }

                        int mod=cellColumnArrayList.size();
                        for(int i=0;i<cells.size();i++)
                        {
                            if(cellColumnArrayList.get(i%mod).getColumn_type().equals("rupees")&&cells.get(i).getData().trim().length()>0&&!cells.get(i).getData().contains("\u20B9"))
                                cells.get(i).setData('\u20B9'+cells.get(i).getData());
                        }
                        cells3.add(new cell(""));
                        for(int i=1;i<=(rows);i++){
                            cells3.add(new cell(""+i));
                        }
                        progressDialog.dismiss();
                        rowbut.setVisibility(View.VISIBLE);
                        colbut.setVisibility(View.VISIBLE);

                        forming_adapter();
                        getTotal();
                        if(setting==-1){
                            cells5.add(new cell(String.valueOf(col_num),Integer.parseInt(id)));
                            getTotal();
                            update_total(col_num);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(GetDocument.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }, error -> Toast.makeText(GetDocument.this, error.getMessage(), Toast.LENGTH_SHORT).show());
                Queue.add(jsonObjectRequest);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(GetDocument.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            Log.d("tagb", "onErrorResponse: "+ error.getMessage());
            Toast.makeText(GetDocument.this, error.getMessage(), Toast.LENGTH_SHORT).show();
        });
        requestQueue.add(objectRequest);*/
    }
    public void forming_adapter(){
        combined_List = new ArrayList<>();
        int var=0,var2=0,var3=0;
        for(int i=0;i<((rows+1)*(column+1));i++){
            if(i%(column+1)==0){ combined_List.add(cells3.get(var)); var++; }
            else if(i<=column){ combined_List.add(cells2.get(var2)); var2++;}
            else{
                if(cells.get(var3)==null){
                    combined_List.add(new cell(" "+i));
                }else {
                    combined_List.add(cells.get(var3));
                    var3++;
                }
            }
        }
        adapter = new Adapter_row_dataCombined(getApplicationContext(),combined_List,column);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),column+1));
        recyclerView.setAdapter(adapter);
        click();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(GetDocument.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //Toast.makeText(GetDocument.this, "beforeText", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        if (cellColumnArrayList.get(column_id).getColumn_type().equals("rupees")){
            String data=""+charSequence;
            if(data.startsWith("\u20B9"))cells_temp.setData(data);
            else cells_temp.setData("\u20B9 "+data);
        }
        else {
            cells_temp.setData("" + charSequence);
        }
        //adapter.notifyItemChanged(pos);
        forming_adapter();
        MenuItem menuItem = toolbar1.getMenu().findItem(R.id.autoSaving);
        menuItem.setVisible(true);
        menuItem.setIcon(R.drawable.ic_baseline_autosaving_24);
        postRequest(datacell_num);
    }

    @Override
    public void afterTextChanged(Editable editable) {
        //Toast.makeText(GetDocument.this, "afterText", Toast.LENGTH_SHORT).show();
        //cells_temp.setData(""+editable);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        onBackPressed();
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 1) && (resultCode == RESULT_OK)) {
            Cursor cursor = null;
            try {
                Uri uri = data.getData();
                cursor = getContentResolver().query(uri, new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER }, null, null, null);
                if (cursor != null && cursor.moveToNext()) {
                    String phone = cursor.getString(0);
                    // Do something with phone
                    et.setText(phone);
                    cells_temp.setData(phone);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public  void search_and_show_total() {
        String urlcol = "http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/get-column-data/" + id;
        RequestQueue Queue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, urlcol, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray arraycol=response.getJSONArray("data");

                    for(int i=0;i<arraycol.length();i++) {
                        JSONObject jsonObjectcol = arraycol.getJSONObject(i);

                        total_col[i]=jsonObjectcol.getInt("total");
                        cells4.clear();
                        for (int k = 1; k <= column; k++) {
                            cells4.add(new cell(""));
                        }


                        for (int j = 0; j <total_col.length; j++) {
                            int col_num = j;
                            if(total_col[j]>0) {
                                Double total = 0.0;
                                int can = 0;
                                String data;
                                for (int k = col_num; k < rows * column; k += column) {
                                    data = cells.get(k).getData().trim();
                                    try {
                                        if (data.equals("")) continue;
                                        else if (data.startsWith("\u20B9")) {
                                            total += Double.parseDouble(data.substring(2));
                                        } else total += Double.parseDouble(data);
                                    } catch (NumberFormatException e) {
                                        can = -1;
                                        break;
                                    }
                                }
                                if (can == -1) {
                                    Toast.makeText(getApplicationContext(), " Cannot be converted as numbers", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (cellColumnArrayList.get(col_num).getColumn_type().equals("rupees"))
                                        cells4.get(col_num).setData("\u20B9 " + String.valueOf(total));
                                    else cells4.get(col_num).setData(String.valueOf(total));
                                    //adapter4.notifyItemChanged(col_num);
                                }
                            }
                        }
                        adapter4 = new AdapterTotal(getApplicationContext(), cells4);
                        recyclerView4.setLayoutManager(new GridLayoutManager(getApplicationContext(), column+1));
                        recyclerView4.setVisibility(View.VISIBLE);
                        recyclerView4.setAdapter(adapter4);
                        ViewCompat.setNestedScrollingEnabled(recyclerView4, false);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(GetDocument.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, error -> Toast.makeText(GetDocument.this, error.getMessage(), Toast.LENGTH_SHORT).show());
        Queue.add(jsonObjectRequest);
    }

    public void update_total(int col_num) {


        String total=String.valueOf(cells4.get(col_num).getData());

        Log.d("babbar cell size",String.valueOf(cells4.size())+" "+total);




        String col_id= String.valueOf(arr_column_id[col_num]);
        String urlcol = "http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/update-total/" +(col_id);
        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());

        StringRequest request=new StringRequest(Request.Method.PUT, urlcol, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(otp.this, response, Toast.LENGTH_SHORT).show();
               // search_and_show_total();
                //return ;

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(otp.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public String getBodyContentType() {
                return "text/plain;charset=UTF-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    // total replace with userid
                    return total == null ? null : total.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", total, "utf-8");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                    // can get more details such as response.headers
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        queue.add(request);
        Log.d("babbar reached here?","hello");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //search_and_show_total();

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


        now.set(Calendar.YEAR,year);
        now.set(MONTH,month);
        now.set(DAY_OF_MONTH,dayOfMonth);
        tpd.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


        freq frequency =new freq(0);
        AlertDialog dialog;
        AlertDialog.Builder builder=new AlertDialog.Builder(GetDocument.this);
        builder.setTitle("   Set Frequency of Reminder");
        builder.setPositiveButton("Repeat daily", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //repeat=1;
                frequency.repeat=1;
                now.set(Calendar.HOUR_OF_DAY,hourOfDay);
                now.set(Calendar.MINUTE,minute);

                String date="";
                date=date+now.get(DAY_OF_MONTH);
                date=date+"/";
                if((now.get(MONTH)+1)/10==0)
                    date=date+"0";
                date=date+(now.get(MONTH)+1);
                date=date+"/";
                date=date+now.get(YEAR);
                date=date+", ";
                date=date+(now.get(Calendar.HOUR_OF_DAY)%12);
                date=date+":";

                if(now.get(Calendar.MINUTE)/10==0)
                    date=date+"0";
                date=date+now.get(Calendar.MINUTE);


                if(now.get(Calendar.AM_PM) == Calendar.AM){
                    // AM
                    date=date+" AM";
                }else{
                    // PM
                    date=date+" PM";
                }
                cells_temp.setData(date);
                adapter.notifyDataSetChanged();

                String title1="Reminder in "+doc_name+"\n";
                String title2="";
                for(int i=1;i<=column;i++)
                {
                    title2=title2+cellColumnArrayList.get(i-1).getColumn_name()+"-"+cells.get((datacell_num)*column+i-1).getData()+", ";
                }

                String FREQ="";
                if(frequency.repeat==1)
                    FREQ="FREQ=DAILY;";
                else
                    FREQ="FREQ=MINUTELY;";


                postRequest(datacell_num);
            }
        });

        builder.setNegativeButton("Repeat once", (dialog1, which) -> {
            //repeat=1;
            //frequency.repeat=1;
            now.set(Calendar.HOUR_OF_DAY,hourOfDay);
            now.set(Calendar.MINUTE,minute);

            String date="";
            date=date+now.get(DAY_OF_MONTH);
            date=date+"/";
            if((now.get(MONTH)+1)/10==0)
                date=date+"0";
            date=date+(now.get(MONTH)+1);
            date=date+"/";
            date=date+now.get(YEAR);
            date=date+", ";
            date=date+(now.get(Calendar.HOUR_OF_DAY)%12);
            date=date+":";

            if(now.get(Calendar.MINUTE)/10==0)
                date=date+"0";
            date=date+now.get(Calendar.MINUTE);


            if(now.get(Calendar.AM_PM) == Calendar.AM){
                // AM
                date=date+" AM";
            }else{
                // PM
                date=date+" PM";
            }
            cells_temp.setData(date);
            adapter.notifyDataSetChanged();

            String title1="Reminder in "+doc_name+"\n";
            String title2="";
            for(int i=1;i<=column;i++)
            {
                title2=title2+cellColumnArrayList.get(i-1).getColumn_name()+"-"+cells.get((datacell_num)*column+i-1).getData()+", ";
            }
            postRequest(datacell_num);
        });

        builder.setCancelable(false);
        dialog=builder.create();
        dialog.setIcon(R.drawable.alarm);
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);



    }
    class freq{
        int repeat;
        freq(int repeat)
        {
            this.repeat=repeat;
        }
    }

    public void cellPropDelete(MenuItem item) {
        Log.d(TAG, "onMenuItemClick: " + "Delete");
        cells_temp=cells.get(posii);
        datacell_num=posii/column;
        cells_temp.setData("");
        postRequest(datacell_num);
        adapter.notifyItemChanged(posii);
        //posii=-1;
    }

    public void cellPropAddAbove(MenuItem menuItem) {
        Log.d(TAG, "onMenuItemClick: " + "add above");
        final String[] url = {"http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/add-row-above/"};
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("rows_num",String.valueOf(rows));
            jsonObject.put("cols_num",String.valueOf(column));
            jsonObject.put("current_row_number",datacell_num+1);
            jsonObject.put("doc_id",String.valueOf(id));
            JSONArray jsonArray=new JSONArray();
            for(int i=0;i<column;i++)jsonArray.put("");
            jsonObject.put("data",jsonArray);
            JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, url[0], jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        row_idabovebelow[0] = Integer.parseInt(response.getString("row_id"));

                        List<dataCell> datatemp = new ArrayList<>();
                        for(int i=0;i<dataCellArrayList.size();i++)datatemp.add(dataCellArrayList.get(i));
                        dataCellArrayList.clear();
                        for (int i=0;i<datacell_num;i++)dataCellArrayList.add(datatemp.get(i));
                        dataCellArrayList.add(new dataCell(row_idabovebelow[0]));
                        //
                        dataCellArrayList.get(dataCellArrayList.size()-1).setSerialno(dataCellArrayList.size());

                        for(int i=datacell_num;i<rows;i++){
                            dataCellArrayList.add(datatemp.get(i));
                            dataCellArrayList.get(dataCellArrayList.size()-1).setSerialno(dataCellArrayList.size());
                        }
                        List<cell> temp=new ArrayList<>();
                        for(int j=0;j<rows*column;j++){
                            temp.add(new cell(cells.get(j).getData()));
                        }
                        cells.clear();
                        for(int i=0;i<(datacell_num)*column;i++)cells.add(new cell(temp.get(i).getData()));
                        for(int i=(datacell_num)*column;i<(datacell_num)*column+column;i++)cells.add(new cell(""));
                        for (int i=(datacell_num)*column+column,k=(datacell_num)*column;i<(rows+1)*column;i++){
                            if(k<rows*column)cells.add(new cell(temp.get(k).getData()));k++;
                        }
                        ++rows;
                        cells3.add(new cell(""+rows));
                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, error -> {

            });
            queue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void cellPropAddBelow(MenuItem menuItem) {
        Log.d(TAG, "onMenuItemClick: " + "add below");
        String addurl="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/add-row-below/";
        RequestQueue requeue = Volley.newRequestQueue(getApplicationContext());
        JSONObject jsonObject1=new JSONObject();
        try {
            jsonObject1.put("rows_num",String.valueOf(rows));
            jsonObject1.put("cols_num",String.valueOf(column));
            jsonObject1.put("current_row_number",datacell_num+1);
            jsonObject1.put("doc_id",String.valueOf(id));
            JSONArray jsonArray=new JSONArray();
            for(int i=0;i<column;i++)jsonArray.put("");
            jsonObject1.put("data",jsonArray);
            JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, addurl, jsonObject1, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        row_idabovebelow[1] = Integer.parseInt(response.getString("row_id"));
                        ArrayList<dataCell> tempcell = new ArrayList<>(dataCellArrayList);
                        dataCellArrayList.clear();
                        for(int i=0;i<datacell_num+1;i++)dataCellArrayList.add(tempcell.get(i));
                        dataCellArrayList.add(new dataCell(row_idabovebelow[1]));
                        //
                        dataCellArrayList.get(dataCellArrayList.size()-1).setSerialno((dataCellArrayList.size()));
                        //
                        for(int i=datacell_num+1;i<rows;i++){
                            dataCellArrayList.add(tempcell.get(i));
                            dataCellArrayList.get(dataCellArrayList.size()-1).setSerialno(dataCellArrayList.size());
                        }
                        List<cell> temp=new ArrayList<>();
                        for(int j=0;j<rows*column;j++){
                            temp.add(new cell(cells.get(j).getData()));
                        }
                        cells.clear();
                        for(int i=0;i<(datacell_num+1)*column;i++)cells.add(new cell(temp.get(i).getData()));
                        for(int i=(datacell_num+1)*column;i<(datacell_num+1)*column+column;i++)cells.add(new cell(""));
                        for (int i=(datacell_num+1)*column+column,k=(datacell_num+1)*column;i<(rows+1)*column;i++){
                            if(k<rows*column)cells.add(new cell(temp.get(k).getData()));k++;
                        }
                        ++rows;
                        cells3.add(new cell(""+rows));
                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, error -> {

            });
            requeue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void cellPropCopy(MenuItem menuItem) {
        Log.d(TAG, "onMenuItemClick: " + "copy");
        String data=cells.get(posii).getData();
        ClipboardManager cm = (ClipboardManager)GetDocument.this.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text",data);
        cm.setPrimaryClip(clipData);
        Toast.makeText(GetDocument.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
    }
}