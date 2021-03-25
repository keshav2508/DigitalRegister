package ds.docusheet.table;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import ds.docusheet.table.database.DatabaseClass;

import static ds.docusheet.table.GetDocument.cells5;


public class columnClicked extends AppCompatActivity {
    ListView listView;
    ListAdapter adapter;
    String heading;
    int col_id;
    String col_name,doc_name,doc_id,column_num,total_col,col_type;
    String rows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_column_clicked);
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(this,"0b359b9b0d40bb01e6381e29c23f1310");
        mixpanel.track("On Resume",null);
        mixpanel.flush();

        listView=findViewById(R.id.listview);
        heading = getIntent().getStringExtra("col_name");
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(heading);
        actionBar.show();

        col_id=getIntent().getIntExtra("col_id",-1);
        col_name=getIntent().getStringExtra("col_name");
        col_type=getIntent().getStringExtra("col_type");
        doc_id=getIntent().getStringExtra("doc_id");
        doc_name=getIntent().getStringExtra("doc_name");
        column_num=getIntent().getStringExtra("column_num");
        total_col=getIntent().getStringExtra("total_col");
        rows=getIntent().getStringExtra("rows");
        final String[] listItem=new String[]{
                getResources().getString(R.string.rename_column),getResources().getString(R.string.change_type),getResources().getString(R.string.show_total),getResources().getString(R.string.setformula),getResources().getString(R.string.delete_column)
        };
        int [] imageItem={
                R.drawable.ic_baseline_edit_24,R.drawable.ic_baseline_merge_type_24,R.drawable.ic_baseline_add_24,R.drawable.ic_baseline_calculate_24 ,R.drawable.ic_baseline_delete_24
        };
        int [] color={Color.BLACK,Color.BLACK,Color.BLACK,Color.BLACK,Color.RED};
        if(!col_type.equals("rupees")&&!col_type.equals("number"))
        {
            color[color.length-2]=Color.GRAY;
            color[color.length-3]=Color.GRAY;
        }
        listView=findViewById(R.id.listview);
        adapter=new MyAdapter(columnClicked.this,listItem,imageItem,color);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if(position==2)
            {
                if(color[color.length-3]==Color.GRAY)
                {
                    Toast.makeText(getApplicationContext(),"Change column type to Number or Rupees to show total",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(columnClicked.this, GetDocument.class);
                    if (cells5.size() >= 1) {
                        if (cells5.get(cells5.size() - 1).getDoc_id() != Integer.parseInt(doc_id))
                            cells5.clear();
                    }
                    cells5.add(new cell(String.valueOf(Integer.parseInt(column_num) - 1), Integer.parseInt(doc_id)));
                    intent.putExtra("doc_id", doc_id);
                    intent.putExtra("doc_name", doc_name);
                    intent.putExtra("col_num", Integer.parseInt(column_num) - 1);
                    intent.putExtra("setting", -1);
                    startActivity(intent);
                    finish();
                }

            }
            else if(position==1){
                Intent intent = new Intent(columnClicked.this, columnsSetting.class);
                intent.putExtra("heading", listItem[position]);
                intent.putExtra("setting", String.valueOf(position));
                intent.putExtra("col_id", col_id);
                intent.putExtra("col_name", col_type);
                intent.putExtra("doc_id", doc_id);
                intent.putExtra("doc_name", doc_name);
                intent.putExtra("total_col", total_col);
                intent.putExtra("column_num", column_num);
                startActivity(intent);

            }
            else if (position == 4) {

                AlertDialog.Builder alert = new AlertDialog.Builder(columnClicked.this);
                alert.setTitle("DELETE");
                alert.setMessage("Do you want to delete the column?");
                alert.setCancelable(false);
                alert.setPositiveButton("Yes, Delete", (dialog, which) -> {

                    DatabaseClass.getDatabaseColumns(this).getColumnDetails().delete(col_id);

                    String url="http://ec2-3-16-83-100.us-east-2.compute.amazonaws.com:8080/delete-column/"+doc_id;
                    RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
                    try {
                        JSONObject object=new JSONObject();
                        object.put("column_nums",total_col);
                        object.put("deleted_column_num",column_num);
                        object.put("id",String.valueOf(col_id));
                        //Toast.makeText(ColumnClicked.this, object.toString(), Toast.LENGTH_SHORT).show();
                        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST,url,object, response -> {
                        }, error -> {

                        });
                        queue.add(request);
                        Intent intent=new Intent(columnClicked.this,GetDocument.class);
                        intent.putExtra("doc_id",doc_id);
                        intent.putExtra("doc_name",doc_name);
                        finish();
                        startActivity(intent);

                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                        //Toast.makeText(ColumnClicked.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                alert.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                AlertDialog dialog = alert.create();
                dialog.show();


            }
            else if(position==3){
                if(color[color.length-2]==Color.GRAY)
                {
                    Toast.makeText(getApplicationContext(),"Change column type to Number or Rupees to set formula",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(columnClicked.this, formulae.class);
                    intent.putExtra("doc_id", doc_id);
                    intent.putExtra("doc_name", doc_name);
                    intent.putExtra("column_num", column_num);
                    intent.putExtra("col_id",String.valueOf(col_id));
                    intent.putExtra("rows", rows);
                    startActivity(intent);
                }
            }
            else {
                Intent intent = new Intent(columnClicked.this, columnsSetting.class);
                intent.putExtra("heading", listItem[position]);
                intent.putExtra("setting", String.valueOf(position));
                intent.putExtra("col_id", col_id);
                intent.putExtra("col_name", col_name);
                intent.putExtra("doc_id", doc_id);
                intent.putExtra("doc_name", doc_name);
                intent.putExtra("total_col", total_col);
                intent.putExtra("column_num", column_num);
                startActivity(intent);
            }
        });

    }
    @Override
    public boolean onSupportNavigateUp()
    {
        finish();
        return true;
    }
}
