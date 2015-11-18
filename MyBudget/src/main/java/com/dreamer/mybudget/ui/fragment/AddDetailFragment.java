package com.dreamer.mybudget.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.dreamer.mybudget.Category;
import com.dreamer.mybudget.Detail;
import com.dreamer.mybudget.R;
import com.dreamer.mybudget.core.db.DBManager;
import com.dreamer.mybudget.core.db.data.CategoryType;
import com.dreamer.mybudget.core.db.data.DetailContent;
import com.dreamer.mybudget.ui.activity.MainActivity;
import com.dreamer.mybudget.ui.activity.custom.DetailLayout;
import com.dreamer.mybudget.ui.adapter.AddDetailOptionAdapter;
import com.dreamer.mybudget.ui.adapter.itemData.DetailOptionItem;
import com.dreamer.mybudget.ui.widget.DateRangePickerFragment;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Roder Hu on 15/8/26.
 */
public class AddDetailFragment extends Fragment implements DetailLayout.OnDetailItemClick, AdapterView.OnItemClickListener{

    private static final String TAG = AddDetailFragment.class.getSimpleName();
    public static final String DETAIL_TYPE_KEY = "DETAIL_TYPE_KEY";
    public static final String DETAIL_OPTION_NEXT_TAG = "Next";
    public static final String DETAIL_OPTION_BACK_SPACE_TAG = "Back";

    private static final int INITIAL_DELAY_MILLIS = 300;

    private DetailContent mCurrentDetailContent = DetailContent.Type;
    private List<DetailContent> allContents = null;

    private Map<DetailContent, List<DetailOptionItem>> optionsMap = null;
    private List<DetailOptionItem> typeOptionsList = null;
    private List<DetailOptionItem> categoryOptionsList = null;
    private List<DetailOptionItem> priceOptionsList = null;
    private List<DetailOptionItem> dateOptionsList = null;
    private List<DetailOptionItem> noteOptionsList = null;
    private String detailType = "";


    private View mRootView = null;
    private Toolbar toolbar = null;
    private DetailLayout detailLayout = null;

    private GridView gridView = null;
    private AddDetailOptionAdapter optionAdapter = null;
    private CategoryType currentCategoryType = CategoryType.Expense;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.activity_add_detail, container, false);

        initOptionDatas();

        initLayout();

        Bundle bundle = getArguments();
        if(bundle!=null) {
            detailType = bundle.getString(DETAIL_TYPE_KEY);
        }

        ((MainActivity)getActivity()).setToolBarWithTitle(getString(R.string.add_detail_title));

        detailLayout.setOnDetailItemClick(this);
        nextOption(true);

        return mRootView;
    }

    private void initOptionDatas() {
        typeOptionsList = new ArrayList<>();
        categoryOptionsList = new ArrayList<>();
        priceOptionsList = new ArrayList<>();
        dateOptionsList = new ArrayList<>();
        noteOptionsList = new ArrayList<>();

        optionsMap = new HashMap<>();
        optionsMap.put(DetailContent.Type, getTypeOptions());
        optionsMap.put(DetailContent.Category, getCategoryOptions(currentCategoryType));
        optionsMap.put(DetailContent.Price, getPriceOptions());
        optionsMap.put(DetailContent.Date, getDateOptions());
        optionsMap.put(DetailContent.Note, getNoteOptions());

        allContents = new ArrayList<>(5);
        allContents.add(DetailContent.Type);
        allContents.add(DetailContent.Date);
        allContents.add(DetailContent.Price);
        allContents.add(DetailContent.Category);
        allContents.add(DetailContent.Note);

    }

    private void initLayout(){
        Log.d(TAG, "initLayout");

        detailLayout = (DetailLayout)mRootView.findViewById(R.id.addDetail_detailLayout);
        gridView = (GridView) mRootView.findViewById(R.id.addDetail_optionGridView);

    }

    private void resetOption(DetailContent content){
        mCurrentDetailContent = content;
        nextOption(true);
    }

    private void nextOption(boolean isFirstOption){
        Log.d(TAG, "nextOption: " + isFirstOption);

        int indexOfDetailCotent = allContents.indexOf(mCurrentDetailContent);
        Log.d(TAG, "indexOfDetailCotent: " + indexOfDetailCotent);

        if(!isFirstOption) {
            indexOfDetailCotent += 1;
        }

        if(indexOfDetailCotent<allContents.size()) {
            mCurrentDetailContent = allContents.get(indexOfDetailCotent);
            prepareOptions(mCurrentDetailContent);
        }else{
            onOptionFinish();
        }
    }

    private void prepareOptions(DetailContent detailContent){
        setGridViewAdapter(detailContent);
        if(DetailContent.Note.equals(detailContent)){
            detailLayout.readyInputKeyboardOnNote();

        }else if(DetailContent.Date.equals(detailContent)){

            ((MainActivity)getActivity()).transactionDateRangePickerFragment(new DateRangePickerFragment.OnDateRangeSelectedListener() {
                @Override
                public void onDateRangeSelected(int day, int month, int year) {
                    StringBuilder date = new StringBuilder();
                    date.append(year).append("-").append(month).append("-").append(day);
                    detailLayout.typeDetailDate(date.toString());

                    nextOption(false);
                }
            });

        }
    }


    private void setGridViewAdapter(DetailContent detailContent){
        List<DetailOptionItem> optionList = optionsMap.get(detailContent);
        optionAdapter = new AddDetailOptionAdapter(getActivity(), optionList);
        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(
                optionAdapter);

        alphaInAnimationAdapter.setAbsListView(gridView);
        if (alphaInAnimationAdapter.getViewAnimator() != null) {
            alphaInAnimationAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);
        }
        gridView.setAdapter(alphaInAnimationAdapter);
        gridView.setOnItemClickListener(this);

    }

    private void onOptionFinish() {
        Detail detail = detailLayout.convertToDetail();
        DBManager.getInstance().getDetailDBHandler().insertDetail(detail);
        Toast.makeText(getActivity(), "Detail: " + detail.getIo()
                + ", " + detail.getCategoryName().getCategory_name()
                + ", " + detail.getTime()
                + ", " + detail.getPrice()
                + ", " + detail.getMark(), Toast.LENGTH_LONG).show();

        clearDetailLayoutValues();
    }

    private void clearDetailLayoutValues(){
        detailLayout.clearOptionValues();
    }

    @Override
    public void onTypeItemClick() {
        resetOption(DetailContent.Type);
    }

    @Override
    public void onDateItemClick() {
        resetOption(DetailContent.Date);
    }

    @Override
    public void onPriceItemClick() {
        resetOption(DetailContent.Price);
    }

    @Override
    public void onCategoryItemClick() {
        resetOption(DetailContent.Category);
    }

    @Override
    public void onNoteItemClick() {
        resetOption(DetailContent.Note);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DetailOptionItem optionItem = optionAdapter.getItem(position);
        switch (optionItem.getDetailContent()){
            case Type:
                detailLayout.typeDetailType(optionItem.getOption());
                currentCategoryType = CategoryType.valueOf(optionItem.getOption());
                optionsMap.put(DetailContent.Category, getCategoryOptions(currentCategoryType));

                nextOption(false);

                break;

            case Category:
                detailLayout.typeDetailCategory(optionItem.getOption());
                nextOption(false);

                break;

            case Date:

                if(DETAIL_OPTION_NEXT_TAG.equals(optionItem.getOption())){
                    nextOption(false);

                }else if(DETAIL_OPTION_BACK_SPACE_TAG.equals(optionItem.getOption())) {
                    detailLayout.backSpaceSingleCharaterInDate();

                }else{
                    detailLayout.addSingleCharaterInDate(optionItem.getOption());
                }
                break;

            case Price:

                if(DETAIL_OPTION_NEXT_TAG.equals(optionItem.getOption())){
                    nextOption(false);

                }else if(DETAIL_OPTION_BACK_SPACE_TAG.equals(optionItem.getOption())) {
                    detailLayout.backSpaceSingleCharaterInPrice();

                }else{
                    detailLayout.addSingleCharaterInPrice(optionItem.getOption());
                }
                break;

            case Note:

                if(DETAIL_OPTION_NEXT_TAG.equals(optionItem.getOption())){
                    nextOption(false);
                }
                break;

        }

    }

    public List<DetailOptionItem> getTypeOptions(){
        if (typeOptionsList.isEmpty()) {

            typeOptionsList.add(new DetailOptionItem(DetailContent.Type, CategoryType.Income.name()));
            typeOptionsList.add(new DetailOptionItem(DetailContent.Type, ""));
            typeOptionsList.add(new DetailOptionItem(DetailContent.Type, CategoryType.Expense.name()));
        }
        return typeOptionsList;
    }

    private List<DetailOptionItem> getCategoryOptions(CategoryType categoryType) {

        categoryOptionsList.clear();
        List<Category> categories = DBManager.getInstance()
                .getCategoryDBHandler().queryCategories(categoryType);
        for(Category category : categories){
            categoryOptionsList.add(new DetailOptionItem(DetailContent.Category ,category.getCategory_name()));
        }


        return categoryOptionsList;
    }

    private List<DetailOptionItem> getPriceOptions(){
        if(priceOptionsList.isEmpty()){
            priceOptionsList.add(new DetailOptionItem(DetailContent.Price, "1"));
            priceOptionsList.add(new DetailOptionItem(DetailContent.Price, "2"));
            priceOptionsList.add(new DetailOptionItem(DetailContent.Price, "3"));
            priceOptionsList.add(new DetailOptionItem(DetailContent.Price, "4"));
            priceOptionsList.add(new DetailOptionItem(DetailContent.Price, "5"));
            priceOptionsList.add(new DetailOptionItem(DetailContent.Price, "6"));
            priceOptionsList.add(new DetailOptionItem(DetailContent.Price, "7"));
            priceOptionsList.add(new DetailOptionItem(DetailContent.Price, "8"));
            priceOptionsList.add(new DetailOptionItem(DetailContent.Price, "9"));
            priceOptionsList.add(new DetailOptionItem(DetailContent.Price, "0"));
            priceOptionsList.add(new DetailOptionItem(DetailContent.Price, DETAIL_OPTION_BACK_SPACE_TAG));
            priceOptionsList.add(new DetailOptionItem(DetailContent.Price, DETAIL_OPTION_NEXT_TAG));

        }

        return priceOptionsList;
    }

    private List<DetailOptionItem> getDateOptions(){
        if(dateOptionsList.isEmpty()){
            dateOptionsList.add(new DetailOptionItem(DetailContent.Date, "1"));
            dateOptionsList.add(new DetailOptionItem(DetailContent.Date, "2"));
            dateOptionsList.add(new DetailOptionItem(DetailContent.Date, "3"));
            dateOptionsList.add(new DetailOptionItem(DetailContent.Date, "4"));
            dateOptionsList.add(new DetailOptionItem(DetailContent.Date, "5"));
            dateOptionsList.add(new DetailOptionItem(DetailContent.Date, "6"));
            dateOptionsList.add(new DetailOptionItem(DetailContent.Date, "7"));
            dateOptionsList.add(new DetailOptionItem(DetailContent.Date, "8"));
            dateOptionsList.add(new DetailOptionItem(DetailContent.Date, "9"));
            dateOptionsList.add(new DetailOptionItem(DetailContent.Date, "0"));
            dateOptionsList.add(new DetailOptionItem(DetailContent.Date, DETAIL_OPTION_BACK_SPACE_TAG));
            dateOptionsList.add(new DetailOptionItem(DetailContent.Date, DETAIL_OPTION_NEXT_TAG));
        }

        return dateOptionsList;
    }

    private List<DetailOptionItem> getNoteOptions(){
        if(noteOptionsList.isEmpty()){
            noteOptionsList.add(new DetailOptionItem(DetailContent.Note, ""));
            noteOptionsList.add(new DetailOptionItem(DetailContent.Note, ""));
            noteOptionsList.add(new DetailOptionItem(DetailContent.Note, ""));
            noteOptionsList.add(new DetailOptionItem(DetailContent.Note, ""));
            noteOptionsList.add(new DetailOptionItem(DetailContent.Note, ""));
            noteOptionsList.add(new DetailOptionItem(DetailContent.Note, ""));
            noteOptionsList.add(new DetailOptionItem(DetailContent.Note, ""));
            noteOptionsList.add(new DetailOptionItem(DetailContent.Note, ""));
            noteOptionsList.add(new DetailOptionItem(DetailContent.Note, ""));
            noteOptionsList.add(new DetailOptionItem(DetailContent.Note, ""));
            noteOptionsList.add(new DetailOptionItem(DetailContent.Note, DETAIL_OPTION_NEXT_TAG));
        }

        return noteOptionsList;
    }
}
