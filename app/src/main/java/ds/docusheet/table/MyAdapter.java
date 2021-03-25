package ds.docusheet.table;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class MyAdapter extends BaseAdapter {
    Context context;
    private String[] listItem;
    private int imageItem[];
    private int [] color;

    public MyAdapter(Context context, String[] listItem, int[] imageItem,int[] color) {
        this.context = context;
        this.listItem = listItem;
        this.imageItem = imageItem;
        this.color=color;
    }

    @Override
    public int getCount() {
        return listItem.length;
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
            convertView =inflater.inflate(R.layout.columnmenuitem,parent,false);
            viewHolder.listname=convertView.findViewById(R.id.textItem);
            viewHolder.listname.setTextColor(color[position]);
            viewHolder.icon=convertView.findViewById(R.id.imageItem);
            viewHolder.icon.setColorFilter(color[position]);
            result = convertView;
            convertView.setTag(viewHolder);


        }
        else {
            viewHolder= (ViewHolder) convertView.getTag();
            result=convertView;
        }
        viewHolder.listname.setText(listItem[position]);
        viewHolder.icon.setImageResource(imageItem[position]);
        return convertView;
    }
    public static class ViewHolder{
        TextView listname;
        ImageView icon;
    }
}
