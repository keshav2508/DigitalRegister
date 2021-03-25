package ds.docusheet.table;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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
import com.google.firebase.auth.FirebaseAuth;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class CompletePDFVIP extends Fragment {

    Button b_name,b_address,b_mobile,branding,b_details;
    String[]  details=new String[8];
    TextView text;
    String value="",value2="";
    ProgressDialog progressDialog;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CompletePDFVIP() {
        // Required empty public constructor
    }

    public static CompletePDFVIP newInstance(String param1, String param2) {
        CompletePDFVIP fragment = new CompletePDFVIP();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_complete_p_d_f_v_i_p, container, false);
        MixpanelAPI mixpanel =
                MixpanelAPI.getInstance(getContext(),"0b359b9b0d40bb01e6381e29c23f1310");

        mixpanel.track("On Resume",null);
        mixpanel.flush();
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Loading");
        progressDialog.show();

        b_name=v.findViewById(R.id.b_namebutton);
        b_address=v.findViewById(R.id.b_addressbutton);
        b_mobile=v.findViewById(R.id.b_mobile);
        String urlcol="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/get-user/"+ FirebaseAuth.getInstance().getUid();
        RequestQueue Queue= Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, urlcol, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                 details[0]=response.getString("phone_no");
                 details[1]=response.getString("b_name");
                 details[2]=response.getString("b_add1");
                 details[3]=response.getString("b_add2");
                 details[4]= response.getString("city_state_country");
                 details[5]=response.getString("pin");
                 details[6]=response.getString("branding");
                 details[7]=response.getString("b_details");

                 if(!details[6].equals("null")){
                     text=v.findViewById(R.id.branding_button);
                     if(details[6].equals("added"))
                     text.setText(v.getResources().getString(R.string.remove));
                     else
                         text.setText(v.getResources().getString(R.string.add));
                 }
                    if(!details[7].equals("null"))
                    {
                        text=v.findViewById(R.id.b_details);
                        if(details[7].equals("added"))
                            text.setText(v.getResources().getString(R.string.remove));
                        else
                            text.setText(v.getResources().getString(R.string.add));
                    }
                 if(!details[1].equals("null")){
                     text=v.findViewById(R.id.bname);
                     text.setText(details[1]);
                 }
                 if(!details[0].equals("null")){
                     text=v.findViewById(R.id.phoneno);
                     text.setText(details[0]);
                 }
                 String address="";
                    if(!details[2].equals("null")){
                       address+=details[2]+", ";
                    }
                    if(!details[3].equals("null")&&details[3].length()>0){
                        address+=details[3]+", ";
                    }
                    if(!details[4].equals("null")){
                        address+=details[4]+", ";
                    }
                    if(!details[5].equals("null")){
                        address+=details[5];
                    }
                    text=v.findViewById(R.id.baddress);
                    text.setText(address);
                    progressDialog.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Queue.add(jsonObjectRequest);



        b_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),BusinessName.class);
                intent.putExtra("name",details[1]);
                startActivity(intent);
            }
        });
        b_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),BusinessAddress.class);
                intent.putExtra("b_add1",details[2]);
                intent.putExtra("b_add2",details[3]);
                intent.putExtra("city",details[4]);
                intent.putExtra("pin",details[5]);
                startActivity(intent);
            }
        });
        b_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),BusinessMobile.class);
                intent.putExtra("mobile",details[0]);
                startActivity(intent);
            }
        });
        branding=v.findViewById(R.id.branding_button);
        b_details=v.findViewById(R.id.b_details);
        branding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(branding.getText().toString().equals(v.getResources().getString(R.string.add))) {
                    branding.setText(v.getResources().getString(R.string.remove));
                    value="added";
                }
                else {
                    branding.setText(v.getResources().getString(R.string.add));
                    value="removed";
                }
                String url = "http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/update-pdf-branding/" + FirebaseAuth.getInstance().getUid();
                RequestQueue queue = Volley.newRequestQueue(getContext());

                StringRequest request = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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
                            return value == null ? null : value.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", value, "utf-8");
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
        });
        b_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(b_details.getText().toString().equals(v.getResources().getString(R.string.add))) {
                   value2="added";
                    b_details.setText(v.getResources().getString(R.string.remove));
                }
                else {
                    value2 = "removed";
                    b_details.setText(v.getResources().getString(R.string.add));
                }
                try{
                String url = "http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/update-business-details/" + FirebaseAuth.getInstance().getUid();
                RequestQueue queue = Volley.newRequestQueue(getContext());

                StringRequest request = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
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
                            return value2 == null ? null : value2.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", value2, "utf-8");
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
                catch (Exception e){
                    Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
                }

            }
        });
        return v;
    }
}