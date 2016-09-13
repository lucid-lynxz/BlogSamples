package org.lynxz.customanimdemoapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;

public class MainCircleRevealActivity extends BaseActivity {

    ImageView mIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_reveal);
        initView();
    }

    private void initView() {
        mIv = findView(R.id.iv_reveal);
        findView(R.id.btn_entry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainCircleRevealActivity.this, MainActivity.class));
            }
        });

        findView(R.id.btn_circle_reveal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the center for the clipping circle
                int cx = mIv.getWidth() / 2;
                int cy = mIv.getHeight() / 2;

                // get the final radius for the clipping circle
                float finalRadius = (float) Math.hypot(cx, cy);

                // create the animator for this view (the start radius is zero)
                Animator anim =
                        ViewAnimationUtils.createCircularReveal(mIv, cx, cy, 0, finalRadius);
                mIv.setVisibility(View.VISIBLE);
                anim.start();
            }
        });
        findView(R.id.btn_circle_reveal_hide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the center for the clipping circle
                int cx = mIv.getWidth() / 2;
                int cy = mIv.getHeight() / 2;

                // get the final radius for the clipping circle
                float finalRadius = (float) Math.hypot(cx, cy);

                // create the animator for this view (the start radius is zero)
                Animator anim =
                        ViewAnimationUtils.createCircularReveal(mIv, cx, cy, finalRadius, 0);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mIv.setVisibility(View.INVISIBLE);
                    }
                });
                anim.start();
            }
        });
    }
}
