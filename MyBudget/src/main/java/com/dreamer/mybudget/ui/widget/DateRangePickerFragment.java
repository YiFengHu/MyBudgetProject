package com.dreamer.mybudget.ui.widget;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;

import com.dreamer.mybudget.R;

/**
 * Created by Roder Hu on 15/8/26.
 */
public class  DateRangePickerFragment extends DialogFragment implements View.OnClickListener{

    private OnDateRangeSelectedListener onDateRangeSelectedListener;

    private DatePicker datePicker;
    private Button confirmButton;
    boolean is24HourMode;

    public DateRangePickerFragment() {
        // Required empty public constructor
    }

    public static DateRangePickerFragment newInstance(OnDateRangeSelectedListener callback, boolean is24HourMode) {
        DateRangePickerFragment dateRangePickerFragment = new DateRangePickerFragment();
        dateRangePickerFragment.initialize(callback, is24HourMode);
        return dateRangePickerFragment;
    }

    public void initialize(OnDateRangeSelectedListener callback,
                           boolean is24HourMode) {
        onDateRangeSelectedListener = callback;
        this.is24HourMode = is24HourMode;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.date_range_picker, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        datePicker = (DatePicker) root.findViewById(R.id.datePicker_picker);
        confirmButton = (Button) root.findViewById(R.id.datePicker_confirmButton);
        confirmButton.setOnClickListener(this);

        return root;

    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() == null)
            return;
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }


    public void setOnDateRangeSelectedListener(OnDateRangeSelectedListener callback) {
        this.onDateRangeSelectedListener = callback;
    }

    @Override
    public void onClick(View v) {
        dismiss();
        onDateRangeSelectedListener.onDateRangeSelected(datePicker.getDayOfMonth(), datePicker.getMonth(), datePicker.getYear());
    }

    public interface OnDateRangeSelectedListener {
        void onDateRangeSelected(int day, int month, int year);
    }

}