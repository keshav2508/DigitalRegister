package ds.docusheet.table;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.Executor;


public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    public static int selected_frag = 0; // 0- home, 1- temp
    BiometricPrompt biometricPrompt;
    Toolbar toolbar;
    BiometricPrompt.PromptInfo promptInfo;
    boolean finger_lock, stop;
    String count = "0";
    ImageView youtubeImageView;
    TextView youtubeTextView;


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (!getSharedPreferences("Lock_pref", MODE_PRIVATE).getBoolean("stop", true)) {
            getSharedPreferences("Lock_pref", MODE_PRIVATE).edit().putBoolean("stop", true).apply();
            stop = true;
            //Toast.makeText(this, "Stopped "+stop, Toast.LENGTH_SHORT).show();
        }
        super.onStop();
    }

    public void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        this.getResources().updateConfiguration(configuration, this.getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = this.getSharedPreferences("LangSettings", Context.MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    private void loadLocale() {
        Intent intent = getIntent();
        String language = intent.getStringExtra("language");
        SharedPreferences prefs = getSharedPreferences("LangSettings", Activity.MODE_PRIVATE);
        String lang = prefs.getString("My_Lang", language);
        setLocale(lang);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        generatenotifications();
        loadLocale();
        setContentView(R.layout.activity_main);
        loadLocale();

        youtubeImageView = findViewById(R.id.youtubeImage);
        youtubeImageView.setOnClickListener(view -> goToUrl("https://youtu.be/3uIVlLEfah8"));

        youtubeTextView = findViewById(R.id.youtubeTextView);
        youtubeTextView.setOnClickListener(view -> goToUrl("https://youtu.be/3uIVlLEfah8"));

        MixpanelAPI mixpanel =
                MixpanelAPI.getInstance(this, "0b359b9b0d40bb01e6381e29c23f1310");

        mixpanel.track("On Resume", null);
        mixpanel.flush();

        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
        setSupportActionBar(toolbar);
        invalidateOptionsMenu();
        toolbar.setOnMenuItemClickListener(toolListener);

        String url = "http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/get-refer-count/" + FirebaseAuth.getInstance().getUid();
        RequestQueue rqueue = Volley.newRequestQueue(this);
        StringRequest srequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                count = response;
            }
        }, error -> {
            try {
                if (!isConnected()) {

                } else
                    Toast.makeText(MainActivity.this, "error" + error.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        rqueue.add(srequest);

        finger_lock = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("finger_lock", false);
        stop = getSharedPreferences("Lock_pref", MODE_PRIVATE).getBoolean("stop", true);
        //toolbar.inflateMenu(R.menu.settings_menu);
        BiometricManager biometricManager = BiometricManager.from(MainActivity.this);
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS: //Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(this, "No Sensor", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(this, "the hardware is unavailable. Try again later.", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(this, "No fingerprint Registered. Please register", Toast.LENGTH_SHORT).show();
                break;

        }
        Executor executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                if (errString.equals("BIOMETRIC_ERROR_HW_NOT_PRESENT"))
                    Toast.makeText(MainActivity.this, "device does not have a biometric sensor", Toast.LENGTH_SHORT).show();
                else if (errString.equals("BIOMETRIC_ERROR_NO_BIOMETRICS"))
                    Toast.makeText(MainActivity.this, "user does not have any biometrics enrolled.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                //Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                if (finger_lock) {
                    //biometricPrompt.authenticate(promptInfo);
                    //menuItem.setTitle("Disable Fingerprint Lock");
                    getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("finger_lock", false).apply();
                    finger_lock = false;
                } else {
                    //biometricPrompt.authenticate(promptInfo);
                    //menuItem.setTitle("Enable Fingerprint Lock");
                    getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("finger_lock", true).apply();
                    finger_lock = true;
                }

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Login").setDescription("Place your finger").setNegativeButtonText("Cancel").build();


        if (isStoragePermissionGranted()) {
            File theDir = new File(Environment.getExternalStorageDirectory().getPath() + "/Digital Register/database");
            if (!theDir.exists()) {
                theDir.mkdirs();
            }
            File source = new File("/data/user/0/ds.docusheet.table/databases/document");
            //  File source1 = new File("/data/user/0/ds.docusheet.table/databases/saveddata");
            // File source2 = new File("/data/user/0/ds.docusheet.table/databases/column_details");
            File dest = new File(Environment.getExternalStorageDirectory().getPath() + "/Digital Register/database/document");
            //File dest1 = new File(Environment.getExternalStorageDirectory().getPath() + "/Digital Register/database/saveddata");
            //File dest2 = new File(Environment.getExternalStorageDirectory().getPath() + "/Digital Register/database/column_details");

            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(source);

                FileOutputStream fileOutputStream = new FileOutputStream(dest);
                int bufferSize;
                byte[] bufffer = new byte[512];
                while ((bufferSize = fileInputStream.read(bufffer)) > 0) {
                    fileOutputStream.write(bufffer, 0, bufferSize);
                }
                // fileInputStream = new FileInputStream(source1);

                //fileOutputStream = new FileOutputStream(dest1);
                // bufferSize=0;
                //bufffer = new byte[512];
                //while ((bufferSize = fileInputStream.read(bufffer)) > 0) {
                //    fileOutputStream.write(bufffer, 0, bufferSize);
                //}
                //fileInputStream = new FileInputStream(source2);

                //fileOutputStream = new FileOutputStream(dest2);
                // bufferSize=0;
                // bufffer = new byte[512];
                //while ((bufferSize = fileInputStream.read(bufffer)) > 0) {
                //      fileOutputStream.write(bufffer, 0, bufferSize);
                //     }
                fileInputStream.close();
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                Toast.makeText(MainActivity.this, "" + e, Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(MainActivity.this, "" + e, Toast.LENGTH_LONG).show();
            }
        }


    }


    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        loadLocale();
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        loadLocale();
        MenuItem item = menu.findItem(R.id.finger_print);
        if (finger_lock) {
            item.setTitle(getResources().getString(R.string.disablefpl));
        } else {
            item.setTitle(getResources().getString(R.string.enablefpl));
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private Toolbar.OnMenuItemClickListener toolListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.change_lang:
                    CustomDialogClassLanguage cdd = new CustomDialogClassLanguage(MainActivity.this);
                    cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    cdd.show();
                    break;

                case R.id.help_feedback:
                    String number = "+91 9981278197";
                    //getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new HomeFragment()).commit();
                    String url = "https://api.whatsapp.com/send?phone=" + number;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    //onPause();
                    startActivity(i);
                    break;
                case R.id.share_app://getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new HomeFragment()).commit();
                    try {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Digital Register");
                        String shareMessage = "\nLet me recommend you this application\n\n";
                        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                        startActivity(Intent.createChooser(shareIntent, "Choose One"));
                    } catch (Exception e) {
                        //e.toString();
                    }
                    break;
                case R.id.finger_print:
                    biometricPrompt.authenticate(promptInfo);
                    /*if(finger_lock){

                        menuItem.setTitle("Disable Fingerprint Lock");
                        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("finger_lock",false).apply();
                        finger_lock=false;
                    }
                    else {
                        biometricPrompt.authenticate(promptInfo);
                        menuItem.setTitle("Enable Fingerprint Lock");
                        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("finger_lock",true).apply();
                        finger_lock=true;
                    }*/
                    break;
                default://getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new HomeFragment()).commit();
                    break;
            }
            return false;
        }


    };


    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            loadLocale();
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.home:
                    selectedFragment = new HomeFragment();
                    selected_frag = 0;
                    break;
                case R.id.template:
                    selectedFragment = new TemplateFragment();
                    selected_frag = 1;
                    break;
                case R.id.pdf:
                    if (count.equals("3")) {
                        selectedFragment = new CompletePDFVIP();
                        selected_frag = 2;
                        break;
                    } else {
                        selectedFragment = new PdfFragment();
                        selected_frag = 2;
                        break;
                    }

            }
            Log.d(String.valueOf(selected_frag), "babbar");
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, selectedFragment).commit();
            return true;
        }
    };

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("EXIT");
        alert.setMessage("Do you want to exit the application?");
        alert.setCancelable(false);
        alert.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
                startActivity(a);

            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();

    }

    private void goToUrl(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(String.valueOf(selected_frag), "babbar");
        if (selected_frag == 0)
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
        else if (selected_frag == 1)
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new TemplateFragment()).commit();
        else {
            if (count.equals("3"))
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new PdfFragment()).commit();
            else
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new CompletePDFVIP()).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(String.valueOf(selected_frag), "babbar");
        if (selected_frag == 0)
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
        else if (selected_frag == 1)
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new TemplateFragment()).commit();
        else {
            if (count.equals("3"))
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new CompletePDFVIP()).commit();
            else
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new PdfFragment()).commit();
        }
    }

    public boolean isConnected() throws InterruptedException, IOException {
        String command = "ping -c 1 google.com";
        return Runtime.getRuntime().exec(command).waitFor() == 0;
    }

    public boolean isStoragePermissionGranted() {
        String Tag = "Permission Granted";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v(Tag, "Permission is granted");
                return true;
            } else {
                Log.v(Tag, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            Log.v(Tag, "Permision is granted");
            return true;
        }
    }

    public void generatenotifications()
    {
        String url="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/get-user/"+FirebaseAuth.getInstance().getUid();
        RequestQueue requestQueue=Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getString("notification").equals("Yes"))
                    {
                        Alaramhelper.setalarm(getApplicationContext());
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                }
            }
        }, error -> {

        });
        requestQueue.add(jsonObjectRequest);
        String url1="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/update-notification/"+FirebaseAuth.getInstance().getUid()+"/"+"No";
        RequestQueue requestQueue1=Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.PUT, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, error -> {

        });
        requestQueue.add(stringRequest);
    }
}