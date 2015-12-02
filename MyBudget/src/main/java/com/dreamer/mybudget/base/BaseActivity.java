package com.dreamer.mybudget.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dreamer.mybudget.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

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

    public void transactionDateRangePickerFragment(DatePickerDialog.OnDateSetListener listener) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                listener,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "DatePicker");
    }

    protected abstract boolean enableSlideInOnCreate();
}
