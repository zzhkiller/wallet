package com.coezal.wallet.biz.util;

import com.alibaba.fastjson.JSONObject;
import com.coezal.wallet.api.bean.TokenTransaction;
import com.coezal.wallet.api.excetion.BizException;
import com.coezal.wallet.api.vo.base.ETHScanBaseResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThirdApiInvoker {
    protected final Logger logger = LoggerFactory.getLogger("ThirdApiInvoker");
    private final RestTemplate restTemplate = initRestTemplate();
    private Gson gson = initGson();

    protected final <T> T doHttpGet(String url, Class<T> clazz, Map<String, Object> params) {
        HttpHeaders httpHeaders = makeHttpHeaders(null);
        HttpEntity<String> requestEntity = new HttpEntity<String>(httpHeaders);
        logger.info("request : urlTemplate={}, params={}", url, gson.toJson(params));
        return doHttpRequest(url, HttpMethod.GET, requestEntity, clazz, params);
    }


    protected final <T> T get(String url, Type typeOfT, Map<String, String> headers, Map<String, Object> params) {
        HttpHeaders httpHeaders = makeHttpHeaders(headers);
        HttpEntity<String> requestEntity = new HttpEntity<String>(httpHeaders);
        logger.info("request : urlTemplate={}, params={}, headers={}", url, gson.toJson(params), gson.toJson(headers));
        return doHttpRequestGetByType(url, HttpMethod.GET, requestEntity, typeOfT, params);
    }


    protected final <T> T doHttpRequestGetByType(String url, HttpMethod method, HttpEntity<String> requestEntity, Type typeOfT, Map<String, Object> params) {
        try {
            ResponseEntity<String> responseEntity;
            if (params == null) {
                responseEntity = restTemplate.exchange(url, method, requestEntity, String.class);
            } else {
                responseEntity = restTemplate.exchange(url, method, requestEntity, String.class, params);
            }
            logger.info("response : {}", responseEntity.getBody());
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                T t = gson.fromJson(responseEntity.getBody(), typeOfT);
                return t;
            } else {
                logger.error("response status {} : {}" ,responseEntity.getStatusCode().value(), responseEntity.getStatusCode().getReasonPhrase());
                return null;
            }
        } catch (Exception e) {
            logger.error("invoke third api error.", e);
            throw new BizException("服务器请求失败，请稍后重试！");
        }
    }


    protected final <T> T doHttpGet(String url, Class<T> clazz, Map<String, String> headers, Map<String, Object> params) {
        HttpHeaders httpHeaders = makeHttpHeaders(headers);
        HttpEntity requestEntity = new HttpEntity<String>(httpHeaders);
//        logger.info("request : urlTemplate={}, params={}, headers={}", url, gson.toJson(params), gson.toJson(headers));
        return doHttpRequest(url, HttpMethod.GET, requestEntity, clazz, params);
    }

    protected final <T> T doHttpPost(String url, Class<T> clazz, Object bodyObj) {
        HttpHeaders httpHeaders = makeHttpHeaders(null);
        String body = null;
        if(bodyObj!=null && bodyObj instanceof String ){
            body=(String)bodyObj;
        }else {
             body = gson.toJson(bodyObj);
        }
        HttpEntity<String> requestEntity = new HttpEntity<String>(body, httpHeaders);
//        logger.info("request : url={}, params={}",url,body);
        return doHttpRequest(url, HttpMethod.POST, requestEntity, clazz, null);
    }




    protected final <T> T post(String url, Type typeOfT, Map<String, String> headers, Object bodyObj) {
        HttpHeaders httpHeaders = makeHttpHeaders(headers);
        String body = gson.toJson(bodyObj);
        HttpEntity<String> requestEntity = new HttpEntity<String>(body, httpHeaders);
        logger.info("request : url={}, params={}, headers={}",url,body,gson.toJson(headers));
        return doHttpRequestPostByType(url, HttpMethod.POST, requestEntity, typeOfT, null);
    }


    protected final <T> T doHttpRequestPostByType(String url, HttpMethod method, HttpEntity<String> requestEntity, Type typeOfT, Map<String, Object> params) {
        try {
            ResponseEntity<String> responseEntity;
            if (params == null) {
                responseEntity = restTemplate.exchange(url, method, requestEntity, String.class);
            } else {
                responseEntity = restTemplate.exchange(url, method, requestEntity, String.class, params);
            }
            logger.info("response : {}", responseEntity.getBody());
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                T t = gson.fromJson(responseEntity.getBody(), typeOfT);
                return t;
            } else {
                logger.error("response status {} : {}" ,responseEntity.getStatusCode().value(), responseEntity.getStatusCode().getReasonPhrase());
                return null;
            }
        } catch (Exception e) {
            logger.error("invoke third api error.", e);
            throw new BizException("服务器请求失败，请稍后重试！");
        }
    }


    protected final <T> T doHttpPost(String url, Type typeOfT, Map<String, String> headers, Object bodyObj) {
        HttpHeaders httpHeaders = makeHttpHeaders(headers);
        String body = gson.toJson(bodyObj);
        HttpEntity<String> requestEntity = new HttpEntity<String>(body, httpHeaders);
        logger.info("request : url={}, params={}, headers={}",url,body,gson.toJson(headers));
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            logger.info("response : {}", responseEntity.getBody());
            HttpStatus httpStatus = responseEntity.getStatusCode();
            if (httpStatus == HttpStatus.OK) {
                T t = gson.fromJson(responseEntity.getBody(), typeOfT);
                return t;
            } else {
                logger.error("response status {} : {}" ,httpStatus.value(), httpStatus.getReasonPhrase());
                return null;
            }
        } catch (Exception e) {
            logger.error("invoke third api error.", e);
            throw new BizException(e.getMessage());
        }
    }


    protected final <T> T doHttpPost(String url, Class<T> clazz, Map<String, String> headers, String rasStr) {
        HttpHeaders httpHeaders = makeHttpHeaders(headers);
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> params= new LinkedMultiValueMap<String, String>();
//  也支持中文
        params.add("datastr", rasStr);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, httpHeaders);
        logger.info("request : url={}, params={}, headers={}",url,params,gson.toJson(headers));
        return doHttpRequest(url, HttpMethod.POST, requestEntity, clazz, null);
    }


    protected final ETHScanBaseResponse<List<TokenTransaction>> getETHScanBaseResponse(String url, HttpEntity<String> requestEntity, Class clazz, Map<String, Object> params) {
        try {
            ResponseEntity<String> responseEntity;
            HttpMethod method=HttpMethod.GET;
            if (params == null) {
                responseEntity = restTemplate.exchange(url, method, requestEntity, String.class);
            } else {
                responseEntity = restTemplate.exchange(url, method, requestEntity, String.class, params);
            }
            System.out.println("----response : {}"+responseEntity.getBody());
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                ETHScanBaseResponse<List<TokenTransaction>> t = (ETHScanBaseResponse<List<TokenTransaction>>) JSONObject.parseObject(responseEntity.getBody(), clazz);
                return t;
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new BizException(e.getMessage());
        }
    }

    protected final <T> T doHttpRequest(String url, HttpMethod method, HttpEntity requestEntity, Class<T> clazz, Map<String, Object> params) {
        try {
            ResponseEntity<String> responseEntity;
            if (params == null) {
                responseEntity = restTemplate.exchange(url, method, requestEntity, String.class);
            } else {
                responseEntity = restTemplate.exchange(url, method, requestEntity, String.class, params);
            }
            System.out.println("----response : {}"+responseEntity.getBody());
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                if(clazz.equals(String.class)){
                    return (T)responseEntity.getBody();
                }
                T t = JSONObject.parseObject(responseEntity.getBody(), clazz);
                return t;
            } else {
//                logger.error("response status {} : {}" ,responseEntity.getStatusCode().value(), responseEntity.getStatusCode().getReasonPhrase());
                return null;
            }
        } catch (Exception e) {
//            logger.error("invoke third api error.", e);
            System.out.println(e.getMessage());
            throw new BizException(e.getMessage());
        }
    }




    private HttpHeaders makeHttpHeaders(Map<String, String> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
//        MediaType type = MediaType.parseMediaType(MediaType.APPLICATION_JSON + ";charset=UTF-8");
//        httpHeaders.setContentType(type);
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpHeaders.set(entry.getKey(), entry.getValue());
            }
        }
        return httpHeaders;
    }

    private Gson initGson() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson;
    }

    private static RestTemplate initRestTemplate() {

        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectTimeout(15000);
        httpRequestFactory.setReadTimeout(15000);
        RestTemplate rest = new RestTemplate(httpRequestFactory);
        List<HttpMessageConverter<?>> messageConverters = rest.getMessageConverters();
        for (int i = 0; i < messageConverters.size(); i++) {
            if (messageConverters.get(i) instanceof StringHttpMessageConverter) {
                messageConverters.set(i, new StringHttpMessageConverter(Charset.forName("utf-8")));
            }
        }
        return rest;
    }
}
