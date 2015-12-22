package com.dreamer.mybudget.ui.chart;

/**
 * Created by Roder Hu on 2015/12/16.
 */
public class DailyDetail {

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
}
