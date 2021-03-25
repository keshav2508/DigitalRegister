package ds.docusheet.table;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.hbb20.CountryCodePicker;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

public class Login extends AppCompatActivity {
    Button button;
    EditText phone;
    CountryCodePicker ccp;
    String code = "91", number;
    BiometricPrompt biometricPrompt;
    androidx.appcompat.widget.Toolbar toolbar;
    BiometricPrompt.PromptInfo promptInfo;
    static boolean first, first_r, firstrun;
    String referral_userid = "no";
    GoogleApiClient googleApiClient;
    private static final int RC_HINT=1000;
    @Override
    protected void onStart() {
        if (getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("finger_lock", false) && getSharedPreferences("Lock_pref", MODE_PRIVATE).getBoolean("stop", true) && FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(Login.this, FingerPrint.class);
            startActivity(intent);
            finish();
        } else {

            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MixpanelAPI mixpanel =
                MixpanelAPI.getInstance(this, "0b359b9b0d40bb01e6381e29c23f1310");

        mixpanel.track("On Resume", null);
        mixpanel.flush();
        button = findViewById(R.id.button);
        phone = findViewById(R.id.editText2);
        ccp = findViewById(R.id.ccp);
        googleApiClient=new GoogleApiClient.Builder(this).addApi(Auth.CREDENTIALS_API).build();
        try {
            requestPhoneNumber();
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, pendingDynamicLinkData -> {
                    // Get deep link from result (may be null if no link is found)
                    Uri deepLink = null;
                    if (pendingDynamicLinkData != null) {
                        deepLink = pendingDynamicLinkData.getLink();

                        assert deepLink != null;
                        int i = deepLink.toString().indexOf("id") + 3;
                        String user_id = "";
                        for (int j = i; j < deepLink.toString().length(); j++) {
                            if (deepLink.toString().charAt(j) != '/') {
                                user_id += deepLink.toString().charAt(j);
                            } else break;
                        }
                        referral_userid = user_id;
                    }
                })
                .addOnFailureListener(this, e -> {

                });
        firstrun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("firstrun", true);
        //Toast.makeText(this, ""+firstrun, Toast.LENGTH_SHORT).show();
        first = getSharedPreferences("PREF", MODE_PRIVATE).getBoolean("first", true);
        //Toast.makeText(this, ""+first, Toast.LENGTH_SHORT).show();
        if (firstrun) {
            //CustomDialogClassLanguage cdd = new CustomDialogClassLanguage(this);
            //cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //cdd.show();
            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("firstrun", false).apply();
            firstrun = false;
        } else {
            getSharedPreferences("PREF", MODE_PRIVATE).edit().putBoolean("first", false).apply();
            first = false;
        }


        ccp.setOnCountryChangeListener(() -> code = ccp.getSelectedCountryCode());

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(phone.getText().toString().length()>0)
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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Debug code
               // startActivity(new Intent(Login.this,GetDocument.class));
                //end here
                number = phone.getText().toString().trim();
                if (code == null) {
                    Toast.makeText(Login.this, "Number is Null", Toast.LENGTH_SHORT).show();
                } else if (number.isEmpty()) {
                    phone.setError("Cannot be empty");
                    phone.requestFocus();
                }
                    else if(number.length() != 10)
                    {
                        Toast.makeText(Login.this, "Number is not correct", Toast.LENGTH_SHORT).show();
                    }
                 else {
                    String phoneNumber = "+" + code + number;
                    Intent intent = new Intent(Login.this, Otp.class);
                    intent.putExtra("phoneNumber", phoneNumber);
                    intent.putExtra("referral_userid",referral_userid);
                    startActivity(intent);
                }
            }
        });
    }

    public void requestPhoneNumber() throws IntentSender.SendIntentException {
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();
        PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(
                googleApiClient, hintRequest);
        startIntentSenderForResult(intent.getIntentSender(),
                RC_HINT, null, 0, 0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_HINT){
            if(resultCode==RESULT_OK){
                assert data != null;
                Credential credential=data.getParcelableExtra(Credential.EXTRA_KEY);
                phone.setText(credential.getId().substring(3,13));
            }
        }
    }

}