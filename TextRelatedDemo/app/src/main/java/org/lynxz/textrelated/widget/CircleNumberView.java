package org.lynxz.textrelated.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import org.lynxz.textrelated.R;


/**
 * Created by zxz on 2016/3/3.
 * description : 圆圈内包含数字序号 0~99
 */
public class CircleNumberView extends View {

    private float textSize;//文字大小,px
    private int circleSize;//绘图矩形区域大小
    private final int AREA_MIN_SIZE = sp2px(getContext(), 19);//绘图区域最小值
    //    private final int AREA_MIN_SIZE = 30;//绘图区域最小值
    private int mNumber;//文字
    private String mText;//绘制的文本
    private int mForeColor;//文本颜色
    private Paint mPaint;

    public CircleNumberView(Context context) {
        this(context, null);
    }

    public CircleNumberView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleNumberView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleNumberView, defStyleAttr, 0);
        this.mForeColor = ta.getColor(R.styleable.CircleNumberView_foreColor, getResources().getColor(R.color.circle_number_color));
        this.mNumber = ta.getInteger(R.styleable.CircleNumberView_numberIndex, 1);//0~99
        this.textSize = ta.getDimensionPixelSize(R.styleable.CircleNumberView_textSize, 30);
        this.mText = this.mNumber + "";
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ta.recycle();
    }

    public void setNumber(int index) {
        this.mNumber = index;
        this.mText = this.mNumber + "";
        invalidate();
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int minSize = (int) Math.max(this.textSize, AREA_MIN_SIZE);
        int width = resolveSize(minSize, widthMeasureSpec);
        int height = resolveSize(minSize, heightMeasureSpec);
        setMeasuredDimension(width, height);
        this.circleSize = Math.min(width, height);
        if (this.circleSize >= this.textSize) {
            double ratio = 0.8;
            if (this.mNumber >= 10) {
                ratio = 0.6;
            }
            this.textSize = (float) (this.circleSize * ratio);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(mForeColor);
        float strokeWidth = this.textSize / 15;
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);

        float radius = this.circleSize / 2;
        canvas.drawCircle(radius, radius, radius - strokeWidth, mPaint);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(this.textSize);

        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;
        float baseY = radius - (fontMetrics.descent - fontHeight / 2);
        canvas.drawText(this.mText, radius, baseY, mPaint);
    }
}
