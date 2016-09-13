package org.lynxz.textrelated.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class URLDrawableProxy extends BitmapDrawable {
    private BitmapDrawable drawable;
    private boolean isScale;
    private int mLimitWidth;
    private WeakReference<TextView> mContainerRef = new WeakReference((Object)null);

    public URLDrawableProxy() {
    }

    public void setContainer(TextView container, int limitWidth) {
        this.mContainerRef = new WeakReference(container);
        this.mLimitWidth = limitWidth;
    }

    protected void setBitmapResource(Context context, Bitmap bitmap, int limitWidth) {
        this.drawable = new BitmapDrawable(context.getResources(), bitmap);
        int width = MixedUtils.dpToPx(context, (float)bitmap.getWidth());
        int height = MixedUtils.dpToPx(context, (float)bitmap.getHeight());
        limitWidth = limitWidth > 0?limitWidth:width;
        boolean isLimit = width > limitWidth;
        if(isLimit) {
            float scale = (float)limitWidth / (float)width;
            width = limitWidth;
            height = (int)((float)height * scale);
            this.isScale = true;
        }

        this.drawable.setBounds(0, 4, width, height);
    }

    protected Bitmap getBitmapResource() {
        return this.drawable != null?this.drawable.getBitmap():null;
    }

    public boolean isScale() {
        return this.isScale;
    }

    protected boolean onStateChange(int[] state) {
        return super.onStateChange(state);
    }

    public void draw(Canvas canvas) {
        if(this.drawable != null) {
            Bitmap bitmap = this.drawable.getBitmap();
            if(bitmap != null && !bitmap.isRecycled()) {
                this.drawable.draw(canvas);
            }
        }

    }

    public int getIntrinsicWidth() {
        return this.drawable == null?0:this.drawable.getBounds().width();
    }

    public int getIntrinsicHeight() {
        return this.drawable == null?0:this.drawable.getBounds().height() + 8;
    }
}
