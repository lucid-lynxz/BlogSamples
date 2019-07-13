package org.lynxz.locationdemo.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;


import java.util.List;


/**
 * Created by lynxz on 17/04/2017.
 * 获取经纬度信息
 */
public class LocationUtil {
    private static final String TAG = "LocationUtil";
    private static LocationManager mLocationManager;
    private static Location mLastLocation;
    private static final LocationUtil instance = new LocationUtil();
    private static final int ACCURACY = 100;// 定位精度,持续定位,单位:米
    private static final long MIN_TIME = 60000;// 定位间隔,单位:毫秒
    //    private HashMap<String, Boolean> hasSetLocationListener = new HashMap<>();
    private LocationListener sLocationListener;
    private long lastUpdateTime = 0;//上次获取定位的时间

    private LocationUtil() {
    }

    /**
     * 获取定位精度值,单位:米
     */
    public static int getAccuracy() {
        return ACCURACY;
    }

    private static Application sContext;

    public static LocationUtil getInstance(@NonNull Activity context) {
        if (sContext == null) {
            sContext = context.getApplication();
        }

        if (mLocationManager == null && sContext != null) {
            mLocationManager = (LocationManager) sContext.getSystemService(Context.LOCATION_SERVICE);
        }
        return instance;
    }

    /**
     * 位置更新后回调
     */
    public void setLocationListener(LocationListener listener) {
        sLocationListener = listener;
    }

    /**
     * 获取上一次的定位信息
     */
    @SuppressLint("MissingPermission")
    @Nullable
    public Location getLastLocation() {
        if (sContext == null
                || PermissionChecker.checkSelfPermission(sContext, Manifest.permission.ACCESS_FINE_LOCATION) != PermissionChecker.PERMISSION_GRANTED) {
            Logger.d(TAG, "lack ACCESS_FINE_LOCATION permission !!!");
        } else {
            if (mLastLocation == null) {
                List<String> allProviders = mLocationManager.getProviders(true);
                if (allProviders != null && allProviders.size() > 0) {
                    try {
                        for (String provider : allProviders) {
                            mLastLocation = mLocationManager.getLastKnownLocation(provider);
                            Logger.d(TAG, "getLastLocation by " + provider + " ,result = " + mLastLocation);
                            if (mLastLocation != null) {
                                break;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Logger.d(TAG, "getLastLocation mLastLocation = " + mLastLocation);
            }
            updateLocation();
        }
        return mLastLocation;
    }

    /**
     * 检查并申请定位权限,然后更新位置信息
     */
    @SuppressLint("MissingPermission")
    private void updateLocation() {
        if (sContext == null
                || PermissionChecker.checkSelfPermission(sContext, Manifest.permission.ACCESS_FINE_LOCATION) != PermissionChecker.PERMISSION_GRANTED) {
            Logger.e(TAG, "lack ACCESS_FINE_LOCATION permission !!!");
        } else {
            List<String> allProviders = mLocationManager.getProviders(true);
            if (allProviders == null || allProviders.size() <= 0) {
                return;
            }
            for (String provider : allProviders) {
                synchronized (LocationUtil.class) {
                    long currentTs = System.currentTimeMillis();
                    long timeDiff = currentTs - lastUpdateTime;
                    if (timeDiff >= MIN_TIME) {
                        Logger.d(TAG, "updateLocation by " + provider);
                        // 单次定位
                        mLocationManager.requestSingleUpdate(provider, locationListener, Looper.getMainLooper());
                        lastUpdateTime = currentTs;
                    }
                }
//                    Boolean aBoolean = hasSetLocationListener.get(provider);
//                    if (aBoolean == null || !aBoolean) {
//                        Logger.d(TAG, "updateLocation by " + provider);
//                        // 持续定位
                ////                        mLocationManager.requestLocationUpdates(provider, MIN_TIME, ACCURACY, locationListener);
//                        hasSetLocationListener.put(provider, true);
//                    }
            }
        }
    }

    /**
     * 检查是否开启gps provider,若无则跳转到设置页面
     * 这里不要去检查 LocationManger 的所有可用provider,不然就算 network_provider 或者  passive_provider 有启用,也获取不到经纬度
     */
    public boolean checkAndOpenGpsProvider() {
        boolean isGpsOpen = isProviderEnable(LocationManager.GPS_PROVIDER);
        if (!isGpsOpen) {
            Logger.d(TAG, "gps定位未开启,需先开启,否则可能获取不到经纬度");
            // 转到手机设置界面，用户设置GPS
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            sContext.startActivity(intent);
        }
        return isGpsOpen;
    }


    @SuppressLint("MissingPermission")
    public void stopLocation() {
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(locationListener);
        }

        if (mLastLocation == null) {
            lastUpdateTime = 0;
        }
    }

    /**
     * 检查指定的 provider 定位功能是否开启,默认为 gps
     * 若传入多个 provider 字符串(String... provider),只要一个有开启就返回true
     */
    private boolean isProviderEnable(String... providers) {
        boolean enableStatus = false;
        for (String provider : providers) {
            enableStatus = mLocationManager.isProviderEnabled(provider);
            if (enableStatus) return true;
        }
        return enableStatus;
    }

    /**
     * 定位监听
     */
    private LocationListener locationListener = new LocationListener() {

        /**
         * 位置发生改变后调用
         */
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                mLastLocation = location;
//                try {
//                    Geocoder geocoder = new Geocoder(sContext, Locale.CHINESE);
//                    Address address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                if (sLocationListener != null) {
                    sLocationListener.onLocationChanged(location);
                }
            }
            Logger.d(TAG, "onLocationChanged  mLastLocation = " + mLastLocation);
        }

        /**
         * 状态改变回调
         *
         * @param provider：定位器名称（NetWork,Gps等）
         * @param status:                      3中状态，超出服务范围，临时不可用，正常可用
         * @param extras:                      包含定位器一些细节信息
         */
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        /**
         * 定位开启后调用
         */
        @Override
        public void onProviderEnabled(String provider) {

        }

        /**
         * 定位关闭后调用
         */
        @Override
        public void onProviderDisabled(String provider) {

        }
    };
}
