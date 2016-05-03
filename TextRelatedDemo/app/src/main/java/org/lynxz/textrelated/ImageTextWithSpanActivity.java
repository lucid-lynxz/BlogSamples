package org.lynxz.textrelated;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.Random;

/**
 * 通过spannableString实现图文混排
 * 参考: http://www.ithao123.cn/content-504375.html
 *          http://www.cuiweiyou.com/1739.html
 */
public class ImageTextWithSpanActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_text_with_span);

        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {
        TextView tvMain = findView(R.id.tv_main);
        tvMain.setFocusable(false);

        // 将随机获得的图像追加到EditText控件的最后
        tvMain.append(parseHtml());
        tvMain.append(" 测试文本 ");
        tvMain.append(parseHtml());
        tvMain.append(" 这表情还不错吧? ");
        tvMain.append(parseHtml());
        tvMain.append(" 随机添加表情 ");
        tvMain.append(parseHtml());
    }

    private SpannableString parseHtml() {
        SpannableString spannableString = null;
        try {
            // 获取资源图片，随机产生1至9的整数
            int randomId = 1 + new Random().nextInt(9);
            // 根据随机产生的1至9的整数从R.drawable类中获得相应资源ID（静态变量）的Field对象
            Field field = R.drawable.class.getDeclaredField("face" +
                    randomId);
            // 获得资源ID的值，也就是静态变量的值
            int resourceId = Integer.parseInt(field.get(null).toString());
            // 根据资源ID获得资源图像的Bitmap对象
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                    resourceId);


            // 根据Bitmap对象创建ImageSpan对象
            ImageSpan imageSpan = new ImageSpan(this, bitmap);
            // 创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
            spannableString = new SpannableString("face");
            // 用ImageSpan对象替换face
            spannableString.setSpan(imageSpan, 0, 4,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            return spannableString;
        }
    }
}
