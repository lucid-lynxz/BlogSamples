package org.lynxz.customanimdemoapplication;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by zxz on 2016/4/26.
 */
public class BaseActivity extends Activity {


    public <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
