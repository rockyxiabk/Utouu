package com.rocky.utouu.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by xiabaikui on 2015/12/5.
 */
public class HttpConnectionhelper {

    /**
     * 判断网络连接状态
     *
     * @param context
     * @return
     */
    public static boolean isNetWorkConntected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 请求数据
     *
     * @param path
     * @return
     */
    public static String doGetSumbit(String path) {
        InputStream is = null;
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.connect();
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                int len = 0;
                byte[] bytes = new byte[1024];
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((len = is.read(bytes)) != -1) {
                    baos.write(bytes, 0, len);
                }
                byte[] toByteArray = baos.toByteArray();
                return new String(toByteArray);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    //首先通过这个方法取得流
    public static InputStream getDataFromURL(Context context, String url) {
        URLConnection connection = null;
        try {
            connection = new URL(url).openConnection();
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(10000);
            return connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //然后开始缓冲数据
    public static String downloadMusic(Context context, String mediaUrl, String fileName) {
        InputStream stream = null;
        File file = null;
        int totalKbRead = 0;
        try {
            stream = getDataFromURL(context, mediaUrl);
            if (stream == null) {
                return "";
            }

            //创建音乐文件，如果已经有了，把之前的删掉
            file = new File(context.getCacheDir(), fileName);
            if (file.exists()) {
                file.delete();
            }
            //下面就是从网络获取数据写到文件中，下载到200Kb以后开始播放
            FileOutputStream out = new FileOutputStream(file);
            byte buf[] = new byte[10 * 1024];
            int totalBytesRead = 0;
            int numRead;
            while ((numRead = stream.read(buf)) != -1) {
                out.write(buf, 0, numRead);
                totalBytesRead += numRead;
                totalKbRead = totalBytesRead / 1024;
                //缓冲到200Kb以后开始播放，返回文件路径
                if (totalKbRead >= 200)
                    return file.getAbsoluteFile().toString();
            }
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}



