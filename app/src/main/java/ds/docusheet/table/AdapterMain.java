package ds.docusheet.table;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ds.docusheet.table.database.DocumentEntity;

public class AdapterMain extends RecyclerView.Adapter<ViewHolderMain> {
    private OnItemClickListener mlistener;
    Context context;
    List<DocumentEntity> list;
    HomePageTemplateAdapter homePageTemplateAdapter;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mlistener = listener;
    }

    public AdapterMain(List<DocumentEntity> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolderMain onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View photoView = inflater.inflate(R.layout.mainlayout, parent, false);
        ViewHolderMain viewHolder = new ViewHolderMain(photoView, mlistener);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ViewHolderMain viewHolder, int position) {
        viewHolder.Name.setText(list.get(position).getDoc_name());
        if (position ==0){
            viewHolder.homePageTemplateRelativeLayout.setVisibility(View.VISIBLE);
            String[] strings={context.getResources().getString(R.string.cashbook) , context.getResources().getString(R.string.payment_received),
                    context.getResources().getString(R.string.daily_spend),context.getResources().getString(R.string.cashbook)};
            int[] drawables={R.drawable.cash,R.drawable.payment_received,R.drawable.payment_spend,R.drawable.order};
            homePageTemplateAdapter = new HomePageTemplateAdapter(context,strings,drawables);

            viewHolder.homePageTemplateRecyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
            viewHolder.homePageTemplateRecyclerView.setAdapter(homePageTemplateAdapter);
            viewHolder.homePageTemplateRecyclerView.setVisibility(View.VISIBLE);
            viewHolder.usePopularTemplates.setVisibility(View.VISIBLE);
            viewHolder.viewMoreButton.setVisibility(View.VISIBLE);
            viewHolder.viewMoreButton.setOnClickListener(v -> {
                //getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new TemplateFragment()).commit();
                AppCompatActivity activity = (AppCompatActivity) context;
                Fragment myFragment = new TemplateFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, myFragment).addToBackStack(null).commit();
            });
        }
        viewHolder.imageButton.setOnClickListener(v -> {
            Context context=v.getContext();
            Intent intent=new Intent(context, DocumentSetting.class);
            intent.putExtra("doc_id",String.valueOf(list.get(position).getDoc_id()));
            intent.putExtra("doc_name",list.get(position).getDoc_name());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}

