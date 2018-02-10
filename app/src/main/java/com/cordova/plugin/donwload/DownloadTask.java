package com.cordova.plugin.donwload;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wuyu on 18/2/8.
 */

public class DownloadTask {
    private OkHttpClient okHttpClient;
    private OnDownloadListener listener;

    public static DownloadTask newInstance(){
        return new DownloadTask();
    }

    private DownloadTask() {
        okHttpClient = new OkHttpClient();
    }

    public DownloadTask setOnDownloadListener(OnDownloadListener listener){
        this.listener = listener;
        return this;
    }

    /**
     * @param url 下载连接
     */
    public void download(final String url, final String dir, final String name) {
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                if(listener != null){
                    listener.onDownloadFailed(e.getMessage());
                }
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                String savePath = isExistDir(dir);
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    String filename = TextUtils.isEmpty(name) ? getNameFromUrl(url) : name;
                    File file = new File(savePath, filename);
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
                        if(listener != null){
                            listener.onDownloading(progress);
                        }
                    }
                    fos.flush();
                    // 下载完成
                    if(listener != null){
                        listener.onDownloadSuccess();
                    }
                } catch (Exception e) {
                    if(listener != null){
                        listener.onDownloadFailed(e.getMessage());
                    }
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    /**
     * @param dir
     * @return
     * @throws IOException
     * 判断下载目录是否存在
     */
    private String isExistDir(String dir) throws IOException {
        // 下载位置
        File downloadDir= new File(Environment.getExternalStorageDirectory(), dir);
        downloadDir.mkdirs();
        String savePath = downloadDir.getAbsolutePath();
        return savePath;
    }

    /**
     * @param url
     * @return
     * 从下载连接中解析出文件名
     */
    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess();

        /**
         * @param progress
         * 下载进度
         */
        void onDownloading(int progress);

        /**
         * 下载失败
         */
        void onDownloadFailed(String message);
    }
}
