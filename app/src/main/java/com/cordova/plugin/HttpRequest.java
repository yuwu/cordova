package com.cordova.plugin;

import android.app.Activity;
import android.os.Environment;
import android.util.ArrayMap;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by wuyu on 18/2/8.
 */

public class HttpRequest extends CordovaPlugin {
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
            Map<String, String> maps = new HashMap<>();
            if(args.isNull(1)){
                maps = jsonObject2Map(args.getJSONObject(1));
            }

            switch (action){
                case "post":{
                    psot(url, maps);
                    break;
                }
                case "get": {
                    psot(url, maps);
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

    private Map<String, String> jsonObject2Map(JSONObject jsonObject){
        Map<String, String> maps = new HashMap<>();
        try {
            JSONArray keys = jsonObject.names();
            for(int i=0; i<keys.length(); i++){
                String name = keys.getString(i);
                maps.put(name, jsonObject.getString(name));
            }
        }catch (Exception ex){
            //
            ex.printStackTrace();
        }
        return  maps;
    }

    private OkHttpClient getDefaultOkHttpClient(){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        return client;
    }

    private void get(String url, Map<String, String> params){
        OkHttpClient client = getDefaultOkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        Set<String> keys = params.keySet();
        for(String name : keys){
            builder.add(name, params.get(name));
        }
        RequestBody requestBodyPost = builder.build();
        Request requestPost = new Request.Builder()
                .url(url)
                .post(requestBodyPost)
                .build();
        client.newCall(requestPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mCallback.error(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                mCallback.success(result);
            }
        });
    }

    private void psot(String url, Map<String, String> params){
        OkHttpClient client = getDefaultOkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        Set<String> keys = params.keySet();
        for(String name : keys){
            builder.add(name, params.get(name));
        }
        RequestBody requestBodyPost = builder.build();
        Request requestPost = new Request.Builder()
                .url(url)
                .post(requestBodyPost)
                .build();
        client.newCall(requestPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mCallback.error(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                mCallback.success(result);
            }
        });
    }
}