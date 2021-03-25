package ds.docusheet.table;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomePageTemplateAdapter extends RecyclerView.Adapter<HomePageTemplateAdapter.holder> {
    private OnItemClickListener mlistener;
    Context context;
    String[] name;
    int[] img;
    public HomePageTemplateAdapter(Context context,String[] name , int[] img) {
        this.name = name;
        this.img = img;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mlistener = listener;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.r_view,parent,false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
    String namedata = name[position];
      int imgdata = img[position];
      holder.imageView.setImageResource(imgdata);
      holder.textView.setText(namedata);

      holder.imageView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
                Intent templateDocIntent = new Intent(context,TemplateDocument.class);
                templateDocIntent.putExtra("heading",name[position]);
                templateDocIntent.putExtra("temp_num",0);
                int cat_num = position;
                if (cat_num == 3)
                    cat_num += 1;
                templateDocIntent.putExtra("cat_num",cat_num);
                context.startActivity(templateDocIntent);
          }
      });

      holder.textView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent templateDocIntent = new Intent(context,TemplateDocument.class);
              templateDocIntent.putExtra("heading",name[position]);
              templateDocIntent.putExtra("temp_num",0);
              int cat_num = position;
              if (cat_num == 3)
                  cat_num += 1;
              templateDocIntent.putExtra("cat_num",cat_num);
              context.startActivity(templateDocIntent);
          }
      });
    }

    @Override
    public int getItemCount() {
        return name.length;
    }

    public class holder extends RecyclerView.ViewHolder{
         ImageView imageView;
         TextView textView;
        public holder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image_view);
            textView=itemView.findViewById(R.id.text_view);
        }
    }
}
