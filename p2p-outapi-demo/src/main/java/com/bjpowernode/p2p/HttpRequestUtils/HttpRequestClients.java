package com.bjpowernode.p2p.HttpRequestUtils;

import com.alibaba.fastjson.JSON;

import javax.jws.Oneway;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 *
 */
public class HttpRequestClients {

    public static String postOutApi(String url, Map<String, Object> map){
        String reqParamJson= JSON.toJSONString(map);

        HttpURLConnection urlConnection=null;
        OutputStream os=null;
        InputStream is=null;
        StringBuffer sb=null;

        try {
            URL reqUrl= new URL(url);
            urlConnection= (HttpURLConnection) reqUrl.openConnection();
            urlConnection.setReadTimeout(50000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setReadTimeout(50000);

            os=urlConnection.getOutputStream();
            os.write(reqParamJson.getBytes("UTF-8"));

            is=urlConnection.getInputStream();
            InputStreamReader isr=new InputStreamReader(is);
            BufferedReader br=new BufferedReader(isr);

            sb= new StringBuffer();
            String str=null;
            while ((str=br.readLine())!=null){
                sb.append(str);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //销毁Http连接
            urlConnection.disconnect();
            try {
                //关闭输入输出流
                os.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
