package com.bjpowernode.p2p.outapi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 *
 */
@Controller
@RequestMapping("/outapi")
public class RealNameVerifyController {
    /*这里应该是controller调用service，在调用mapper，然后从数据库里查询apikey和身份证信息
    因为是模拟API接口,所以这里我们就定义一个list集合存放apikey，定义一个map存放身份证信息*/
    private static List<String> apiKeys=new ArrayList<>();
    private static Map<String,String> map=new HashMap<>();
    static {
        apiKeys.add("wehh64ggjhjst8dfd6adj8hdsdmk");
        apiKeys.add("we46g865lnnjk0945njkrt98fso9");
        map.put("342655196510150509","李小龙");
        map.put("342655456510150857","张三丰");
        map.put("342422198510230498","乔峰");
    }
    @RequestMapping("/realNameVerify")
    public @ResponseBody Object realNameVerify(HttpServletRequest request){
        System.out.println("==========realNameVerify方法============");
        ReturnObject returnObject=new ReturnObject();
        //1)读取请求这种的数据
        try {
            String data = IOUtils.toString(request.getInputStream(), "UTF-8");
            System.out.println("data"+data);
            //2)把读取的数据流转化成json格式:{"apiKey":xxxxxxx,"realName":"张三","idCard":"xxxxxxxxxxxx"}
            JSONObject jsonObject = (JSONObject) JSON.parse(data);
            //3)从json字符串中获取相应值,
            //请求端传过来的数据格式必须是{"apiKey":xxxxxxx,"realName":"张三","idCard":"xxxxxxxxxxxx"}
            String apiKey = jsonObject.getString("apiKey");
            String realName = jsonObject.getString("realName");
            String idCard = jsonObject.getString("idCard");
            //4)将请求过来的数据与数据库里的数据相比对
            if (StringUtils.isEmpty(apiKey)) {
                returnObject.setCode("1000");
                returnObject.setMessage("apikey不能为空");
                return returnObject;
            } else if (!apiKeys.contains(apiKey)) {
                returnObject.setCode("1000");
                returnObject.setMessage("apikey不合法");
                return returnObject;
            }
            if (StringUtils.isEmpty(realName)) {
                returnObject.setCode("1000");
                returnObject.setMessage("realName不能为空");
                return returnObject;
            } else if (StringUtils.isEmpty(idCard)) {
                returnObject.setCode("1000");
                returnObject.setMessage("idCard不能为空");
                return returnObject;
            } else {
                if (map.containsKey(idCard) && map.get(idCard).equals(realName)) {
                    returnObject.setCode("1001");
                    returnObject.setMessage("实名认证成功");
                    return returnObject;
                } else {
                    returnObject.setCode("1000");
                    returnObject.setMessage("实名认证失败");
                    return returnObject;
                }
            }
        }catch (Exception e){
            System.out.println("异常处理--------");
            e.printStackTrace();
            System.out.println("异常处理......");
            returnObject.setCode("1000");
            returnObject.setMessage("请求参数错误");
            return returnObject;

        }

    }
}
