package com.dreamer.mybudget.ui.adapter.itemData;

import com.dreamer.mybudget.core.db.data.DetailContent;

/**
 * Created by Roder Hu on 15/7/30.
 */

public class DetailOptionItem {
    private DetailContent detailContent;
    private String option;

    public DetailOptionItem(DetailContent detailContent, String option){
        this.detailContent = detailContent;
        this.option = option;
    }

    public DetailContent getDetailContent() {
        return detailContent;
    }

    public void setDetailContent(DetailContent detailContent) {
        this.detailContent = detailContent;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }
}
