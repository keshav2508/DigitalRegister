package ds.docusheet.table;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.javascript.Scriptable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ds.docusheet.table.GetDocument.cellColumnArrayList;

public class formulae extends AppCompatActivity {
    private final String TAG = "formulae";
    Button buttonadd, buttonsub, buttondiv, buttonx, buttondeci, buttoncol, buttonC;
    Button button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    Button save;
    String doc_id, doc_name, col_num;
    TextView edittext;
    LinearLayout calc;
    FrameLayout frame;
    ListView listView;
    ListAdapter adapter;
    List<Integer>[] lists = new List[10];
    String finalformula = "";
    ImageView back;
    String[][] tempdata=new String[40][20];
    String[] formulaes;
    String[] ans;
    boolean lastInput=false;
    String rows;
    StringBuilder formulaForColumn = null;
    StringBuilder indexForColumn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulae);
        MixpanelAPI mixpanel =
                MixpanelAPI.getInstance(this,"0b359b9b0d40bb01e6381e29c23f1310");

        mixpanel.track("On Resume",null);
        mixpanel.flush();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Set Formula");
        actionBar.show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Arrays.setAll(lists, ArrayList::new);
        }

        doc_id = getIntent().getStringExtra("doc_id");
        doc_name = getIntent().getStringExtra("doc_name");
        col_num = getIntent().getStringExtra("column_num");
//        Log.d(TAG, "onCreate: " + col_num);
        rows = getIntent().getStringExtra("rows");
        listView = findViewById(R.id.listview);
        back = findViewById(R.id.back);
        buttoncol = findViewById(R.id.buttoncol);
        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);
        button9 = findViewById(R.id.button9);
        buttondeci = findViewById(R.id.buttondeci);
        buttonx = findViewById(R.id.buttonx);
        buttonadd = findViewById(R.id.buttonadd);
        buttonsub = findViewById(R.id.buttonsub);
        buttondiv = findViewById(R.id.buttondiv);
        buttonC = findViewById(R.id.buttonC);
        edittext = findViewById(R.id.editText3);
        save = findViewById(R.id.save_formulae);
        calc = findViewById(R.id.calc);
        frame = findViewById(R.id.frame);
        formulaes = new String[Integer.parseInt(rows)];
        for(int j=0;j<Integer.parseInt(rows);j++)
        {
            formulaes[j]="";
        }
                             final String url = "http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/get-document-serial/"+doc_id;
                             final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                     (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                                         @Override
                                         public void onResponse(JSONObject response) {

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
                                                 }
                                                 //Toast.makeText(getContext(), ""+k, Toast.LENGTH_SHORT).show();
                                             } catch (JSONException e) {
                                                 //Toast.makeText(getContext(),""+e,Toast.LENGTH_LONG).show();
                                             }
                                         }
                                     }, new Response.ErrorListener() {
                                         @Override
                                         public void onErrorResponse(VolleyError error) {
                                             //Toast.makeText(getContext(),""+error,Toast.LENGTH_LONG).show();
                                         }
                                     });
                             MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);


                    back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calc.setVisibility(View.VISIBLE);
                frame.setVisibility(View.GONE);
            }
        });
        buttoncol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calc.setVisibility(View.GONE);
                frame.setVisibility(View.VISIBLE);
                ArrayList<String> listItem = new ArrayList<>();
                ArrayList<Integer> clicks=new ArrayList<>();
                if (indexForColumn == null)
                    indexForColumn = new StringBuilder();
                for (int i = 0; i < cellColumnArrayList.size(); i++) {
                    if(cellColumnArrayList.get(i).getColumn_type().equals("number")||cellColumnArrayList.get(i).getColumn_type().equals("rupees"))
                    {
                        listItem.add(cellColumnArrayList.get(i).getColumn_name());
//                        Log.d(TAG, "onClick: "  + "Col id : "+ cellColumnArrayList.get(i).getCol_id());
                        clicks.add(i);
                    }
                }
                if (listItem.size() == 0){
                    Toast.makeText(formulae.this," Change the column type to Number or Rupees to use in the formula.",Toast.LENGTH_LONG).show();
                    calc.setVisibility(View.VISIBLE);
                    frame.setVisibility(View.GONE);
                    return;
                }
                adapter = new ColAdapter(formulae.this, listItem);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (!lastInput) {
//                            if (formulaForColumn == null)
//                                formulaForColumn = new StringBuilder();
                            indexForColumn.append("<").append(cellColumnArrayList.get(clicks.get(i)).getColIndex()).append(">");
                            Log.d(TAG, "onClick: " + "indexForColumn : " + indexForColumn);
                            String text = edittext.getText().toString();
                            edittext.setText(text + listItem.get(i));
                            for (int j = 0; j < Integer.parseInt(rows); j++) {
                                if ("".equals(tempdata[j][clicks.get(i)])) {
                                    String temp = finalformula + "0";
                                    formulaes[j] = formulaes[j] + temp;
                                } else if (" ".equals(tempdata[j][clicks.get(i)])) {
                                    String temp = finalformula + "0";
                                    formulaes[j] = formulaes[j] + temp;
                                } else {
                                    String temp = finalformula + tempdata[j][clicks.get(i)];
                                    formulaes[j] = formulaes[j] + temp;
                                }
                            }
//                            Log.d("Final Formula", "onItemClick: " + finalformula);
                            finalformula = "";
                            lastInput = true;
                        }
                        else{
                            Toast.makeText(formulae.this,"can't input choose some operator (+,-,/,X)",Toast.LENGTH_LONG).show();
                        }
                        calc.setVisibility(View.VISIBLE);
                        frame.setVisibility(View.GONE);
                    }
                });
            }
        });

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!lastInput) {
                    String text = edittext.getText().toString();
                    edittext.setText(text + "0");
                    indexForColumn.append("0");
                    for (int j = 0; j < Integer.parseInt(rows); j++) {
                        formulaes[j] = formulaes[j] + "0";
                    }
                    lastInput = true;
                }
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!lastInput) {
                String text = edittext.getText().toString();
                indexForColumn.append("1");
                edittext.setText(text + "1");
                for(int j=0;j<Integer.parseInt(rows);j++)
                {
                    formulaes[j]=formulaes[j]+"1";
                }
                lastInput=true;
            }}
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!lastInput) {
                String text = edittext.getText().toString();
                edittext.setText(text + "2");
                indexForColumn.append("2");
                for(int j=0;j<Integer.parseInt(rows);j++)
                {
                    formulaes[j]=formulaes[j]+"2";
                }
                lastInput=true;
            }}
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!lastInput) {
                String text = edittext.getText().toString();
                indexForColumn.append("3");
                edittext.setText(text + "3");
                for(int j=0;j<Integer.parseInt(rows);j++)
                {
                    formulaes[j]=formulaes[j]+"3";
                }
                lastInput=true;
            }}
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!lastInput) {
                String text = edittext.getText().toString();
                edittext.setText(text + "4");
                indexForColumn.append("4");
                for(int j=0;j<Integer.parseInt(rows);j++)
                {
                    formulaes[j]=formulaes[j]+"4";
                }
                lastInput=true;
            }}
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!lastInput) {
                String text = edittext.getText().toString();
                edittext.setText(text + "5");
                indexForColumn.append("5");
                for(int j=0;j<Integer.parseInt(rows);j++)
                {
                    formulaes[j]=formulaes[j]+"5";
                }
                lastInput=true;
            }}
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!lastInput) {
                String text = edittext.getText().toString();
                edittext.setText(text + "6");
                indexForColumn.append("6");
                for(int j=0;j<Integer.parseInt(rows);j++)
                {
                    formulaes[j]=formulaes[j]+"6";
                }
                lastInput=true;
            }}
        });
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!lastInput) {
                String text = edittext.getText().toString();
                edittext.setText(text + "7");
                indexForColumn.append("7");
                for(int j=0;j<Integer.parseInt(rows);j++)
                {
                    formulaes[j]=formulaes[j]+"7";
                }
                lastInput=true;
            }}
        });
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!lastInput) {
                String text = edittext.getText().toString();
                edittext.setText(text + "8");
                indexForColumn.append("8");
                for(int j=0;j<Integer.parseInt(rows);j++)
                {
                    formulaes[j]=formulaes[j]+"8";
                }
                lastInput=true;
            }}
        });
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!lastInput) {
                String text = edittext.getText().toString();
                edittext.setText(text + "9");
                indexForColumn.append("9");
                for(int j=0;j<Integer.parseInt(rows);j++)
                {
                    formulaes[j]=formulaes[j]+"9";
                }
                lastInput=true;
            }}
        });
        buttondeci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = edittext.getText().toString();
                if(text.equals("")){

                }else if(lastInput){
                    edittext.setText(text + ".");
                    indexForColumn.append(".");
                    for (int j = 0; j < Integer.parseInt(rows); j++) {
                        formulaes[j] = formulaes[j] + ".";
                    }
                    lastInput=false;
                }
            }
        });
        buttonadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = edittext.getText().toString();
              if(text.equals("")){

              }else if(lastInput){
                edittext.setText(text + "+");
                indexForColumn.append("+");
                for(int j=0;j<Integer.parseInt(rows);j++)
                {
                    formulaes[j]=formulaes[j]+"+";
                }
                  lastInput=false;
            }}
        });
        buttonsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = edittext.getText().toString();
               if(text.equals("")){

               }else if(lastInput){
                edittext.setText(text + "-");
                indexForColumn.append("-");
                for(int j=0;j<Integer.parseInt(rows);j++)
                {
                    formulaes[j]=formulaes[j]+"-";
                }
                   lastInput=false;
            }}
        });
        buttonx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = edittext.getText().toString();
                if(text.equals("")){

                }
                else if(lastInput)
                {
                edittext.setText(text + "*");
                indexForColumn.append("*");
                for(int j=0;j<Integer.parseInt(rows);j++)
                {
                    formulaes[j]=formulaes[j]+"*";
                }
                lastInput=false;

                //Toast.makeText(formulae.this, ""+muldiv, Toast.LENGTH_SHORT).show();
            }}
        });
        buttondiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = edittext.getText().toString();
                if(text.equals(""))
                {

                }
                else if(lastInput){
                    edittext.setText(text + "/");
                    indexForColumn.append("/");
                    for (int j = 0; j < Integer.parseInt(rows); j++) {
                        formulaes[j] = formulaes[j] + "/";
                    }
                    lastInput=false;
                }
            }
        });
        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edittext.setText("");
                indexForColumn.delete(0,indexForColumn.length());
                finalformula = "";
                for(int j=0;j<Integer.parseInt(rows);j++)
                {
                    formulaes[j]="";
                }
                lastInput=false;
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                org.mozilla.javascript.Context rhino = org.mozilla.javascript.Context.enter();
                rhino.setLanguageVersion(org.mozilla.javascript.Context.VERSION_1_2);
                rhino.setOptimizationLevel(-1);
                Scriptable scope = rhino.initStandardObjects();
                if (formulaForColumn == null)
                    formulaForColumn = new StringBuilder();
                formulaForColumn.append(edittext.getText().toString());
                Log.d("Final Formulae", "onClick: " + formulaForColumn.toString());
                ans=new String[formulaes.length];
                for (int i = 0; i < formulaes.length; i++) {
                    Log.d("Formula", "onClick: " + formulaes[i]);
                }
                for(int j=0;j<formulaes.length;j++) {
//                    Log.d("Formula", "onClick: " + formulaes[j]);
                    String temp="";
                    for(int k=0;k<formulaes[j].length();k++)
                    {
                        if(formulaes[j].charAt(k)=='\u20B9')
                            continue;
                        temp=temp+formulaes[j].charAt(k);
                    }
                    Object result = rhino.evaluateString(scope, temp, "test", 1, null);
//                    if(cellColumnArrayList.get(Integer.valueOf(col_num)-1).getColumn_type().equals("rupees"))
//                    ans[j] = "\u20B9"+result.toString();
//                    else
                    ans[j]=result.toString();
                }
                String url="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/change-column-data";
                String formulaUrl = "http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/add-formula";
                RequestQueue queue= Volley.newRequestQueue(getApplicationContext());

                JSONObject formulaObject = new JSONObject();
                try {
                    formulaObject.put("col_id",getIntent().getStringExtra("col_id"));
//                    Log.d("Column Id", "onClick: " + getIntent().getStringExtra("col_id"));
//                    Log.d("Doc Id", "onClick: " + doc_id);
                    formulaObject.put("formula",indexForColumn.toString());
                    //Toast.makeText(getApplicationContext(),getIntent().getStringExtra("col_id"),Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onClick id: "+getIntent().getStringExtra("col_id"));
                    JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.PUT, formulaUrl, formulaObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
//                            Log.d("Putting formula", "onResponse: " + "Successful");
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                            Log.d("Putting formula", "onResponse: " + error.getMessage());
                        }
                    });
                    queue.add(jsonObjectRequest1);
                } catch (JSONException e) {
//                    Log.d("Formula exception", "onClick: " + e.getMessage());
                    e.printStackTrace();
                }
                try {
                    JSONObject object=new JSONObject();
                    //Toast.makeText(formulae.this, getIntent().getStringExtra("column_num"), Toast.LENGTH_SHORT).show();
                    object.put("current_col_number",getIntent().getStringExtra("column_num"));
                    JSONArray jsonArray=new JSONArray();
                    for(int i=0;i<ans.length;i++)
                        jsonArray.put(ans[i]);
                    object.put("data",jsonArray);
                    object.put("doc_id",doc_id);
                    //Toast.makeText(ColumnClicked.this, object.toString(), Toast.LENGTH_SHORT).show();
                    JsonObjectRequest request=new JsonObjectRequest(Request.Method.PUT,url,object,new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //Toast.makeText(formulae.this, "Success", Toast.LENGTH_SHORT).show();
                            //dialog.dismiss();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                           // Toast.makeText(formulae.this,""+error, Toast.LENGTH_SHORT).show();
                        }
                    });
                    queue.add(request);
                    Intent intent=new Intent(formulae.this,GetDocument.class);
                    intent.putExtra("doc_id",doc_id);
                    intent.putExtra("doc_name",doc_name);
                    finish();
                    startActivity(intent);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(formulae.this,""+e, Toast.LENGTH_SHORT).show();
                }
                formulaForColumn = null;
                indexForColumn = null;
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp()
    {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}

class ColAdapter extends BaseAdapter {
    Context context;
    private ArrayList<String> listItem;

    public ColAdapter(Context context, ArrayList<String> listItem) {
        this.context = context;
        this.listItem = listItem;
    }

    @Override
    public int getCount() {
        return listItem.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.columnmenuitem, parent, false);
            viewHolder.listname = convertView.findViewById(R.id.textItem);
            result = convertView;
            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        viewHolder.listname.setText(listItem.get(position));
        return convertView;
    }

    public static class ViewHolder {
        TextView listname;
    }
}



