package com.house.backend.houseservice.utils;


import com.alibaba.fastjson.JSONObject;
import com.house.backend.houseservice.enums.Status;
import com.house.backend.houseservice.exception.HouseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by liuyaowen on 2017/5/11.
 */
@Slf4j
@Component
public class HttpUtils {
    private static Boolean proxyFlag;

    @Value("${app.proxyFlag:false}")
    public void setProxyFlag(Boolean proxyFlag) {
        HttpUtils.proxyFlag = proxyFlag;
    }

    public static final String METHOD_POST = "POST";
    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final String METHOD_GET = "GET";

    public static String doGet(String url) {
        return doGet(url, null);
    }

    public static String doGet(String url, ArrayList<String> header) {
        String ctype = "application/x-www-form-urlencoded;charset=" + DEFAULT_CHARSET;
        StringBuffer sbf = new StringBuffer();
        HttpURLConnection urlConnection = null;
        BufferedReader br = null;
        String content = null;
        try {
            URL u = new URL(url);
            urlConnection = (HttpURLConnection) u.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(1000 * 60 * 25);
            urlConnection.setReadTimeout(1000 * 60 * 25);

            for (Object arr : header) {
                String[] arrs = arr.toString().split(":");
                if (arrs.length >= 1) {
                    urlConnection.setRequestProperty(arrs[0], arrs[1]);
                }
            }


            int responseCode = urlConnection.getResponseCode();

            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                while ((content = br.readLine()) != null) {
                    sbf.append(content);
                }
            }

            log.info("responseCode {}", responseCode);
        } catch (Exception e) {
            log.error("请求数据失败", e);
            throw new HouseException("请求数据失败" + e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    log.error("请求数据失败", e);
                }
            }
            urlConnection.disconnect();
        }
        return sbf.toString();
    }


    public static String doGet2(String url) {
        StringBuffer sbf = new StringBuffer();
        HttpURLConnection urlConnection = null;
        BufferedReader br = null;
        String content = null;
        try {
            URL u = new URL(url);
            urlConnection = (HttpURLConnection) u.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setConnectTimeout(1000 * 60 * 25);
            urlConnection.setReadTimeout(1000 * 60 * 25);
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                while ((content = br.readLine()) != null) {
                    sbf.append(content);
                }
            }
            log.info("responseCode {}", responseCode);
        } catch (Exception e) {
            log.error("请求数据失败", e.getMessage());
            log.error("请求数据失败", e);
            throw new HouseException("请求数据失败" + e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    log.error("请求数据失败", e);
                }
            }
            assert urlConnection != null;
            urlConnection.disconnect();
        }
        return sbf.toString();
    }


    public static String doPost(String url, String ctype, Map<String, Object> params) throws Exception {
        return doPost(url, ctype, 10000, 10000, params);
    }

    public static String doPost(String url, Map<String, Object> params) throws Exception {
        String ctype = "application/x-www-form-urlencoded;charset=" + DEFAULT_CHARSET;
        return doPost(url, ctype, 10000, 10000, params);
    }

    public static String doPost(String url, Map<String, Object> params, Map<String, String> headers) throws Exception {
        String ctype = "application/json";
        HttpURLConnection conn = null;
        OutputStream out = null;
        String rsp;
        try {
            try {
                conn = getConnection(new URL(url), METHOD_POST, ctype, params, headers);
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
            } catch (Exception e) {
                log.error("GET_CONNECTOIN_ERROR, URL = " + url, e);
                throw e;
            }
            try {
                out = conn.getOutputStream();
                //out.write(content);
                rsp = getResponseAsString(conn);
            } catch (IOException e) {
                log.error("REQUEST_RESPONSE_ERROR, URL = " + url, e);
                throw e;
            }

        } finally {
            if (out != null) {
                out.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }

        return rsp;
    }

    public static String doPost(String url, String ctype, int connectTimeout, int readTimeout, Map<String, Object> params) throws Exception {
        HttpURLConnection conn = null;
        OutputStream out = null;
        String rsp = null;
        try {
            try {
                conn = getConnection(new URL(url), METHOD_POST, ctype, params);
                conn.setConnectTimeout(connectTimeout);
                conn.setReadTimeout(readTimeout);
            } catch (Exception e) {
                log.error("GET_CONNECTOIN_ERROR, URL = " + url, e);
                throw e;
            }
            try {
                out = conn.getOutputStream();
                //out.write(content);
                rsp = getResponseAsString(conn);
            } catch (IOException e) {
                log.error("REQUEST_RESPONSE_ERROR, URL = " + url, e);
                throw e;
            }

        } finally {
            if (out != null) {
                out.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }

        return rsp;
    }

    private static HttpURLConnection getConnection(URL url, String method, String ctype, Map<String, Object> params)
            throws IOException {
        // post参数
        StringBuilder postData = new StringBuilder();
        JSONObject json = new JSONObject();

        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) {
                postData.append('&');
            }
            String key = URLEncoder.encode(param.getKey(), "UTF-8");
            String value = URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8");


            postData.append(key);
            postData.append('=');
            postData.append(value);
            //封装json 只有msg需要转移
            json.put(key, String.valueOf(param.getValue()));
        }
        log.info(postData.toString());
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");


        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("Accept", "*/*");
        conn.setRequestProperty("User-Agent", "stargate");
        conn.setRequestProperty("Content-Type", ctype);
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setRequestProperty("Authorization", "Basic SEd4JVB6Z2Q6RkQ2RjlBMkQtMEJFQS03NEVDLTRCODktODA3ODQzNDM4NzNF");

        if ("application/json".equalsIgnoreCase(ctype)) {
            conn.getOutputStream().write(json.toString().getBytes("UTF-8"));
        } else {
            conn.getOutputStream().write(postDataBytes);
        }


        return conn;
    }

    private static HttpURLConnection getConnection(URL url, String method, String ctype, Map<String, Object> params, Map<String, String> headers)
            throws IOException {
        // post参数
        StringBuilder postData = new StringBuilder();
        JSONObject json = new JSONObject();

        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) {
                postData.append('&');
            }
//            String key = URLEncoder.encode(param.getKey(), "UTF-8");
//            String value = URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8");
            String key = param.getKey();
            String value = String.valueOf(param.getValue());

            postData.append(key);
            postData.append('=');
            postData.append(value);
            //封装json 只有msg需要转移
            json.put(key, String.valueOf(param.getValue()));
        }
        log.info(postData.toString());
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");


        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("Accept", "*/*");
        conn.setRequestProperty("User-Agent", "stargate");
        conn.setRequestProperty("Content-Type", ctype);
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setRequestProperty("Authorization", "Basic SEd4JVB6Z2Q6RkQ2RjlBMkQtMEJFQS03NEVDLTRCODktODA3ODQzNDM4NzNF");
        for (String key : headers.keySet()) {
            conn.setRequestProperty(key, headers.get(key));
        }
        if ("application/json".equalsIgnoreCase(ctype)) {
            conn.getOutputStream().write(json.toString().getBytes("UTF-8"));
        } else {
            conn.getOutputStream().write(postDataBytes);
        }


        return conn;
    }

    protected static String getResponseAsString(HttpURLConnection conn) throws IOException {
        String charset = getResponseCharset(conn.getContentType());
        InputStream es = conn.getErrorStream();
        if (es == null) {
            return getStreamAsString(conn.getInputStream(), charset);
        } else {
            String msg = getStreamAsString(es, charset);
            if (StringUtils.isEmpty(msg)) {
                throw new IOException(conn.getResponseCode() + ":" + conn.getResponseMessage());
            } else {
                throw new IOException(msg);
            }
        }
    }

    private static String getStreamAsString(InputStream stream, String charset) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset));
            StringWriter writer = new StringWriter();

            char[] chars = new char[256];
            int count = 0;
            while ((count = reader.read(chars)) > 0) {
                writer.write(chars, 0, count);
            }

            return writer.toString();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    private static String getResponseCharset(String ctype) {
        String charset = DEFAULT_CHARSET;

        if (!StringUtils.isEmpty(ctype)) {
            String[] params = ctype.split(";");
            for (String param : params) {
                param = param.trim();
                if (param.startsWith("charset")) {
                    String[] pair = param.split("=", 2);
                    if (pair.length == 2) {
                        if (!StringUtils.isEmpty(pair[1])) {
                            charset = pair[1].trim();
                        }
                    }
                    break;
                }
            }
        }
        return charset;
    }


    public static void main(String[] args) throws Exception {
        String str = "with   \n" +
                " (select  max(data_date)\n" +
                "    from da.oc_area_market_kpi_day_all\n" +
                "   where hold_amt_with_income > 0\n" +
                "     ) as tmp_datetmp_date\n" +
                "select";

        String rex = "pivot(.*?)dw";

    }


    public int test1() {
        int b = 20;
        try {
            System.out.println("try block");
            return b += 80;
        } catch (Exception e) {
            System.out.println("catch block");
        } finally {
            System.out.println("finally block");
            if (b > 25) {
                System.out.println("b>25, b = " + b);
            }
        }
        return b;
    }

    /**
     * 从网络Url中下载文件
     *
     * @param urlStr
     * @param fileName
     * @param savePath
     */
    public static void downLoadFromUrl(String urlStr, String fileName, String savePath) {
        Instant now = Instant.now();
        //本地测试需代理地址
//        System.setProperty("proxyType", "4");
//        System.setProperty("proxyPort", "8080");
//        System.setProperty("proxyHost", "172.30.15.152");
//        System.setProperty("proxySet", "true");
        try {
            RestTemplate template = new RestTemplate();
            ClientHttpRequestFactory clientFactory = new HttpComponentsClientHttpRequestFactory();
            template.setRequestFactory(clientFactory);
            //定义请求头的接收类型
            RequestCallback requestCallback = request -> request.getHeaders()
                    .setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL));
            // getForObject会将所有返回直接放到内存中,使用流来替代这个操作
            ResponseExtractor<Void> responseExtractor = response -> {
                // Here I write the response to a file but do what you like
                Files.copy(response.getBody(), Paths.get(savePath + File.separator + fileName), StandardCopyOption.REPLACE_EXISTING);
                return null;
            };
            template.execute(urlStr, HttpMethod.GET, requestCallback, responseExtractor);
        } catch (Throwable e) {
            log.error("[下载文件] 写入失败:", e);
            throw new HouseException(Status.ERR_FILE_DOWNLOAD);
        }
        log.info("[下载文件] 完成,耗时:{}", ChronoUnit.MILLIS.between(now, Instant.now()));
    }

    /**
     *      * TODO 下载文件到本地
     *      *
     *      * @param fileUrl   远程地址
     *      * @param fileLocal 本地路径
     *      * @throws Exception
     *     
     */

    public static void downloadFileSSL(String fileUrl, String fileLocal) throws Exception {
//        System.setProperty("proxyType", "4");
//        System.setProperty("proxyPort", "8080");
//        System.setProperty("proxyHost", "172.30.15.152");
//        System.setProperty("proxySet", "true");
        SSLContext sslcontext = SSLContext.getInstance("TLSv1.2", "SunJSSE");
        sslcontext.init(null, new TrustManager[]{new X509TrustUtiil()}, new java.security.SecureRandom());
        URL url = new URL(fileUrl);
        HostnameVerifier ignoreHostnameVerifier = (s, sslsession) -> {
            System.out.println("WARNING: Hostname is not matched for cert.");
            return true;
        };
        HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
        HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
        HttpsURLConnection urlCon = (HttpsURLConnection) url.openConnection();
        urlCon.setConnectTimeout(6000);
        urlCon.setReadTimeout(6000);
        int code = urlCon.getResponseCode();
        if (code != HttpURLConnection.HTTP_OK) {
            throw new Exception("文件读取失败");
        }
        // 读文件流
        try (DataInputStream in = new DataInputStream(urlCon.getInputStream());
             DataOutputStream out = new DataOutputStream(new FileOutputStream(fileLocal));
        ) {
            byte[] buffer = new byte[2048];
            int count = 0;
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }

        }

    }

}
