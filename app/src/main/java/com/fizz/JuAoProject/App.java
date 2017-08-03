package com.fizz.JuAoProject;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.common.QueuedWork;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fizz on 2017/6/6.
 */
public class App extends Application {

    private static App mApplication = null;

    public static App getInstance() {
        return mApplication;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        mApplication = this;
        //开启debug模式，方便定位错误，具体错误检查方式可以查看http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式
        Config.DEBUG = true;
        QueuedWork.isUseThreadPool = false;
        UMShareAPI.get(this);
    }

    {

        PlatformConfig.setWeixin("wxb0396c30bc46c834", "86d07da7697599697d9704b062373da4");
        PlatformConfig.setQQZone("1106137503", "ewRlq2p2oDFmmJUD");
        PlatformConfig.setSinaWeibo("3344629817", "17c181213f16619221c706c0af295b88", "http://sns.whalecloud.com");
//        PlatformConfig.setTwitter("3aIN7fuF685MuZ7jtXkQxalyi", "MK6FEYG63eWcpDFgRYw4w9puJhzDl0tyuqWjZ3M7XJuuG7mMbO");
    }
}