package com.fizz.JuAoProject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.fizz.JuAoProject.bean.Information;
import com.fizz.JuAoProject.bean.PushBean;
import com.fizz.JuAoProject.widget.CustomWebViewClient;
import com.google.gson.Gson;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Map;
import java.util.TimerTask;

public class WebViewActivity extends AppCompatActivity {

    private String IndexUrl = "http://www.flag-usb.com";
    //    private String IndexUrl = "http://115.159.68.211:88"; //新闻链接
//    private String IndexUrl = "http://192.168.1.124:88"; //新闻链接
//    private String IndexUrl = "http://192.168.1.8"; //测试链接
    private String IndexPath = IndexUrl + "/site/mew/news"; //新闻链接
    private String ActivePath = IndexUrl + "/site/mew/huodong_list";//活动链接
    private String JuPath = IndexUrl + "/site/mew/goods_list";//聚划算链接
    private String LifeShowPath = IndexUrl + "/site/mew/toupiao_index";//生活秀链接
    private String InfoPath = IndexUrl + "/site/mew/user_center";  //个人中心链接

    private ArrayList<SnsPlatform> platforms = new ArrayList<>();
    private SHARE_MEDIA[] list = {SHARE_MEDIA.SINA, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ, SHARE_MEDIA.SMS};
    private SHARE_MEDIA share_media;

    private Information information;
    public final static int FILECHOOSER_RESULTCODE = 1;
    private final static int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 2;

    private WebView mWebView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        XGPushConfig.enableDebug(this, true);
        setContentView(R.layout.activity_web_view);
        String token = XGPushConfig.getToken(this);
        getToken();
        Log.d("token", "onCreate: " + token);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        toMain();
        initState();

        initView();
        initPlatforms();
        initRadioButton();

    }

    private void initState() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        }
    }

    //    可以直接在activity中获取，下面这个方法是写在activity中的
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        XGPushClickedResult click = XGPushManager.onActivityStarted(this);

        Log.d("TPush", "onResumeXGPushClickedResult:" + click);
        if (click != null) { // 判断是否来自信鸽的打开方式
//            Toast.makeText(this, "通知被点击:" + click.toString(),
//                    Toast.LENGTH_SHORT).show();
            String content = click.getContent();
            String customContent = click.getCustomContent();
            if (customContent != null) {
                PushBean pushBean = new Gson().fromJson(customContent, PushBean.class);
                if (pushBean != null && pushBean.getUrl() != null) {
                    mWebView.loadUrl(pushBean.getUrl());
                }
            }
            Log.d("json", "onResume: " + customContent);

            Log.d("content", "onResume: " + content);
        }


        Intent intent = getIntent();

        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            if (uri != null) {
//                String host = uri.getHost();
//                String dataString = intent.getDataString();
//                String id = uri.getQueryParameter("id");
                String url = uri.getQueryParameter("url");
//                String path = uri.getPath();
//                String path1 = uri.getEncodedPath();
//                String queryString = uri.getQuery();
//                System.out.println("host:" + host);
//                System.out.println("dataString:" + dataString);
//                System.out.println("id:" + id);
//                System.out.println("path:" + path);
//                System.out.println("path1:" + path1);
//                System.out.println("url:" + url);
//                System.out.println("queryString:" + queryString);
//                Toast.makeText(this, "浏览器 打开", Toast.LENGTH_SHORT).show();
                if (mWebView != null && url != null) {

                    mWebView.loadUrl(url);
                }
                intent.setData(null);
            }
        }


    }

    public void toMain() {
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }

    private void initPlatforms() {
        platforms.clear();
        for (SHARE_MEDIA e : list) {
            if (!e.toString().equals(SHARE_MEDIA.GENERIC.toString())) {
                platforms.add(e.toSnsPlatform());
            }
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        refresh();
//    }

    private void initRadioButton() {

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.main_radio);

        assert radioGroup != null;
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.main_button1:
                        mWebView.loadUrl(IndexPath);
                        break;
                    case R.id.main_button2:
                        mWebView.loadUrl(ActivePath);
                        break;
                    case R.id.main_button3:
                        mWebView.loadUrl(JuPath);
                        break;
                    case R.id.main_button4:
                        mWebView.loadUrl(LifeShowPath);
                        break;
                    case R.id.main_button5:
                        mWebView.loadUrl(InfoPath);
                        break;

                    default:
                        mWebView.loadUrl(IndexPath);
                        break;
                }
            }
        });
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void initView() {


        share_media = (SHARE_MEDIA) getIntent().getSerializableExtra("platform");

        mWebView = (WebView) findViewById(R.id.webview);

        //设置是否支持JavaScript
        assert mWebView != null;
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        String ua = mWebView.getSettings().getUserAgentString();
//        mWebView.getSettings().setUserAgentString(ua.replace("Android", "JuAoAPP"));
        mWebView.getSettings().setUserAgentString("JuAoAPP " + ua);
        System.out.println("initView: " + mWebView.getSettings().getUserAgentString());

        mWebView.addJavascriptInterface(new ShareInfo(this), "Toyun");

        mWebView.loadUrl(IndexPath);

        mWebView.getSettings().setAllowFileAccess(true);

        mWebView.setWebChromeClient(new WebChromeClient() {


            ///扩展浏览器上传文件
            //3.0++版本
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                openFileChooserImpl(uploadMsg);
            }

            //3.0--版本
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                openFileChooserImpl(uploadMsg);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                openFileChooserImpl(uploadMsg);
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                onenFileChooseImpleForAndroid(filePathCallback);
                return true;
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     final JsResult result) {
                Log.i("aaa", "onJsConfirm" + "," + "url: " + url);
                AlertDialog.Builder builder = new AlertDialog.Builder(WebViewActivity.this);
                builder.setMessage(message)
                        .setNeutralButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                arg0.dismiss();
//                                finish();
                            }
                        }).show();
                result.cancel();
                return true;
            }


        });
        mWebView.setWebViewClient(new CustomWebViewClient(WebViewActivity.this));


    }

    public class ShareInfo {

        private Context mContext;

        public ShareInfo(Context context) {
            mContext = context;
        }

        @JavascriptInterface
        public void share(String info) {

            Log.i("flag", "share: " + info);

            information = new Gson().fromJson(info, Information.class);

            String url = information.getImg();
            Log.d("flag", "share: " + url);

            ShareWeb(url);

        }

        @JavascriptInterface
        public void WeiXin() {

            WeixinLogin();

            Log.d("flag", "WeiXin: ");


        }

    }

    private void WeixinLogin() {


        UMShareAPI mShareAPI = UMShareAPI.get(WebViewActivity.this);
        mShareAPI.getPlatformInfo(WebViewActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, final Map<String, String> data) {
            Toast.makeText(getApplicationContext(), "Authorize succeed", Toast.LENGTH_SHORT).show();

            runOnUiThread(new TimerTask() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript:WXLogin('" + data.get("screen_name") + "','" + data.get("profile_image_url") + "','" + data.get("unionid") + "')");
                    Log.d("flag", "onComplete: " + data.get("unionid") + "aoao:" + data.get("screen_name") + "icon:" + data.get("profile_image_url"));
                }
            });


        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(getApplicationContext(), "Authorize fail", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(getApplicationContext(), "Authorize cancel", Toast.LENGTH_SHORT).show();
        }
    };

    private void ShareWeb(String thumb_img) {
        UMImage thumb = new UMImage((WebViewActivity.this), thumb_img);

        UMWeb web = new UMWeb(information.getShareUrl());
        web.setThumb(thumb);
        String title = information.getTitle();
        String content = information.getContent();
        Log.d("flag", "ShareWeb: " + title);
        if (content != null && !content.equals("")) {

            web.setDescription(content);
        } else {
            web.setDescription(title);
        }
        web.setTitle(title);
        new ShareAction((WebViewActivity.this))
                .withText(title)
                .withMedia(web)
                .setPlatform((share_media))
                .setCallback((umShareListener)).open();
    }


    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);

//            Toast.makeText(WebViewActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
//            Toast.makeText(WebViewActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());

//                Toast.makeText(WebViewActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
//            Toast.makeText(WebViewActivity.this,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    public ValueCallback<Uri> mUploadMessage;

    private void openFileChooserImpl(ValueCallback<Uri> uploadMsg) {
        mUploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
    }

    public ValueCallback<Uri[]> mUploadMessageForAndroid5;

    private void onenFileChooseImpleForAndroid(ValueCallback<Uri[]> filePathCallback) {
        mUploadMessageForAndroid5 = filePathCallback;
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("image/*");

        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");

        startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;

        } else if (requestCode == FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
            if (null == mUploadMessageForAndroid5)
                return;
            Uri result = (intent == null || resultCode != RESULT_OK) ? null : intent.getData();
            if (result != null) {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
            } else {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
            }
            mUploadMessageForAndroid5 = null;
        }
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mWebView.canGoBack() && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            //获取历史列表
            WebBackForwardList mWebBackForwardList = mWebView
                    .copyBackForwardList();
            //判断当前历史列表是否最顶端,其实canGoBack已经判断过
            if (mWebBackForwardList.getCurrentIndex() > 0) {
                mWebView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    Message m = null;

    /**
     * 获取设备Token
     */
    private void getToken() {

        Handler handler = new HandlerExtension(WebViewActivity.this);
        m = handler.obtainMessage();
        // 注册接口
        XGPushManager.registerPush(getApplicationContext(), "juao",
                new XGIOperateCallback() {
                    @Override
                    public void onSuccess(Object data, int flag) {
                        Log.i("info", "+++ register push sucess. token:" + data);
                        m.obj = "+++ register push sucess. token:" + data;
                        m.sendToTarget();
                    }

                    @Override
                    public void onFail(Object data, int errCode, String msg) {
                        Log.i("info", "+++ register push fail. token:" + data
                                + ", errCode:" + errCode + ",msg:" + msg);

                        m.obj = "+++ register push fail. token:" + data
                                + ", errCode:" + errCode + ",msg:" + msg;
                        m.sendToTarget();
                    }
                });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        XGPushManager.onActivityStoped(this);
    }

    private static class HandlerExtension extends Handler {
        WeakReference<WebViewActivity> mActivity;

        HandlerExtension(WebViewActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            WebViewActivity theActivity = mActivity.get();
            if (theActivity == null) {
                theActivity = new WebViewActivity();
            }
            if (msg != null) {
                Log.i("info", "msg--->" + msg.obj.toString());
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

