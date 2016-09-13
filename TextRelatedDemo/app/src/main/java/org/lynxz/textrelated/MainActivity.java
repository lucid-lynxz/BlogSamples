package org.lynxz.textrelated;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        Button btnImageTextHtml = findView(R.id.btn_image_text_form_html);
        Button btnImageTextSpan = findView(R.id.btn_image_text_span);
        Button btnCircleNumber = findView(R.id.btn_circle_number);
        Button btnHtmlText = findView(R.id.btn_html_text_helper);
        Button btnImgTextAlignCenter = findView(R.id.btn_img_align_center);
        btnImageTextHtml.setOnClickListener(this);
        btnImageTextSpan.setOnClickListener(this);
        btnCircleNumber.setOnClickListener(this);
        btnHtmlText.setOnClickListener(this);
        btnImgTextAlignCenter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = null;
        switch (id) {
            case R.id.btn_image_text_form_html:
                intent = new Intent(this, ImageTextWithFromHtmlActivity.class);
                break;
            case R.id.btn_image_text_span:
                intent = new Intent(this, ImageTextWithSpanActivity.class);
                break;
            case R.id.btn_circle_number:
                intent = new Intent(this, CircleNumberActivity.class);
                break;
            case R.id.btn_html_text_helper:
                intent = new Intent(this, HtmlTextHelperActivity.class);
                break;
            case R.id.btn_img_align_center:
                intent = new Intent(this, ImageTextAlignActivity.class);
                break;
            default:

        }

        if (intent != null) {
            startActivity(intent);
        }
    }
}
