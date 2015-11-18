package com.dreamer.mybudget.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamer.mybudget.R;
import com.dreamer.mybudget.ui.activity.AddDetailActivity;
import com.dreamer.mybudget.ui.activity.MainActivity;

/**
 * Created by Roder Hu on 15/8/26.
 */
public class MainFragment extends Fragment implements View.OnClickListener{


    private View mRootView = null;
    private FloatingActionButton addButton = null;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_main, container, false);;
        addButton = (FloatingActionButton)mRootView.findViewById(R.id.mainFragment_addButton);
        addButton.setOnClickListener(this);

        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setMainToolBar();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mainFragment_addButton:

                ((MainActivity)getActivity()).transactionAddDetailFragment(null);
                break;
        }
    }
}
