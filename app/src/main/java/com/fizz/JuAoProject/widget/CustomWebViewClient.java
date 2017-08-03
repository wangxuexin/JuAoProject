package com.fizz.JuAoProject.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fizz.JuAoProject.WebViewActivity;

/**
 * Created by fizz on 2016/8/24.
 */
public class CustomWebViewClient extends WebViewClient {
    private Activity mActivity;
    private ValueCallback<Uri> mUploadMessage;

    public CustomWebViewClient(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        if (parseScheme(url)) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            view.getContext().startActivity(intent);
            Log.d("flag", "shouldOverrideUrlLoading: " + "this");
        } else {
            view.loadUrl(url);
        }
        return true;
    }
    // For Android 3.0+
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        if (mUploadMessage != null) return;
        mUploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        mActivity.startActivityForResult(Intent.createChooser(i, "File Chooser"), WebViewActivity.FILECHOOSER_RESULTCODE);
    }

    // For Android < 3.0
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        openFileChooser(uploadMsg, "");
    }

    // For Android  > 4.1.1
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        openFileChooser(uploadMsg, acceptType);
    }

    public boolean parseScheme(String url) {
        if (url.contains("https://mapi.alipay.com")) {

            return true;
        } else {

            return false;
        }
    }
}
