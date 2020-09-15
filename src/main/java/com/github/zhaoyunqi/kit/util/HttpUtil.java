package com.github.zhaoyunqi.kit.util;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.core.io.ClassPathResource;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;


//      ┏┛ ┻━━━━━┛ ┻┓
//      ┃　　　　　　 ┃
//      ┃　　　━　　　┃
//      ┃　┳┛　  ┗┳　┃
//      ┃　　　　　　 ┃
//      ┃　　　┻　　　┃
//      ┗━┓　　　┏━━━┛
//        ┃　　　┃   神兽保佑
//        ┃　　　┃   代码无BUG！
//        ┃　　　┗━━━━━━━━━━━┓
//        ┃　　　　　　　     ┣┓
//        ┃　　　　         ┏┛
//        ┗━┓ ┓ ┏━━━┳ ┓ ┏━┛
//          ┃ ┫ ┫   ┃ ┫ ┫
//          ┗━┻━┛   ┗━┻━┛

public class HttpUtil {


    public static String post(String json, String url) {

        //链式构建请求
        String res = HttpRequest.post(url)
                .header(Header.CONTENT_TYPE, "application/json;charset=utf-8")
                .form(json)
                .timeout(30000)//超时，毫秒
                .execute().body();
        System.out.println(res);

        return res;
    }


    public static String sendPfxPost(String url, String body, String header, String pfxName, String keyStorePwd) throws Exception {

        KeyStore clientStore = KeyStore.getInstance("PKCS12");
        ClassPathResource classPathResource = new ClassPathResource(pfxName);
        InputStream instream = classPathResource.getInputStream();
        try {
            clientStore.load(instream, keyStorePwd.toCharArray());
        } finally {
            instream.close();
        }

        //Trust everybody
        X509TrustManager tm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        SSLContext sslCtx = SSLContext.getInstance("TLS");
        KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmfactory.init(clientStore, keyStorePwd != null ? keyStorePwd.toCharArray() : null);
        KeyManager[] keymanagers = kmfactory.getKeyManagers();
        sslCtx.init(keymanagers, new TrustManager[]{tm}, null);
        SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(sslCtx, NoopHostnameVerifier.INSTANCE);
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create().
                register("https", sslConnectionFactory).register("http", new PlainConnectionSocketFactory()).build();
        PoolingHttpClientConnectionManager pcm = new PoolingHttpClientConnectionManager(registry);
        HttpClientBuilder hcb = HttpClientBuilder.create();
        hcb.setConnectionManager(pcm);
        CloseableHttpClient httpClient = hcb.build();
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(60000)
                .setConnectTimeout(60000).setSocketTimeout(60000).build();
        // 创建httpclient对象
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", header);
        httpPost.setConfig(requestConfig);
        // 2 直接是拼接好的key=value或者json字符串等
        httpPost.setEntity(new StringEntity(body, "utf-8"));
        // 执行请求操作，并拿到结果
        CloseableHttpResponse response = httpClient.execute(httpPost);
        // 获取响应头
        org.apache.http.Header[] rspHeaders = response.getAllHeaders();
        if (ArrayUtils.isNotEmpty(rspHeaders)) {
            Map<String, String> tmp = new HashMap<>();
            for (org.apache.http.Header headers : rspHeaders) {
                tmp.put(headers.getName(), headers.getValue());
            }
        }
        // 获取结果实体
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            /*
             * 按指定编码转换结果实体为String类型。 如果这行报错 connection
             * reset，那么有可能是链路不通或者post的url过长。
             */
        }
        String result = EntityUtils.toString(entity, "utf-8");
        // 关闭流
        EntityUtils.consume(entity);
        // 释放链接
        response.close();
        // 关闭客户端
        return result;
    }



}
