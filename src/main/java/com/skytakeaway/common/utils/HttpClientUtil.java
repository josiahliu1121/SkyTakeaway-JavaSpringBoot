package com.skytakeaway.common.utils;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientUtil {

    final static int TIMEOUT = 1000;
    final static int TIMEOUT_MSEC = 5 * 1000;

    public static String doGet(String url, Map<String, String> paramMap){
        CloseableHttpClient httpClient = HttpClients.createDefault();

        String result = "";
        CloseableHttpResponse response = null;

        try {
            //create url with parameter need
            URIBuilder builder = new URIBuilder(url);
            if(paramMap != null){
                for(String key : paramMap.keySet()){
                    builder.addParameter(key, paramMap.get(key));
                }
            }
            URI uri = builder.build();

            //creating a http get request
            HttpGet httpGet = new HttpGet(uri);

            //execute the http request
            response = httpClient.execute(httpGet);

            //get the return value of the request
            if(response.getStatusLine().getStatusCode() == 200){
                result = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static String doPost(String url, Map<String, String> paramMap) throws IOException{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String result = "";

        try {
            //create a http post request
            HttpPost httpPost = new HttpPost(url);

            //create request param query
            if (paramMap != null){
                List<NameValuePair> paramList = new ArrayList<>();
                for (Map.Entry<String, String> param : paramMap.entrySet()){
                    paramList.add(new BasicNameValuePair(param.getKey(), param.getValue()));
                }

                //parse in parameter into post request
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                httpPost.setEntity(entity);
            }

            httpPost.setConfig(builderRequestConfig());

            response = httpClient.execute(httpPost);

            result = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally {
            response.close();
            httpClient.close();
        }

        return result;
    }

    private static RequestConfig builderRequestConfig(){
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(TIMEOUT_MSEC)
                .setConnectTimeout(TIMEOUT)
                .build();
        return requestConfig;
    }

}
