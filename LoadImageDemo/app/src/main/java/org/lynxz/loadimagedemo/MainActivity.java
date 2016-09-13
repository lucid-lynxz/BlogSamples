package org.lynxz.loadimagedemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

public class MainActivity extends AppCompatActivity {

    //    private String imageUrl = "http://img5.imgtn.bdimg.com/it/u=2370968757,1012541204&fm=11&gp=0.jpg";
    //        private String imageUrl = "http://v11.huayu.nd/s/p/3038/bddb446ca24240569dc3736f30d4f097/image/1.jpg/d736544a6ec0b44c8e8b6cecf6e04158";
    //        private String imageUrl = "http://7sbxno.com2.z0.glb.clouddn.com/aa.jpg";
    private String imageUrl = "http://7sbxno.com2.z0.glb.clouddn.com/bb.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initPara();
    }

    private void initPara() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Log.i("xxx", "densityDpi = " + metrics.density + " -- " + metrics.densityDpi + " -- " + metrics.scaledDensity);

    }

    private void initView() {
        final ImageView iv = (ImageView) findViewById(R.id.iv);

        Glide.with(this).load(imageUrl)
                .asBitmap()
                .fitCenter()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        //                        iv.setImageBitmap(zoomImage(resource, getScreenSize().x, getScreenSize().y));
                        iv.setImageBitmap(resource);
                    }
                });
    }


    public Point getScreenSize() {
        Activity act = MainActivity.this;
        WindowManager wm = act.getWindowManager();
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return point;
    }

    /***
     * 图片的缩放方法
     *
     * @param bgimage   ：源图片资源
     * @param newWidth  ：缩放后宽度
     * @param newHeight ：缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        float scale = scaleWidth;//scaleWidth >= scaleHeight ? scaleHeight : scaleWidth;

        scale = scale >= 2 ? 2 : scale;
        // 缩放图片动作
        //        matrix.postScale(scaleWidth, scaleHeight);
        matrix.postScale(scale, scale);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }
}

