package com.dotwait.http;

import okhttp3.*;
import org.junit.Test;

import java.io.IOException;
import java.util.Random;

public class OkHttpTests {
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    @Test
    public void okHttpTest() throws IOException {
        String url = "https://lvzhou-at.h3c.com:31443/v3/user-group/createUserGroup";
        String json = "{\"groupCreator\":\"yang09174\",\"groupName\":\"1234\",\"groupRule\":1,\"groupType\":1,\"storeId\":657760,\"storeName\":\"msgceshi\",\"tags\":[\"5d01b8943406920005e1b372\"]}";
        String post = post(url, json);
        System.out.println(post);
    }



    OkHttpClient client = new OkHttpClient();

    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .header("Cookie", "_ga=GA1.2.1638345067.1548377866; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%2216882833d1e166-0a43af9e3d96c1-3a3a5d0c-1296000-16882833d1f499%22%2C%22%24device_id%22%3A%2216882833d1e166-0a43af9e3d96c1-3a3a5d0c-1296000-16882833d1f499%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E8%87%AA%E7%84%B6%E6%90%9C%E7%B4%A2%E6%B5%81%E9%87%8F%22%2C%22%24latest_referrer%22%3A%22https%3A%2F%2Fwww.google.com%2F%22%2C%22%24latest_referrer_host%22%3A%22www.google.com%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC%22%7D%7D; Hm_lvt_df7237ab1ce22c31bbe68ebd1817c1c4=1561428000; _dvt_uid=5d118028.130490a1; connect.sid=s%3Aqrg3Ha9Z4L3Mq_uHs0vkIVCZI-hV_S9d.BDUa%2Bp8Asr2CKFgYQPFtlom1UBhORb%2BmQ7ocFun0YdM; lang=cn")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    @Test
    public void getOkHttpTest() throws IOException {
        String url = "https://lvzhou-at.h3c.com:31443/v3/auth-ppsk/listUser?storeId=354556&size=10&page=1&_=1565320252271";
        String s = get(url);
        System.out.println(s);
    }

    String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .header("Cookie","_ga=GA1.2.1638345067.1548377866; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%2216882833d1e166-0a43af9e3d96c1-3a3a5d0c-1296000-16882833d1f499%22%2C%22%24device_id%22%3A%2216882833d1e166-0a43af9e3d96c1-3a3a5d0c-1296000-16882833d1f499%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E8%87%AA%E7%84%B6%E6%90%9C%E7%B4%A2%E6%B5%81%E9%87%8F%22%2C%22%24latest_referrer%22%3A%22https%3A%2F%2Fwww.google.com%2F%22%2C%22%24latest_referrer_host%22%3A%22www.google.com%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC%22%7D%7D; Hm_lvt_df7237ab1ce22c31bbe68ebd1817c1c4=1561428000; _dvt_uid=5d118028.130490a1; connect.sid=s%3Aqrg3Ha9Z4L3Mq_uHs0vkIVCZI-hV_S9d.BDUa%2Bp8Asr2CKFgYQPFtlom1UBhORb%2BmQ7ocFun0YdM; lang=cn")
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    @Test
    public void baiduTest() throws IOException {
        String url = "https://www.baidu.com";
        String s = getBaidu(url);
        System.out.println(s);
    }

    String getBaidu(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    @Test
    public void sseTest() throws IOException {
        Random random = new Random();
        int callBack = random.nextInt(80000) + 10000;
        String url = "http://query.sse.com.cn/security/stock/getStockListData2.do?" +
                "&jsonCallBack=jsonpCallback"+callBack+"&isPagination=true&stockCode=" +
                "&csrcCode=&areaName=&stockType=1&pageHelp.cacheSize=1&pageHelp.beginPage=1&pageHelp.pageSize=25&pageHelp.pageNo=1&_="+System.currentTimeMillis();
        String s = getSse(url);
        System.out.println(s);
    }

    String getSse(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    @Test
    public void timeTest(){
        System.out.println(System.currentTimeMillis());
    }
}
