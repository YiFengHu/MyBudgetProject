package com.dreamer.mybudget.ui.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.dreamer.mybudget.R;
import com.dreamer.mybudget.ui.adapter.AddDetailOptionAdapter;

/**
 * Created by Roder Hu on 15/10/26.
 */
public class OptionGridViewHelper {

    private Context context;
    private ViewGroup rootView;
    private View container;
    private GridView optionGridView;
    private AddDetailOptionAdapter optionAdapter = null;

    public OptionGridViewHelper(Context context, ViewGroup container){
        this.context = context;
        this.rootView = container;

        init();
    }

    private void init() {
        container = LayoutInflater.from(context).inflate(R.layout.option_grid_view, rootView);

        optionGridView = (GridView)container.findViewById(R.id.optionGrid_optionGridView);
    }
}
