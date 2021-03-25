package ds.docusheet.table;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter_row_dataCombined extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    int row_index=RecyclerView.NO_POSITION;
    int h=-1;
    List<cell> combined_list;
    private OnItemClickListener3 m_colTitleListener;
    Context context;
    cell cells;
    int column;
    private OnItemClickListener mlistener;
    private onItemLongClickListener llistener;

    public Adapter_row_dataCombined(Context applicationContext, List<cell> combined_list,int column) {
        this.context = applicationContext;
        this.combined_list = combined_list;
        this.column=column;

    }

    public void setOnItemClickListenerCell(OnItemClickListener listener) {
        this.mlistener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener3 listener) {
        this.m_colTitleListener = listener;
    }

    public interface OnItemClickListener3 {
        void onItemClick(int position,View view);
    }
    @Override
    public int getItemViewType(int position) {
        if(position%(column+1)==0) return 0;
        else if(position<=column) return 1;
        else return 2;
    }
    public void setOnLongItemClick(onItemLongClickListener longItemClick){this.llistener=longItemClick;}
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case 0:
                View layoutOne = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardtitlerow, parent, false);
                return new holder_rowNo(layoutOne);
            case 1:
                View layoutTwo = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardtitle, parent, false);
                return new holder_columnTitle(layoutTwo , m_colTitleListener);
            case 2:
                View layoutThree = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardcell, parent, false);
                return new holder_cells(layoutThree,mlistener,llistener);
            default:
                return null;
        }
    }




    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {

            case 0:
                cells = combined_list.get(position);
                ((holder_rowNo)holder).editText.setText(cells.getData());
                if(cells.getData().contains("=")){ ((holder_rowNo)holder).editText.setTextSize(10f); }
                break;
            case 1:
                cells = combined_list.get(position);
                ((holder_columnTitle)holder).editText.setText(cells.getData());
                if(cells.getData().contains("=")){ ((holder_columnTitle)holder).editText.setTextSize(10f); }
                break;
            case 2:
                cells = combined_list.get(position);
                ((holder_cells)holder).editText.setText(cells.getData());
                ((holder_cells)holder).editText.setSelected(row_index == position);
                h = ((holder_cells)holder).cardView.getMeasuredHeight();
                final float scale = context.getResources().getDisplayMetrics().density;
                int pixels = (int) (40 * scale + 0.5f);
                ((holder_cells)holder).editText.setMinimumHeight(pixels);
                break;

        }
    }
    @Override
    public int getItemCount() {
        return combined_list.size();
    }


    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }
    public interface onItemLongClickListener{
        void onLongItemClick(int position, View v);
    }

    public class holder_cells extends RecyclerView.ViewHolder {
        TextView editText;
        CardView cardView;

        public holder_cells(@NonNull View itemView, OnItemClickListener listener,onItemLongClickListener longClickListener) {
            super(itemView);
            editText = itemView.findViewById(R.id.edit);
            cardView=itemView.findViewById(R.id.cardview);
            editText.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    listener.onItemClick(position, view);
                    notifyItemChanged(row_index);
                    row_index = getLayoutPosition();
                    notifyItemChanged(row_index);
                }

            });
            editText.setOnLongClickListener(view -> {
                if (longClickListener!=null){
                    int position = getAdapterPosition();
                    longClickListener.onLongItemClick(position, view);
                    notifyItemChanged(row_index);
                    row_index = getLayoutPosition();
                    notifyItemChanged(row_index);
                }
                return true;
            });
        }
    }
    public class holder_rowNo extends RecyclerView.ViewHolder {
        TextView editText;
        public holder_rowNo(@NonNull final View itemView) {
            super(itemView);
            editText = itemView.findViewById(R.id.titlerow);
        }
    }

    public class holder_columnTitle extends RecyclerView.ViewHolder {
        TextView editText;

        public holder_columnTitle(@NonNull View itemView, OnItemClickListener3 listener) {
            super(itemView);
            editText = itemView.findViewById(R.id.title);
            itemView.setOnClickListener(view -> {
                if(listener!=null){
                    listener.onItemClick(getAdapterPosition(),view);
                }
            });

        }
    }
}
