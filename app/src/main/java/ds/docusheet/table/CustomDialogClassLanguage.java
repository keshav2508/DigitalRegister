package ds.docusheet.table;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.util.Locale;


public class CustomDialogClassLanguage extends Dialog implements
        View.OnClickListener {
    private static final String TAG = "CustomDialogClassLanguage";
    public Activity c;
    public Dialog d;
    public RadioButton english, hindi, marathi, gujarati, kannada, urdu, bengali;
    Button cancel;
    public CustomDialogClassLanguage(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_language);
        MixpanelAPI mixpanel =
                MixpanelAPI.getInstance(getContext(),"0b359b9b0d40bb01e6381e29c23f1310");

        mixpanel.track("On Resume",null);
        mixpanel.flush();
        english =findViewById(R.id.radioButtonEnglish);
        hindi = findViewById(R.id.radioButtonHindi);
        marathi = findViewById(R.id.radioButtonMarathi);
        gujarati = findViewById(R.id.radioButtonGujarati);
        kannada = findViewById(R.id.radioButtonKannada);
        urdu = findViewById(R.id.radioButtonUrdu);
        urdu.setVisibility(View.GONE);
        bengali = findViewById(R.id.radioButtonBengali);
        cancel = (Button) findViewById(R.id.language_cancel);
        cancel.setOnClickListener(this);
        english.setOnClickListener(this);
        hindi.setOnClickListener(this);
        marathi.setOnClickListener(this);
        gujarati.setOnClickListener(this);
        kannada.setOnClickListener(this);
        urdu.setOnClickListener(this);
        bengali.setOnClickListener(this);

        SharedPreferences prefs = c.getSharedPreferences("LangSettings", c.MODE_PRIVATE);
        String lang = prefs.getString("My_Lang", "");
        if(lang.equals("hi")){
            hindi.setChecked(true);
            english.setChecked(false);
            marathi.setChecked(false);
            gujarati.setChecked(false);
            kannada.setChecked(false);
            urdu.setChecked(false);
            bengali.setChecked(false);
        }
        else if (lang.equals("mr")){
            marathi.setChecked(true);
            english.setChecked(false);
            hindi.setChecked(false);
            gujarati.setChecked(false);
            kannada.setChecked(false);
            urdu.setChecked(false);
            bengali.setChecked(false);
        }
        else if (lang.equals("gu")){
            marathi.setChecked(false);
            english.setChecked(false);
            hindi.setChecked(false);
            gujarati.setChecked(true);
            kannada.setChecked(false);
            urdu.setChecked(false);
            bengali.setChecked(false);
        }
        else if (lang.equals("kn")){
            marathi.setChecked(false);
            english.setChecked(false);
            hindi.setChecked(false);
            gujarati.setChecked(false);
            kannada.setChecked(true);
            urdu.setChecked(false);
            bengali.setChecked(false);
        }
//        else if (lang.equals("ur")){
//            marathi.setChecked(false);
//            english.setChecked(false);
//            hindi.setChecked(false);
//            gujarati.setChecked(false);
//            kannada.setChecked(false);
//            urdu.setChecked(true);
//            bengali.setChecked(false);
//        }
        else if (lang.equals("bn")){
            marathi.setChecked(false);
            english.setChecked(false);
            hindi.setChecked(false);
            gujarati.setChecked(false);
            kannada.setChecked(false);
            urdu.setChecked(false);
            bengali.setChecked(true);
        }
        else{
            marathi.setChecked(false);
            english.setChecked(true);
            hindi.setChecked(false);
            gujarati.setChecked(false);
            kannada.setChecked(false);
            urdu.setChecked(false);
            bengali.setChecked(false);
        }
    }

    @Override
    public void onClick(View v) {
        Log.d("dialogCheck", "onClick: "+v.getId());
        switch (v.getId()) {
            case R.id.radioButtonEnglish:
                setLocale("");
                c.recreate();
                break;
            case R.id.radioButtonHindi:
                setLocale("hi");
                Intent i=new Intent();
                c.recreate();
                break;
            case R.id.radioButtonMarathi:
                setLocale("mr");
                c.recreate();
                break;
            case R.id.radioButtonGujarati:
                setLocale("gu");
                c.recreate();
                break;
            case R.id.radioButtonKannada:
                setLocale("kn");
                c.recreate();
                break;
//            case R.id.radioButtonUrdu:
//                setLocale("ur");
//                c.recreate();
//                break;
            case R.id.radioButtonBengali:
                setLocale("bn");
                c.recreate();
                break;
            default:
                dismiss();
                break;
        }
        dismiss();
    }
    private void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        c.getBaseContext().getResources().updateConfiguration(configuration,c.getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = c.getSharedPreferences("LangSettings", Context.MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }
}
