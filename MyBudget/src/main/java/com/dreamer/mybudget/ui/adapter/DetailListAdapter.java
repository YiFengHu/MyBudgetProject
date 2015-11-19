package com.dreamer.mybudget.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dreamer.mybudget.Detail;
import com.dreamer.mybudget.R;
import com.dreamer.mybudget.core.db.data.CategoryType;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Roder Hu on 2015/11/20.
 */
public class DetailListAdapter extends RecyclerView.Adapter<DetailListAdapter.ViewHolder>{

    private Context context = null;
    private List<Detail> data = null;

    public DetailListAdapter(Context context, List<Detail> data){
        this.context = context;
        this.data = data;
    }

    @Override
    public DetailListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_item_detail_list, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DetailListAdapter.ViewHolder holder, int position) {
        Detail detail  = data.get(position);

        String priceText = "";
        if(CategoryType.Income.name().equals(detail.getIo())){
            holder.priceTextView.setTextColor(context.getResources().getColor(R.color.green));
            priceText += "+ ";
        }else{
            holder.priceTextView.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
            priceText += "- ";
        }

        holder.priceTextView.setText(priceText+String.valueOf(detail.getPrice()));
        holder.noteTextView.setText(detail.getMark());
        holder.categoryTextView.setText(detail.getCategoryName().getCategory_name());
        holder.dateTextView.setText(getDate(detail.getTime()));
    }

    private String getDate(long time) {
        SimpleDateFormat fomatter = new SimpleDateFormat("yyyy/MM/dd");
        return fomatter.format(time);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView priceTextView = null;
        public TextView noteTextView = null;
        public TextView categoryTextView = null;
        public TextView dateTextView = null;

        public ViewHolder(View itemView) {
            super(itemView);

            priceTextView = (TextView)itemView.findViewById(R.id.adapterItemDetailList_priceTextView);
            noteTextView = (TextView)itemView.findViewById(R.id.adapterItemDetailList_noteTextView);
            categoryTextView = (TextView)itemView.findViewById(R.id.adapterItemDetailList_categoryTextView);
            dateTextView = (TextView)itemView.findViewById(R.id.adapterItemDetailList_dateTextView);

        }
    }
}
