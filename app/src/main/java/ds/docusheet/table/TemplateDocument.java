 package ds.docusheet.table;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ds.docusheet.table.database.ColumnDetailsEntity;
import ds.docusheet.table.database.DatabaseClass;
import ds.docusheet.table.database.DocumentEntity;
import ds.docusheet.table.database.SavedDataEntity;

 public class TemplateDocument extends AppCompatActivity {


    private final String TAG = "Template Document";
    int temp_num,cat_num;
    List<cell> cells,cells2,cells3,cells4,cells5;
    int rows=5,column=5;
    Button use;
    String id,heading;
    RecyclerView recyclerView,recyclerView4;
    Adapter_row_dataCombined adapter;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_document);
        MixpanelAPI mixpanel =
                MixpanelAPI.getInstance(this,"0b359b9b0d40bb01e6381e29c23f1310");

        mixpanel.track("On Resume",null);
        mixpanel.flush();
        mAuth=FirebaseAuth.getInstance();
        heading = getIntent().getStringExtra("heading");
        cat_num=getIntent().getIntExtra("cat_num",-1);
        temp_num=getIntent().getIntExtra("temp_num",-1);
        //temp_pos=cat_num+temp_num;
        //Toast.makeText(this, cat_num+" "+temp_num, Toast.LENGTH_SHORT).show();
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(heading);

        mAuth=FirebaseAuth.getInstance();

        recyclerView=findViewById(R.id.recycle1);

        recyclerView4=findViewById(R.id.recycler4);
        use=findViewById(R.id.use);

        cells=new ArrayList<>();
        cells2=new ArrayList<>();
        cells3=new ArrayList<>();
        cells4=new ArrayList<>();
        cells5=new ArrayList<>();

        layout();


    }

    public void saving(){
        HomeFragmentDocumentChecker.setCheck(1);
        DatabaseClass databasedoc,databasecolumn,databasedata;
        databasecolumn = DatabaseClass.getDatabaseColumns(this);
        databasedata = DatabaseClass.getDatabaseData(this);
        databasedoc = DatabaseClass.getDatabaseDocument(this);


        String url="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/save-document-create";
        try {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("rows_num", rows);
            jsonBody.put("cols_num", column);
            jsonBody.put("user_id",mAuth.getUid());
            jsonBody.put("document_name",heading);
            JSONArray json1=new JSONArray();
            for (int i=1;i<=rows*column;i++){
                json1.put("10");
            }
            jsonBody.put("width",json1);
            JSONArray json2=new JSONArray();
            for (int i=1;i<=rows*column;i++){
                json2.put("10");
            }
            jsonBody.put("height",json2);
            JSONArray json3=new JSONArray();
            for (int i = 0; i < cells2.size(); i++) {
                cell tempCell = cells2.get(i);
                String string = tempCell.getData();
                json3.put(string);
            }
            jsonBody.put("column_name",json3);
            JSONArray json4=new JSONArray();
            for (int i = 0; i < cells5.size(); i++){
                cell tempCell = cells5.get(i);
                String string = tempCell.getData();
                json4.put(string);
            }
            jsonBody.put("column_type",json4);
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < cells.size(); i++) {
                cell tempCell = cells.get(i);
                String string = tempCell.getData().trim();
                if (string.length() >= 2 && string.charAt(0) == '\u20B9'){
                    string = string.substring(1);
                }
                jsonArray.put(string);
            }
            jsonBody.put("data",jsonArray);
            Log.d(TAG, "saving: " + jsonBody.toString());
            JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, url,jsonBody, response -> {
                try {
                    id=response.getString("doc_id");
                    String JsonURL="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/get-document-serial/"+id;
                    RequestQueue requestQueue= Volley.newRequestQueue(this);
                    JsonObjectRequest objectRequest=new JsonObjectRequest(Request.Method.GET, JsonURL, null, response1 -> {
                        try {
                            JSONArray array= response1.getJSONArray("data");
                            for(int i=0;i<array.length();i++){
                                JSONObject jsonObjectt=array.getJSONObject(i);
                                SavedDataEntity datacell=new SavedDataEntity(jsonObjectt.getInt("id"),
                                        jsonObjectt.getString("column1"),
                                        jsonObjectt.getString("column2"),
                                        jsonObjectt.getString("column3"),
                                        jsonObjectt.getString("column4"),
                                        jsonObjectt.getString("column5"),
                                        jsonObjectt.getString("column6"),
                                        jsonObjectt.getString("column7"),
                                        jsonObjectt.getString("column8"),
                                        jsonObjectt.getString("column9"),
                                        jsonObjectt.getString("column10"),
                                        jsonObjectt.getString("column11"),
                                        jsonObjectt.getString("column12"),
                                        jsonObjectt.getString("column13"),
                                        jsonObjectt.getString("column14"),
                                        jsonObjectt.getString("column15"),
                                        jsonObjectt.getString("column16"),
                                        jsonObjectt.getString("column17"),
                                        jsonObjectt.getString("column18"),
                                        jsonObjectt.getString("column19"),
                                        jsonObjectt.getString("column20"),

                                        jsonObjectt.getString("height"),
                                        jsonObjectt.getString("width"),id,String.valueOf(jsonObjectt.getInt("serialno")));
                                databasedata.getSavedDataDao().insert(datacell);
                            }
                            String urlcol="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/get-column-data/"+id;
                            RequestQueue Queue=Volley.newRequestQueue(this);
                            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, urlcol, null, response11 -> {
                                try {
                                    JSONArray arraycol= response11.getJSONArray("data");
                                    for(int i=0;i<arraycol.length();i++) {
                                        JSONObject jsonObjectcol = arraycol.getJSONObject(i);
                                        ColumnDetailsEntity cellcol = new ColumnDetailsEntity(jsonObjectcol.getLong("id"),
                                                jsonObjectcol.getString("column_names"),
                                                jsonObjectcol.getString("column_nums"),
                                                jsonObjectcol.getString("column_type"),
                                                id,
                                                jsonObjectcol.getString("total"),
                                                jsonObjectcol.getString("formula"));
                                        databasecolumn.getColumnDetails().insert(cellcol);
                                        databasedoc.getDocumentDao().insert(new DocumentEntity(
                                                Long.parseLong(id), heading, FirebaseAuth.getInstance().getUid(), "yes"));

                                        Intent intent=new Intent(TemplateDocument.this,GetDocument.class);
                                        intent.putExtra("doc_id",id);
                                        intent.putExtra("doc_name",heading);
                                        intent.putExtra("update_document","yes");
                                        startActivity(intent);
                                        finish();
                                        Log.d("Keshav",jsonObjectcol.getString("formula"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }, error -> Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show());
                            Queue.add(jsonObjectRequest);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }, error -> {
                        Log.d("tagb", "onErrorResponse: "+ error.getMessage());
                        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                    requestQueue.add(objectRequest);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> {
                Log.d(TAG, "onErrorResponse: " + error.toString());
                Toast.makeText(TemplateDocument.this, ""+error.toString(), Toast.LENGTH_SHORT).show();
            });
            queue.add(request);


        }
        catch (JSONException e){
            Toast.makeText(TemplateDocument.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void layout(){
        if(temp_num==0) {
            if(cat_num==0){
                cells2.add(new cell("Cash In"));
                cells2.add(new cell("Cash Out"));
                cells2.add(new cell("Date"));
                cells2.add(new cell("Remark"));
                cells5.add(new cell("rupees"));cells5.add(new cell("rupees"));cells5.add(new cell("date"));cells5.add(new cell("text"));
                column=4;
                cells.add(new cell("\u20B9 100"));cells.add(new cell(" "));cells.add(new cell("16/01/2021"));cells.add(new cell("Tea"));
                cells.add(new cell(" "));cells.add(new cell("\u20B9 150"));cells.add(new cell("16/01/2021"));cells.add(new cell("Luggage"));
                for(int i=9;i<=20;i++)cells.add(new cell(""));
            }
            else if(cat_num==1){
                cells2.add(new cell("Date"));
                cells2.add(new cell("Payment Mode"));
                cells2.add(new cell("Amount"));
                cells2.add(new cell("Mobile Number"));
                cells5.add(new cell("date"));cells5.add(new cell("text"));cells5.add(new cell("rupees"));cells5.add(new cell("phone"));
                column=4;
                cells.add(new cell("16/01/2021"));cells.add(new cell("PhonePay"));cells.add(new cell("\u20B9 1000"));cells.add(new cell("9999999999"));
                cells.add(new cell("16/01/2021"));cells.add(new cell("Paytm"));cells.add(new cell("\u20B9 2500"));cells.add(new cell("9999999999"));
                cells.add(new cell("16/01/2021"));cells.add(new cell("Cash"));cells.add(new cell("\u20B9 1090"));cells.add(new cell("9999999999"));
                for(int i=13;i<=20;i++)cells.add(new cell(""));

            }
            else if(cat_num==2){
                cells2.add(new cell("Date"));
                cells2.add(new cell("Item"));
                cells2.add(new cell("Amount"));
                cells2.add(new cell("Notes"));
                cells5.add(new cell("date"));cells5.add(new cell("text"));cells5.add(new cell("rupees"));cells5.add(new cell("text"));
                column=4;
                cells.add(new cell("16/01/2021"));cells.add(new cell("Transport"));cells.add(new cell("\u20B9 1000"));cells.add(new cell("Delhi transport"));
                cells.add(new cell("16/01/2021"));cells.add(new cell("Goods Purchasing"));cells.add(new cell("\u20B9 2500"));cells.add(new cell("Various Bills"));
                cells.add(new cell("16/01/2021"));cells.add(new cell("Broadband"));cells.add(new cell("\u20B9 1090"));cells.add(new cell("Paid on PhonePe"));
                for(int i=13;i<=20;i++)cells.add(new cell(""));
            }
            else if(cat_num==3){
                cells2.add(new cell("Date of Order"));
                cells2.add(new cell("Name"));
                cells2.add(new cell("Item"));
                cells2.add(new cell("Quantity(in Kg)"));
                cells2.add(new cell("Address"));
                cells2.add(new cell("Payment Received"));
                cells2.add(new cell("Order Received"));
                cells2.add(new cell("Notes"));
                cells5.add(new cell("date"));cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));
                column=8;
                cells.add(new cell("16/01/2021"));cells.add(new cell("Karan"));cells.add(new cell("Cement"));cells.add(new cell("2"));cells.add(new cell("Jaipur Rd"));cells.add(new cell("Yes"));cells.add(new cell("Yes"));cells.add(new cell("-"));
                //cells.add(new cell("16/01/2021"));cells.add(new cell("Abdul"));cells.add(new cell(""));cells.add(new cell("Various Bills"));
                for(int i=9;i<=40;i++)cells.add(new cell(""));
            }
            else if(cat_num==4){
                cells2.add(new cell("Date"));
                cells2.add(new cell("Item"));
                cells2.add(new cell("Quantity"));
                cells2.add(new cell("Amount"));
                cells2.add(new cell("Payment type"));
                cells2.add(new cell("Notes"));
                cells5.add(new cell("date"));cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("rupees"));cells5.add(new cell("text"));cells5.add(new cell("text"));
                column=6;
                cells.add(new cell("31/1/2021"));cells.add(new cell("Cement"));cells.add(new cell("2 Bags"));
                cells.add(new cell("\u20B9 600"));cells.add(new cell("Cash"));cells.add(new cell("Deliver Tomorrow"));
                for(int i=7;i<=30;i++)cells.add(new cell(""));
            }
            //111
           else if(cat_num==5){
               // Toast.makeText(this, "Shivam Yadav", Toast.LENGTH_SHORT).show();
                cells2.add(new cell("Date"));
                cells2.add(new cell("Name"));
                cells2.add(new cell("Total salary"));
                cells2.add(new cell("Paid"));
                cells2.add(new cell("Due"));
                cells2.add(new cell("Note"));
                cells5.add(new cell("date"));
                cells5.add(new cell("text"));
                cells5.add(new cell("rupees"));
                cells5.add(new cell("rupees"));
                cells5.add(new cell("rupees"));
                cells5.add(new cell("text"));
                column=6;
                cells.add(new cell("7/3/2021"));
                cells.add(new cell("Mayank"));
                cells.add(new cell("200000"));
                cells.add(new cell("2000"));
                cells.add(new cell("18000"));
                cells.add(new cell("zero absent"));
                cells.add(new cell("7/3/2021"));
                cells.add(new cell("Karan"));
                cells.add(new cell("200000"));
                cells.add(new cell("5000"));
                cells.add(new cell("15000"));
                cells.add(new cell("one absent"));
                for(int i=13;i<=30;i++)cells.add(new cell(""));
            }
            else if(cat_num==6){

                cells2.add(new cell("Name"));
                for(int i=1;i<=column;i++) {
                    String Date= "0"+i+"/01/2021";
                    cells2.add(new cell(Date));
                }
                cells5.add(new cell("text"));
                for(int i=1;i<=column;i++)
                cells5.add(new cell("text"));
                column=6;
                cells.add(new cell("Krishna"));
                //doubt
                cells.add(new cell("Yes"));
                cells.add(new cell("No"));
                for(int i=4;i<=30;i++) cells.add(new cell(""));
            }

        }

        else if(temp_num == 1) {
            if(cat_num==0){
                cells2.add(new cell("Topic"));
                cells2.add(new cell("Book"));
                cells2.add(new cell("Notes"));
                cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));
                column=3;
                cells.add(new cell("Basic Formulae"));cells.add(new cell("Maths"));cells.add(new cell("Revise class work"));
                cells.add(new cell("Page 80 in Chapter 6"));cells.add(new cell("Science"));cells.add(new cell("Solve example questions"));
                cells.add(new cell("Ramnath poetries"));cells.add(new cell("Hindi"));cells.add(new cell("Practice learning"));
                for(int i=10;i<=15;i++)cells.add(new cell(""));
            }
            else if(cat_num==1){
                cells2.add(new cell("Subject"));
                cells2.add(new cell("Date"));
                cells2.add(new cell("Notes"));
                cells5.add(new cell("text"));cells5.add(new cell("date"));cells5.add(new cell("text"));
                column=3;
                cells.add(new cell("English"));cells.add(new cell("23/1/2021"));cells.add(new cell("Learn 3 poems"));
                cells.add(new cell("Maths"));cells.add(new cell("23/1/2021"));cells.add(new cell("Revise Q1-Q5 in C-2"));
                cells.add(new cell("Science"));cells.add(new cell("23/1/2021"));cells.add(new cell("Revise Pg-16 in C-3"));
                for(int i=10;i<=15;i++)cells.add(new cell(""));

            }
            else if(cat_num==2){
                cells2.add(new cell("Date"));
                cells2.add(new cell("Time"));
                cells2.add(new cell("Subject"));
                cells2.add(new cell("Topic"));
                cells2.add(new cell("Notes"));
                cells2.add(new cell("Reminder"));
                cells5.add(new cell("date"));
                cells5.add(new cell("time"));
                cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));
                cells5.add(new cell("Reminder"));
                column=6;
                cells.add(new cell("7/3/2021")); cells.add(new cell("6pm")); cells.add(new cell("Maths"));
                cells.add(new cell("Linear Equation")); cells.add(new cell("Prepare For Test")); cells.add(new cell(""));
                cells.add(new cell("7/3/2021")); cells.add(new cell("7pm")); cells.add(new cell("Physics"));
                cells.add(new cell("Thermodynamics")); cells.add(new cell("Work on Fundamnetals"));
                cells.add(new cell(""));

                for(int i=13;i<=30;i++)cells.add(new cell(""));

            }
            else if(cat_num==3){
                cells2.add(new cell("Subject"));
                cells2.add(new cell("Date"));
                cells2.add(new cell("Important Notes"));
                cells2.add(new cell("Rating (out of 10)"));
                cells5.add(new cell("text"));cells5.add(new cell("date"));cells5.add(new cell("text"));
                cells5.add(new cell("number"));
                column=4;

                for(int i=1;i<=20;i++)cells.add(new cell(""));

            }
            else if(cat_num==4){
                cells2.add(new cell("Friend/Person Name"));
                cells2.add(new cell("Amount due to pay"));
                cells2.add(new cell("Amount due to receive"));
                cells2.add(new cell("Date to receive"));
                cells5.add(new cell("text"));cells5.add(new cell("rupees"));cells5.add(new cell("rupees"));
                cells5.add(new cell("Reminder"));
                column=4;
                cells.add(new cell("Abhishek"));cells.add(new cell("500"));cells.add(new cell(""));
                cells.add(new cell(""));
                cells.add(new cell("Rahul"));cells.add(new cell(""));cells.add(new cell("400"));
                cells.add(new cell(""));
                for(int i=9;i<=20;i++) cells.add(new cell(""));
            }
            else if(cat_num==5){
                cells2.add(new cell("Subject Name"));
                cells2.add(new cell("Subject Weightage"));
                cells2.add(new cell("Daily Study Plan"));
                cells2.add(new cell("priority"));
                cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));
                cells5.add(new cell("text"));
                column=4;

                for(int i=1;i<=20;i++)cells.add(new cell(""));

            }
            else if(cat_num==6){
                cells2.add(new cell("Name"));
                cells2.add(new cell("Date"));
                cells2.add(new cell("Reason"));
                cells2.add(new cell("Reminder"));
                cells5.add(new cell("text"));cells5.add(new cell("date"));cells5.add(new cell("text")); cells5.add(new cell("Reminder"));
                column=4;
                cells.add(new cell("Abhishek"));cells.add(new cell("7/3/2021"));cells.add(new cell("Birthday")); cells.add(new cell(""));
                cells.add(new cell("Rahul"));cells.add(new cell("10/3/2021"));cells.add(new cell("Meeting"));cells.add(new cell(""));
                cells.add(new cell("Aman"));cells.add(new cell("15/3/2021"));cells.add(new cell("Studies"));cells.add(new cell(""));
                for(int i=13;i<=20;i++)cells.add(new cell(""));
            }
            else if(cat_num==7){
                cells2.add(new cell("Exam Date"));
                cells2.add(new cell("Subject"));
                cells2.add(new cell("Exam Timing"));
                cells2.add(new cell("Sitting Plan (room no.)"));
                cells5.add(new cell("date"));
                cells5.add(new cell("text"));cells5.add(new cell("time"));cells5.add(new cell("text"));
                column=4;
                cells.add(new cell("8/3/2021"));
                cells.add(new cell("English"));cells.add(new cell("10:00 AM - 1:00 PM"));cells.add(new cell("10-A"));
                cells.add(new cell("9/3/2021"));
                cells.add(new cell("Maths"));cells.add(new cell("10:00 AM - 1:00 PM"));cells.add(new cell("10-A"));
                cells.add(new cell("10/3/2021"));
                cells.add(new cell("Science"));cells.add(new cell("10:00 AM - 1:00 PM"));cells.add(new cell("10-A"));
                cells.add(new cell("11/3/2021"));
                cells.add(new cell("Computer"));cells.add(new cell("10:00 AM - 1:00 PM"));cells.add(new cell("10-A"));
                cells.add(new cell("7/3/2021"));
                cells.add(new cell("History "));cells.add(new cell("10:00 AM - 1:00 PM"));cells.add(new cell("10-A"));

            }

        }

        else if(temp_num==2) {
            if(cat_num==0){
                cells2.add(new cell("Student Name"));
                cells2.add(new cell("Father Name"));
                cells2.add(new cell("Course"));
                cells2.add(new cell("Payment Received"));
                cells2.add(new cell("Payment Pending"));
                cells2.add(new cell("Date"));
                cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("rupees"));cells5.add(new cell("rupees"));cells5.add(new cell("date"));
                column=6;
                cells.add(new cell("Sheela Singh"));cells.add(new cell("Mukesh Singh"));cells.add(new cell("8th Class"));cells.add(new cell("\u20B9 2500"));cells.add(new cell("\u20B9 500"));cells.add(new cell("16/01/2021"));
                cells.add(new cell("Karan Ram"));cells.add(new cell("Diwakar Ram"));cells.add(new cell("8th Class"));cells.add(new cell("\u20B9 2700"));cells.add(new cell("\u20B9 300"));cells.add(new cell("16/01/2021"));
                cells.add(new cell("Vaibav Kumar"));cells.add(new cell("Mayank Kumar"));cells.add(new cell("8th Class"));cells.add(new cell("\u20B9 2400"));cells.add(new cell("\u20B9 600"));cells.add(new cell("16/01/2021"));
                for(int i=19;i<=30;i++)cells.add(new cell(""));
            }
            else if(cat_num==1){
                cells2.add(new cell("Item Name"));
                cells2.add(new cell("Quantity"));
                cells2.add(new cell("Order Placed"));
                cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));
                column=3;
                cells.add(new cell("Science Books"));cells.add(new cell("120"));cells.add(new cell("Yes"));
                cells.add(new cell("Notebooks"));cells.add(new cell("150"));cells.add(new cell("No"));
                cells.add(new cell("Pencils"));cells.add(new cell("200"));cells.add(new cell("Yes"));
                for(int i=10;i<=15;i++)cells.add(new cell(""));
            }
            else if(cat_num==2){
                cells2.add(new cell("Name"));
                cells2.add(new cell("First Exam"));
                cells2.add(new cell("Second Exam"));
                cells2.add(new cell("Half-Year"));
                cells2.add(new cell("Annual"));
                cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));
                column=5;
                cells.add(new cell("Mayank"));cells.add(new cell("90.5%"));cells.add(new cell("95.4%"));cells.add(new cell("93%"));cells.add(new cell("93%"));
                cells.add(new cell("Karan"));cells.add(new cell("80.5%"));cells.add(new cell("85.4%"));cells.add(new cell("83%"));cells.add(new cell("85.8%"));
                cells.add(new cell("Nitin"));cells.add(new cell("94.5%"));cells.add(new cell("93.4%"));cells.add(new cell("96%"));cells.add(new cell("96.8%"));
                for(int i=16;i<=25;i++)cells.add(new cell(""));
            }
        }

        else if(temp_num==3) {
            if(cat_num==0){
                cells2.add(new cell("Item"));
                cells2.add(new cell("Quantity"));
                cells2.add(new cell("Purchased"));
                cells2.add(new cell("Notes"));
                cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));
                column=4;
                cells.add(new cell("Tea"));cells.add(new cell("1 kg"));cells.add(new cell("Yes"));cells.add(new cell(""));
                cells.add(new cell("Red Chilli Powder"));cells.add(new cell("500 g"));cells.add(new cell("Yes"));cells.add(new cell("Buy the same"));
                cells.add(new cell("Sugar"));cells.add(new cell("2 kg"));cells.add(new cell("No"));cells.add(new cell("Immediate need"));
                for(int i=13;i<=20;i++)cells.add(new cell(""));
            }
            else if(cat_num==1){
                cells2.add(new cell("Date"));
                cells2.add(new cell("Item"));
                cells2.add(new cell("Amount"));
                cells2.add(new cell("Notes"));
                cells5.add(new cell("date"));cells5.add(new cell("text"));cells5.add(new cell("rupees"));cells5.add(new cell("text"));
                column=4;
                cells.add(new cell("16/01/2021"));cells.add(new cell("Transport"));cells.add(new cell("\u20B9 1000"));cells.add(new cell("Delhi transport"));
                cells.add(new cell("16/01/2021"));cells.add(new cell("Goods Purchasing"));cells.add(new cell("\u20B9 2500"));cells.add(new cell("Various Bills"));
                cells.add(new cell("16/01/2021"));cells.add(new cell("Broadband"));cells.add(new cell("\u20B9 1090"));cells.add(new cell("Paid on PhonePe"));
                for(int i=13;i<=20;i++)cells.add(new cell(""));
            }
        }

        else if(temp_num==4) {
            if(cat_num==0){
                cells2.add(new cell("Item"));
                cells2.add(new cell("Type"));
                cells2.add(new cell("Quantity"));
                cells2.add(new cell("Notes"));
                cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));
                column=4;
                cells.add(new cell("Boiled green veg"));cells.add(new cell("fresh"));cells.add(new cell("200 gm"));cells.add(new cell("boil mildly"));
                cells.add(new cell("Dry fruits"));cells.add(new cell("any"));cells.add(new cell("20 pieces"));cells.add(new cell("eat daily"));
                cells.add(new cell("Chapati"));cells.add(new cell("mix grain"));cells.add(new cell("3 daily"));cells.add(new cell("1 at a time"));
                for(int i=13;i<=20;i++)cells.add(new cell(""));
            }
            else if(cat_num==1){
                cells2.add(new cell("Exercise"));
                cells2.add(new cell("Repetitions"));
                cells2.add(new cell("Sets"));
                cells2.add(new cell("Notes"));
                cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));
                column=4;
                cells.add(new cell("Squats"));cells.add(new cell("10"));cells.add(new cell("2"));cells.add(new cell("daily"));
                cells.add(new cell("Pushups"));cells.add(new cell("10"));cells.add(new cell("2"));cells.add(new cell("alternate day"));
                cells.add(new cell("Cycling"));cells.add(new cell("5 Minutes"));cells.add(new cell("2"));cells.add(new cell("daily"));
                for(int i=13;i<=20;i++)cells.add(new cell(""));
            }
        }

        else if(temp_num==5) {
            if(cat_num==0){
                cells2.add(new cell("Pick Up"));
                cells2.add(new cell("Drop"));
                cells2.add(new cell("Amount"));
                cells2.add(new cell("Payment Mode"));
                cells2.add(new cell("Time"));
                cells2.add(new cell("Date"));
                cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("rupees"));cells5.add(new cell("text"));cells5.add(new cell("time"));cells5.add(new cell("date"));
                column=6;
                cells.add(new cell("Dhola Kuan"));cells.add(new cell("Haus Khas"));cells.add(new cell("\u20B9 200"));cells.add(new cell("Online"));cells.add(new cell("5:00 pm"));cells.add(new cell("23/1/2021"));
                cells.add(new cell("Haus Khas"));cells.add(new cell("Green Park"));cells.add(new cell("\u20B9 500"));cells.add(new cell("Online"));cells.add(new cell("6:00 pm"));cells.add(new cell("23/1/2021"));
                cells.add(new cell("Haus Khas"));cells.add(new cell(" Vasant Kunj"));cells.add(new cell("\u20B9 350"));cells.add(new cell("Online"));cells.add(new cell("6:40 pm"));cells.add(new cell("23/1/2021"));
                for(int i=19;i<=30;i++)cells.add(new cell(""));
            }
            else if(cat_num==1){
                cells2.add(new cell("Date"));
                cells2.add(new cell("Work"));
                cells2.add(new cell("Amount"));
                cells2.add(new cell("Notes"));
                cells5.add(new cell("date"));cells5.add(new cell("text"));cells5.add(new cell("rupees"));cells5.add(new cell("text"));
                column=4;
                cells.add(new cell("21/01/2021"));cells.add(new cell("Engine service"));cells.add(new cell("\u20B9 6000"));cells.add(new cell("Service/5000 km"));
                cells.add(new cell("22/01/2021"));cells.add(new cell("Tyre change"));cells.add(new cell("\u20B9 8000"));cells.add(new cell(""));
                for(int i=9;i<=20;i++)cells.add(new cell(""));
            }
        }

        else if(temp_num==6) {
            if(cat_num==0){
                cells2.add(new cell("Name"));
                cells2.add(new cell("No of people"));
                cells2.add(new cell("Invite Sent"));
                cells2.add(new cell("Notes"));
                cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));
                column=4;
                cells.add(new cell("Ravi"));cells.add(new cell("1"));cells.add(new cell("Yes"));cells.add(new cell("will reach on same day"));
                cells.add(new cell("Mayank"));cells.add(new cell("2"));cells.add(new cell("Yes"));cells.add(new cell("Not sure"));
                cells.add(new cell("Utkarsh"));cells.add(new cell("1"));cells.add(new cell("No"));cells.add(new cell(" not decided"));
                for(int i=13;i<=20;i++)cells.add(new cell(""));
            }
            else if(cat_num==1){
                cells2.add(new cell("Name"));
                cells2.add(new cell("Type"));
                cells2.add(new cell("Notes"));
                cells5.add(new cell("text"));cells5.add(new cell("text"));cells5.add(new cell("text"));
                column=3;
                cells.add(new cell("Rabdi Jamun"));cells.add(new cell("fresh Rabdi"));cells.add(new cell(""));
                cells.add(new cell("Paneer"));cells.add(new cell("Tomato gravy"));cells.add(new cell("use the best quality"));
                cells.add(new cell("Chapati"));cells.add(new cell("Tandoori"));cells.add(new cell("use 3 Tandoor Bhatti"));
                for(int i=10;i<=15;i++)cells.add(new cell(""));
            }
            else if(cat_num==2){
                cells2.add(new cell("Date"));
                cells2.add(new cell("Item"));
                cells2.add(new cell("Amount"));
                cells2.add(new cell("Notes"));
                cells5.add(new cell("date"));cells5.add(new cell("text"));cells5.add(new cell("rupees"));cells5.add(new cell("text"));
                column=4;
                cells.add(new cell("21/01/2021"));cells.add(new cell("Shervani"));cells.add(new cell("On Rent"));cells.add(new cell("return by next day"));
                cells.add(new cell("22/01/2021"));cells.add(new cell("Hotel stay for guests"));cells.add(new cell("\u20B9 1500"));cells.add(new cell("per room/day"));
                cells.add(new cell("22/01/2021"));cells.add(new cell("Catering"));cells.add(new cell("\u20B9 80000"));cells.add(new cell(""));
                for(int i=13;i<=20;i++)cells.add(new cell(""));
            }
        }

        cells3.add(new cell(""));

        for(int i=1;i<=(rows);i++){
            cells3.add(new cell(""+i));
        }
        forming_adapter();

        use.setOnClickListener(view -> {
            use.setEnabled(false);
            saving();
        });
    }
    public void forming_adapter(){
        List<cell> combined_List;
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
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}