package org.lynxz.textrelated;

import android.app.Activity;

/**
 * Created by zxz on 2016/5/3.
 */
public class BaseActivity extends Activity {
    public <T> T findView(int id) {
        return (T) findViewById(id);
    }
}
