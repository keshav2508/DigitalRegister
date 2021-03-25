package ds.docusheet.table;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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


public class NewDocument extends AppCompatActivity {
    List<cell> cells_data,cells_row,cells_column;
    int rows=5,column=3;
    String id;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    DatabaseClass database,databaseData,databaseColumns;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_document);
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(this,"0b359b9b0d40bb01e6381e29c23f1310");

        mixpanel.track("On Resume",null);
        mixpanel.flush();
        mAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        cells_data=new ArrayList<>();
        cells_row=new ArrayList<>();
        cells_column=new ArrayList<>();
        cells_row.add(new cell(""));
        for(int i=1;i<=(rows);i++){
            cells_row.add(new cell(""+i));
        }
        for(int i=1;i<=(column);i++){
            cells_column.add(new cell(""+getResources().getString(R.string.column)+" "+i));
        }
        for(int i=1;i<=(rows*column);i++){
            cells_data.add(new cell(""));
        }
        database=DatabaseClass.getDatabaseDocument(NewDocument.this);
        databaseData=DatabaseClass.getDatabaseData(NewDocument.this);
        databaseColumns=DatabaseClass.getDatabaseColumns(NewDocument.this);

        if(internetIsConnected()){
            startingMethod();
        }else{
            offlineStarting();
        }
    }
    public void offlineStarting(){
        String user_id= FirebaseAuth.getInstance().getUid();
        String Offlineid = user_id+;
        database.getDocumentDao().insert(new DocumentEntity(12345,Offlineid,"new Register",user_id,"yes"));
        for(int i=0;i<cells_column.size();i++) {
            databaseColumns.getColumnDetails().insert(new ColumnDetailsEntity(12345, Offlineid, cells_column.get(i).getData(),
                    String.valueOf(column),"text",String.valueOf(12345), Offlineid,String.valueOf(0),String.valueOf(0)));
        }
        for(int i=0;i<cells_row.size();i++){
            databaseData.getSavedDataDao().insert(new SavedDataEntity(12345,Offlineid,"","","","","","","","","","","","","","","","",
                    "","","","","height","width",String.valueOf(12345),Offlineid,
                    "serial no"));
        }

    }

    public void startingMethod(){


        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("rows_num", rows);
            jsonBody.put("cols_num", column);
            jsonBody.put("user_id",mAuth.getUid());
            jsonBody.put("document_name",getResources().getString(R.string.new_register));
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
            for (int i = 0; i < cells_column.size(); i++) {
                cell tempCell = cells_column.get(i);
                String string = tempCell.getData();
                json3.put(string);
            }
            jsonBody.put("column_name",json3);
            JSONArray json4=new JSONArray();
            for (int i=1;i<=rows*column;i++){
                json4.put("text");
            }
            jsonBody.put("column_type",json4);
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < cells_data.size(); i++) {
                cell tempCell = cells_data.get(i);
                String string = tempCell.getData();
                jsonArray.put(string);
            }
            jsonBody.put("data",jsonArray);

            String url="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/save-document-create-referral";
            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, url,jsonBody, response -> {
                try {
                    id = response.getString("doc_id");

                    String JsonURL="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/get-document-serial/"+id;
                    RequestQueue requestQueue= Volley.newRequestQueue(NewDocument.this);
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
                                        jsonObjectt.getString("width"),
                                        id,
                                        String.valueOf(jsonObjectt.getInt("serialno")));
                                databaseData.getSavedDataDao().insert(datacell);
                            }

                            String urlcol="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/get-column-data/"+id;
                            RequestQueue Queue=Volley.newRequestQueue(NewDocument.this);
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
                                        databaseColumns.getColumnDetails().insert(cellcol);
                                    }
                                    database.getDocumentDao().insert(new DocumentEntity(
                                            Long.parseLong(id),null, "New Register",
                                            FirebaseAuth.getInstance().getUid(), "yes"));

                                    Intent intent=new Intent(NewDocument.this,GetDocument.class);
                                    intent.putExtra("doc_id",id);
                                    intent.putExtra("doc_name",getResources().getString(R.string.new_register));
                                    startActivity(intent);
                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(NewDocument.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }, error -> Toast.makeText(NewDocument.this, error.getMessage(), Toast.LENGTH_SHORT).show());
                            Queue.add(jsonObjectRequest);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(NewDocument.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }, error -> {
                        Log.d("tagb", "onErrorResponse: "+ error.getMessage());
                        Toast.makeText(NewDocument.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                    requestQueue.add(objectRequest);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> Toast.makeText(NewDocument.this, error.getMessage(), Toast.LENGTH_SHORT).show());
            queue.add(request);
        }
        catch (JSONException e){
            Toast.makeText(NewDocument.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    @Override
    public boolean onSupportNavigateUp()
    {
        finish();
        return true;
    }
    public boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }
}