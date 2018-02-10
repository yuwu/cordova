package com.cordova.plugin;

import android.app.Activity;

import com.cordova.plugin.donwload.DownloadTask;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONException;

/**
 * Created by wuyu on 18/2/8.
 */

public class Download extends CordovaPlugin {
    private Activity mActivity;
    private CallbackContext mCallback;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        this.mActivity = cordova.getActivity();
    }

    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
        this.mCallback = callbackContext;
        try {
            String url = args.getString(0);
            String dir = args.getString(1);
            String name = args.getString(2);

            switch (action){
                case "download":{
                    download(url, dir, name);
                    break;
                }
                default:
                    break;
            }
            return true;
        }catch (Exception ex){
            callbackContext.error(ex.getMessage());
        }
        return false;
    }

    private void download(final String url, final String dir, final String name){
        DownloadTask task = DownloadTask.newInstance();
        task.setOnDownloadListener(new DownloadTask.OnDownloadListener() {
            @Override
            public void onDownloadSuccess() {
                PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, 100);
                pluginResult.setKeepCallback(true);
                mCallback.sendPluginResult(pluginResult);
            }

            @Override
            public void onDownloading(int progress) {
                PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, progress);
                pluginResult.setKeepCallback(true);
                mCallback.sendPluginResult(pluginResult);
            }

            @Override
            public void onDownloadFailed(String message) {
                PluginResult pluginResult = new PluginResult(PluginResult.Status.ERROR, message);
                pluginResult.setKeepCallback(true);
                mCallback.sendPluginResult(pluginResult);
            }
        });
        task.download(url, dir, name);
    }
}
