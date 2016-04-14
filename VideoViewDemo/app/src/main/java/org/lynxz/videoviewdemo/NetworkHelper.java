package org.lynxz.videoviewdemo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkHelper {
    public static final int NETWORK_TYPE_INVALID = 0;
    public static final int NETWORK_TYPE_MOBILE = 1;
    public static final int NETWORK_TYPE_WIFI = 2;

    /**
     * 获取当前网络连接状况
     *
     * @return <br>
     * 网络不可用 : {@link #NETWORK_TYPE_INVALID} <br>
     * 蜂窝网络 : {@link #NETWORK_TYPE_MOBILE}<br>
     * wifi连接 : {@link #NETWORK_TYPE_WIFI}
     */
    public static int getNetworkType(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();
            if (type.equalsIgnoreCase("WIFI")) {
                return NETWORK_TYPE_WIFI;
            } else if (type.equalsIgnoreCase("MOBILE")) {
                return NETWORK_TYPE_MOBILE;
            }
        }
        return NETWORK_TYPE_INVALID;
    }
}