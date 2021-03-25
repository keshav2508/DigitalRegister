package ds.docusheet.table;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import ds.docusheet.table.database.DatabaseClass;

public class columnsSetting extends AppCompatActivity {
    Button button, save_radio;
    EditText cols_name;
    int col_id = -1;
    String doc_id, doc_name, col_name, total_col, column_num,string;
    RelativeLayout relativeLayout3, relativeLayout4, relativeLayout5, relativeLayout6;
    RadioGroup radioGroup;
    String[][] tempdata;
    int rows=0;

    boolean conversion=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_columns_setting);
        MixpanelAPI mixpanel =
                MixpanelAPI.getInstance(this,"0b359b9b0d40bb01e6381e29c23f1310");

        mixpanel.track("On Resume",null);
        mixpanel.flush();
        String heading = getIntent().getStringExtra("heading");
        String setting = getIntent().getStringExtra("setting");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(heading);
        actionBar.show();

        doc_id = getIntent().getStringExtra("doc_id");
        col_id = getIntent().getIntExtra("col_id", -1);
        doc_name = getIntent().getStringExtra("doc_name");
        col_name = getIntent().getStringExtra("col_name");
        //Toast.makeText(this, ""+col_name, Toast.LENGTH_SHORT).show();
        column_num = getIntent().getStringExtra("column_num");
        total_col = getIntent().getStringExtra("total_col");
        tempdata=new String[40][20];
        relativeLayout3 = findViewById(R.id.relativeLayout3);
        relativeLayout3.setVisibility(View.INVISIBLE);

        relativeLayout4 = findViewById(R.id.relativeLayout4);
        relativeLayout4.setVisibility(View.INVISIBLE);

        relativeLayout5 = findViewById(R.id.relativeLayout5);
        relativeLayout5.setVisibility(View.INVISIBLE);

        relativeLayout6 = findViewById(R.id.relativeLayout6);
        relativeLayout6.setVisibility(View.INVISIBLE);

        cols_name = findViewById(R.id.cols_name);
        button = findViewById(R.id.save);
        save_radio = findViewById(R.id.save_radio);
        radioGroup=findViewById(R.id.radiogroup);

        final String geturl = "http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/get-document-serial/"+doc_id;
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, geturl, null, response -> {

                    try {
                        JSONArray jsonArray=response.getJSONArray("data");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            tempdata[i][0]=jsonObject.getString("column1");
                            tempdata[i][1]=jsonObject.getString("column2");
                            tempdata[i][2]=jsonObject.getString("column3");
                            tempdata[i][3]=jsonObject.getString("column4");
                            tempdata[i][4]=jsonObject.getString("column5");
                            tempdata[i][5]=jsonObject.getString("column6");
                            tempdata[i][6]=jsonObject.getString("column7");
                            tempdata[i][7]=jsonObject.getString("column8");
                            tempdata[i][8]=jsonObject.getString("column9");
                            tempdata[i][9]=jsonObject.getString("column10");
                            tempdata[i][10]=jsonObject.getString("column11");
                            tempdata[i][11]=jsonObject.getString("column12");
                            tempdata[i][12]=jsonObject.getString("column13");
                            tempdata[i][13]=jsonObject.getString("column14");
                            tempdata[i][14]=jsonObject.getString("column15");
                            tempdata[i][15]=jsonObject.getString("column16");
                            tempdata[i][16]=jsonObject.getString("column17");
                            tempdata[i][17]=jsonObject.getString("column18");
                            tempdata[i][18]=jsonObject.getString("column19");
                            tempdata[i][19]=jsonObject.getString("column20");
                            rows++;
                        }
                        //Toast.makeText(getContext(), ""+k, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        //Toast.makeText(getContext(),""+e,Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getContext(),""+error,Toast.LENGTH_LONG).show();
                    }
                });
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
        switch (col_name) {
            case "text":
                radioGroup.check(R.id.radioText);
                break;
            case "number":
                radioGroup.check(R.id.radioNum);
                break;
            case "time":
                radioGroup.check(R.id.radioTime);
                break;
            case "date":
                radioGroup.check(R.id.radioDate);
                break;
            case "rupees":
                radioGroup.check(R.id.radioRupees);
                break;
            case "phone":
                radioGroup.check(R.id.radioMobile);
                break;
            case "toggle":
                radioGroup.check(R.id.radioToggle);
                break;
            case "checkbox":
                radioGroup.check(R.id.radioCheckbox);
                break;
            case "Reminder":
                radioGroup.check(R.id.radioReminder);
                break;
        }

        cols_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(cols_name.getText().toString().length()>0)
                {
                    button.setBackground(getResources().getDrawable(R.drawable.changablecorners));
                }
                else{
                    button.setBackground(getResources().getDrawable(R.drawable.corners));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (setting.equals("0")) {
            relativeLayout4.setVisibility(View.VISIBLE);
            relativeLayout3.setVisibility(View.VISIBLE);

        }
        if (setting.equals("1")) {
            relativeLayout5.setVisibility(View.VISIBLE);
            relativeLayout6.setVisibility(View.VISIBLE);
        }

        save_radio.setOnClickListener(view -> {
            save_radio.setEnabled(false);
            int selectedradio=radioGroup.getCheckedRadioButtonId();
            if (selectedradio==R.id.radioText)string="text";
            else if(selectedradio==R.id.radioNum)string="number";
            else if(selectedradio==R.id.radioTime)string="time";
            else if(selectedradio==R.id.radioDate)string="date";
            else if(selectedradio==R.id.radioRupees)string="rupees";
            else if(selectedradio==R.id.radioMobile)string="phone";
            else if(selectedradio==R.id.radioToggle)string="toggle";
            else if(selectedradio==R.id.radioReminder)string="Reminder";
            else if(selectedradio==R.id.radioCheckbox)string="checkbox";

            if(!string.equals("text")) {
                for (int i = 0; i < rows; i++) {
                    String temp=tempdata[i][Integer.parseInt(column_num) - 1];
                     if (isConversion(tempdata[i][Integer.parseInt(column_num) - 1]))
                        conversion = true;
                    else if (" ".equals(temp))
                         conversion = true;
                     else if ("".equals(temp))
                         conversion = true;
                    else{
                        conversion = false;
                        break;
                    }
                }
            }else{
                conversion=true;
            }




                if(conversion) {

                    DatabaseClass.getDatabaseColumns(this).getColumnDetails().updateColumnType(col_id,string);


                    String url = "http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/change-column-type/" + col_id;
                    RequestQueue queue = Volley.newRequestQueue(columnsSetting.this);
                    StringRequest request = new StringRequest(Request.Method.PUT, url, response -> {
                        //Toast.makeText(otp.this, response, Toast.LENGTH_SHORT).show();
                    }, error -> {
                        //Toast.makeText(otp.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }) {
                        @Override
                        public String getBodyContentType() {
                            return "text/plain;charset=UTF-8";
                        }

                        @Override
                        public byte[] getBody() {
                            return string == null ? null : string.getBytes(StandardCharsets.UTF_8);
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
                    Intent intent = new Intent(columnsSetting.this, GetDocument.class);
                    intent.putExtra("doc_id", doc_id);
                    intent.putExtra("doc_name", doc_name);
                    finish();
                    startActivity(intent);
                }
                else{
                    Toast.makeText(columnsSetting.this,"can't change column type",Toast.LENGTH_LONG).show();
                }
        });

        button.setOnClickListener(v -> {
            button.setEnabled(false);
            String string = cols_name.getText().toString();
            if (string == null) {
                cols_name.setError("Should not be empty");
                cols_name.requestFocus();
                return;
            }
            DatabaseClass.getDatabaseColumns(this).getColumnDetails().updateColumnName(col_id,string);
            String url = "http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/change-column-name/" + col_id;
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            StringRequest request = new StringRequest(Request.Method.PUT, url, response -> {
                //Toast.makeText(otp.this, response, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(columnsSetting.this, GetDocument.class);
                intent.putExtra("doc_id", doc_id);
                intent.putExtra("doc_name", doc_name);

                finish();
                startActivity(intent);

            }, error -> {
                //Toast.makeText(otp.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
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
        });
    }
    @Override
    public boolean onSupportNavigateUp()
    {
        finish();
        return true;
    }
    public boolean isConversion(String value){
        try{
            Double.parseDouble(value);
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }
}