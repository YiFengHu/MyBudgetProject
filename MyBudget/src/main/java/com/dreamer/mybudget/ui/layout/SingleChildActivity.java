package com.dreamer.mybudget.ui.layout;

import android.animation.Animator;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;

import com.dreamer.mybudget.R;


public class SingleChildActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = SingleChildActivity.class.getSimpleName();

    private FloatingActionButton mFAB = null;
    private RevealLayout mRevealLayout;
    private Runnable revealRunnable;

    private int revealX;
    private int revealY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        mFAB = (FloatingActionButton) findViewById(R.id.single_fab);
        mFAB.setOnClickListener(this);

        mRevealLayout = (RevealLayout) findViewById(R.id.reveal_layout);
        mRevealLayout.setContentShown(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFAB.animate().rotation(45f).setDuration(500).start();

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

                break;
        }
    }

    private Runnable getRevealRunnable(){
        if(revealRunnable == null){
            revealRunnable = new Runnable() {
                @Override
                public void run() {

                    revealX = mRevealLayout.getWidth() - mFAB.getPaddingRight() - mFAB.getWidth()/2;
                    revealY = mRevealLayout.getHeight() - mFAB.getPaddingBottom() - mFAB.getHeight()/2;

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
