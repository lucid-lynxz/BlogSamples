package org.lynxz.textrelated;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

/**
 * 使用Html.fromText实现简单的图文混排
 * 参考: http://my.oschina.net/zhongwenhao/blog/157009
 */
public class ImageTextWithFromHtmlActivity extends BaseActivity {

    private String mHtmlStr;
    private static final String TAG = "ImageTextActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_text);

        initData();
        initView();
    }

    private void initData() {
        mHtmlStr = "<font>毕业后,</font><img src='home'>常回家看看,<img src='study'>更要好好学习,<br><img src='laugh'>,最重要的是保持好心态";
    }

    private void initView() {
        TextView tvMain = findView(R.id.tv_main);
        CharSequence text = Html.fromHtml(mHtmlStr, new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                Drawable draw = getResources().getDrawable(R.drawable.book);
                draw.setBounds(0, 0, 50, 50);
                if (source.equalsIgnoreCase("home")) {
                    draw = getResources().getDrawable(R.drawable.home);
                    draw.setBounds(0, 0, draw.getIntrinsicWidth(), draw.getIntrinsicHeight());
                } else if (source.equalsIgnoreCase("study")) {
                    draw = getResources().getDrawable(R.drawable.study);
                    draw.setBounds(0, 0, draw.getIntrinsicWidth(), draw.getIntrinsicHeight());
                } else if (source.equalsIgnoreCase("laugh")) {
                    draw = getResources().getDrawable(R.drawable.laugh);
                    draw.setBounds(0, 0, draw.getIntrinsicWidth(), draw.getIntrinsicHeight());
                }
                Log.d(TAG, "source = " + source + " w-h " + draw.getIntrinsicWidth() + " - " + draw.getIntrinsicHeight());
                return draw;
            }
        }, null);
        tvMain.setText(text);
    }
}
