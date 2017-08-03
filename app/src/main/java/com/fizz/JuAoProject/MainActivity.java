package com.fizz.JuAoProject;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.fizz.JuAoProject.widget.FullScreenVideoView;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HandlerWhat.SUCESS_WHAT:
                    /*Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
//                    if (getIntent().getBundleExtra(Constants.EXTRA_BUNDLE) != null) {
//                        intent.putExtra(Constants.EXTRA_BUNDLE,
//                                getIntent().getBundleExtra(Constants.EXTRA_BUNDLE));
//                    }
                    MainActivity.this.startActivity(intent);*/
                    MainActivity.this.finish();

                    break;

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        App.getInstance().addActivity(this);

        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        FFmpegNativeHelper.runCommand("ffmpeg -i input.mp4 output.gif");

        initView();

    }

    private void initView() {

        String videoPath = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.frist).toString();
        FullScreenVideoView videoView = (FullScreenVideoView) findViewById(R.id.videoView);
        assert videoView != null;
        videoView.setVideoPath(videoPath);
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
//                mp.setLooping(true);
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                goMain();
            }
        });
    }

    private void goMain() {

        /*new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = mHandler.obtainMessage();
                if (msg != null) {
                    msg = new Message();
                }
                msg.what = HandlerWhat.SUCESS_WHAT;

                mHandler.sendMessage(msg);
            }
        },3000);*/

        Message msg = mHandler.obtainMessage();
        if (msg == null) {
            msg = new Message();
        }
        msg.what = HandlerWhat.SUCESS_WHAT;

        mHandler.sendMessage(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        App.getInstance().removeActivity(this);
    }
}
