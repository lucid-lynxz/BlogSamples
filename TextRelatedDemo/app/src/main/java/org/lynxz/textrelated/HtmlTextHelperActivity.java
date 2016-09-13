package org.lynxz.textrelated;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.lynxz.textrelated.utils.HtmlTextHelper;

public class HtmlTextHelperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html_text_helper);


        String bodyStr = "<p style=\"text-align: center;\"><strong>推理能力测评</strong><img alt=\"\" src=\"http://betacs.101.com/v0.1/download?dentryId=abc7c2f5-e167-400c-b247-8cc5f2729d10\" style=\"float:right\" width=\"200\" /></p>\n\n<p>本测试中，你需要在3分钟内，根据现有图形的规律寻找一张大图中，缺失的九分之一的内容，判断并点击正确选项。<strong>​​</strong></p>\n\n<p>例：正确答案为H</p>\n";


        TextView tvHelper = (TextView) findViewById(R.id.tv_helper);
        HtmlTextHelper.setHtmlTextAndImageLightBox(tvHelper, 0, bodyStr, this);

    }
}
