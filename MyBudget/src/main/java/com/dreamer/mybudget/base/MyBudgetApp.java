package com.dreamer.mybudget.base;

import android.app.Application;

import com.dreamer.mybudget.db.DBManager;
import com.dreamer.mybudget.manager.AppManager;

/**
 * Created by Roder Hu on 15/6/6.
 */
public class MyBudgetApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppManager.getInstance().init(this);

        //Init DBManager
        DBManager.getInstance();
    }
}
