package com.dreamer.mybudget.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.dreamer.mybudget.R;
import com.dreamer.mybudget.base.BaseFragment;
import com.dreamer.mybudget.base.CircularRevealActivity;
import com.dreamer.mybudget.ui.activity.AddDetailActivity;
import com.dreamer.mybudget.ui.activity.MainActivity;
import com.dreamer.mybudget.ui.widget.FloatingActionMenu;

/**
 * Created by Roder Hu on 15/8/26.
 */
public class MainFragment extends BaseFragment implements Toolbar.OnMenuItemClickListener, FloatingActionMenu.OnActionButtonClickListener{

    private View mRootView = null;
//    private FloatingActionButton addButton = null;
    private FloatingActionMenu menu = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_main, container, false);;
//        addButton = (FloatingActionButton)mRootView.findViewById(R.id.mainFragment_addButton);
//        addButton.setOnClickListener(this);
        menu = (FloatingActionMenu)mRootView.findViewById(R.id.mainFragment_menu);
        menu.setActionClickListener(this);
        return mRootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setMainToolBar(this);

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_details:
                ((MainActivity)getActivity()).transactionDetailListFragment();
                return true;
        }
        return false;
    }

    @Override
    public void onActionClick(int actionId) {
        switch (actionId){
            case FloatingActionMenu.ACTION_ADD_DETAIL:
                Intent intent = new Intent();
                intent.setClass(getActivity(), AddDetailActivity.class);
                startActivity(intent);
                break;

            case FloatingActionMenu.ACTION_ADD_BUDGET:
                break;

            case FloatingActionMenu.ACTION_ADD_ALARM:
                break;
        }
    }
}
