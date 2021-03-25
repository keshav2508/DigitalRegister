package ds.docusheet.table;

import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderMain extends RecyclerView.ViewHolder
{
    TextView Name;
    ImageButton imageButton;
    RelativeLayout homePageTemplateRelativeLayout;
    RecyclerView homePageTemplateRecyclerView;
    TextView viewMoreButton;
    TextView usePopularTemplates;
    ViewHolderMain(View itemView, final AdapterMain.OnItemClickListener listener)
    {
        super(itemView);
        Name=itemView.findViewById(R.id.doc_id);
        imageButton=itemView.findViewById(R.id.imageButton);
        homePageTemplateRelativeLayout = itemView.findViewById(R.id.homePageTemplateRelativeLayout);
        homePageTemplateRecyclerView = itemView.findViewById(R.id.homePageTemplateRecyclerView);
        viewMoreButton = itemView.findViewById(R.id.viewMoreButton);
        usePopularTemplates = itemView.findViewById(R.id.popularTemplates);
        itemView.setOnClickListener(v -> {
            if(listener!=null)
            {
                int position=getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION){
                    listener.onItemClick(position);
                }
            }
        });
    }
}