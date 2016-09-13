package org.lynxz.textrelated.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.lynxz.textrelated.R;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class HtmlImageParser implements Html.ImageGetter {
    private final WeakReference<TextView> mContainerRef;
    private final int mLimitWidth;
    private static Map<String, Bitmap> bitmapMap = new HashMap();

    public HtmlImageParser(TextView container, int limitWidth) {
        this.mContainerRef = new WeakReference(container);
        this.mLimitWidth = limitWidth;
    }

    public Drawable getDrawable(final String source) {
        final URLDrawableProxy drawable = new URLDrawableProxy();
        TextView tv = (TextView) this.mContainerRef.get();
        if (tv == null) {
            return null;
        } else {
            Bitmap bitmap = (Bitmap) bitmapMap.get(source);
            if (bitmap != null) {
                this.showImage(drawable, bitmap, this.mLimitWidth);
            } else {
                this.showImage(drawable, BitmapFactory.decodeResource(tv.getResources(), R.drawable.hyhtt_image_error), (int) tv.getTextSize());
                Glide.with(tv.getContext()).load(source).asBitmap().into(new SimpleTarget<Bitmap>(this.mLimitWidth, this.mLimitWidth) {
                    public void onResourceReady(Bitmap loadedImage, GlideAnimation anim) {
                        HtmlImageParser.bitmapMap.put(source, loadedImage);
                        HtmlImageParser.this.showImage(drawable, loadedImage, HtmlImageParser.this.mLimitWidth);
                    }
                });
            }

            return drawable;
        }
    }

    private void showImage(URLDrawableProxy drawable, Bitmap loadedImage, int limitWidth) {
        TextView container = (TextView) this.mContainerRef.get();
        if (container != null) {
            if (loadedImage != null) {
                drawable.setBitmapResource(container.getContext(), loadedImage, limitWidth);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                this.resizeContainer(drawable);
            }
        }
    }

    private void resizeContainer(Drawable d) {
        TextView container = (TextView) this.mContainerRef.get();
        if (container != null) {
            container.postDelayed(new Runnable() {
                public void run() {
                    HtmlImageParser.this.doResize();
                }
            }, 200L);
        }
    }

    private void doResize() {
        TextView container = (TextView) this.mContainerRef.get();
        if (container != null) {
            if (Build.VERSION.SDK_INT >= 14) {
                container.setMaxHeight(99999);
            } else {
                container.setEllipsize((TextUtils.TruncateAt) null);
            }

            container.setText(container.getText());
        }
    }
}
