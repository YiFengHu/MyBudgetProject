package com.dreamer.mybudget.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.dreamer.mybudget.Category;
import com.dreamer.mybudget.Detail;
import com.dreamer.mybudget.R;
import com.dreamer.mybudget.base.BaseActivity;
import com.dreamer.mybudget.base.CircularRevealActivity;
import com.dreamer.mybudget.core.db.DBManager;
import com.dreamer.mybudget.core.db.data.CategoryType;
import com.dreamer.mybudget.core.db.data.DetailContent;
import com.dreamer.mybudget.ui.custom.DetailLayout;
import com.dreamer.mybudget.ui.custom.TypeEditText;
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
public class AddDetailActivity extends CircularRevealActivity implements DetailLayout.OnDetailItemClick, AdapterView.OnItemClickListener{

    private static final String TAG = AddDetailActivity.class.getSimpleName();
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
    private String detailType = "";

    private View rootView;
//    private Toolbar toolbar = null;
    private DetailLayout detailLayout = null;

    private GridView gridView = null;
    private Button submitButton = null;
    private AddDetailOptionAdapter optionAdapter = null;
    private CategoryType currentCategoryType = CategoryType.Expense;

    @Override
    protected View getContentView() {
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_add_detail, null);
        initLayout();
//        initToolBar();

        initOptionDatas();


        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            detailType = bundle.getString(DETAIL_TYPE_KEY);
        }

        detailLayout.setOnDetailItemClick(this);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        nextOption(true);
    }

    @Override
    protected boolean enableSlideInOnCreate() {
        return false;
    }

    private void initOptionDatas() {
        typeOptionsList = new ArrayList<>();
        categoryOptionsList = new ArrayList<>();
        priceOptionsList = new ArrayList<>();
        dateOptionsList = new ArrayList<>();

        optionsMap = new HashMap<>();
        optionsMap.put(DetailContent.Type, getTypeOptions());
        optionsMap.put(DetailContent.Category, getCategoryOptions(currentCategoryType));
        optionsMap.put(DetailContent.Price, getPriceOptions());
        optionsMap.put(DetailContent.Date, getDateOptions());

        allContents = new ArrayList<>(5);
        allContents.add(DetailContent.Type);
        allContents.add(DetailContent.Date);
        allContents.add(DetailContent.Price);
        allContents.add(DetailContent.Category);
        allContents.add(DetailContent.Note);

    }

    private void initLayout(){
        Log.d(TAG, "initLayout");

        detailLayout = (DetailLayout)rootView.findViewById(R.id.addDetail_detailLayout);
        gridView = (GridView)rootView.findViewById(R.id.addDetail_optionGridView);
        submitButton =  (Button)rootView.findViewById(R.id.addDetail_submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionFinish();
            }
        });
    }

//    private void initToolBar() {
//        toolbar = (Toolbar)rootView.findViewById(R.id.addDetail_toolbar);
//        if(Build.VERSION.SDK_INT>=21) {
//            toolbar.setElevation(getResources().getDimension(R.dimen.elevation));
//        }
//
//        toolbar.setTitle(getString(R.string.add_detail_title));
//        setSupportActionBar(toolbar);
//    }

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
        prepareView(detailContent);

        if(DetailContent.Note.equals(detailContent)){
            detailLayout.readyInputKeyboardOnNote();

        }else if(DetailContent.Date.equals(detailContent)){

            transactionDateRangePickerFragment(new DateRangePickerFragment.OnDateRangeSelectedListener() {
                @Override
                public void onDateRangeSelected(int day, int month, int year) {
                    StringBuilder date = new StringBuilder();
                    date.append(year).append("-").append(month).append("-").append(day);
                    detailLayout.typeContent(DetailLayout.ContentViewType.date, date.toString());

                    nextOption(false);
                }
            });

        }
    }

    private void prepareView(DetailContent detailContent) {
        detailLayout.setHighlight(getContentViewType(detailContent));

        if(DetailContent.Note.equals(detailContent)){
            gridView.setVisibility(View.GONE);
            submitButton.setVisibility(View.VISIBLE);

        }else{
            submitButton.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
            setGridViewAdapter(detailContent);
        }
    }


    private void setGridViewAdapter(DetailContent detailContent){
        List<DetailOptionItem> optionList = optionsMap.get(detailContent);
        optionAdapter = new AddDetailOptionAdapter(this, optionList);
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
        if(detailLayout.isAllContentValueValid()) {
            Detail detail = detailLayout.convertToDetail();
            DBManager.getInstance().getDetailDBHandler().insertDetail(detail);
            Toast.makeText(this, "Detail: " + detail.getIo()
                    + ", " + detail.getCategoryName().getCategory_name()
                    + ", " + detail.getTime()
                    + ", " + detail.getPrice()
                    + ", " + detail.getMark(), Toast.LENGTH_LONG).show();

//            clearDetailLayoutValues();

            finishActivity();
        }else{
            DetailLayout.ContentViewType invalidType = detailLayout.getInvalidContentType();
            resetOption(getDetailContent(invalidType));
            Toast.makeText(this, R.string.add_detail_value_invalid_hint, Toast.LENGTH_SHORT).show();
        }
    }

    private void clearDetailLayoutValues(){
        detailLayout.clearOptionValues();
    }

    @Override
    public void onItemClick(DetailLayout.ContentViewType type) {
        resetOption(getDetailContent(type));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DetailOptionItem optionItem = optionAdapter.getItem(position);
        switch (optionItem.getDetailContent()){
            case Type:
                final CategoryType type = CategoryType.valueOf(optionItem.getOption());
                detailLayout.typeContent(
                        DetailLayout.ContentViewType.type,
                        optionItem.getOption(),
                        new TypeEditText.OnTypeListener() {
                            @Override
                            public void onTypeStart() {

                            }

                            @Override
                            public void onTypeFinish() {
                                currentCategoryType = type;
                                optionsMap.put(DetailContent.Category, getCategoryOptions(currentCategoryType));

                                nextOption(false);
                            }
                        });

                break;

            case Category:
                detailLayout.typeContent(DetailLayout.ContentViewType.category, optionItem.getOption());
                nextOption(false);

                break;

            case Date:

                if(DETAIL_OPTION_NEXT_TAG.equals(optionItem.getOption())){
                    nextOption(false);

                }else if(DETAIL_OPTION_BACK_SPACE_TAG.equals(optionItem.getOption())) {
                    detailLayout.backSpaceSingleCharacterInDate();

                }else{
                    detailLayout.addSingleCharacterInDate(optionItem.getOption());
                }
                break;

            case Price:

                if(DETAIL_OPTION_NEXT_TAG.equals(optionItem.getOption())){
                    nextOption(false);

                }else if(DETAIL_OPTION_BACK_SPACE_TAG.equals(optionItem.getOption())) {
                    detailLayout.backSpaceSingleCharacterInPrice();

                }else{
                    detailLayout.addSingleCharacterInPrice(optionItem.getOption());
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

    public DetailContent getDetailContent(DetailLayout.ContentViewType type){
        switch (type) {
            case type:
                return DetailContent.Type;

            case date:
                return DetailContent.Date;

            case price:
                return DetailContent.Price;

            case category:
                return DetailContent.Category;

            case note:
                return DetailContent.Note;

            default:
                return null;
        }
    }

    public DetailLayout.ContentViewType getContentViewType(DetailContent content){
        switch (content) {
            case Type:
                return DetailLayout.ContentViewType.type;

            case Date:
                return DetailLayout.ContentViewType.date;

            case Price:
                return DetailLayout.ContentViewType.price;

            case Category:
                return DetailLayout.ContentViewType.category;

            case Note:
                return DetailLayout.ContentViewType.note;

            default:
                return null;
        }
    }

}
