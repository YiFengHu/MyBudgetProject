package com.dreamer.mybudget.core.db;


import com.dreamer.mybudget.DaoSession;
import com.dreamer.mybudget.Detail;
import com.dreamer.mybudget.DetailDao;
import com.dreamer.mybudget.core.db.data.CategoryType;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.CountQuery;
import de.greenrobot.dao.query.QueryBuilder;

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
        if(detail!=null) {
            return detailDao.insert(detail);
        }
        return -1;
    }

    public void insertInTx(Detail[] details){
       detailDao.insertInTx(details);
    }

    public List<Detail> getAllDetail(){
        List<Detail> allDetails = detailDao.loadAll();
        return (allDetails==null? new ArrayList<Detail>() : allDetails);
    }

    public List<Detail> queryDetailsAfterAsc(CategoryType type, long time){
        QueryBuilder<Detail> query = detailDao.queryBuilder();
        query.where(DetailDao.Properties.Io.eq(type.getTypeDBName()));
        query.where(DetailDao.Properties.Time.ge(time));
        query.orderAsc(DetailDao.Properties.Time);
        List<Detail> result = query.list();
        return result == null? new ArrayList<Detail>():result;
    }

    public List<Detail> queryDurationDetailsAsc(long startTime, long endTime){
        QueryBuilder<Detail> query = detailDao.queryBuilder();
        query.where(DetailDao.Properties.Time.ge(startTime));
        query.where(DetailDao.Properties.Time.lt(endTime));
        query.orderAsc(DetailDao.Properties.Time);
        List<Detail> result = query.list();
        return result == null? new ArrayList<Detail>():result;
    }

    public long queryMaxPriceAfter(long time){
        QueryBuilder<Detail> query = detailDao.queryBuilder();
        query.where(DetailDao.Properties.Time.gt(time));
        query.orderDesc(DetailDao.Properties.Price);
        query.limit(1);

        List<Detail> result = query.list();
        return result == null || result.isEmpty()? 0:result.get(0).getPrice();
    }

    public void deleteAll(){
        detailDao.deleteAll();
    }
}
