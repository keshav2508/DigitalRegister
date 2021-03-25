package ds.docusheet.table;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONObject;

public class BusinessAddress extends AppCompatActivity {

    Button save;
    EditText textView,b_add2,city,pin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_address);
        MixpanelAPI mixpanel =
                MixpanelAPI.getInstance(this,"0b359b9b0d40bb01e6381e29c23f1310");

        mixpanel.track("On Resume",null);
        mixpanel.flush();
        textView=findViewById(R.id.b_add1);
        b_add2=findViewById(R.id.b_add2);
        city=findViewById(R.id.city);
        pin=findViewById(R.id.pincode);
        save=findViewById(R.id.save_address);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Business Address");
        if(!getIntent().getStringExtra("b_add1").equals("null"))
        textView.setText(getIntent().getStringExtra("b_add1"));
        else textView.setText("");
        textView = findViewById(R.id.b_add2);
        if(!getIntent().getStringExtra("b_add2").equals("null"))
        textView.setText(getIntent().getStringExtra("b_add2"));
        else textView.setText("");
        textView=findViewById(R.id.city);
        if(!getIntent().getStringExtra("city").equals("null"))
        textView.setText(getIntent().getStringExtra("city"));
        else textView.setText("");
        textView=findViewById(R.id.pincode);
        if(!getIntent().getStringExtra("pin").equals("null"))
        textView.setText(getIntent().getStringExtra("pin"));
        else textView.setText("");
        textView=findViewById(R.id.b_add1);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textView.getText().toString().length() <= 0) {
                    textView.setError("name can't be empty");
                } else if (city.getText().toString().length() <= 0) {
                    city.setError("city,state & country can't be empty");
                } else if (pin.getText().toString().length() <= 0) {
                    pin.setError("pincode can't be empty");
                } else {
                    try {
                        String url = "http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/update-user-address/" + FirebaseAuth.getInstance().getUid();
                        RequestQueue Queue = Volley.newRequestQueue(getApplicationContext());
                        String badd1 = textView.getText().toString();

                        JSONObject object = new JSONObject();
                        object.put("b_add1", badd1);
                        textView = findViewById(R.id.b_add2);
                        badd1 = textView.getText().toString();
                        object.put("b_add2", badd1);
                        textView = findViewById(R.id.city);
                        object.put("city_state_country", textView.getText().toString());
                        textView = findViewById(R.id.pincode);
                        object.put("pin", textView.getText().toString());
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, object, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                finish();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                finish();
                                //Toast.makeText(BusinessAddress.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                        Queue.add(jsonObjectRequest);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        });
        pin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(pin.getText().toString().length()>0)
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