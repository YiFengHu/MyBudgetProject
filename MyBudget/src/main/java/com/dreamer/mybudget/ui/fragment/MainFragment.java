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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.dreamer.mybudget.ui.widget.FloatingActionMenu;
import com.dreamer.mybudget.util.DateUtil;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Roder Hu on 15/8/26.
 */
public class MainFragment extends BaseFragment implements Toolbar.OnMenuItemClickListener,
        FloatingActionMenu.OnActionButtonClickListener, OnEntryClickListener {

    private static final String TAG = MainFragment.class.getSimpleName();

    private List<DailyDetail> mExpenseDetails;
    private List<DailyDetail> mIncomeDetails;
    private String[] currentExpenseDateXScale;
    private float[] currentExpensePriceYScale;
    private String[] currentIncomeDateXScale;
    private float[] currentIncomePriceYScale;

    private View mRootView = null;
    private FloatingActionMenu fabMenu = null;
    private LineChartView lineChartView = null;

    private TextView entryDateTextView;
    private TextView entryExpenseTextView;
    private TextView entryIncomeTextView;
    private TextView entryPlusOrMinusTextView;
    private TextView entryBalanceTextView;
    private Button entryDetailButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_main, container, false);
        initLayout();

        requestData();

        return mRootView;
    }

    public void initLayout(){
        fabMenu = (FloatingActionMenu)mRootView.findViewById(R.id.mainFragment_menu);
        fabMenu.setActionClickListener(this);

        lineChartView = (LineChartView)mRootView.findViewById(R.id.mainFragment_lineChartView);
        lineChartView.setOnEntryClickListener(this);

        entryDateTextView = (TextView)mRootView.findViewById(R.id.mainEntryDetail_dateValue);
        entryExpenseTextView = (TextView)mRootView.findViewById(R.id.mainEntryDetail_expenseValue);
        entryIncomeTextView = (TextView)mRootView.findViewById(R.id.mainEntryDetail_incomeValue);
        entryPlusOrMinusTextView = (TextView)mRootView.findViewById(R.id.mainEntryDetail_plusOrMinus);
        entryBalanceTextView = (TextView)mRootView.findViewById(R.id.mainEntryDetail_balanceValue);
        entryDetailButton = (Button)mRootView.findViewById(R.id.mainEntryDetail_moreButton);
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
        long dbStart = System.currentTimeMillis();
        mExpenseDetails = DBAdapter.getMonthlyDetails(CategoryType.Expense);
        mIncomeDetails = DBAdapter.getMonthlyDetails(CategoryType.Income);

        long dbEnd = System.currentTimeMillis();
        Log.d(TAG, "requestData: DB load duration: "+(dbEnd - dbStart));


        if(!mExpenseDetails.isEmpty() || !mIncomeDetails.isEmpty()) {

            currentExpenseDateXScale = new String[mExpenseDetails.size()];
            currentExpensePriceYScale = new float[mExpenseDetails.size()];

            currentIncomeDateXScale = new String[mExpenseDetails.size()];
            currentIncomePriceYScale = new float[mExpenseDetails.size()];

            DailyDetail dailyDetail = null;

            long dataSetStart = System.currentTimeMillis();
            String date = null;

            int maxPrice = 0;

            for (int i = 0; i < mExpenseDetails.size(); i++) {
                dailyDetail = mExpenseDetails.get(i);
                date = dailyDetail.getDate();
                date = Integer.valueOf(date)%2==0?"" : date;

                currentExpenseDateXScale[i] = date;
                currentExpensePriceYScale[i] = dailyDetail.getDetailPrice();

                maxPrice = dailyDetail.getDetailPrice() > maxPrice? (int)dailyDetail.getDetailPrice(): maxPrice;
            }

            for (int i = 0; i < mIncomeDetails.size(); i++) {
                dailyDetail = mIncomeDetails.get(i);
                date = dailyDetail.getDate();
                date = Integer.valueOf(date)%2==0?"" : date;

                currentIncomeDateXScale[i] = date;
                currentIncomePriceYScale[i] = dailyDetail.getDetailPrice();

                maxPrice = dailyDetail.getDetailPrice() > maxPrice? (int)dailyDetail.getDetailPrice(): maxPrice;

            }
            long dataSetEnd = System.currentTimeMillis();
            Log.d(TAG, "requestData: Data set generate duration: " + (dataSetEnd - dataSetStart));


            LineSet expenseDataSet = new LineSet(currentExpenseDateXScale, currentExpensePriceYScale);

            expenseDataSet.setColor(getResources().getColor(R.color.chat_line_light_yellow))
                    .setThickness(Tools.fromDpToPx(4));

            LineSet incomeDataSet = new LineSet(currentIncomeDateXScale, currentIncomePriceYScale);

            incomeDataSet.setColor(getResources().getColor(R.color.chat_line_light_pink))
                    .setThickness(Tools.fromDpToPx(4));

            lineChartView.addData(expenseDataSet);
            lineChartView.addData(incomeDataSet);

            if(maxPrice%4 != 0){
                maxPrice += (4-(maxPrice%4));
            }

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
                    .setYAxis(true);

            Animation anim = new Animation();
            anim.setEasing(new QuadEase());
            lineChartView.show(anim);
        }
    }

    private void setEntryData(int entryIndex){
        int expenseValue = (int)mExpenseDetails.get(entryIndex).getDetailPrice();
        int incomeValue = (int)mIncomeDetails.get(entryIndex).getDetailPrice();

        String expense = String.valueOf(expenseValue);
        String income = String.valueOf(incomeValue);
        String date = DateUtil.getYearMothDate(mExpenseDetails.get(entryIndex).getTimeStamp());
        String balance = String.valueOf(Math.abs(expenseValue - incomeValue));
        String plusOrMinus = incomeValue > expenseValue? "+":"-";

        int colorRes = incomeValue > expenseValue? R.color.green :android.R.color.holo_red_dark;

        entryDateTextView.setText(date);
        entryExpenseTextView.setText(expense);
        entryIncomeTextView.setText(income);

        entryPlusOrMinusTextView.setText(plusOrMinus);
        entryPlusOrMinusTextView.setTextColor(getResources().getColor(colorRes));
        entryBalanceTextView.setText(balance);
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

    @Override
    public void onClick(int setIndex, int entryIndex, Rect rect) {
        setEntryData(entryIndex);
    }
}
