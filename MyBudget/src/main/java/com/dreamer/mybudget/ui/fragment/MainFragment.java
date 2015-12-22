package com.dreamer.mybudget.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.db.chart.Tools;
import com.db.chart.listener.OnEntryClickListener;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.LineChartView;
import com.db.chart.view.animation.Animation;
import com.db.chart.view.animation.easing.QuadEase;
import com.dreamer.mybudget.Detail;
import com.dreamer.mybudget.R;
import com.dreamer.mybudget.base.BaseFragment;
import com.dreamer.mybudget.core.db.DBAdapter;
import com.dreamer.mybudget.core.db.DBManager;
import com.dreamer.mybudget.core.db.data.CategoryType;
import com.dreamer.mybudget.ui.activity.AddDetailActivity;
import com.dreamer.mybudget.ui.activity.MainActivity;
import com.dreamer.mybudget.ui.chart.DailyDetail;
import com.dreamer.mybudget.ui.widget.ChartPointPopupWindow;
import com.dreamer.mybudget.ui.widget.FloatingActionMenu;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Roder Hu on 15/8/26.
 */
public class MainFragment extends BaseFragment implements Toolbar.OnMenuItemClickListener, FloatingActionMenu.OnActionButtonClickListener{

    private static final String TAG = MainFragment.class.getSimpleName();

    private View mRootView = null;
//    private FloatingActionButton addButton = null;
    private FloatingActionMenu menu = null;
    private LineChartView lineChartView = null;

    private ChartPointPopupWindow popupWindow;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_main, container, false);;
//        addButton = (FloatingActionButton)mRootView.findViewById(R.id.mainFragment_addButton);
//        addButton.setOnClickListener(this);
        menu = (FloatingActionMenu)mRootView.findViewById(R.id.mainFragment_menu);
        menu.setActionClickListener(this);

        lineChartView = (LineChartView)mRootView.findViewById(R.id.mainFragment_lineChartView);
        popupWindow = new ChartPointPopupWindow(getActivity(), mRootView);

        requestData();

        return mRootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setMainToolBar(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_details:
                ((MainActivity)getActivity()).transactionDetailListFragment();
                return true;

            case R.id.main_test:

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Detail[] testDatas = new Detail[30];
                        Detail[] testDatas2 = new Detail[30];

                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.HOUR_OF_DAY, 0);
                        cal.set(Calendar.MONTH, 11);// ! clear would not reset the hour of day !
                        cal.clear(Calendar.MINUTE);
                        cal.clear(Calendar.SECOND);
                        cal.clear(Calendar.MILLISECOND);
                        cal.set(Calendar.DAY_OF_MONTH, 1);


                        for (int i = 0; i < 30; i++) {
                            testDatas[i] = newTestDetail(i, CategoryType.Expense, cal.getTimeInMillis());
                            testDatas2[i] = newTestDetail(i, CategoryType.Income, cal.getTimeInMillis());

                            Log.d(TAG, "generate data number "+i);
                        }

                        DBManager.getInstance().getDetailDBHandler().insertInTx(testDatas);
                        DBManager.getInstance().getDetailDBHandler().insertInTx(testDatas2);

                        Log.d(TAG, "insert in tx ");

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lineChartView.reset();
                                requestData();
                            }
                        });

                    }
                }).start();
                return true;
        }
        return false;
    }

    @Override
    public void onActionClick(int actionId) {
        switch (actionId){
            case FloatingActionMenu.ACTION_ADD_DETAIL:
                Intent intent = new Intent();
                intent.setClass(getActivity(), AddDetailActivity.class);
                startActivity(intent);

                //Disable activity transaction animation
                getActivity().overridePendingTransition(0, 0);
                break;

            case FloatingActionMenu.ACTION_ADD_BUDGET:
                break;

            case FloatingActionMenu.ACTION_ADD_ALARM:
                break;
        }
    }



    private void requestData() {
        DBManager dbManager = DBManager.getInstance();

        long dbStart = System.currentTimeMillis();
        List<DailyDetail> expenseDetails = DBAdapter.getMonthlyDetails(CategoryType.Expense);
        List<DailyDetail> incomeDetails = DBAdapter.getMonthlyDetails(CategoryType.Income);

        long dbEnd = System.currentTimeMillis();
        Log.d(TAG, "requestData: DB load duration: "+(dbEnd - dbStart));


        if(!expenseDetails.isEmpty() || !incomeDetails.isEmpty()) {

            String[] expenseDateXScale = new String[expenseDetails.size()];
            float[] expensePriceYScale = new float[expenseDetails.size()];

            String[] incomeDateXScale = new String[expenseDetails.size()];
            float[] incomePriceYScale = new float[expenseDetails.size()];

            DailyDetail dailyDetail = null;

            long dataSetStart = System.currentTimeMillis();
            String date = null;

            int maxPrice = 0;

            for (int i = 0; i < expenseDetails.size(); i++) {
                dailyDetail = expenseDetails.get(i);
                date = dailyDetail.getDate();
                date = Integer.valueOf(date)%2==0?"" : date;

                expenseDateXScale[i] = date;
                expensePriceYScale[i] = dailyDetail.getDetailPrice();

                maxPrice = dailyDetail.getDetailPrice() > maxPrice? (int)dailyDetail.getDetailPrice(): maxPrice;
            }

            for (int i = 0; i < expenseDetails.size(); i++) {
                dailyDetail = incomeDetails.get(i);
                date = dailyDetail.getDate();
                date = Integer.valueOf(date)%2==0?"" : date;

                incomeDateXScale[i] = date;
                incomePriceYScale[i] = dailyDetail.getDetailPrice();

                maxPrice = dailyDetail.getDetailPrice() > maxPrice? (int)dailyDetail.getDetailPrice(): maxPrice;

            }
            long dataSetEnd = System.currentTimeMillis();
            Log.d(TAG, "requestData: Data set generate duration: " + (dataSetEnd - dataSetStart));


            LineSet expenseDataSet = new LineSet(expenseDateXScale, expensePriceYScale);

            expenseDataSet.setColor(getResources().getColor(R.color.chat_line_light_yellow))
//                    .setDotsRadius(10f)
//                    .setDotsStrokeThickness(Tools.fromDpToPx(2))
//                    .setDotsStrokeColor(getResources().getColor(android.R.color.holo_blue_light))
//                    .setDotsColor(getResources().getColor(android.R.color.white));
                    .setThickness(Tools.fromDpToPx(4));

            LineSet incomeDataSet = new LineSet(incomeDateXScale, incomePriceYScale);

            incomeDataSet.setColor(getResources().getColor(R.color.chat_line_light_pink))
//                    .setDotsRadius(10f)
//                    .setDotsStrokeThickness(Tools.fromDpToPx(2))
//                    .setDotsStrokeColor(getResources().getColor(android.R.color.holo_blue_light))
//                    .setDotsColor(getResources().getColor(android.R.color.white));
                    .setThickness(Tools.fromDpToPx(4));

            lineChartView.addData(expenseDataSet);
            lineChartView.addData(incomeDataSet);

            if(maxPrice%4 != 0){
                maxPrice += (4-(maxPrice%4));
            }
            Log.d(TAG, "max= " + maxPrice);

            Log.d(TAG, "max/4= " + (maxPrice/4));

            if(maxPrice > 0) {
                lineChartView.setAxisBorderValues(0, maxPrice, (maxPrice / 4));
            }

            lineChartView.setBorderSpacing(Tools.fromDpToPx(5))
                    .setTopSpacing(Tools.fromDpToPx(5))
                    .setAxisColor(getResources().getColor(R.color.chat_label_light_white))
                    .setXLabels(AxisController.LabelPosition.OUTSIDE)
                    .setYLabels(AxisController.LabelPosition.OUTSIDE)
                    .setAxisThickness(Tools.fromDpToPx(3))
                    .setLabelsColor(getResources().getColor(android.R.color.white))
                    .setXAxis(true)
                    .setYAxis(true)
                    .setOnEntryClickListener(new OnEntryClickListener() {
                        @Override
                        public void onClick(int setIndex, int entryIndex, Rect rect) {
                            popupWindow.show(rect);
                        }
                    });

//            Tooltip tip = new Tooltip(getActivity(), R.layout.linechart_three_tooltip, R.id.value);
//
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//
//                tip.setEnterAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 1),
//                        PropertyValuesHolder.ofFloat(View.SCALE_X, 1f),
//                        PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f));
//
//                tip.setExitAnimation(PropertyValuesHolder.ofFloat(View.ALPHA,0),
//                        PropertyValuesHolder.ofFloat(View.SCALE_X,0f),
//                        PropertyValuesHolder.ofFloat(View.SCALE_Y,0f));
//            }
//
//            lineChartView.setTooltips(tip);

            Animation anim = new Animation();
            anim.setEasing(new QuadEase());
            lineChartView.show(anim);
        }
    }

    /**
     * Method to create test detail data
     */
    private Detail newTestDetail(int testNum, CategoryType type, long startTime){
        long categoryCid = DBManager.getInstance().getCategoryDBHandler()
                .queryCategory(CategoryType.getCategoryType(CategoryType.Expense.name()), "Food").getCid();

        Detail detail = new Detail();
        detail.setIo(type.name());

        detail.setTime(startTime + (1000 * 60 * 60 * 24 * testNum));
        if(type.equals(CategoryType.Expense)) {
            detail.setPrice(1000 + 50 * testNum);
        }else{
            detail.setPrice(3000 - 10 * testNum);

        }
        detail.setCategory_cid(categoryCid);
        detail.setMark("Test Detail Data number "+testNum);
        return detail;
    }
}
