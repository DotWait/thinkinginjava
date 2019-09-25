package com.dotwait.util;

import okhttp3.*;
import org.junit.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpUtil {
    private static OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    public static final String GET = "get";
    public static final String POST = "post";
    private static List<Header> headers = new ArrayList<>();

    public String functionTest(String url, String requestMethod, Header header, String body){
        Assert.assertTrue("url and request method can not be null",
                url != null && requestMethod != null );
        if (POST.equals(requestMethod)){
            return post(url, header, body);
        }else if (GET.equals(requestMethod)){
            return get(url, header);
        }
        return "request method is not right";
    }

    public String functionTest(String url, String requestMethod, List<Header> headers, String body){
        return "";
    }

    private String post(String url, Header header, String body){
        RequestBody requestBody = RequestBody.create(JSON, body);
        Request request = new Request.Builder()
                .url(url)
                .header(header.getKey(), header.getValue())
                .post(requestBody)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            System.out.println("request is error, url:"+url);
            return "{}";
        }
    }

    private String get(String url, Header header){
        Request request = new Request.Builder()
                .url(url)
                .header(header.getKey(), header.getValue())
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }catch (IOException e){
            System.out.println("request is error, url:"+url);
            return "{}";
        }
    }
}
