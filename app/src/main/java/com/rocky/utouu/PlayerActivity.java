package com.rocky.utouu;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class PlayerActivity extends AppCompatActivity implements
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnVideoSizeChangedListener, SurfaceHolder.Callback, View.OnClickListener {

    private int mVideoWidth;
    private int mVideoHeight;
    private MediaPlayer mMediaPlayer;
    private SurfaceHolder holder;
    private String path;
    private boolean mIsVideoSizeKnown = false;
    private boolean mIsVideoReadyToBePlayed = false;
    private SurfaceView surfaceView;
    private TextView play_back;
    private SeekBar seekBar;
    private ImageView iv_playorpause;
    private int duration;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            path = bundle.getString("video_path");
            Log.i("tag", "----->播放path" + path);
        } else {
            Log.i("tag", "------>默认" + path);
            finish();
        }
        surfaceView = ((SurfaceView) findViewById(R.id.vedio_surfaceview));
        play_back = ((TextView) findViewById(R.id.play_back));
        seekBar = ((SeekBar) findViewById(R.id.play_seekbar));
        iv_playorpause = ((ImageView) findViewById(R.id.play_iv_playorpause));
        play_back.setOnClickListener(this);
        iv_playorpause.setOnClickListener(this);
        holder = surfaceView.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        seekBar.setOnSeekBarChangeListener(change);

    }

    //进度条改变的监听
    private SeekBar.OnSeekBarChangeListener change = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mMediaPlayer.seekTo(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int progress = seekBar.getProgress();
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                mMediaPlayer.seekTo(progress);
            }
        }
    };

    @Override
    protected void onResume() {
        //设置为横屏
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            startVideoPlayback();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaPlayer();
        doCleanUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
        doCleanUp();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        Log.v("tag", " == ---ConfigurationChanged");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i("tag", "-------->surfareCreated开始播放");
        playVideo();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i("tag", "------->surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i("tag", "------->surfaceDestroyed");
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Log.i("tag", "----->缓冲" + percent + "%");
        if (percent == 100) {
            Toast.makeText(this, "缓存完毕", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.i("tag", "----->oncompletion");
        Toast.makeText(this, "播放完毕", Toast.LENGTH_SHORT).show();
        play_back.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.i("tag", "----->onprepared");
        mIsVideoReadyToBePlayed = true;
        if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
            startVideoPlayback();
        }
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        Log.i("tag", "----->onVedioSizeChanged");
        if (width == 0 || height == 0) {
            Log.e("tag", "invalid video width(" + width + ") or height(" + height + ")");
            return;
        }
        mIsVideoSizeKnown = true;
        mVideoWidth = width;
        mVideoHeight = height;
        if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
            startVideoPlayback();
        }
    }

    private void playVideo() {
        doCleanUp();
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.setDisplay(holder);
            mMediaPlayer.prepare();
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnVideoSizeChangedListener(this);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    isPlaying = false;
                    return false;
                }
            });

        } catch (Exception e) {
            Log.i("tag", "error: " + e.getMessage(), e);
        }
    }

    private void doCleanUp() {
        mVideoWidth = 0;
        mVideoHeight = 0;
        mIsVideoReadyToBePlayed = false;
        mIsVideoSizeKnown = false;
    }

    private void startVideoPlayback() {
        Log.v("tag", "-------->startVideoPlayback开始播放");
        holder.setFixedSize(mVideoWidth, mVideoHeight);
        mMediaPlayer.start();
        iv_playorpause.setSelected(true);
        mMediaPlayer.seekTo(0);
        duration = mMediaPlayer.getDuration();
        seekBar.setMax(duration);
        new Thread() {
            @Override
            public void run() {
                try {
                    isPlaying = true;
                    while (isPlaying) {
                        seekBar.setProgress(mMediaPlayer.getCurrentPosition());
                        sleep(500);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void onClick(View v) {
        int itemid = v.getId();
        switch (itemid) {
            case R.id.play_back:
                finish();
                break;
            case R.id.play_iv_playorpause:
                if (isPlaying) {
                    iv_playorpause.setSelected(false);
                    mMediaPlayer.pause();
                } else {
                    iv_playorpause.setSelected(true);
                    mMediaPlayer.start();
                }
                break;
        }
    }

    private void progressDialog()   {

    }
}
