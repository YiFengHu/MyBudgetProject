package com.dreamer.mybudget.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.dreamer.mybudget.R;
import com.dreamer.mybudget.ui.widget.DateRangePickerFragment;

/**
 * Created by Roder Hu on 15/6/6.
 */
public abstract class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(enableSlideInOnCreate()) {
            slideInTransition();
        }
    }

    protected void slideInTransition() {
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }


    public void transactionDateRangePickerFragment(DateRangePickerFragment.OnDateRangeSelectedListener callback){
        DateRangePickerFragment dateRangePickerFragment = DateRangePickerFragment.newInstance(callback, false);
        dateRangePickerFragment.show(getSupportFragmentManager(), null);
    }

    protected abstract boolean enableSlideInOnCreate();
}
