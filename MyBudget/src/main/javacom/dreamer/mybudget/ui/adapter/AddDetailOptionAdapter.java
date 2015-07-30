package com.dreamer.mybudget.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dreamer.mybudget.R;
import com.dreamer.mybudget.ui.adapter.itemData.DetailOptionItem;

import java.util.List;

/**
 * Created by Roder Hu on 15/7/29.
 */
public class AddDetailOptionAdapter extends BaseAdapter{

    private Context mContext;
    private List<DetailOptionItem> options = null;


    public AddDetailOptionAdapter(Context context, List<DetailOptionItem> options) {
        mContext = context;
        this.options = options;
    }

    @Override
    public int getCount() {
        return options.size();
    }

    @Override
    public DetailOptionItem getItem(int position) {
        return options.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if(viewHolder == null){
            convertView = LayoutInflater.from(mContext)
                .inflate(R.layout.adapter_item_add_detail_option, parent, false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        if(!getItem(position).getOption().isEmpty()) {
            convertView.setVisibility(View.VISIBLE);
            viewHolder.optionTextView.setText(getItem(position).getOption());
        }else{
            convertView.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    class ViewHolder{
        public TextView optionTextView;

        ViewHolder(View view){
            optionTextView = (TextView) view.findViewById(R.id.adapterItemAddDetail_optionTextView);
        }
    }
}
