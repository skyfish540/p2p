package com.bjpowernode.p2p.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.*;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.bjpowernode.p2p.commons.AlipayConfig;
import com.bjpowernode.p2p.commons.Constants;
import com.bjpowernode.p2p.commons.UUIDUtils;
import com.bjpowernode.p2p.model.RechargeRecord;
import com.bjpowernode.p2p.model.User;
import com.bjpowernode.p2p.service.RechargeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 *
 */
@Controller
@RequestMapping("/recharge")
public class RecharegeController {
    @Resource
    private RechargeService rechargeService;

    @RequestMapping("/myRecharge")
    public String rechargeRecord(@RequestParam(name = "pageNo",required = false) Integer pageNo,
                                 HttpSession session,
                                 Model model){
        User user= (User) session.getAttribute(Constants.SESSION_USER);
        //计算startRow
        if (pageNo==null){
            pageNo=1;
        }
        //startRow=(pageNo-1)*pageSize
        Integer startRow=(pageNo-1)*Constants.LIST_PAGE_SIZE;
        //将变量保存到map中
        Map<String,Object> map = new HashMap();
        map.put("uid", user.getId());
        map.put("pageNo", pageNo);
        map.put("pageSize", Constants.LIST_PAGE_SIZE);
        map.put("startRow", startRow);

        //查询所有充值记录
        List<RechargeRecord> rechargeRecordList=rechargeService.queryRechargeByUid(map);

        //查询总条数
        long totalRows=rechargeService.queryRechargeCountByUid(user.getId());

        //计算总页数
        long totalPages=1;
        if (totalRows%totalPages==0){
            totalPages=totalRows/Constants.LIST_PAGE_SIZE;
        }else {
            totalPages=totalRows/Constants.LIST_PAGE_SIZE+1;
        }
        //把数据保存到model中,返回给页面
        model.addAttribute("rechargeRecordList",rechargeRecordList);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("totalRows",totalRows);
        model.addAttribute("totalPages",totalPages);
        return "myRecharge";
    }

    @RequestMapping("/toRecharge")
    public String toRecharge(){

        return "toRecharge";
    }

    @RequestMapping("/toAlipayRecharge")
    public String toAlipayRecharge(@RequestParam("alipayMoney") double alipayMoney,
                                   HttpSession session,
                                   Model model){

        User user= (User) session.getAttribute(Constants.SESSION_USER);
        //下单
        RechargeRecord rechargeRecord =new RechargeRecord();
        rechargeRecord.setUid(user.getId());
        rechargeRecord.setRechargeNo(UUIDUtils.getUUID());
        rechargeRecord.setRechargeMoney(alipayMoney);
        rechargeRecord.setRechargeStatus(Constants.RECHARGE_STATUS_ING);
        rechargeRecord.setRechargeTime(new Date());
        rechargeRecord.setRechargeDesc("支付宝充值");
        int rechargeRecordCount=rechargeService.saveRechargeRecord(rechargeRecord);

        //下单成功
        if (rechargeRecordCount>0){
                //获得初始化的AlipayClient
                AlipayClient alipayClient =new DefaultAlipayClient(AlipayConfig.GATEWAY_URL,AlipayConfig.APP_ID,AlipayConfig.MERCHANT_PRIVATE_KEY,"json",AlipayConfig.CHARSET,AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGN_TYPE);
                //设置请求参数
                AlipayTradePagePayRequest alipayRequest=new AlipayTradePagePayRequest();
                alipayRequest.setReturnUrl(AlipayConfig.RETURN_URL);
                alipayRequest.setNotifyUrl(AlipayConfig.NOTIFY_URL);
                alipayRequest.setBizContent("{\"out_trade_no\":\""+ rechargeRecord.getRechargeNo() +"\","
                        + "\"total_amount\":\""+ alipayMoney +"\","
                        + "\"subject\":\""+ "充值" +"\","
                        + "\"body\":\""+ "充值-支付宝" +"\","
                        + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}"
                );
                //发起请求
                try {
                    String responseBody = alipayClient.pageExecute(alipayRequest).getBody();
                    System.out.println(responseBody);
                    model.addAttribute("responseResult",responseBody);
                    return "toAlipay";
                } catch (AlipayApiException e) {
                    e.printStackTrace();
                    //支付宝下单失败
                    model.addAttribute("trade_msg","支付宝下单失败");
                    return "toRechargeBack";
                }
        }else {
                //下单失败
                model.addAttribute("trade_msg","下单失败");
                return "toRechargeBack";
        }
    }

    @RequestMapping("/alipayReturnURL")
    public String alipayReturnURLAndQueryTradeResult(HttpServletRequest request,Model model){

        try {
            //获取支付宝GET过来反馈信息
            Map<String, String> params = new HashMap<String, String>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
                String name = (String) iter.next();
                String[] values = (String[]) requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    //System.out.print(values[i]+",");
                    valueStr = (i == values.length - 1) ? valueStr + values[i]
                            : valueStr + values[i] + ",";
                }
               // System.out.println(valueStr);
                //乱码解决，这段代码在出现乱码时使用
                valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
                params.put(name, valueStr);
            }

            //调用SDK验证签名
            boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, AlipayConfig.SIGN_TYPE);
            //验签通过
            if (signVerified){
                String out_trade_no=new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
                String trade_no=new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
                String total_amount=new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");

                System.out.println("trade_no:" + trade_no + "<br/>out_trade_no:" + out_trade_no + "<br/>total_amount:" + total_amount);
                //调用支付宝查询接口，得到交易结果
                //获得初始化的AlipayClient
                AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.GATEWAY_URL, AlipayConfig.APP_ID,
                        AlipayConfig.MERCHANT_PRIVATE_KEY, "json", AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,
                        AlipayConfig.SIGN_TYPE);

                //设置请求参数
                AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();
                alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","+"\"trade_no\":\""+ trade_no +"\"}");
                //请求
                String result = alipayClient.execute(alipayRequest).getBody();
                System.out.println(result);

                //将字符串转成json对象,以便获取键值对
                JSONObject jsonObject=(JSONObject) JSON.parse(result);
                JSONObject responseJsonObject=jsonObject.getJSONObject("alipay_trade_query_response");
                String code=responseJsonObject.getString("code");

                if ("10000".equals(code)){
                    //接口调用成功,获取交易状态
                    String trade_status=responseJsonObject.getString("trade_status");
                    if ("TRADE_SUCCESS".equals(trade_status)||"TRADE_FINISHED".equals(trade_status)){
                        //交易成功
                        //更新充值记录状态为充值成功、修改用户账户可用金额
                    }

                }




                System.out.println("===========验签通过=============");
            }

        }catch (Exception e){

        }
        return "";
    }
}
