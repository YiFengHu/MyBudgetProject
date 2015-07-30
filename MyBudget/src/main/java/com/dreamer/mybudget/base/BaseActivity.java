package com.dreamer.mybudget.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.dreamer.mybudget.R;

/**
 * Created by Roder Hu on 15/6/6.
 */
public class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        slideInTransition();
    }

    protected void slideInTransition() {
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}
