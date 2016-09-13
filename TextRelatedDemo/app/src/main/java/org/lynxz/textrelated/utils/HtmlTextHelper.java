package org.lynxz.textrelated.utils;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.widget.TextView;

import org.xml.sax.XMLReader;

public class HtmlTextHelper {
    public HtmlTextHelper() {
    }

    //处理Html中的img标签, 显示src引用的图片, 并在每张图片上添加ClickableSpan
    public static Spanned setHtmlTextAndImageLightBox(TextView tv, int limitImageWidth, String html,
                                                      FragmentActivity activity, String quizTypeName) {
        if (null != tv && null != html) {
            html = cleanHtml(html);
            String header = "";

            for (int i = 0; i < quizTypeName.length() * 4 + 3; ++i) {
                header = header + "&nbsp";
            }

            header = header + " ";
            if (html.startsWith("<p>")) {
                html = html.replaceFirst("<p>", header);
            } else {
                html = header + html;
            }

            return setHtmlTextAndImageLightBox(tv, limitImageWidth, html, activity);
        } else {
            return null;
        }
    }


    /**
     * 处理Html中的img标签, 显示src引用的图片, 并在每张图片上添加ClickableSpan
     *
     * @param tv
     * @param limitImageWidth 图片默认宽度
     * @param html            html字符串
     * @param activity
     * @return
     */
    public static Spanned setHtmlTextAndImageLightBox(TextView tv, int limitImageWidth, String html,
                                                      FragmentActivity activity, boolean isImageClickable) {
        if (null == tv || null == html) {
            return null;
        }
        html = cleanHtml(html);
        limitImageWidth = limitImageWidth == 0 ? getScreenLimit(activity, 32) : limitImageWidth;
        Spanned htmlSpan = parseImgTag(tv, limitImageWidth, cleanHtmlLineFeed(html));
        //modify 修改图片点击打开大图
        if (isImageClickable) {
            setImageSpanClickable((Spannable) htmlSpan, activity);
        }
        //modify by zjz
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        tv.setText(htmlSpan);
        return htmlSpan;
    }

    /**
     * 处理Html中的img标签, 显示src引用的图片, 并在每张图片上添加ClickableSpan
     *
     * @param tv
     * @param limitImageWidth 图片默认宽度
     * @param html            html字符串
     * @param activity
     * @return
     */
    public static Spanned setHtmlTextAndImageLightBox(TextView tv, int limitImageWidth, String html,
                                                      FragmentActivity activity) {
        if (null != tv && null != html) {
            html = cleanHtml(html);
            limitImageWidth = limitImageWidth == 0 ? getScreenLimit(activity, 32) : limitImageWidth;
            Spanned htmlSpan = parseImgTag(tv, limitImageWidth, cleanHtmlLineFeed(html));
            setImageSpanClickable((Spannable) htmlSpan, activity);
            tv.setMovementMethod(LinkMovementMethod.getInstance());
            tv.setText(htmlSpan);
            return htmlSpan;
        } else {
            return null;
        }
    }

    private static String cleanHtml(String html) {
        html = html.replaceAll("<p style=\\\" text-align:justify; \\\">", "<p>");
        html = html.replaceAll("<p></p>", "");
        int lastP;
        String first;
        String second;
        if (html.endsWith("</span>")) {
            lastP = html.lastIndexOf("<span>");
            if (lastP >= 0) {
                first = html.substring(0, lastP);
                second = html.substring(lastP + 6, html.lastIndexOf("</span>"));
                html = first + second;
            }
        }

        if (html.endsWith("</p>")) {
            lastP = html.lastIndexOf("<p>");
            if (lastP >= 0) {
                first = html.substring(0, lastP);
                second = html.substring(lastP + 3, html.lastIndexOf("</p>"));
                html = first + second;
            }
        }

        return html;
    }

    private static Spanned parseImgTag(TextView tv, int limitImageWidth, String html) {
        HtmlImageParser getter = new HtmlImageParser(tv, limitImageWidth);
        return Html.fromHtml(html, getter, new Html.TagHandler() {
            public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
                if ("img".equals(tag)) {
                    output.insert(output.length() - 1, " ");
                    output.append(" ");
                }

            }
        });
    }

    private static void setImageSpanClickable(Spannable htmlSpan, FragmentActivity activity) {
        ImageSpan[] imageSpans = (ImageSpan[]) htmlSpan.getSpans(0, htmlSpan.length(), ImageSpan.class);
        ImageSpan[] var3 = imageSpans;
        int var4 = imageSpans.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            ImageSpan imageSpan = var3[var5];
            int imageSpanStart = htmlSpan.getSpanStart(imageSpan);
            int imageSpanEnd = htmlSpan.getSpanEnd(imageSpan);
            ClickableImageSpan clickableImageSpan = new ClickableImageSpan(imageSpan, activity);
            htmlSpan.setSpan(clickableImageSpan, imageSpanStart, imageSpanEnd, 17);
        }

    }

    /**
     * 去除html中的换行
     *
     * @param src
     * @return
     */
    public static String cleanHtmlLineFeed(String src) {
        boolean pos = true;
        boolean subPos = true;
        String LINE_FEED_LEFT = "<p>";
        String LINE_FEED_LEFT_UPPER = "<P>";
        String LINE_FEED_RIGHT = "</p>";
        String LINE_FEED_RIGHT_UPPER = "</P>";
        String dest = src;
        if (src.length() < LINE_FEED_LEFT.length()) {
            return src;
        } else {
            int pos1;
            do {
                pos1 = dest.indexOf(LINE_FEED_LEFT);
                if (pos1 == 0) {
                    dest = dest.substring(LINE_FEED_LEFT.length());
                }

                pos1 = dest.indexOf(LINE_FEED_LEFT_UPPER);
                if (pos1 == 0) {
                    dest = dest.substring(LINE_FEED_LEFT_UPPER.length());
                }
            } while (pos1 == 0);

            int subPos1;
            do {
                pos1 = dest.lastIndexOf(LINE_FEED_RIGHT);
                subPos1 = dest.length() - LINE_FEED_RIGHT.length();
                if (pos1 != -1 && pos1 == subPos1) {
                    dest = dest.substring(0, subPos1);
                }

                pos1 = dest.indexOf(LINE_FEED_RIGHT_UPPER);
                subPos1 = dest.length() - LINE_FEED_RIGHT_UPPER.length();
                if (pos1 != -1 && pos1 == subPos1) {
                    dest = dest.substring(0, subPos1);
                }
            } while (pos1 != -1 && pos1 == subPos1);

            return dest;
        }
    }

    private static int getScreenLimit(Context context, int dpSize) {
        return MixedUtils.getScreenDimention(context)[0] - MixedUtils.dpToPx(context, (float) dpSize);
    }

    public static String clearTag(String html) {
        if (html == null) {
            return "";
        } else {
            String str = html.replaceFirst("<p>", "");
            str = str.replaceFirst("</p>", "");
            return str;
        }
    }
}
