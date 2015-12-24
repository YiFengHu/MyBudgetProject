package com.dreamer.mybudget.core.db;

import android.util.Log;

import com.dreamer.mybudget.Detail;
import com.dreamer.mybudget.core.db.data.CategoryType;
import com.dreamer.mybudget.ui.chart.DailyDetail;
import com.dreamer.mybudget.util.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Roder Hu on 2015/12/16.
 */
public class DBAdapter {
    private static final String TAG = DBAdapter.class.getSimpleName();

    public static List<DailyDetail> getMonthlyDetails(CategoryType type){
        // get today and clear time of day
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Log.d(TAG, "Start of the month: " + cal.getTimeInMillis()+" ("+cal.getTime()+")");
        List<Detail> detailsOfMonth = DBManager.getInstance().getDetailDBHandler().queryDetailsAfterAsc(type, cal.getTimeInMillis());

        int dayCountInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        List<DailyDetail> monthDetails = new ArrayList<>(dayCountInMonth);
        for (int i = 1; i <= dayCountInMonth; i++) {
            monthDetails.add(new DailyDetail(""+i, 0F));
        }

        if(detailsOfMonth.size()>0) {

            Detail detail;
            DailyDetail dailyDetail;

            int date;

            for (int i = 0; i < detailsOfMonth.size(); i++) {
                detail = detailsOfMonth.get(i);
                date = Integer.valueOf(DateUtil.getDateOfMonth(detail.getTime()));
                Log.d(TAG, "getMonthlyDetails: i["+i+"], date["+date+"]");
                dailyDetail = monthDetails.get(date - 1);

                dailyDetail.setDetailPrice(dailyDetail.getDetailPrice() + detail.getPrice());
                dailyDetail.setTimeStamp(detail.getTime());
            }
        }

        return monthDetails;
    }

    public static long getMaxPriceOfMonth(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return DBManager.getInstance().getDetailDBHandler().queryMaxPriceAfter(cal.getTimeInMillis());
    }
}
