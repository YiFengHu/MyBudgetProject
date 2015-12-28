package com.dreamer.mybudget.util;

import java.text.SimpleDateFormat;

/**
 * Created by Roder Hu on 2015/12/3.
 */
public class DateUtil {

    public static String getMothAndDate(long timeMillis){
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd");
        return formatter.format(timeMillis);
    }

    public static String getDateOfMonth(long timeMillis){
        SimpleDateFormat formatter = new SimpleDateFormat("dd");
        return formatter.format(timeMillis);
    }

    public static String getYearMothDate(long timeMillis){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        return formatter.format(timeMillis);
    }

    public static String getDayName(long timeMillis){
        //Result example : "Monday", "Tuesday" ...
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        return formatter.format(timeMillis);
    }

    public static String getMonthName(long timeMillis){
        //Result example : "June", "July" ...
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM");
        return formatter.format(timeMillis);
    }


}
