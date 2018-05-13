package com.yingliguoji.fee.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author albert
 * @package com.xianduankeji.ktv.base.util
 * @email cn.lu.duke@gmail.com
 * @date January 10, 2018
 */

@Slf4j
public class HttpTool {

    /**
     * url 过滤
     * @param url
     * @return
     */
    public static String post(String url){
        return post(url, new HashMap<>(), "");
    }

    /**
     * map 参数 doPost
     * @param url
     * @param map
     * @return
     */
    public static String doPost(String url, Map<String, Object> map){
        return doPost(url, map, 4000, 4000);
    }

    /**
     *
     * @param baseurl
     * @param params
     * @param ua
     * @return
     */
    public static String post(String baseurl, Map<String, Object> params, String ua) {
        return post(baseurl, params, ua, 4000, 4000);
    }

    /**
     * do json action
     * @return
     */
    public static String jsonPost(String url, JSONObject jsonObject, LinkedMultiValueMap<String, Object> headers) {
        RestTemplate template = new RestTemplate();
        HttpEntity requestEntity = new HttpEntity(jsonObject, headers);
        ResponseEntity<byte[]> entity = template.exchange(url.toString(), HttpMethod.POST,
                requestEntity, byte[].class, new Object[0]);
        return entity.toString();
    }

    /**
     * 发起请求
     * @param baseurl
     * @param params
     * @param ua
     * @param connTimeout
     * @param dataTimeout
     * @return resposeBody
     */
    public static String post(String baseurl, Map<String, Object> params, String ua,
                              int connTimeout, int dataTimeout) {
        HttpClient client = new HttpClient(new SimpleHttpConnectionManager(true));
        //ua 不为空的时候设置ua
        if (!StringUtils.isEmpty(ua)) {
            client.getParams().setParameter("http.useragent", ua);
        }
        //设置timeout
        client.setConnectionTimeout(connTimeout);
        client.setTimeout(dataTimeout);
        PostMethod post = new PostMethod(baseurl);
        List pairs = null;

        String execCode;
        try {
            pairs = getNameValuePairs(params);
            NameValuePair[] requestParams = new NameValuePair[pairs.size()];
            pairs.toArray(requestParams);
            post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            post.setRequestHeader("Connection", "close");
            post.setRequestBody(requestParams);
            int code = client.executeMethod(post);
            if (code != 200) {
                execCode = null;
                return execCode;
            }
            execCode = post.getResponseBodyAsString();
        } catch (Exception e) {
            log.error("", e);
            return null;
        } finally {
            closeClient(client, post);
        }

        return execCode;
    }

    /**
     * doPost 获取response响应流
     * @param url
     * @param map
     * @param connTimeout
     * @param dataTimeout
     * @return
     */
    public static String doPost(String url, Map<String, Object> map, int connTimeout, int dataTimeout){

        HttpClient client = new HttpClient(new SimpleHttpConnectionManager(true));
        //设置timeout
        client.setConnectionTimeout(connTimeout);
        client.setTimeout(dataTimeout);
        PostMethod post = new PostMethod(url);
        List pairs = null;

        String execCode;
        try {
            pairs = getNameValuePairs(map);
            NameValuePair[] requestParams = new NameValuePair[pairs.size()];
            pairs.toArray(requestParams);
            post.addRequestHeader("Content-Type", "application/json;charset=utf-8");
            post.setRequestHeader("Connection", "close");
            post.setRequestBody(requestParams);
            int code = client.executeMethod(post);
            if (code != 200) {
                execCode = null;
                return post.getResponseBodyAsString();
            }
            execCode = post.getResponseBodyAsString();
        } catch (Exception e) {
            log.error("", e);
            return null;
        } finally {
            closeClient(client, post);
        }
        return execCode;
    }

    /**
     * 获取post 流
     * @param url
     * @param map
     * @param headers
     * @return
     */
    public static InputStream doPost(String url, Map<String, Object> map, LinkedMultiValueMap<String, String> headers){
        RestTemplate template = new RestTemplate();
        HttpEntity requestEntity = new HttpEntity(map, headers);
        ResponseEntity<byte[]> entity = template.exchange(url.toString(), HttpMethod.POST, requestEntity, byte[].class, new Object[0]);
        byte[] result = entity.getBody();
        InputStream stream = new ByteArrayInputStream(result);
        return stream;
    }

    /**
     * 关闭HTTP连接
     * @param client
     * @param post
     */
    private static void closeClient(HttpClient client, PostMethod post) {
        try {
            post.releaseConnection();
        } catch (Exception varClose) {
            log.error("", varClose);
        }
    }

    /**
     * 键值对list
     * @param paramsMap
     * @return
     * @throws UnsupportedEncodingException
     */
    private static List<NameValuePair> getNameValuePairs(Map<String, Object> paramsMap){
        List<NameValuePair> pairs = new ArrayList();
        Iterator i$ = paramsMap.keySet().iterator();

        while(i$.hasNext()) {
            String key = (String)i$.next();
            Object value = paramsMap.get(key);
            if (null != value) {
                NameValuePair nameValuePair = new NameValuePair();
                nameValuePair.setName(key);
                nameValuePair.setValue(value.toString());
                pairs.add(nameValuePair);
            }
        }
        return pairs;
    }

}
