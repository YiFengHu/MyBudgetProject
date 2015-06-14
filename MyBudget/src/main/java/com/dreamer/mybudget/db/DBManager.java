package com.dreamer.mybudget.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.dreamer.mybudget.DaoMaster;
import com.dreamer.mybudget.DaoSession;
import com.dreamer.mybudget.DetailDao;
import com.dreamer.mybudget.manager.AppManager;

/**
 * Created by Roder Hu on 15/6/6.
 */
public class DBManager {

    private static String TAG = DBManager.class.getSimpleName();
    private static DBManager instance = null;

    public static String DBName = "MyBudget-db";

    private Context context;
    private DaoMaster.DevOpenHelper daoDevOpenHelper = null;
    private DaoMaster daoMaster = null;
    private DaoSession daoSession = null;
    private SQLiteDatabase db = null;

    private DetailDBHandler detailDBHandler = null;
    private CategoryDBHandler categoryDBHandler = null;

    private DBManager(){
        context = AppManager.getInstance().getContext();
        daoDevOpenHelper = new DaoMaster.DevOpenHelper(context, DBName, null);
        db = daoDevOpenHelper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public static DBManager getInstance(){
        if(instance ==null){
            instance = new DBManager();
        }
        return instance;
    }

    public DetailDBHandler getDetailDBHandler(){
        if(detailDBHandler == null){
            detailDBHandler = new DetailDBHandler(daoSession);
        }
        return detailDBHandler;
    }

    public CategoryDBHandler getCategoryDBHandler(){
        if(categoryDBHandler == null){
            categoryDBHandler = new CategoryDBHandler(context, daoSession);
        }
        return categoryDBHandler;
    }
}
