package ds.docusheet.table;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

public class UnlockPDF extends AppCompatActivity {
    Button share;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock_p_d_f);
        MixpanelAPI mixpanel =
                MixpanelAPI.getInstance(this,"0b359b9b0d40bb01e6381e29c23f1310");

        mixpanel.track("On Resume",null);
        mixpanel.flush();
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getResources().getString(R.string.unlock_access_to_pdf_vip));
        share = findViewById(R.id.share_link);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            createLink();
            }
        });
    }
        public void createLink()
        {
            //DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
            //.setLink(Uri.parse("https://play.google.com/store/apps/details?id=ds.docusheet.table/"))
            //.setDomainUriPrefix("https://referdocusheet.page.link")
            // Open links with this app on Android
            //.setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
            // Open links with com.example.ios on iOS
            //      .setIosParameters(new DynamicLink.IosParameters.Builder("com.example.ios").build())
            //    .buildDynamicLink();

            //Uri dynamicLinkUri = dynamicLink.getUri();
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
                    .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                        @Override
                        public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                            if (task.isSuccessful()) {
                                // Short link created
                                Uri shortLink = task.getResult().getShortLink();
                                Uri flowchartLink = task.getResult().getPreviewLink();
                                Intent intent=new Intent();
                                intent.setAction(Intent.ACTION_SEND);
                                intent.putExtra(Intent.EXTRA_TEXT,"Your friend invites you to download Digital Register app to maintain records easily. " +shortLink.toString());
                                intent.setType("text/plain");
                                startActivity(intent);
                            } else {
                                Toast.makeText(UnlockPDF.this,"error",Toast.LENGTH_LONG).show();
                            }
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