package com.dreamer.mybudget.manager;

import android.content.Context;

/**
 * Created by Roder Hu on 15/6/6.
 */
public class AppManager {

    private static String TAG = AppManager.class.getSimpleName();

    private static AppManager instance = null;
    private static Context appContext = null;
    private String versionName;
    private int versionCode;
    private int dbVersion;

    public static AppManager getInstance(){
        if(instance == null){
            instance = new AppManager();
        }
        return instance;
    }

    public void init(Context context){
        if(appContext ==null)
            appContext = context;
    }

    public Context getContext(){
        return appContext;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public int getDbVersion() {
        return dbVersion;
    }

    public void setDbVersion(int dbVersion) {
        this.dbVersion = dbVersion;
    }

}
