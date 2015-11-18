package com.dreamer.mybudget.ui.activity.custom;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamer.mybudget.Detail;
import com.dreamer.mybudget.R;
import com.dreamer.mybudget.core.db.DBManager;
import com.dreamer.mybudget.core.db.data.CategoryType;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Roder Hu on 15/7/2.
 */
public class DetailLayout extends RelativeLayout implements View.OnFocusChangeListener{
    private static final String TAG = DetailLayout.class.getSimpleName();

    private static final int ITEM_COUNT = 5;
    public static final String ITEM_NAME_TYPE = "type";
    public static final String ITEM_NAME_DATE = "date";
    public static final String ITEM_NAME_PRICE = "price";
    public static final String ITEM_NAME_CATEGORY = "category";
    public static final String ITEM_NAME_NOTE = "note";

    private View container = null;
    private CardView cardView = null;
    private GridLayout gridLayout = null;
    private TextView typeTitleTextView = null;
    private TypeEditText typeContentEditText = null;
    private TextView dateTitleTextView = null;
    private TypeEditText dateContentEditText = null;
    private TextView priceTitleTextView = null;
    private TypeEditText priceContentEditText = null;
    private TextView categoryTitleTextView = null;
    private TypeEditText categoryContentEditText = null;
    private TextView noteTitleTextView = null;
    private TypeEditText noteContentEditText = null;

    private Map<Integer, TypeEditText> allEditText = new HashMap<>(ITEM_COUNT);
    private Map<Integer, String> allItemNames = new HashMap<>(ITEM_COUNT);

    private OnClickListener clickListener = null;

    private OnDetailItemClick onDetailItemClickListener = null;

    public void setOnDetailItemClick(OnDetailItemClick onDetailItemClick) {
        this.onDetailItemClickListener = onDetailItemClick;
    }

    public void addSingleCharaterInPrice(String character) {
        String currentText = priceContentEditText.getText().toString();

        if(currentText.length() == 10) {
            Toast.makeText(getContext(), "You can only input 10 figures!", Toast.LENGTH_SHORT).show();
            return;
        }

        priceContentEditText.setText(currentText+character);
    }

    public void addSingleCharaterInDate(String character) {
        String currentText = dateContentEditText.getText().toString();

        if(currentText.length() == 10){
            Toast.makeText(getContext(), "Date format error!", Toast.LENGTH_SHORT).show();
            return;

        }else if(currentText.length() == 4 || currentText.length() == 7){
            currentText += "-";
        }

        dateContentEditText.setText(currentText+character);
    }

    public void backSpaceSingleCharaterInPrice() {
        String currentText = priceContentEditText.getText().toString();
        if(currentText.length() == 6 || currentText.length() == 9){
            currentText = currentText.substring(0, currentText.length()-1);
        }
        if(currentText.length()>0) {
            currentText = currentText.substring(0, currentText.length() - 1);
            priceContentEditText.setText(currentText);
        }
    }

    public void backSpaceSingleCharaterInDate() {
        String currentText = dateContentEditText.getText().toString();

        if(currentText.length()>0) {
            currentText = currentText.substring(0, currentText.length() - 1);
            dateContentEditText.setText(currentText);
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if(view instanceof TypeEditText) {
            if (hasFocus) {
                InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);

            } else {

                InputMethodManager imm = (InputMethodManager)getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public void clearOptionValues() {
        for(TypeEditText editText: allEditText.values()){
            editText.setText("");
        }
    }

    public interface OnDetailItemClick{
        void onTypeItemClick();
        void onDateItemClick();
        void onPriceItemClick();
        void onCategoryItemClick();
        void onNoteItemClick();
    }

    public DetailLayout(Context context) {
        super(context);
        init(context);
    }

    public DetailLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DetailLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        container = layoutInflater.inflate(R.layout.custom_detail_view, null, false);

        cardView = (CardView)container.findViewById(R.id.detailView_cardView);
        cardView.setCardElevation(20);

        gridLayout = (GridLayout)container.findViewById(R.id.detailView_gridLayout);
        gridLayout.setColumnCount(2);

        typeTitleTextView = (TextView)container.findViewById(R.id.detailView_typeTitle);
        typeContentEditText = (TypeEditText)container.findViewById(R.id.detailView_typeContent);
        allEditText.put(typeContentEditText.getId(), typeContentEditText);
        allItemNames.put(typeContentEditText.getId(), ITEM_NAME_TYPE);

        dateTitleTextView = (TextView)container.findViewById(R.id.detailView_dateTitle);
        dateContentEditText = (TypeEditText)container.findViewById(R.id.detailView_dateContent);
        allEditText.put(dateContentEditText.getId(), dateContentEditText);
        allItemNames.put(dateContentEditText.getId(), ITEM_NAME_DATE);

        priceTitleTextView = (TextView)container.findViewById(R.id.detailView_priceTitle);
        priceContentEditText = (TypeEditText)container.findViewById(R.id.detailView_priceContent);
        allEditText.put(priceContentEditText.getId(), priceContentEditText);
        allItemNames.put(priceContentEditText.getId(), ITEM_NAME_PRICE);

        categoryTitleTextView = (TextView)container.findViewById(R.id.detailView_categoryTitle);
        categoryContentEditText = (TypeEditText)container.findViewById(R.id.detailView_categoryContent);
        allEditText.put(categoryContentEditText.getId(), categoryContentEditText);
        allItemNames.put(categoryContentEditText.getId(), ITEM_NAME_CATEGORY);

        noteTitleTextView = (TextView)container.findViewById(R.id.detailView_noteTitle);
        noteContentEditText = (TypeEditText)container.findViewById(R.id.detailView_noteContent);
        allEditText.put(noteContentEditText.getId(), noteContentEditText);
        allItemNames.put(noteContentEditText.getId(), ITEM_NAME_NOTE);

        addClickListener();

        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        MarginLayoutParams marginParam = new MarginLayoutParams(layoutParams);
        marginParam.setMargins(20, 20, 20, 20);

        addView(container, layoutParams);
    }

    private void addClickListener(){
        clickListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
//                TypeEditText editText = allEditText.get(view.getId());
//                editText.requestFocus();

                if(onDetailItemClickListener!=null){
                    switch (allItemNames.get(view.getId())){
                        case ITEM_NAME_TYPE:
                            onDetailItemClickListener.onTypeItemClick();
                            break;

                        case ITEM_NAME_DATE:
                            onDetailItemClickListener.onDateItemClick();
                            break;

                        case ITEM_NAME_PRICE:
                            onDetailItemClickListener.onPriceItemClick();
                            break;

                        case ITEM_NAME_CATEGORY:
                            onDetailItemClickListener.onCategoryItemClick();
                            break;

                        case ITEM_NAME_NOTE:
                            onDetailItemClickListener.onNoteItemClick();
                            break;
                    }

                }
            }
        };

        for(TypeEditText typeEditText: allEditText.values()){
            typeEditText.setOnClickListener(clickListener);
        }
    }

    public void typeDetailType(String typeContent){
        typeContentEditText.startTypeText(typeContent);
    }

    public void typeDetailDate(String typeContent){
        dateContentEditText.startTypeText(typeContent);
    }

    public void typeDetailPrice(String typeContent){
        priceContentEditText.startTypeText(typeContent);
    }

    public void typeDetailCategory(String typeContent){
        categoryContentEditText.startTypeText(typeContent);
    }

    public void typeDetailNote(String typeContent){
        noteContentEditText.startTypeText(typeContent);
    }

    public void typeDetailType(String typeContent, TypeEditText.OnTypeListener listener){
        typeContentEditText.setOnTypeListener(listener);
        typeContentEditText.startTypeText(typeContent);
    }

    public void typeDetailDate(String typeContent, TypeEditText.OnTypeListener listener){
        dateContentEditText.setOnTypeListener(listener);
        dateContentEditText.startTypeText(typeContent);
    }

    public void typeDetailPrice(String typeContent, TypeEditText.OnTypeListener listener){
        priceContentEditText.setOnTypeListener(listener);
        priceContentEditText.startTypeText(typeContent);
    }

    public void typeDetailCategory(String typeContent, TypeEditText.OnTypeListener listener){
        categoryContentEditText.setOnTypeListener(listener);
        categoryContentEditText.startTypeText(typeContent);
    }

    public void typeDetailNote(String typeContent, TypeEditText.OnTypeListener listener){
        noteContentEditText.setOnTypeListener(listener);
        noteContentEditText.startTypeText(typeContent);
    }

    public void readyInputKeyboardOnNote(){
        noteContentEditText.setEnabled(true);
        noteContentEditText.setOnFocusChangeListener(this);
        noteContentEditText.requestFocus();
    }

    public String getTypeValue(){
        return typeContentEditText.getText().toString();
    }

    public String getCategoryValue(){
        return categoryContentEditText.getText().toString();
    }

    public String getDateValue(){
        return dateContentEditText.getText().toString();
    }

    public String getPriceValue(){
        return priceContentEditText.getText().toString();
    }

    public String getNoteValue(){
        return noteContentEditText.getText().toString();
    }

    public Detail convertToDetail(){
        long categoryCid = DBManager.getInstance().getCategoryDBHandler()
                .queryCategory(CategoryType.getCategoryType(getTypeValue()), getCategoryValue()).getCid();

        Detail detail = new Detail();
        detail.setIo(getTypeValue());
        detail.setTime(getTime(getDateValue()));
        detail.setPrice(Integer.valueOf(getPriceValue()));
        detail.setCategory_cid(categoryCid);
        detail.setMark(getNoteValue());
        return detail;
    }

    private long getTime(String dateString){
        //  準備輸出的格式，如：星期四 2009/01/01
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(dateString).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return System.currentTimeMillis();
        }

    }
}
