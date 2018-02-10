package com.cordova.plugin;

import android.app.Activity;
import android.os.Environment;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONException;

import java.io.File;

/**
 * Created by wuyu on 18/2/8.
 */

public class FileSystem extends CordovaPlugin {
    private Activity mActivity;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        this.mActivity = cordova.getActivity();
    }

    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
        try {
            String path = args.getString(0);
            File file = new File(Environment.getExternalStorageDirectory(), path);
            switch (action){
                case "exists":{
                    callbackContext.success(file.exists() ? 1 : 0);
                    break;
                }
                case "delete": {
                    callbackContext.success(file.delete() ? 1 : 0);
                    break;
                }
                case "mkdirs": {
                    callbackContext.success(file.mkdirs() ? 1 : 0);
                    break;
                }
                default:
                    callbackContext.error("not found " + action);
                    break;
            }
            return true;
        }catch (Exception ex){
            callbackContext.error(ex.getMessage());
        }
        return false;
    }
}