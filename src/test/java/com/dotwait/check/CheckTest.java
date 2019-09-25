package com.dotwait.check;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dotwait.util.Header;
import com.dotwait.util.HttpUtil;
import jdk.nashorn.internal.parser.JSONParser;
import org.junit.Test;

import java.util.List;

public class CheckTest {
    @Test
    public void checkTest() throws Exception {
        Object father = CheckUtil.parseLimit(Father.class);
        System.out.println(father);

        List<Object> objects = CheckUtil.generateOneNullFieldObjects(Father.class, father);
        objects.forEach(System.out::println);
    }

    @Test
    public void checkNullFatherTest() throws Exception {
        Object father = CheckUtil.parseLimit(Father.class);
        System.out.println(father);
        List<Object> objects = CheckUtil.generateNullFieldObjects(Father.class, father);
        objects.forEach(System.out::println);
        System.out.println(objects.size());
    }

    @Test
    public void checkNullSonTest() throws Exception {
        Object son = CheckUtil.parseLimit(Son.class);
        System.out.println(son);
        List<Object> objects = CheckUtil.generateNullFieldObjects(Son.class, son);
        objects.forEach(System.out::println);
        System.out.println(objects.size());
    }

    @Test
    public void charTest() {
        for (int i = 0; i < 255; i++) {
            System.out.println(i + " ==> " + (char) i);
        }
    }

    @Test
    public void beanTest(){
        GetBandwidthLimitRuleList g1 = new GetBandwidthLimitRuleList();
        g1.setSn("sn");
        GetBandwidthLimitRuleList g2 = new GetBandwidthLimitRuleList();
        BeanUtil.copyProperties(g1, g2);
        System.out.println(g2);
    }

    @Test
    public void getBandwidthLimitRuleList() throws Exception {
        Header header = new Header();
        header.setKey("Cookie");
        header.setValue("_ga=GA1.2.1638345067.1548377866; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%2216882833d1e166-0a43af9e3d96c1-3a3a5d0c-1296000-16882833d1f499%22%2C%22%24device_id%22%3A%2216882833d1e166-0a43af9e3d96c1-3a3a5d0c-1296000-16882833d1f499%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E8%87%AA%E7%84%B6%E6%90%9C%E7%B4%A2%E6%B5%81%E9%87%8F%22%2C%22%24latest_referrer%22%3A%22https%3A%2F%2Fwww.google.com%2F%22%2C%22%24latest_referrer_host%22%3A%22www.google.com%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC%22%7D%7D; Hm_lvt_df7237ab1ce22c31bbe68ebd1817c1c4=1561428000; _dvt_uid=5d118028.130490a1; connect.sid=s%3AZSuaEgNHA8Aq6Cwbtlq4aOm7601DeAzI.IKRoHDnw0r1wQ6gIGBJ9sH0OLz4uWUqSlWEVzaSjZFU; lang=cn");
        HttpUtil httpUtil = new HttpUtil();
        String url = "https://oasisrd.h3c.com/v3/security-configuration/getBandwidthLimitRuleList";
        GetBandwidthLimitRuleList obj = (GetBandwidthLimitRuleList)CheckUtil.parseLimit(GetBandwidthLimitRuleList.class);
        String body = JSONUtil.toJsonStr(obj);
        System.out.println(body);
        String result = httpUtil.functionTest(url, HttpUtil.POST, header, body);
        System.out.println(result);
        JSONObject jsonObject = JSONUtil.parseObj(result);
        Object data = jsonObject.get("data");
        System.out.println(JSONUtil.toJsonStr(data));

        List<Object> objects = CheckUtil.generateNullFieldObjects(GetBandwidthLimitRuleList.class, obj);
        for (Object object : objects) {
            body = JSONUtil.toJsonStr(object);
            System.out.println(body);
            result = httpUtil.functionTest(url, HttpUtil.POST, header, body);
            System.out.println(result);
        }
    }
}
