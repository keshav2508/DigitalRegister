package ds.docusheet.table;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ds.docusheet.table.database.ColumnDetailsEntity;
import ds.docusheet.table.database.DatabaseClass;
import ds.docusheet.table.database.DocumentEntity;
import ds.docusheet.table.database.SavedDataEntity;

public class HomeFragment extends Fragment {
    TextView ViewMoreButtonZeroSize;
    RecyclerView recyclerView;
    RecyclerView homePageTemplateRecyclerViewZeroSize;
    HomePageTemplateAdapter homePageTemplateAdapterZeroSize;
    RelativeLayout relativeLayout;
    ConstraintLayout constraintLayout;
    RelativeLayout homePageTemplateRelativeLayoutZeroSize;
    HomeFragmentDocumentChecker documentChecker;
    SharedPreferences preferences;
    FirebaseAuth mAuth;
    LinearLayoutManager layoutManager;
    int k=0;

    DatabaseClass databaseDocument,databaseData,databaseColumns;
    List<DocumentEntity> list=new ArrayList<>();

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_home,container,false);
        MixpanelAPI mixpanel =
                MixpanelAPI.getInstance(getContext(),"0b359b9b0d40bb01e6381e29c23f1310");

        mixpanel.track("On Resume",null);
        mixpanel.flush();
        preferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
        ViewMoreButtonZeroSize = v.findViewById(R.id.viewMoreButtonZeroSize);
        homePageTemplateRecyclerViewZeroSize = v.findViewById(R.id.homePageTemplateRecyclerViewZeroSize);
        documentChecker = HomeFragmentDocumentChecker.getInstance();
        relativeLayout = v.findViewById(R.id.new_document);
        homePageTemplateRelativeLayoutZeroSize = v.findViewById(R.id.homePageTemplateRelativeLayoutZerSize);
        mAuth=FirebaseAuth.getInstance();
        homePageTemplateRelativeLayoutZeroSize.setVisibility(View.GONE);
        recyclerView = v.findViewById(R.id.homerecyclerView);
        constraintLayout=v.findViewById(R.id.clayout);

        databaseDocument=DatabaseClass.getDatabaseDocument(getContext());
        databaseData=DatabaseClass.getDatabaseData(getContext());
        databaseColumns=DatabaseClass.getDatabaseColumns(getContext());

        relativeLayout.setOnClickListener(v1 -> {
            final Animation myAnim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);

            BounceAnim interpolator = new BounceAnim(0.1, 25);
            myAnim.setInterpolator(interpolator);

            relativeLayout.startAnimation(myAnim);
            new Handler().postDelayed(() -> {
                Intent i = new Intent(getContext(), NewDocument.class);
                startActivity(i);
            }, 500);
        });

        String user_id = mAuth.getUid();
        try {
            if(isConnected()){
                List<DocumentEntity> listOffline;
                listOffline =databaseDocument.getDocumentDao().getAllDocumentsNotOnline(user_id);
                if(listOffline.size()>0){
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("rows_num", rows);
                    jsonBody.put("cols_num", column);
                    jsonBody.put("user_id",user_id);
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

                    for(int i=0;i<listOffline.size();i++){
                        String url="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/save-document-create-referral";
                        RequestQueue queue = Volley.newRequestQueue(getContext());
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
                                                        Long.parseLong(id), "New Register",
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
                        }, error -> Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show());
                        queue.add(request);
                    }
                }

            }
        } catch (InterruptedException | IOException | JSONException e) {
            e.printStackTrace();
        }
        /*if(list.size()==0) {
            Toast.makeText(getContext(), "Getting Documents", Toast.LENGTH_SHORT).show();

            final String url = "http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/get-all-document/" + user_id;

            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, response -> {
                        try {
                            JSONArray jsonArray = response.getJSONArray("document");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                k++;
                                list.add(new DocumentEntity(
                                        Long.parseLong(jsonObject.getString("doc_id")),
                                        jsonObject.getString("document_name"),
                                        user_id,
                                        jsonObject.getString("update_document")));

                                databaseDocument.getDocumentDao().insert(new DocumentEntity(
                                        Long.parseLong(jsonObject.getString("doc_id")),
                                        jsonObject.getString("document_name"),
                                        user_id,
                                        jsonObject.getString("update_document")));

                                String JsonURL="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/get-document-serial/"+jsonObject.getString("doc_id");
                                RequestQueue requestQueue= Volley.newRequestQueue(getContext());
                                JsonObjectRequest objectRequest=new JsonObjectRequest(Request.Method.GET, JsonURL, null, response1 -> {
                                    try {
                                        JSONArray array= response1.getJSONArray("data");
                                        for(int i1 = 0; i1 <array.length(); i1++){
                                            JSONObject jsonObjectt=array.getJSONObject(i1);
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

                                                    jsonObject.getString("doc_id"),
                                                    String.valueOf(jsonObjectt.getInt("serialno")));
                                            databaseData.getSavedDataDao().insert(datacell);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }, error -> {
                                    Log.d("tagb", "onErrorResponse: "+ error.getMessage());
                                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                                requestQueue.add(objectRequest);
                                String urlcol="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/get-column-data/"+jsonObject.getString("doc_id");
                                RequestQueue Queue=Volley.newRequestQueue(getContext());
                                JsonObjectRequest jsonObjectRequest1 =new JsonObjectRequest(Request.Method.GET, urlcol, null, response12 -> {

                                    try {
                                        JSONArray arraycol= response12.getJSONArray("data");
                                        for(int i12 = 0; i12 <arraycol.length(); i12++) {
                                            JSONObject jsonObjectcol = arraycol.getJSONObject(i12);
                                            ColumnDetailsEntity cellcol = new ColumnDetailsEntity(jsonObjectcol.getLong("id"),
                                                    jsonObjectcol.getString("column_names"),
                                                    jsonObjectcol.getString("column_nums"),
                                                    jsonObjectcol.getString("column_type"),
                                                    jsonObject.getString("doc_id"),
                                                    jsonObjectcol.getString("total"),
                                                    jsonObjectcol.getString("formula"));
                                            databaseColumns.getColumnDetails().insert(cellcol);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }, error -> Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show());
                                Queue.add(jsonObjectRequest1);
                            }

                            if (list.size() > 0) {
                                homePageTemplateRelativeLayoutZeroSize.setVisibility(View.GONE);
                                makeList(list);
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }, error -> {

                    });
            MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
            String TAG = "HomeFragment";
            Log.d(TAG, "onCreateView: " + list.size());
            if (list.size() == 0) {
                Log.d(TAG, "onResponse: " + "Called");
                homePageTemplateRelativeLayoutZeroSize.setVisibility(View.VISIBLE);
                setTemplateRecyclerView();
            }
        }
        else{
            homePageTemplateRelativeLayoutZeroSize.setVisibility(View.GONE);
            makeList(list);
        }

         */

        list = databaseDocument.getDocumentDao().getAllDocuments(user_id);
        if (list.size() == 0) {
            homePageTemplateRelativeLayoutZeroSize.setVisibility(View.VISIBLE);
            setTemplateRecyclerView();
        }
        makeList(list);
        return v;
    }
    private void setTemplateRecyclerView() {
        ViewMoreButtonZeroSize.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            Fragment myFragment = new TemplateFragment();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, myFragment).addToBackStack(null).commit();
        });

        String[] strings={"Cash Book" , "Payment Received","Daily Spend","Sell Register"};
        int[] drawables={R.drawable.cash,R.drawable.payment_received,R.drawable.payment_spend,R.drawable.order};
        homePageTemplateAdapterZeroSize = new HomePageTemplateAdapter(getContext(),strings,drawables);
        homePageTemplateRecyclerViewZeroSize.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        homePageTemplateRecyclerViewZeroSize.setAdapter(homePageTemplateAdapterZeroSize);
    }

    public void makeList(final List<DocumentEntity> list)
    {
        AdapterMain adapter=new AdapterMain(list,getContext());
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter.setOnItemClickListener(position -> {
            try {
                if(isConnected()) {
                    Intent intent = new Intent(getContext(), GetDocument.class);
                    intent.putExtra("doc_id", String.valueOf(list.get(position).getDoc_id()));
                    intent.putExtra("doc_name", list.get(position).getDoc_name());
                    intent.putExtra("update_document", list.get(position).getUpdate());
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getContext(), "please check your internet connection", Toast.LENGTH_SHORT).show();
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        });
    }
    public boolean isConnected() throws InterruptedException, IOException {
        String command="ping -c 1 google.com";
        return Runtime.getRuntime().exec(command).waitFor()==0;
    }

}