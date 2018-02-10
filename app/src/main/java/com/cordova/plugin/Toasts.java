package com.cordova.plugin;

import android.app.Activity;
import android.widget.Toast;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONException;

/**
 * Created by wuyu on 18/2/8.
 */

public class Toasts extends CordovaPlugin {

    private Activity mActivity;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        this.mActivity = cordova.getActivity();
    }

    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
        try {
            String message = args.getString(0);
            switch (action){
                case "showShort":{
                    android.widget.Toast.makeText(mActivity, message, android.widget.Toast.LENGTH_SHORT).show();
                    callbackContext.success(0);
                    break;
                }
                default:
                    break;
            }
            callbackContext.error("not found " + message);
        }catch (Exception ex){
            callbackContext.error(ex.getMessage());
        }
        return false;
    }
}
