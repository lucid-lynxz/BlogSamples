package org.lynxz.textrelated.utils;

import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;

import org.xml.sax.XMLReader;

/**
 * Created by zxz on 2016/5/8.
 */
public class ImageUtil {
    private static Spanned parseImgTag(TextView tv, int limitImageWidth, String html) {
        HtmlImageParser getter = new HtmlImageParser(tv, limitImageWidth);
        return Html.fromHtml(html, getter, new Html.TagHandler() {
            public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
                if("img".equals(tag)) {
                    output.insert(output.length() - 1, " ");
                    output.append(" ");
                }

            }
        });
    }

}
