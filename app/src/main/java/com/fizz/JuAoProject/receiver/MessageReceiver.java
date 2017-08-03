package com.fizz.JuAoProject.receiver;

import android.content.Context;
import android.util.Log;

import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

/**
 * Created by xiexinwang on 2017/7/20.
 */

public class MessageReceiver extends XGPushBaseReceiver {

    @Override
    public void onRegisterResult(Context context, int i, XGPushRegisterResult xgPushRegisterResult) {

    }

    @Override
    public void onUnregisterResult(Context context, int i) {

    }

    @Override
    public void onSetTagResult(Context context, int i, String s) {

    }

    @Override
    public void onDeleteTagResult(Context context, int i, String s) {

    }


    /**
     * 消息透传
     *
     * @param context
     * @param xgPushTextMessage
     */
    @Override
    public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {
        if (xgPushTextMessage != null) {
            Log.e("info", "message:" + xgPushTextMessage.toString());
        }
    }

    /**
     * 通知点击回调
     *
     * @param context
     * @param xgPushClickedResult
     */

    //可以直接在activity中获取，下面这个方法是写在activity中的
    //    protected void onResume() {
    //        // TODO Auto-generated method stub
    //        super.onResume();
    //        XGPushClickedResult click = XGPushManager.onActivityStarted(this);
    //        Log.d("TPush", "onResumeXGPushClickedResult:" + click);
    //        if (click != null) { // 判断是否来自信鸽的打开方式
    //            Toast.makeText(this, "通知被点击:" + click.toString(),
    //                    Toast.LENGTH_SHORT).show();
    //        }
    //    }
    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {
        if (xgPushClickedResult != null) {
            Log.e("info", "click:" + xgPushClickedResult.toString());

        }
    }

    /**
     * 通知的内容
     *
     * @param context
     * @param xgPushShowedResult
     */
    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {
        if (xgPushShowedResult != null) {
            Log.e("info", "notifaction:" + xgPushShowedResult.toString());
        }
    }
}
