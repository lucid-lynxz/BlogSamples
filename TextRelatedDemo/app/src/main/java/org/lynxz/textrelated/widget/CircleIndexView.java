package org.lynxz.textrelated.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import org.lynxz.textrelated.R;


/**
 * Created by zxz on 2016/3/3.
 * description : 圆圈内包含序号 0~99,a,b,c...
 */
public class CircleIndexView extends View {

    private float mTextSize;//文字大小,px
    private int circleSize;//绘图矩形区域大小
    private final int AREA_MIN_SIZE = sp2px(getContext(), 19);//绘图区域最小值
    private String mText;//绘制的文本
    private int mForeColor;//文本颜色
    private Paint mPaint;
    private float mTextWidth;
    private int mStrokeWidth;

    public CircleIndexView(Context context) {
        this(context, null);
    }

    public CircleIndexView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleIndexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleIndexView, defStyleAttr, 0);
        this.mForeColor = ta.getColor(R.styleable.CircleIndexView_foreColor, getResources().getColor(R.color.circle_number_color));
        this.mText = ta.getString(R.styleable.CircleIndexView_index);
        this.mTextSize = ta.getDimensionPixelSize(R.styleable.CircleIndexView_textSize, 30);
        this.mTextWidth = getTextWidth(mText);
        this.mStrokeWidth = (int) (this.mTextSize / 15);
        ta.recycle();
    }

    public void setText(String text) {
        this.mText = text;
        this.mTextWidth = getTextWidth(mText);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int minSize = (int) Math.max(this.mTextSize, AREA_MIN_SIZE);
        int minWidth = (int) Math.max(mTextWidth, minSize);

        int width = resolveSize(minWidth, widthMeasureSpec);
        int height = resolveSize(minSize, heightMeasureSpec);
        this.circleSize = Math.max(width, height);

        setMeasuredDimension(circleSize, circleSize);
        // Log.i("xxx", "circleSize " + circleSize + " mTextWidth = " + mTextWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(mForeColor);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        float radius = this.circleSize / 2;
        canvas.drawCircle(radius, radius, radius - mStrokeWidth, mPaint);

        if (Math.abs(circleSize - mTextWidth) <= 2 * mStrokeWidth
                || Math.abs(circleSize - mTextSize) <= 2 * mStrokeWidth) {
            mTextSize *= 0.88;
        }

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(this.mTextSize);

        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;
        float baseY = radius - (fontMetrics.descent - fontHeight / 2);
        canvas.drawText(this.mText, radius, baseY, mPaint);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public boolean isNumeric(String str) {
        if (!TextUtils.isEmpty(str))
            return str.matches("^[0-9]*$");
        else {
            return false;
        }
    }

    public float getTextWidth(String str) {
        Paint pFont = new Paint();
        pFont.setTextSize(this.mTextSize);
        return pFont.measureText(str);
    }

    /**
     * 计算允许的最大字体大小
     */
    private float calcMaxTextSize() {
        double powSize = Math.abs(Math.pow(circleSize, 2) - Math.pow(mTextWidth / 2, 2));
        return (float) Math.pow(powSize, 0.5);
    }
}
