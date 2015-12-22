package com.dreamer.mybudget.ui.chart;

/**
 * Created by Roder Hu on 2015/12/16.
 */
public class DailyDetail {

    private long timeStamp;
    private String date;
    private float detailPrice;

    public DailyDetail(String date, float detailPrice){
        this.date = date;
        this.detailPrice = detailPrice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getDetailPrice() {
        return detailPrice;
    }

    public void setDetailPrice(float price) {
        this.detailPrice = price;
    }


    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
