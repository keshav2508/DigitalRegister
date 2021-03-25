package ds.docusheet.table;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.io.File;
import java.lang.reflect.Method;

import ds.docusheet.table.database.DatabaseClass;

public class DocumentSetting extends AppCompatActivity {
    ListView listView;
    ListAdapter adapter;
    String heading,doc_id;
    LoadingBar loadingBar;
    ProgressDialog progressBar;
    DownloadManager dm;
    int check=0;
    long queueid;
    DatabaseClass databaseDocument;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_setting);
        MixpanelAPI mixpanel =
                MixpanelAPI.getInstance(this,"0b359b9b0d40bb01e6381e29c23f1310");
        mixpanel.track("On Resume",null);
        mixpanel.flush();

        databaseDocument=DatabaseClass.getDatabaseDocument(this);
        doc_id= getIntent().getStringExtra("doc_id");
        heading=getIntent().getStringExtra("doc_name");
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(heading);
        actionBar.show();
        listView = findViewById(R.id.listview);
       progressBar= new ProgressDialog(DocumentSetting.this);
        final String[] listItem = new String[]{
                getResources().getString(R.string.Rename),
                getResources().getString(R.string.Share_as_Pdf),
                getResources().getString(R.string.Download_pdf),
                getResources().getString(R.string.Delete)
        };
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action=intent.getAction();
                if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)){

                    DownloadManager.Query query=new DownloadManager.Query();
                    query.setFilterById(queueid);
                    Cursor c = dm.query(query);
                    if(c.moveToFirst()){
                        int col=c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if(DownloadManager.STATUS_SUCCESSFUL==c.getInt(col)) {

                            if (check == 1) {
                                try {
                                    Toast.makeText(DocumentSetting.this, "Download Complete", Toast.LENGTH_LONG).show();
                                    if (Build.VERSION.SDK_INT >= 24) {
                                        try {
                                            Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                                            m.invoke(null);
                                        } catch (Exception e) {
                                            Toast.makeText(DocumentSetting.this, "" + e, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    try {
                                        String filename = heading + doc_id + ".pdf";
                                        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + filename);
                                        Intent target = new Intent(Intent.ACTION_VIEW);
                                        Uri fileUri;
                                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
                                            fileUri = Uri.fromFile(folder);
                                        }
                                        else{
                                            fileUri = FileProvider.getUriForFile(DocumentSetting.this,BuildConfig.APPLICATION_ID + ".provider",folder);
                                        }
                                        target.setDataAndType(fileUri, "application/pdf");
                                        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        Intent i = Intent.createChooser(target, "Open File");
                                        try {
                                            startActivity(i);
                                        } catch (Exception e) {
                                            Toast.makeText(DocumentSetting.this, "" + e, Toast.LENGTH_LONG).show();
                                        }
                                        loadingBar.dismissDialog();
                                    } catch (Exception e) {
                                        Log.d("pdf3", e.toString());
                                    }
                                }
                                catch (Exception e)
                                {
                                    Log.d("pdf3",e.toString());
                                }
                            }
                            else if(check==2)
                            {
                                try {
                                    if (Build.VERSION.SDK_INT >= 24) {
                                        try {
                                            Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                                            m.invoke(null);
                                        } catch (Exception e) {
                                            Toast.makeText(DocumentSetting.this, "" + e, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    try {
                                        String filename = heading + doc_id + ".pdf";
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
                                            Toast.makeText(DocumentSetting.this, "" + e, Toast.LENGTH_LONG).show();
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
registerReceiver(receiver,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        int[] imageItem = {
                R.drawable.ic_baseline_edit_24,
                R.drawable.ic_baseline_share_24,
                R.drawable.ic_baseline_arrow_downward_24,
                R.drawable.ic_baseline_delete_24,
        };
        final int[] color = {Color.BLACK, Color.BLACK, Color.BLACK, Color.RED};

        listView = findViewById(R.id.listview);
        adapter = new MyDocumentAdapter(DocumentSetting.this, listItem, imageItem, color);
        listView.setAdapter(adapter);
       loadingBar = new LoadingBar(DocumentSetting.this);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 2) {
                try {
                    if (isStoragePermissionGranted()) {
                        loadingBar.startLoading();
                        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/Download/" + heading + doc_id + ".pdf";
                        File file = new File(directory_path);
                        if (file.exists()) {
                            if(file.delete()){
                                Toast.makeText(this,"File Replaced",Toast.LENGTH_SHORT).show();
                            }
                        }
                        check = 1;
                        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/create-pdf/" + doc_id));
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI)
                                .setAllowedOverRoaming(false).setTitle("").setDescription("").setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, heading + doc_id + ".pdf");
                        queueid = dm.enqueue(request);

                    } else {

                        Toast.makeText(DocumentSetting.this, "Error", Toast.LENGTH_LONG).show();

                    }
                }
                catch (Exception e)
                {
                    Log.d("not downloaded",e.toString());
                }
            }

            else if(position==1) {
                try {
                    if (isStoragePermissionGranted()) {
                        loadingBar.startLoading();
                        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/Download/" + heading + doc_id + ".pdf";
                        File file = new File(directory_path);
                        if (file.exists()) {
                            if(file.delete()){
                                Toast.makeText(this,"File Replaced",Toast.LENGTH_SHORT).show();
                            }
                        }
                        check = 2;
                        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/create-pdf/" + doc_id));
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI)
                                .setAllowedOverRoaming(false).setTitle("").setDescription("").setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, heading + doc_id + ".pdf");
                        queueid = dm.enqueue(request);

                    }
                }
                catch (Exception e)
                {
                    Log.d("pdf exc",e.toString());
                }
            }

            else if(position==0) {
                Intent intent = new Intent(DocumentSetting.this, RenameDocument.class);
                intent.putExtra("doc_id", doc_id);
                intent.putExtra("heading", listItem[position]);
                startActivity(intent);
            }
            else if(position==3)
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(DocumentSetting.this);
                alert.setTitle("DELETE");
                alert.setMessage("Do you want to delete the document?");
                alert.setCancelable(false);
                alert.setPositiveButton("Yes, Delete", (dialog, which) -> {

                    HomeFragmentDocumentChecker.setCheck(1);

                    databaseDocument.getDocumentDao().delete(Integer.parseInt(doc_id));
                    String url="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/delete-document/"+doc_id;
                    RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
                    JsonObjectRequest request=new JsonObjectRequest(Request.Method.DELETE, url, null, response -> {
                    }, error -> {
                    });
                    queue.add(request);
                    startActivity(new Intent(DocumentSetting.this,MainActivity.class));
                    overridePendingTransition(0,0);
                    finish();
                });
                alert.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                AlertDialog dialog = alert.create();
                dialog.show();
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }



//    private void checkPermission() {
//        // Here, thisActivity is the current activity
//        if (ContextCompat.checkSelfPermission(getApplicationContext(),
//                Manifest.permission.READ_PHONE_STATE)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(DocumentSetting.this,
//                    Manifest.permission.READ_PHONE_STATE)) {
//
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//
//            } else {
//
//                // No explanation needed, we can request the permission.
//
//                ActivityCompat.requestPermissions(DocumentSetting.t,
//                        new String[]{Manifest.permission.READ_PHONE_STATE},
//                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
//
//                // MY_PERMISSIONS_REQUEST_READ_PHONE_STATE is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
//            }
//        }
//    }
//
        public boolean isStoragePermissionGranted() {
        String Tag="Permission Granted";
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            if(this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
            {
                Log.v(Tag,"Permission is granted");
                return true;
            }
            else
            {
                Log.v(Tag,"Permission is revoked");
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                return false;
            }
        }
        else
        {
            Log.v(Tag,"Permision is granted");
            return true;
        }
    }
}
class MyDocumentAdapter extends BaseAdapter {
    Context context;
    private final String[] listItem;
    private final int[] imageItem;
    private final int [] color;

    public MyDocumentAdapter(Context context, String[] listItem, int[] imageItem,int[] color) {
        this.context = context;
        this.listItem = listItem;
        this.imageItem = imageItem;
        this.color=color;
    }

    @Override
    public int getCount() {
        return listItem.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView =inflater.inflate(R.layout.columnmenuitem,parent,false);
            viewHolder.listname=convertView.findViewById(R.id.textItem);
            viewHolder.listname.setTextColor(color[position]);
            viewHolder.icon=convertView.findViewById(R.id.imageItem);
            viewHolder.icon.setColorFilter(color[position]);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.listname.setText(listItem[position]);
        viewHolder.icon.setImageResource(imageItem[position]);
        return convertView;
    }

    public static class ViewHolder{
        TextView listname;
        ImageView icon;
    }
}