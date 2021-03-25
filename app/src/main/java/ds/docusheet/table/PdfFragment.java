package ds.docusheet.table;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

public class PdfFragment extends Fragment {
    TextView moredetails;
    Button share;
    TextView loginCount,register;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PdfFragment() {
        // Required empty public constructor
    }


    public static PdfFragment newInstance(String param1, String param2) {
        PdfFragment fragment = new PdfFragment();
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
        View v=inflater.inflate(R.layout.fragment_pdf, container, false);
        MixpanelAPI mixpanel =
                MixpanelAPI.getInstance(getContext(),"0b359b9b0d40bb01e6381e29c23f1310");

        mixpanel.track("On Resume",null);
        mixpanel.flush();
        moredetails=v.findViewById(R.id.more_details);
        loginCount=v.findViewById(R.id.logCount);
        register=v.findViewById(R.id.register);
        share=v.findViewById(R.id.share_referral);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dynamicLink="https://digitalregisters.page.link/?"+
                        "link=https://play.google.com/store/apps/details?id="+ FirebaseAuth.getInstance().getUid().toString()+"/"+
                        "&apn="+"ds.docusheet.table"+
                        "&st="+"My Refer Link"+
                        "&sd="+"PDF VIP Link";
                Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                        .setLongLink(Uri.parse(dynamicLink))
                        // Set parameters
                        // ...
                        .buildShortDynamicLink()
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<ShortDynamicLink>() {
                            @Override
                            public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                                if (task.isSuccessful()) {
                                    Uri shortLink = task.getResult().getShortLink();
                                    Uri flowchartLink = task.getResult().getPreviewLink();
                                    Intent intent=new Intent();
                                    intent.setAction(Intent.ACTION_SEND);
                                    intent.putExtra(Intent.EXTRA_TEXT,"Your friend invites you to download Digital Register app to maintain records easily. " +shortLink.toString());
                                    intent.setType("text/plain");
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getContext(),"error",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
        moredetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getActivity(), getActivity().toString(), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getActivity(),UnlockPDF.class);
                startActivity(intent);
            }
        });
        String urlcol = "http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/referrals-count/" + FirebaseAuth.getInstance().getUid();
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.GET, urlcol, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               loginCount.setText(response+"/3");
               if(Integer.parseInt(response)>=3)
                   loginCount.setText("3/3");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "p"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
        String url = "http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/get-refer-count/" + FirebaseAuth.getInstance().getUid();
        RequestQueue rqueue = Volley.newRequestQueue(getContext());
        StringRequest srequest = new StringRequest(Request.Method.GET, url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              register.setText(response+"/3");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "P2"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        rqueue.add(srequest);

        return v;
    }
}
