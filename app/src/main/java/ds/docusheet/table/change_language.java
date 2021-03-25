package ds.docusheet.table;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import worker8.com.github.radiogroupplus.RadioGroupPlus;

public class change_language extends AppCompatActivity {

    Intent intent;
    RadioGroupPlus mRadioGroupPlus;
    TextView textView;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_language);
        textView= findViewById(R.id.urdutext);
        textView.append("Urdu\n\t\t\t\t"+"اردو");
         intent=getIntent();
        String ch=intent.getStringExtra("response");
        if(ch.equals("true"))
        {
            intent=new Intent(change_language.this,MainActivity.class);
            intent.putExtra("language","en");
            startActivity(intent);
        }
        mRadioGroupPlus = findViewById(R.id.radio_group_plus);
        RadioButton rbutton= findViewById(R.id.english);
        rbutton.setChecked(true);
        button=(Button)findViewById(R.id.continuebtn);
        button.setOnClickListener(v -> {
            int select=mRadioGroupPlus.getCheckedRadioButtonId();
            if(select==R.id.bengali)
            {
                intent=new Intent(change_language.this,MainActivity.class);
                intent.putExtra("language","bn");
                openactivity();
            }
            else if(select==R.id.kannada)
            {

                intent=new Intent(change_language.this,MainActivity.class);
                intent.putExtra("language","kn");
                openactivity();

            }
            else if(select==R.id.marathi)
            {

                intent=new Intent(change_language.this,MainActivity.class);
                intent.putExtra("language","mr");
                openactivity();
            }
            else if(select==R.id.gujarati)
            {

                intent=new Intent(change_language.this,MainActivity.class);
                intent.putExtra("language","gu");
                openactivity();
            }
            else if(select==R.id.hindi)
            {

                intent=new Intent(change_language.this,MainActivity.class);
                intent.putExtra("language","hi");
                openactivity();
            }
            else if(select==R.id.urdu)
            {
                intent=new Intent(change_language.this,MainActivity.class);
                intent.putExtra("language","ur");
                openactivity();

            }
            else if(select==R.id.tamil)
            {
                intent=new Intent(change_language.this,MainActivity.class);
                intent.putExtra("language","ta");
                openactivity();

            }
            else if(select==R.id.telugu)
            {
                intent=new Intent(change_language.this,MainActivity.class);
                intent.putExtra("language","te");
                openactivity();

            }
            else if(select==R.id.malayalam)
            {
                intent=new Intent(change_language.this,MainActivity.class);
                intent.putExtra("language","ml");
                openactivity();

            }
            else if(select==R.id.punjabi)
            {
                intent=new Intent(change_language.this,MainActivity.class);
                intent.putExtra("language","pa");
                openactivity();

            }
                else
            {
                intent=new Intent(change_language.this,MainActivity.class);
                intent.putExtra("language","en");
                openactivity();
            }
        });

    }
    public  void openactivity()
    {
        startActivity(intent);
    }
}