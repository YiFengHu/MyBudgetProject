package com.dreamer.mybudget.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
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

    private LayoutInflater layoutInflater = null;
    private LinearLayout container = null;

    private Map<ContentViewType, ContentViewHolder> allRawViews = new HashMap<ContentViewType, ContentViewHolder>(ITEM_COUNT);

    private OnClickListener rawLayoutClickListener = null;

    private OnDetailItemClick onDetailItemClickListener = null;

    public void setOnDetailItemClick(OnDetailItemClick onDetailItemClick) {
        this.onDetailItemClickListener = onDetailItemClick;
    }

    public void addSingleCharacterInPrice(String character) {
        TypeEditText editText = getTypeEditText(ContentViewType.price);
        String currentText = editText.getText().toString();

        if(currentText.length() == 10) {
            Toast.makeText(getContext(), "You can only input 10 figures!", Toast.LENGTH_SHORT).show();
            return;
        }

        editText.setText(currentText+character);
    }

    public void addSingleCharacterInDate(String character) {
        TypeEditText editText = getTypeEditText(ContentViewType.date);

        String currentText = editText.getText().toString();

        if(currentText.length() == 10){
            Toast.makeText(getContext(), "Date format error!", Toast.LENGTH_SHORT).show();
            return;

        }else if(currentText.length() == 4 || currentText.length() == 7){
            currentText += "-";
        }

        editText.setText(currentText+character);
    }

    public void backSpaceSingleCharacterInPrice() {
        TypeEditText editText = getTypeEditText(ContentViewType.price);

        String currentText = editText.getText().toString();
        if(currentText.length() == 6 || currentText.length() == 9){
            currentText = currentText.substring(0, currentText.length()-1);
        }
        if(currentText.length()>0) {
            currentText = currentText.substring(0, currentText.length() - 1);
            editText.setText(currentText);
        }
    }

    public void backSpaceSingleCharacterInDate() {
        TypeEditText editText = getTypeEditText(ContentViewType.date);

        String currentText = editText.getText().toString();

        if(currentText.length()>0) {
            currentText = currentText.substring(0, currentText.length() - 1);
            editText.setText(currentText);
        }
    }

    private TypeEditText getTypeEditText(ContentViewType type){
        return allRawViews.get(type).valueEditText;
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
        for(ContentViewHolder holder: allRawViews.values()){
            holder.valueEditText.setText("");
        }
    }

    public interface OnDetailItemClick{
//        void onTypeItemClick();
//        void onDateItemClick();
//        void onPriceItemClick();
//        void onCategoryItemClick();
//        void onNoteItemClick();
        void onItemClick(ContentViewType type);
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

        container = new LinearLayout(context);
        container.setOrientation(LinearLayout.VERTICAL);

        layoutInflater = LayoutInflater.from(context);

        addContentView(ContentViewType.type);
        addContentView(ContentViewType.date);
        addContentView(ContentViewType.price);
        addContentView(ContentViewType.category);
        addContentView(ContentViewType.note);

        addClickListener();

        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        MarginLayoutParams marginParam = new MarginLayoutParams(layoutParams);
        marginParam.setMargins(20, 20, 20, 20);

        addView(container, layoutParams);


        setHighlight(ContentViewType.type);
    }

    private void addContentView(ContentViewType type){
        View contentView = layoutInflater.inflate(R.layout.custom_detail_view_raw, null, false);
        ContentViewHolder typeHolder = new ContentViewHolder(contentView, type);
        typeHolder.titleTextView.setText(type.titleRes);

        allRawViews.put(type, typeHolder);
        container.addView(contentView);
    }

    private void addClickListener(){
        rawLayoutClickListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentViewType type = (ContentViewType)view.getTag();

                if(onDetailItemClickListener!=null){
                    onDetailItemClickListener.onItemClick(type);
                }
            }
        };

        for(ContentViewHolder holder : allRawViews.values()){
            holder.layout.setOnClickListener(rawLayoutClickListener);
        }
    }

    public void setHighlight(ContentViewType type) {
        ContentViewType eachType;
        for(ContentViewHolder holder: allRawViews.values()){

            eachType = (ContentViewType)holder.layout.getTag();
            if(eachType.equals(type)){
                holder.layout.setBackgroundColor(getResources().getColor(R.color.detail_layout_highlight_yellow));
            }else{
                holder.layout.setBackgroundColor(getResources().getColor(android.R.color.white));
            }
        }

    }

    public void typeContent(ContentViewType type, String content, TypeEditText.OnTypeListener lisnter){
        allRawViews.get(type).valueEditText.setOnTypeListener(lisnter);
        allRawViews.get(type).valueEditText.startTypeText(content);
    }

    public void typeContent(ContentViewType type, String content){
        allRawViews.get(type).valueEditText.startTypeText(content);
    }

    public void readyInputKeyboardOnNote(){
        TypeEditText noteContentEditText = getTypeEditText(ContentViewType.note);
        noteContentEditText.setEnabled(true);
        noteContentEditText.setOnFocusChangeListener(this);
        noteContentEditText.requestFocus();
    }

    public String getValue(ContentViewType type){
        return allRawViews.get(type).valueEditText.getText().toString();
    }

    public boolean isAllContentValueValid(){
        ContentViewType type;
        for (ContentViewHolder eachHolder : allRawViews.values()){

            type = (ContentViewType)eachHolder.layout.getTag();
            if(ContentViewType.note.equals(type)){
                if(eachHolder.valueEditText.getText() != null){
                    continue;
                }else{
                    return false;
                }

            }else{
                if(!isEmpty(eachHolder.valueEditText.getText().toString())){
                    continue;
                }else {
                    return false;
                }
            }
        }

        return true;
    }

    public ContentViewType getInvalidContentType(){
        ContentViewType type;
        for (ContentViewHolder eachHolder : allRawViews.values()){

            type = (ContentViewType)eachHolder.layout.getTag();
            if(ContentViewType.note.equals(type)){
                if(eachHolder.valueEditText.getText() != null){
                    continue;
                }else{
                    return type;
                }

            }else{
                if(!isEmpty(eachHolder.valueEditText.getText().toString())){
                    continue;
                }else {
                    return type;
                }
            }
        }

        return null;
    }

    public Detail convertToDetail(){
        long categoryCid = DBManager.getInstance().getCategoryDBHandler()
                .queryCategory(CategoryType.getCategoryType(getValue(ContentViewType.type)), getValue(ContentViewType.category)).getCid();

        Detail detail = new Detail();
        detail.setIo(getValue(ContentViewType.type));
        detail.setTime(getTime(getValue(ContentViewType.date)));
        detail.setPrice(Integer.valueOf(getValue(ContentViewType.price)));
        detail.setCategory_cid(categoryCid);
        detail.setMark(getValue(ContentViewType.note));
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

    private class ContentViewHolder{
        public View layout;
        public TextView titleTextView;
        public TypeEditText valueEditText;

        ContentViewHolder(View rowContainer, ContentViewType type){
            layout = rowContainer;
            layout.setTag(type);
            layout.setEnabled(true);
            layout.setClickable(true);
            titleTextView = (TextView)layout.findViewById(R.id.detailRawView_titleTextView);
            valueEditText = (TypeEditText)layout.findViewById(R.id.detailRawView_valueEditText);
        }
    }

    public enum ContentViewType {
        type("type", R.string.add_detail_view_type),
        date("date", R.string.add_detail_view_date),
        price("price", R.string.add_detail_view_price),
        category("category", R.string.add_detail_view_category),
        note("note", R.string.add_detail_view_note);

        public final String typeName;
        public final int titleRes;

        private ContentViewType(String typeName, int titleRes){
            this.typeName = typeName;
            this.titleRes = titleRes;

        }
    }

    private boolean isEmpty(String str){
        return str == null || str.isEmpty();
    }
}
