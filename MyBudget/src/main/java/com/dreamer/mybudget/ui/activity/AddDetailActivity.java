package com.dreamer.mybudget.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.dreamer.mybudget.R;
import com.dreamer.mybudget.base.BaseActivity;

/**
 * Created by Roder Hu on 15/6/14.
 */
public class AddDetailActivity extends BaseActivity{

    private static final String TAG = AddDetailActivity.class.getSimpleName();
    public static final String DETAIL_TYPE_KEY = "DETAIL_TYPE_KEY";
    private String detailType = "";
    private Toolbar toolbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            detailType = bundle.getString(DETAIL_TYPE_KEY);
        }

        setContentView(R.layout.activity_add_detail);
        initActionBar();
    }

    private void initActionBar() {
        toolbar = (Toolbar)findViewById(R.id.addDetail_toolbar);
        if(Build.VERSION.SDK_INT>=21) {
            toolbar.setElevation(getResources().getDimension(R.dimen.elevation));
        }
        toolbar.setTitle(getString(R.string.add_detail_title));

        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
