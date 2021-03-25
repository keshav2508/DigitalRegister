package ds.docusheet.table;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.io.UnsupportedEncodingException;
public class BusinessName extends AppCompatActivity {
    EditText text;
    Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_name);
        MixpanelAPI mixpanel =
                MixpanelAPI.getInstance(this,"0b359b9b0d40bb01e6381e29c23f1310");

        mixpanel.track("On Resume",null);
        mixpanel.flush();
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
       actionBar.setTitle("Business Name");
        save=findViewById(R.id.namesave);
        text=findViewById(R.id.nametext);
        if(!getIntent().getStringExtra("name").equals("null"))
        text.setText(getIntent().getStringExtra("name"));
        else text.setText("");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (text.getText().toString().length() <= 0) {
                    text.setError("name can't be empty");
                } else {

                    String name = text.getText().toString();
                    String url = "http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/update-user-bname/" + FirebaseAuth.getInstance().getUid();
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                    StringRequest request = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            finish();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(BusinessName.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        public String getBodyContentType() {
                            return "text/plain;charset=UTF-8";
                        }

                        @Override
                        public byte[] getBody() throws AuthFailureError {
                            try {
                                // total replace with userid
                                return name == null ? null : name.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", name, "utf-8");
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
                }
            }
        });

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(text.getText().toString().length()>0)
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
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    public boolean onSupportNavigateUp()
    {
        finish();
        return true;
    }

}



