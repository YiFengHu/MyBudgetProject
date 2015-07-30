package com.dreamer.mybudget.ui.activity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.dreamer.mybudget.Category;
import com.dreamer.mybudget.Detail;
import com.dreamer.mybudget.R;
import com.dreamer.mybudget.base.BaseActivity;
import com.dreamer.mybudget.core.db.DBManager;
import com.dreamer.mybudget.core.db.data.CategoryType;
import com.dreamer.mybudget.core.db.data.DefaultCategory;

import java.util.List;


public class MainActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener{

    private static String TAG = MainActivity.class.getSimpleName();
    private Toolbar toolbar = null;
    private FloatingActionButton addButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActionBar();
        addButton = (FloatingActionButton)findViewById(R.id.main_addButton);
        addButton.setOnClickListener(this);

//        insertDetail();
//        queryAllDetail();

    }

    private void insertDetail() {
        DBManager.getInstance().getDetailDBHandler().deleteAll();

        Category category = DBManager.getInstance()
                .getCategoryDBHandler().queryCategory(CategoryType.Expense, DefaultCategory.EXPENSE_FOOD);

        for(int i=0; i<20;i++) {
            Detail detail = new Detail();
            detail.setIo(CategoryType.Expense.name());
            detail.setTime(System.currentTimeMillis());
            detail.setCategory_cid(category.getCid());
            detail.setPrice(i * 100);
            detail.setMark("mark number " + i);
            DBManager.getInstance().getDetailDBHandler().insertDetail(detail);
        }

    }

    private void queryAllDetail() {
        List<Detail> details = DBManager.getInstance().getDetailDBHandler().getAllDetail();
        for(int i=0; i<details.size();i++) {
            Log.i(TAG, "did: " + details.get(i).getDid()+", mark:"+details.get(i).getMark());
        }
    }

    private void initActionBar() {
        toolbar = (Toolbar)findViewById(R.id.main_toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        if(Build.VERSION.SDK_INT>=21) {
            toolbar.setElevation(getResources().getDimension(R.dimen.elevation));
        }

        setSupportActionBar(toolbar);

        //must place after setSupportActionBar(toolbar) method. - Roder
        toolbar.setNavigationIcon(R.drawable.ic_launcher);
        toolbar.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){

            case R.id.action_example:
                Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_addButton:

                startActivity(new Intent(this, AddDetailActivity.class));
                break;
        }
    }
}
