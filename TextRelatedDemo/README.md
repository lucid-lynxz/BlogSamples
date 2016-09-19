# TextView相关

[TOC]

# 图文混排
## 1. 使用Html.fromText实现简单的图文混排
[参考文章](http://my.oschina.net/zhongwenhao/blog/15700)

```java
// 需要解析的html源字符串
String mHtmlStr = "<font>毕业后,</font><img src='home'>常回家看看";
    
TextView tvMain = (TextView)findViewById(R.id.tv_main);
CharSequence text = Html.fromHtml(mHtmlStr, new Html.ImageGetter() {
    @Override
    public Drawable getDrawable(String source) {
        // 获取要替换的图片
        Drawable draw = getResources().getDrawable(R.drawable.book);
        // 设置尺寸,不设置的话,图片会显示不出来
        draw.setBounds(0, 0, 50, 50);

        // 解析img标签源,并替换响应图片
        if (source.equalsIgnoreCase("home")) {
            draw = getResources().getDrawable(R.drawable.home);
            draw.setBounds(0, 0, draw.getIntrinsicWidth(), draw.getIntrinsicHeight());
        }  
        Log.d(TAG, "source = " + source + " w-h " + draw.getIntrinsicWidth() + " - " + draw.getIntrinsicHeight());
        return draw;
    }
}, null);
// 将会处理好的图文混排字符串设置到TextView中
tvMain.setText(text);
```

## 2. 使用 `ImageSpan` 和 `SpannableString` 
```java
 // 根据Bitmap对象创建ImageSpan对象
ImageSpan imageSpan = new ImageSpan(this, bitmap);
// 创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
SpannableString spannableString = new SpannableString("face");
// 用ImageSpan对象替换face, 数值是要替换的字符起止位置,flag表示是否包含起止位置处的字符
spannableString.setSpan(imageSpan, 0, 4,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
```
## 3. 图片跟多行文本首行居中对齐显示


# 文字序号指定控件
![效果](https://raw.githubusercontent.com/lucid-lynxz/markdownPhotos/master/AndroidCustomWidget/CircleIndexDemo.png)