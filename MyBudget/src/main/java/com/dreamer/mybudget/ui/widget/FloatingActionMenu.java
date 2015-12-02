package com.dreamer.mybudget.ui.widget;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import com.dreamer.mybudget.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roder Hu on 2015/11/26.
 */
public class FloatingActionMenu extends FrameLayout implements View.OnClickListener {
    private static final String TAG = FloatingActionMenu.class.getSimpleName();

    private static final int MENU_COUNT = 3;
    
    public static final int ACTION_ADD_DETAIL = 1;
    public static final int ACTION_ADD_BUDGET = 2;
    public static final int ACTION_ADD_ALARM = 3;

    private Context mContext = null;
    private FloatingActionButton mToggleButton = null;
    private FloatingActionButton mAddDetailButton = null;
    private FloatingActionButton mAddBudgetButton = null;
    private FloatingActionButton mAddAlarmButton = null;

    private OnActionButtonClickListener mActionClickListener = null;

    public void setActionClickListener(OnActionButtonClickListener actionClickListener) {
        this.mActionClickListener = actionClickListener;
    }

    private List<FloatingActionButton> menuButtons = new ArrayList<>(3);

    private boolean mIsExpand = false;

    private int animationCounter = 0;

    public FloatingActionMenu(Context context) {
        super(context);
        init(context);
    }

    public FloatingActionMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FloatingActionMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;

        View menuPanelView = LayoutInflater.from(mContext).inflate(R.layout.layout_floating_action_button_menu, null);
        mToggleButton = (FloatingActionButton) menuPanelView.findViewById(R.id.floatingActionButtonMenu_toggleButton);
        mToggleButton.setOnClickListener(this);

        mAddDetailButton = (FloatingActionButton) menuPanelView.findViewById(R.id.floatingActionButtonMenu_addDetailButton);
        setButton(mAddDetailButton, ACTION_ADD_DETAIL);

        mAddBudgetButton = (FloatingActionButton) menuPanelView.findViewById(R.id.floatingActionButtonMenu_addBudgetButton);
        setButton(mAddBudgetButton, ACTION_ADD_BUDGET);

        mAddAlarmButton = (FloatingActionButton) menuPanelView.findViewById(R.id.floatingActionButtonMenu_addAlarmButton);
        setButton(mAddAlarmButton, ACTION_ADD_ALARM);

        addView(menuPanelView);
    }

    private void setButton(FloatingActionButton menuButton, int actionId) {
        menuButton.setVisibility(View.INVISIBLE);
        menuButton.setTag(actionId);
        menuButton.setOnClickListener(this);
        menuButtons.add(menuButton);
    }

    public void expand() {
        //TODO Reset animatin counter
        animationCounter = 0;

        mIsExpand = true;
        mToggleButton.setEnabled(false);
        mToggleButton.animate()
                .setInterpolator(new OvershootInterpolator(4.0F))
                .rotation(45f)
                .setDuration(200)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mToggleButton.setEnabled(true);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();

        for (int i = 0; i < MENU_COUNT; i++) {
            Animation anim = geFadeInAnimation(i);
            menuButtons.get(i).startAnimation(anim);
        }
    }

    public void collapse() {
        mIsExpand = false;
        mToggleButton.setEnabled(false);
        for (int i = 0; i < MENU_COUNT; i++) {
            menuButtons.get(i).setEnabled(false);
        }

        mToggleButton.animate()
                .setInterpolator(new OvershootInterpolator(4.0F))
                .rotation(0f)
                .setDuration(200)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mToggleButton.setEnabled(true);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();
        for (int i = 0; i < MENU_COUNT; i++) {
            Animation anim = getSimpleFadeOutAnimation(i);
            menuButtons.get(i).startAnimation(anim);

        }
    }

    public void collapse(Runnable endAction) {
        //TODO Reset animation counter
        animationCounter = 0;

        mIsExpand = false;
        mToggleButton.setEnabled(false);
        for (int i = 0; i < MENU_COUNT; i++) {
            menuButtons.get(i).setEnabled(false);
        }

        mToggleButton.animate()
                .setInterpolator(new OvershootInterpolator(4.0F))
                .rotation(0f)
                .setDuration(200)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mToggleButton.setEnabled(true);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();

        for (int i = 0; i < MENU_COUNT; i++) {
            Animation anim = getFadeOutAnimationWithEndAction(i, endAction);
            menuButtons.get(i).startAnimation(anim);
        }

    }

    private Animation geFadeInAnimation(final int menuIndex) {
        AnimationSet anim = new AnimationSet(true);
        anim.setInterpolator(new OvershootInterpolator(2.5F));
        anim.setDuration(250);
        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        TranslateAnimation translate = new TranslateAnimation(0, 0, (float) ((4 - menuIndex) * 100), 0);
        anim.addAnimation(alpha);
        anim.addAnimation(translate);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                menuButtons.get(menuIndex).setVisibility(View.VISIBLE);
                animationCounter++;

                if(animationCounter == MENU_COUNT){
                    for (int i = 0; i < MENU_COUNT; i++) {
                        menuButtons.get(i).setEnabled(true);
                    }
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        return anim;
    }

    private Animation getSimpleFadeOutAnimation(final int menuIndex){
        return getFadeOutAnimation(menuIndex, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(TAG, "onAnimationEnd() called with: " + "animation = [" + animation + "]");

                menuButtons.get(menuIndex).setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private Animation getFadeOutAnimationWithEndAction(final int menuIndex, final Runnable endAction){
        return getFadeOutAnimation(menuIndex, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(TAG, "WithEndAction onAnimationEnd() called with: " + "animation = [" + animation + "]");

                menuButtons.get(menuIndex).setVisibility(View.INVISIBLE);
                animationCounter++;
                Log.d(TAG, "WithEndAction onAnimationEnd() called with: " + "animationCounter = [" + animationCounter + "]");

                if(animationCounter == MENU_COUNT){
                    endAction.run();

                    for (int i = 0; i < MENU_COUNT; i++) {
                        menuButtons.get(i).setEnabled(true);
                    }
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private Animation getFadeOutAnimation(final int menuIndex, Animation.AnimationListener listener) {
        AnimationSet anim = new AnimationSet(true);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(200);

        AlphaAnimation alpha = new AlphaAnimation(1, 0);
        TranslateAnimation translate = new TranslateAnimation(0, 0, 0, (float) ((4 - menuIndex) * 100));
        anim.addAnimation(alpha);
        anim.addAnimation(translate);
        anim.setAnimationListener(listener);

        return anim;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.floatingActionButtonMenu_toggleButton:
                if (mIsExpand) {
                    collapse();
                } else {
                    expand();
                }
                break;

            default:

                final int actionId = (int)view.getTag();
                collapse(new Runnable() {
                    @Override
                    public void run() {
                        Activity activity = (Activity)mContext;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mActionClickListener != null) {
                                    mActionClickListener.onActionClick(actionId);
                                }
                            }
                        });

                    }
                });

                break;
        }
    }

    public interface OnActionButtonClickListener {
        void onActionClick(int actionId);
    }
}
