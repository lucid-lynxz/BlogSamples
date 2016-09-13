package org.lynxz.textrelated;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.widget.TextView;

import org.lynxz.textrelated.widget.CenteredImageSpan;

public class ImageTextAlignActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_text_align);

        initView();
    }

    private void initView() {
        TextView tvImg = findView(R.id.tv_img_center);
        CenteredImageSpan is = new CenteredImageSpan(this, R.drawable.shape_circle);
        String txtContent = tvImg.getText().toString();
        SpannableString ss = new SpannableString(txtContent);
        ss.setSpan(is, 0, txtContent.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        tvImg.setText(ss);
    }
}
