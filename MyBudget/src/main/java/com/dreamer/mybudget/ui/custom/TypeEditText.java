package com.dreamer.mybudget.ui.custom;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

public class TypeEditText extends EditText implements View.OnTouchListener {

    private Context context = null;
    private String text = "";
    private Timer typeTimer = null;
    private long TYPE_DELAY_TIME = 0;
    private long DEFAULT_TYPE_SPEED = 100;
    private OnTypeListener listener = null;
    private OnClickListener onClickListener = null;

    public interface OnTypeListener {
        void onTypeStart();
        void onTypeFinish();
    }

    public TypeEditText(Context context) {
        super(context);
        init(context);
    }

    public TypeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TypeEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        this.context = context;
        hideKeyboard();
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (onClickListener != null) {
                onClickListener.onClick(v);
            }
        }
        return true;

    }


    @Override
    public void setOnClickListener(OnClickListener l) {
        this.onClickListener = l;
    }

    public void setOnTypeListener(OnTypeListener listener){
        this.listener = listener;
    }

    public void removeOnTypeListener(){
        this.listener = null;
    }

    public void startTypeText(String text) {

        this.text = text;
        getText().clear();
        hideKeyboard();
        requestFocus();
        createTypeTimer();
        typeTimer.schedule(new TypeTimerTask(), TYPE_DELAY_TIME, DEFAULT_TYPE_SPEED);
        if(listener != null) {
            listener.onTypeStart();
        }

    }

    private void createTypeTimer(){

        if(typeTimer != null){
            typeTimer.cancel();
        }
        typeTimer = new Timer();

    }

    private class TypeTimerTask extends TimerTask {
        @Override
        public void run() {
            post(new Runnable() {
                @Override
                public void run() {
                    String currentText = getText().toString();
                    int currentTextLength = currentText.length();
                    if(currentText.length() < text.length()) {
                        String appendText = text.substring(currentTextLength, currentTextLength + 1);
                        getText().append(appendText);
                        setSelection(currentTextLength + 1);
                    } else {
                        if(listener != null){
                            listener.onTypeFinish();
                        }
                        typeTimer.cancel();
                    }

                }
            });
        }
    };

    private void hideKeyboard(){

        InputMethodManager imm = (InputMethodManager) (context.getSystemService(Activity.INPUT_METHOD_SERVICE));
        imm.hideSoftInputFromWindow(this.getWindowToken(), 0);

    }

}
