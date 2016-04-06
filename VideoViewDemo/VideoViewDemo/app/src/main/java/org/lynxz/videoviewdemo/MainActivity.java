package org.lynxz.videoviewdemo;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends BaseActivity implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnInfoListener, View.OnTouchListener {

    private VideoView mVv;
    private View mRlVv;
    private GestureDetector mGestureDetector;
    private AudioManager mAudioManager;
    //    private int mStreamVolume;
    private static final String VIDEO_TAG = "video_sample";
    private int mPlayingPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
    }

    private void initData() {
        try {
            int screenMode = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
            int bright = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);

            if (screenMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            }

            //            Settings.System.putInt(getContentResolver(),Settings.System.SCREEN_BRIGHTNESS,)
            //            setScreenBrightness(0);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setVoiceVolume(float value) {
        int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int flag = value > 0 ? -1 : 1;
        currentVolume += flag * 0.1 * maxVolume;
        // 对currentVolume进行限制
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
    }

    /**
     * 设置当前屏幕亮度值 0--255，并使之生效
     */
    private void setScreenBrightness(float value) {
        Window mWindow = getWindow();
        WindowManager.LayoutParams mParams = mWindow.getAttributes();
        int flag = value > 0 ? -1 : 1;

        mParams.screenBrightness += flag * 20 / 255.0F;
        if (mParams.screenBrightness >= 1) {
            mParams.screenBrightness = 1;
        } else if (mParams.screenBrightness <= 0.1) {
            mParams.screenBrightness = 0.1f;
        }
        mWindow.setAttributes(mParams);

        // 保存设置的屏幕亮度值
        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, (int) value);
    }

    private void initView() {
        // mRlVv容器用于全屏旋转,使视频宽高比保持不变
        mRlVv = findViewById(R.id.rl_vv);
        mVv = (VideoView) findViewById(R.id.vv);

        //添加播放控制条并设置视频源
        mVv.setMediaController(new MediaController(this));
        Uri rawUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.shuai_dan_ge);
        //Uri onlineUri = Uri.parse("http://v2.mukewang.com/c4f8a4c9-e888-4793-8fd6-ba3c592de8f0/L.mp4?auth_key=1459388431-0-0-ad75792f577d6fc9e045425a706e892f");
        mVv.setVideoURI(rawUri);

        mVv.setOnPreparedListener(this);
        mVv.setOnErrorListener(this);
        mVv.setOnCompletionListener(this);

        findViewById(R.id.btn_play).setOnClickListener(View -> {
            mVv.start();
            //            mVv.requestFocus();
        });

        findViewById(R.id.btn_seek).setOnClickListener(view -> {
            int targetPos = mVv.getDuration() / 2;
            mVv.seekTo(targetPos);
            showToast("after seek currPos = " + mVv.getCurrentPosition() + " targetPos = " + targetPos);
        });

        findViewById(R.id.btn_change).setOnClickListener(View -> {
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        });


        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        //        mStreamVolume = mAudioManager.getStreamVolume(mAudioManager.STREAM_MUSIC);

        mGestureDetector = new GestureDetector(this, mGestureListener);
        mRlVv.setOnTouchListener(this);
    }


    private GestureDetector.OnGestureListener mGestureListener = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float v = e2.getY() - e1.getY();
            Log.d(VIDEO_TAG, "v = " + v);
            if (Math.abs(v) > 10) {
                //setScreenBrightness(v);
                setVoiceVolume(v);
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            return false;
        }
    };

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        showToast("播放时发生错误 " + what);
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //        mVv.start();
        Log.d(VIDEO_TAG, "onPrepared currPos = " + mVv.getCurrentPosition());
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        showToast("播放完毕");
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mVv == null) {
            return;
        }
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().getDecorView().invalidate();
            float height = DensityUtil.getWidthInPx(this);
            float width = DensityUtil.getHeightInPx(this);
            mRlVv.getLayoutParams().height = (int) width;
            mRlVv.getLayoutParams().width = (int) height;
        } else {
            final WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attrs);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            float width = DensityUtil.getWidthInPx(this);
            float height = DensityUtil.dip2px(this, 200.f);
            mRlVv.getLayoutParams().height = (int) height;
            mRlVv.getLayoutParams().width = (int) width;
        }
    }


    @Override
    protected void onResume() {
        if (mPlayingPos > 0) {
            mVv.start();
            mVv.seekTo(mPlayingPos);
            mPlayingPos = 0;
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        mPlayingPos = mVv.getCurrentPosition();
        mVv.pause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVv.stopPlayback();
        Window mWindow = getWindow();
        WindowManager.LayoutParams mParams = mWindow.getAttributes();
        mParams.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        mWindow.setAttributes(mParams);
    }

    @Override
    public void onBackPressed() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            finish();
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private boolean ifSdCardAccessable() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }
}
