package com.example.administrator.filemanagementassistant.util;


import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MyHttpUtil {
    //访问网络的方法

    public static void  SendRequestWithOkHttp(String url, okhttp3.Callback  callback){

        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder()
                .url(url)
                .build();
        okHttpClient.newCall(request).enqueue(callback);


    }


}
