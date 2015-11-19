package com.dreamer.mybudget.ui.custom;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dreamer.mybudget.R;

import java.util.ArrayList;

/**
 * Created by Roder Hu on 15/7/1.
 */
public class DetailSetterView extends RelativeLayout{

    private static final int DEFAULT_OPTION_COUNT = 3;

    private Context context = null;
    private LayoutInflater layoutInflater = null;
    private GridLayout gridlayout = null;
    private OnClickListener cardViewClickLitener = null;

    private ArrayList<String> optionList = null;
    private ArrayList<CardView> allOptionTextViews = null;

    public DetailSetterView(Context context) {
        super(context);
        this.context = context;
        this.optionList = new ArrayList<String>(DEFAULT_OPTION_COUNT);
        allOptionTextViews = new ArrayList<CardView>(optionList.size());
        initCardViewClickListener();
        init(context);
    }

    public DetailSetterView(Context context, AttributeSet attrs) {
        super(context);
        this.context = context;
        this.optionList = new ArrayList<String>(DEFAULT_OPTION_COUNT);
        allOptionTextViews = new ArrayList<CardView>(optionList.size());
        initCardViewClickListener();
        init(context);
    }

    public DetailSetterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context);
        this.context = context;
        this.optionList = new ArrayList<String>(DEFAULT_OPTION_COUNT);
        allOptionTextViews = new ArrayList<CardView>(optionList.size());
        initCardViewClickListener();
        init(context);
    }

    private void init(Context context) {
        setBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_light));
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View DetailSetterView = layoutInflater.inflate(R.layout.detail_setter_view, null, false);

        gridlayout = (GridLayout)DetailSetterView.findViewById(R.id.detailSetterView_gridLayout);

        for(int i=0; i<optionList.size(); i++){
            TextView textView = new TextView(context);
            textView.setText(optionList.get(i));
            textView.setTextSize(20);
            textView.setPadding(2, 2, 2, 2);
            textView.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_bright));

            LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

            CardView cardView = new CardView(context);
            cardView.setPadding(2, 2, 2, 2);
            cardView.setOnClickListener(cardViewClickLitener);

            cardView.addView(textView, layoutParams);

            gridlayout.addView(cardView, layoutParams);

            allOptionTextViews.add(cardView);
        }

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(DetailSetterView, params);
    }

    private void initCardViewClickListener() {
        cardViewClickLitener = new OnClickListener() {
            @Override
            public void onClick(final View view) {

                int viewWidth = view.getWidth();
                DisplayMetrics displaymetrics = new DisplayMetrics();
                int screenWidth = displaymetrics.widthPixels;
                float widthRate = screenWidth / viewWidth;

                ScaleAnimation spanAnimation = new ScaleAnimation(1, 5, 1, 1);
                spanAnimation.setDuration(300);
                Animation fadeOutAnimation = AnimationUtils.loadAnimation(context, R.anim.abc_fade_out);
                final Animation moveUpAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_top_move_out);

                CardView otherCardView = null;
                for (int i = 0; i < optionList.size(); i++) {
                    otherCardView = allOptionTextViews.get(i);
                    if(view.getId() != otherCardView.getId()){
                        otherCardView.startAnimation(fadeOutAnimation);
                    }
                    otherCardView.setVisibility(View.INVISIBLE);
                }


                    spanAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            view.startAnimation(moveUpAnimation);

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                    view.startAnimation(spanAnimation);
            }
        };
    }

    public void setOptionList(ArrayList<String> optionList){
        this.optionList = optionList;
        init(context);
    }

}
