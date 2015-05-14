package com.hltc.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.List;  

import javax.net.ssl.SSLContext;



import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;



import ytx.org.apache.http.client.methods.HttpGet;
import ytx.org.apache.http.client.methods.HttpHead;





public class HttpUtil {

	/**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
//	private static Log log = LogFactory.getLog(HttpUtil.class); 
//    public static String sendGet(String url, String param) {
//        String result = "";
//        BufferedReader in = null;
//        try {
//            String urlNameString = url + "?" + param;
//            URL realUrl = new URL(urlNameString);
//            // 打开和URL之间的连接
//            URLConnection connection = realUrl.openConnection();
//            // 设置通用的请求属性
//            connection.setRequestProperty("accept", "*/*");
//            connection.setRequestProperty("connection", "Keep-Alive");
//            connection.setRequestProperty("user-agent",
//                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//            // 建立实际的连接
//            connection.connect();
//            // 获取所有响应头字段
//            Map<String, List<String>> map = connection.getHeaderFields();
//            // 遍历所有的响应头字段
//            for (String key : map.keySet()) {
//                System.out.println(key + "--->" + map.get(key));
//            }
//            // 定义 BufferedReader输入流来读取URL的响应
//            in = new BufferedReader(new InputStreamReader(
//                    connection.getInputStream()));
//            String line;
//            while ((line = in.readLine()) != null) {
//                result += line;
//            }
//        } catch (Exception e) {
//            System.out.println("发送GET请求出现异常！" + e);
//            e.printStackTrace();
//        }
//        // 使用finally块来关闭输入流
//        finally {
//            try {
//                if (in != null) {
//                    in.close();
//                }
//            } catch (Exception e2) {
//                e2.printStackTrace();
//            }
//        }
//        return result;
//    }

    /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
//    public static String sendPost(String url, String param) {
//    	
//        PrintWriter out = null;
//        BufferedReader in = null;
//        String result = "";
//        try {
//            URL realUrl = new URL(url);
//            // 打开和URL之间的连接
//            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
//            conn.setRequestMethod("POST");
//            // 设置通用的请求属性
//            conn.setRequestProperty("accept", "*/*");
//            conn.setRequestProperty("connection", "Keep-Alive");
//            conn.setRequestProperty("user-agent",
//                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//            conn.setRequestProperty("Content-Type",  "application/json");
//            // 发送POST请求必须设置如下两行
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            // 获取URLConnection对象对应的输出流
//            out = new PrintWriter(conn.getOutputStream());
//            // 发送请求参数
//            out.print(param);
//            // flush输出流的缓冲
//            out.flush();
//            // 定义BufferedReader输入流来读取URL的响应
//            in = new BufferedReader(
//                    new InputStreamReader(conn.getInputStream()));
//            String line;
//            while ((line = in.readLine()) != null) {
//                result += line;
//            }
//        } catch (Exception e) {
//            System.out.println("发送 POST 请求出现异常！"+e);
//            e.printStackTrace();
//        }
//        //使用finally块来关闭输出流、输入流
//        finally{
//            try{
//                if(out!=null){
//                    out.close();
//                }
//                if(in!=null){
//                    in.close();
//                }
//            }
//            catch(IOException ex){
//                ex.printStackTrace();
//            }
//        }
//        return result;
//    }   
    
    /** 
     * 执行一个HTTP POST请求，返回请求响应的HTML 
     * 
     * @param url        请求的URL地址 
     * @param params 请求的查询参数,可以为null 
     * @return 返回请求响应的HTML 
     */ 
//    public static String doPost(String url, Map<String, String> params) { 
//            String response = null; 
//            HttpClient client = new HttpClient(); 
//            HttpMethod  method = new PostMethod(url); 
//            for (Iterator it = params.entrySet().iterator(); it.hasNext();) { 
//            	
//            } 
//            //设置Http Post数据 
//            if (params != null) { 
//                    HttpMethodParams p = new HttpMethodParams(); 
//                    for (Map.Entry<String, String> entry : params.entrySet()) { 
//                            p.setParameter(entry.getKey(), entry.getValue()); 
//                    } 
//                    method.setParams(p); 
//            } 
//            try { 
//                    client.executeMethod(method); 
//                    if (method.getStatusCode() == HttpStatus.SC_OK) { 
//                            response = method.getResponseBodyAsString(); 
//                    } 
//            } catch (IOException e) { 
////                    log.error("执行HTTP Post请求" + url + "时，发生异常！", e);
//            	System.out.println("执行HTTP Post请求" + url + "时，发生异常！"+e);
//            } finally { 
//                    method.releaseConnection(); 
//            } 
//            System.out.println(1);
//            return response; 
//    } 
	
	/** 
     * HttpClient连接SSL 
     */  
    public void ssl() {  
        CloseableHttpClient httpclient = null;  
        try {  
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());  
            FileInputStream instream = new FileInputStream(new File("d:\\tomcat.keystore"));  
            try {  
                // 加载keyStore d:\\tomcat.keystore    
                trustStore.load(instream, "123456".toCharArray());  
            } catch (CertificateException e) {  
                e.printStackTrace();  
            } finally {  
                try {  
                    instream.close();  
                } catch (Exception ignore) {  
                }  
            }  
            // 相信自己的CA和所有自签名的证书  
            SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build();  
            // 只允许使用TLSv1协议  
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,  
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);  
            httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();  
            // 创建http请求(get方式)  
            HttpGet httpget = new HttpGet("https://localhost:8443/myDemo/Ajax/serivceJ.action");  
            System.out.println("executing request" + httpget.getRequestLine());  
            CloseableHttpResponse response = httpclient.execute((HttpUriRequest) httpget);  
            try {  
                HttpEntity entity = response.getEntity();  
                System.out.println("----------------------------------------");  
                System.out.println(response.getStatusLine());  
                if (entity != null) {  
                    System.out.println("Response content length: " + entity.getContentLength());  
                    System.out.println(EntityUtils.toString(entity));  
                    EntityUtils.consume(entity);  
                }  
            } finally {  
                response.close();  
            }  
        } catch (ParseException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (KeyManagementException e) {  
            e.printStackTrace();  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        } catch (KeyStoreException e) {  
            e.printStackTrace();  
        } finally {  
            if (httpclient != null) {  
                try {  
                    httpclient.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
    }  

    
    /** 
     * 发送 post请求访问本地应用并根据传递参数不同返回不同结果 
     */  
    
	public static void post() {  
        // 创建默认的httpClient实例.    
        CloseableHttpClient httpclient = HttpClients.createDefault();  
        // 创建httppost    
        HttpPost httppost = new HttpPost("http://yuntuapi.amap.com/datamanage/data/create");  
        httppost.setHeader("Content-Type", "application/x-www-form-urluncoded");
        // 创建参数队列    
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
        formparams.add(new BasicNameValuePair("key", "c779e72cc00bdd408f58ff24765462e4"));  
        formparams.add(new BasicNameValuePair("tableid","551e4487e4b098078fd9395a"));
        formparams.add(new BasicNameValuePair("loctype", "1"));
        
        JSONObject json = new JSONObject();
        json.put("key", "c779e72cc00bdd408f58ff24765462e4");
        json.put("tableid","551e4487e4b098078fd9395a");
        json.put("loctype", 1);
        
        JSONObject data = new JSONObject();
        data.put("_name", "光谷广场地铁站E出口");
        data.put("_location","114.397875,30.50611");
        data.put("coordtype", "autonavi");
        data.put("_address","轨道交通2号线");
        data.put("_poiid", "BX10004737");
        
        List<Object> arr = new ArrayList<Object>();
        arr.add(data);
        json.put("data", arr);
        
        System.out.println(arr.toString());
        formparams.add(new BasicNameValuePair("data", data.toString()));
        
        
        StringEntity se = new StringEntity(json.toString(),"UTF-8");
        
        UrlEncodedFormEntity uefEntity;  
        try {  
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(uefEntity);  
            System.out.println(httppost.toString());
//            httppost.setEntity(se);
            
             
            System.out.println("executing request " + httppost.getURI());  
            CloseableHttpResponse response = httpclient.execute(httppost);  
            try {  
                HttpEntity entity = response.getEntity();  
                if (entity != null) {  
                    System.out.println("--------------------------------------");  
                    System.out.println(entity.toString());
                    System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));  
                    System.out.println("--------------------------------------");  
                }  
            } finally {  
                response.close();  
            }  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (UnsupportedEncodingException e1) {  
            e1.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            // 关闭连接,释放资源    
            try {  
                httpclient.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
    
    
    public static void main(String[] args) {
        //发送 GET 请求
//        String s=HttpUtil.sendGet("http://localhost:8080/maitian/v1/user/register/verify_code.json","phone_number=15527207898");
//        System.out.println(s);
        
        //发送 POST 请求
        post();
        
        
    }
} 