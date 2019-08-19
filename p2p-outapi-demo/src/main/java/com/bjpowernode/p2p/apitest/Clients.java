package com.bjpowernode.p2p.apitest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class Clients {
    public static void main(String[] args) {
        //1)模拟客户端请求服务端
        String url="http://localhost:8082/p2p-outapi/outapi/realNameVerify";
        //2)携带请求参数
        Map<String,Object> map = new HashMap<>();
        map.put("apiKey", "wehh64ggjhjst8dfd6adj8hdsdmk");
        map.put("realName", "李小龙");
        map.put("idCard", "342655196510150509");
        //3)将map类型数据转化成json字符串类型
        //{"realName":"李小龙","apiKey":"wehh64ggjhjst8dfd6adj8hdsdmk","idCard":"342655196510150509"}
        String paramJson= JSON.toJSONString(map);
        System.out.println(paramJson);
        //4)用URL类，将json字符串写入请求体中
        try {
            URL reqUrl= new URL(url);
            //1)打开http请求连接
            HttpURLConnection urlConnection= (HttpURLConnection) reqUrl.openConnection();
            //2)设置连接超时时间
            urlConnection.setConnectTimeout(50000);
            //3)设置请求方式
            urlConnection.setRequestMethod("POST");
            //4)指定可以进行写操作的那些连接
            urlConnection.setDoOutput(true);
            //5)设置响应读取超时时间
            urlConnection.setReadTimeout(60000);
            //6)将数据写入请求体,从连接中获取输出流通道（写入流）
            OutputStream os=urlConnection.getOutputStream();
            os.write(paramJson.getBytes("UTF-8"));//得到的是在指定字符集下的字节数组
            System.out.println(Arrays.toString(paramJson.getBytes("UTF-8")));
            //7)读取响应数据，从连接中获取输入流通道（读取流）
            InputStream is=urlConnection.getInputStream();
            //将字节流转成字符流
            InputStreamReader isr=new InputStreamReader(is);
            //将字符流转成缓冲流
            BufferedReader br = new BufferedReader(isr);
            //8)将响应数据打印到控制台
            StringBuffer sb= new StringBuffer();
            String str=null;
            while ((str=br.readLine())!=null){
                sb.append(str);
            }
            System.out.println(sb.toString());
            JSONObject jsonObject= (JSONObject) JSON.parse(sb.toString());
            String code=jsonObject.getString("code");
            String message=jsonObject.getString("message");
            System.out.println("code:"+code);
            System.out.println("message:"+message);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
