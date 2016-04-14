package org.lynxz.videoviewdemo.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by zxz on 2016/4/14.
 * 显示通知消息
 */
public class MessageUtils {

    public static void showToast(@NonNull Context cxt, String msg) {
        Toast.makeText(cxt, msg, Toast.LENGTH_SHORT).show();
    }


    /**
     * 显示带单一按钮的提示框
     */
    public static void showAlertDialog(@NonNull Context cxt, String title, String msg, DialogInterface.OnClickListener clickListener) {

        if (TextUtils.isEmpty(title)) {
            title = "提示";
        }

        new AlertDialog.Builder(cxt).setTitle(title).setMessage(msg).setPositiveButton("确定", clickListener).show();
    }

}
