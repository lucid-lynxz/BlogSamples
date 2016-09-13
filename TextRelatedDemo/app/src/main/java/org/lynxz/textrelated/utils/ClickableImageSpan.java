package org.lynxz.textrelated.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import uk.co.senab.photoview.PhotoView;

public class ClickableImageSpan extends ClickableSpan {
    private ImageSpan mImageSpan;
    private FragmentActivity mActivity;

    public ClickableImageSpan() {
    }

    public ClickableImageSpan(ImageSpan span, FragmentActivity activity) {
        this.mImageSpan = span;
        this.mActivity = activity;
    }

    private boolean isCanClick() {
        return this.mImageSpan.getDrawable() instanceof URLDrawableProxy?((URLDrawableProxy)this.mImageSpan.getDrawable()).isScale():false;
    }

    public void onClick(View widget) {
        if(this.isCanClick()) {
            ClickableImageSpan.ImgLightBoxFragment.newInstance(this.mImageSpan).show(this.mActivity.getSupportFragmentManager(), "ImgLightBoxFragment");
        }

    }

    public static class ImgLightBoxFragment extends DialogFragment {
        private ImageSpan mImageSpan;
        private ImageView mIVLightBox;

        public static ClickableImageSpan.ImgLightBoxFragment newInstance(ImageSpan span) {
            return new ClickableImageSpan.ImgLightBoxFragment(span);
        }

        @SuppressLint({"ValidFragment"})
        public ImgLightBoxFragment(ImageSpan span) {
            this.mImageSpan = span;
        }

        public ImgLightBoxFragment() {
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            this.mIVLightBox = new PhotoView(inflater.getContext());
            this.mIVLightBox.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
            Bitmap bitmap = ((URLDrawableProxy)this.mImageSpan.getDrawable()).getBitmapResource();
            Context ctx = inflater.getContext();
            int width = MixedUtils.dpToPx(ctx, (float)(bitmap.getWidth() << 1));
            int height = MixedUtils.dpToPx(ctx, (float)(bitmap.getHeight() << 1));
            BitmapDrawable drawable = new BitmapDrawable(this.getResources(), bitmap);
            drawable.setBounds(0, 0, width, height);
            int[] size = MixedUtils.getScreenDimention(inflater.getContext());
            size = this.resetSize(size, width, height);
            this.mIVLightBox.setMinimumWidth(size[0] - MixedUtils.dpToPx(ctx, 16.0F));
            this.mIVLightBox.setMinimumHeight(size[1]);
            this.mIVLightBox.setImageDrawable(drawable);
            return this.mIVLightBox;
        }

        private int[] resetSize(int[] screenSize, int picWidth, int picHeight) {
            int width = screenSize[0];
            int limitHeight = screenSize[1] * 2 / 3;
            int height = limitHeight > picHeight?picHeight:limitHeight;
            return new int[]{width, height};
        }

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.setStyle(1, 0);
        }

        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            this.getDialog().setCanceledOnTouchOutside(true);
        }
    }
}
