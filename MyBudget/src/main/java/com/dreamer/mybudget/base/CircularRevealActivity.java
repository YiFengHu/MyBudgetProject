package com.dreamer.mybudget.base;

import android.animation.Animator;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;

import com.dreamer.mybudget.R;
import com.dreamer.mybudget.ui.layout.RevealLayout;


public abstract class CircularRevealActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = CircularRevealActivity.class.getSimpleName();

    private View mRootView = null;
    protected LinearLayout mContainerView = null;
    private FloatingActionButton mFAB = null;
    private RevealLayout mRevealLayout;
    private Runnable revealRunnable;

    private int revealX;
    private int revealY;

    protected abstract View getContentView();

    @Override
    protected void onStart() {
        super.onStart();
        setTheme(R.style.TransparentTheme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = LayoutInflater.from(this).inflate(R.layout.activity_circular_reveal, null);
        setContentView(mRootView);

        mContainerView = (LinearLayout)findViewById(R.id.circular_container);
        mFAB = (FloatingActionButton) findViewById(R.id.single_fab);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mFAB.setElevation(0f);
        }

        mFAB.setOnClickListener(this);

        mRevealLayout = (RevealLayout) findViewById(R.id.reveal_layout);
        mRevealLayout.setContentShown(false);

        mContainerView.addView(getContentView());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFAB.animate().setInterpolator(new OvershootInterpolator(3.0F))
                .rotation((180f + 45f)).setDuration(500).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(getRevealRunnable());
            }
        }).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Disable finishing animation
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.single_fab:

                finishActivity();
                break;
        }
    }

    protected void finishActivity(){
        setTheme(R.style.TransparentTheme);
        mRevealLayout.hide(revealX, revealY);

        mFAB.animate().rotation(0f).setDuration(400).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();
    }

    private Runnable getRevealRunnable(){
        if(revealRunnable == null){
            revealRunnable = new Runnable() {
                @Override
                public void run() {

                    ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(mFAB.getLayoutParams());

                    revealX = mRevealLayout.getWidth() - margin.rightMargin - mFAB.getWidth()/2;
                    revealY = mRevealLayout.getHeight() - margin.bottomMargin - mFAB.getHeight()/2;

                    if (!mRevealLayout.isContentShown()) {
                        mRevealLayout.show(revealX, revealY, new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }
                }
            };
        }

        return revealRunnable;
    }
}
