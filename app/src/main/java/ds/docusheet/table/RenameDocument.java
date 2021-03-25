package ds.docusheet.table;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.io.UnsupportedEncodingException;

import ds.docusheet.table.database.DatabaseClass;

public class RenameDocument extends AppCompatActivity {

    String name,doc_name;
    EditText newname;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rename_document);
        MixpanelAPI mixpanel =
                MixpanelAPI.getInstance(this,"0b359b9b0d40bb01e6381e29c23f1310");

        mixpanel.track("On Resume",null);
        mixpanel.flush();
        String heading=getIntent().getStringExtra("heading");
        String doc_id=getIntent().getStringExtra("doc_id");
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(heading);
        actionBar.show();
        newname=findViewById(R.id.cols_name);
        save=findViewById(R.id.save);

        newname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(newname.getText().toString().length()>0)
                {
                    save.setBackground(getResources().getDrawable(R.drawable.changablecorners));
                }
                else{
                    save.setBackground(getResources().getDrawable(R.drawable.corners));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        name=doc_name;
        save.setOnClickListener(view -> {
            DatabaseClass.getDatabaseDocument(this).getDocumentDao().updateName(newname.getText().toString(),Integer.parseInt(doc_id));
            HomeFragmentDocumentChecker.setCheck(1);
            save.setEnabled(false);
            String string=newname.getText().toString();
            if(string==null){
                newname.setError("Should not be empty");
                newname.requestFocus();
                return;
            }
            String url="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/change-document-name/"+doc_id;
            RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
            StringRequest request=new StringRequest(Request.Method.PUT, url, response -> {
            }, error -> {
            }){
                @Override
                public String getBodyContentType() {
                    return "application/json";
                }

                @Override
                public byte[] getBody() {
                    try {
                        return string == null ? null : string.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", string, "utf-8");
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
            // request.setRetryPolicy(defaultRetryPolicy);
            Intent intent=new Intent(RenameDocument.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
            overridePendingTransition(R.anim.slide_out_bottom,R.anim.slide_in_bottom);
            finish();
        });
        newname.setText(name);
    }
    @Override
    public boolean onSupportNavigateUp()
    {
        finish();
        return true;
    }
}