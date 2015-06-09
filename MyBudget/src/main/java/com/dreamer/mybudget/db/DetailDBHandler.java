package com.dreamer.mybudget.db;


import com.dreamer.mybudget.DaoSession;
import com.dreamer.mybudget.Detail;
import com.dreamer.mybudget.DetailDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roder Hu on 15/6/6.
 */
public class DetailDBHandler {

    private static String TAG = DetailDBHandler.class.getSimpleName();

    private DetailDao detailDao = null;

    public DetailDBHandler(DaoSession daoSession){
        this.detailDao = daoSession.getDetailDao();
    }

    public long insertDetail(Detail detail){
        return detailDao.insert(detail);
    }

    public List<Detail> getAllDetail(){
        List<Detail> allDetails = detailDao.loadAll();
        return (allDetails==null? new ArrayList<Detail>() : allDetails);
    }
}
