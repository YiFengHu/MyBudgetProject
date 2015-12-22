package com.dreamer.mybudget.ui.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.dreamer.mybudget.R;

/**
 * Created by Roder Hu on 2015/12/19.
 */
public class ChartPointPopupWindow {

    private PopupWindow popupWindow;
    private Context context;
    private View ancrowView;

    public ChartPointPopupWindow(Context context, View ancroView) {
        this.context = context;
        this.ancrowView = ancroView;
        init();
    }

    private void init() {
        popupWindow = new PopupWindow(context);

        View view = LayoutInflater.from(context).inflate(R.layout.chart_point_popup, null);
        popupWindow.setContentView(view);
    }

    public void show(Rect rect){
        if(popupWindow.isShowing()){
            popupWindow.dismiss();
        }
        popupWindow.showAtLocation(ancrowView, Gravity.CENTER, rect.centerX(), rect.centerY());
    }

}
