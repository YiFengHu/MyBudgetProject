package com.dreamer.mybudget.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.dreamer.mybudget.Detail;
import com.dreamer.mybudget.R;
import com.dreamer.mybudget.base.BaseFragment;
import com.dreamer.mybudget.core.db.DBManager;
import com.dreamer.mybudget.ui.activity.MainActivity;
import com.dreamer.mybudget.ui.adapter.DetailListAdapter;
import com.dreamer.mybudget.ui.adapter.DetailListAdapter.DetailBrowseMode;
import com.dreamer.mybudget.ui.recyclerView.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roder Hu on 2015/11/20.
 */
public class DetailListFragment extends BaseFragment implements Toolbar.OnMenuItemClickListener{

    private View rootView = null;
    private RecyclerView recyclerView = null;

    private DetailListAdapter mDetailListAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mDetailListAdapter = new DetailListAdapter(getActivity(), new ArrayList<Detail>());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_detail_list, null);
        initLayout();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_detail_list, menu);
    }

    private void initLayout(){
        recyclerView = (RecyclerView)rootView.findViewById(R.id.detailList_recyclerView);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());

//        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.detail_list_spacing);
//        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        recyclerView.setLayoutManager(manager);

        initToolbar();

        recyclerView.setAdapter(mDetailListAdapter);
        mDetailListAdapter.loadData(DetailBrowseMode.Daily);
    }

    private void initToolbar() {
        ((MainActivity)getActivity()).setToolBarWithTitle(getString(R.string.detail_list_title), this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.detailList_daily:

                loadData(DetailBrowseMode.Daily);
                return true;

            case R.id.detailList_weekly:

                loadData(DetailBrowseMode.Weekly);
                return true;

            case R.id.detailList_monthly:

                loadData(DetailBrowseMode.Monthly);
                return true;
        }
        return false;
    }

    private void loadData(DetailBrowseMode mode) {
        mDetailListAdapter.loadData(mode);
    }
}
