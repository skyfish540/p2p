package com.bjpowernode.p2p.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bjpowernode.p2p.commons.Constants;
import com.bjpowernode.p2p.commons.ReturnObject;
import com.bjpowernode.p2p.model.User;
import com.bjpowernode.p2p.service.BidService;
import com.bjpowernode.p2p.service.LoanService;
import com.bjpowernode.p2p.service.UserService;
import org.apache.commons.lang3.StringUtils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Reference
    private UserService userService;
    @Reference
    private LoanService loanService;
    @Reference
    private BidService bidService;

    @RequestMapping("/checkPhone")
    public @ResponseBody Object checkPhone(@RequestParam("phone") String phone){
        System.out.println(phone);
        ReturnObject returnObject = new ReturnObject();
        User user=userService.queryUserByPhone(phone);
        System.out.println(user);
        if (user!=null){
            returnObject.setCode(Constants.ERROR_CODE);
            returnObject.setMessage("用户已存在，请更换！");
        }else {
            returnObject.setCode(Constants.SUCCESS_CODE);
            returnObject.setMessage("手机未被注册，可以使用！");
        }
        return returnObject;
    }

    @RequestMapping("/checkCaptcha")
    public @ResponseBody Object checkCaptcha(@RequestParam("captcha") String captcha,
                                             HttpSession session){
        ReturnObject returnObject = new ReturnObject();
        String sessionCaptcha= (String) session.getAttribute(Constants.SESSION_CAPTCHA);
        System.out.println("sessionCaptcha:"+sessionCaptcha);
        if (captcha!=null){
            if (!captcha.equalsIgnoreCase(sessionCaptcha)){
                returnObject.setCode(Constants.ERROR_CODE);
                returnObject.setMessage("验证码输入不正确");
            }else {
                returnObject.setCode(Constants.SUCCESS_CODE);
                returnObject.setMessage("验证码输入正确");
            }
        }
        return returnObject;
    }

    @RequestMapping("/register")
    public @ResponseBody Object register(@RequestParam("phone")String phone,
                           @RequestParam("loginPassword")String loginPassword,
                           @RequestParam("replayLoginPassWord")String replayLoginPassWord,
                           @RequestParam("captcha")String captcha,
                           HttpSession session){

        System.out.println("register方法");
        ReturnObject returnObject = new ReturnObject();
        //对前端传过来的参数进行后台的验证
        if (StringUtils.isEmpty(phone)){
            returnObject.setCode(Constants.ERROR_CODE);
            returnObject.setMessage("手机号不能为空");
            return returnObject;
        }
        //字符串phone是不是匹配正则表达式^1[1-9]\d{9}$；如果匹配就返回ture；如果不匹配就返回false
        if (!Pattern.matches("^1[1-9]\\d{9}$", phone)){
            returnObject.setCode(Constants.ERROR_CODE);
            returnObject.setMessage("手机号格式不正确");
            return returnObject;
        }
        if (StringUtils.isEmpty(loginPassword)){
            returnObject.setCode(Constants.ERROR_CODE);
            returnObject.setMessage("请输入登录密码");
            return returnObject;
        }
        if (StringUtils.isEmpty(replayLoginPassWord)){
            returnObject.setCode(Constants.ERROR_CODE);
            returnObject.setMessage("请输入确认密码");
            return returnObject;
        }
        if (!StringUtils.equals(loginPassword,replayLoginPassWord)){
            returnObject.setCode(Constants.ERROR_CODE);
            returnObject.setMessage("两次输入的密码不一致");
            return returnObject;
        }
        if (StringUtils.isEmpty(captcha)){
            returnObject.setCode(Constants.ERROR_CODE);
            returnObject.setMessage("验证码不能为空");
            return returnObject;
        }
        String sessionCaptcha= (String) session.getAttribute(Constants.SESSION_CAPTCHA);
        if (!StringUtils.containsIgnoreCase(captcha,sessionCaptcha)){
            returnObject.setCode(Constants.ERROR_CODE);
            returnObject.setMessage("验证码输入不正确");
            return returnObject;
        }
        //当上面都验证通过，需要把用户信息添加到数据库里
        Map<String,Object> map = new HashMap<>();
        map.put("phone",phone);
        map.put("loginPassword",loginPassword);
        //保存数据，当service抛出异常，controller捕获到，说明注册失败，
        // 要么插入用户名表失败要么插入用户账户表失败，事务都会回滚

        try {
            userService.addUser(map);
            returnObject.setCode(Constants.SUCCESS_CODE);
            //returnObject.setMessage("注册成功");
            return  returnObject;
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Constants.ERROR_CODE);
            returnObject.setMessage("注册失败");
            return  returnObject;
        }
    }

    //初始化登录页面数据
    @RequestMapping("/initLoanState")
    public @ResponseBody Object loadLoanState(){
        System.out.println("loadLoanState方法");
        //查询历史年化收益率
        double historyRate = loanService.queryHistoryRate();
        //查询用户总数
        long allUsers = userService.queryCountAllUsers();
        //查询累计成交额
        double allBidMoney = bidService.queryAllBidMoney();
        //把数据保存到model中,返回给页面
        Map<String,Object> map = new HashMap<>();
        map.put("historyRate",historyRate);
        map.put("allUsers",allUsers);
        map.put("allBidMoney",allBidMoney);
        return map;
    }

    @RequestMapping("/login")
    public @ResponseBody Object login(@RequestParam("phone") String phone,
                                      @RequestParam("loginPassword") String loginPassword,
                                      HttpSession session){
        ReturnObject returnObject = new ReturnObject();
        if (StringUtils.isEmpty(phone)){
            returnObject.setCode(Constants.ERROR_CODE);
            returnObject.setMessage("请输入账号");
            return returnObject;
        }
        if (StringUtils.isEmpty(loginPassword)){
            returnObject.setCode(Constants.ERROR_CODE);
            returnObject.setMessage("请输入密码");
            return returnObject;
        }
        Map<String,Object> map = new HashMap<>();
        map.put("phone",phone);
        map.put("loginPassword",loginPassword);
        try {
            User user = userService.queryUserByPhoneAndLoginPassword(map);
            if (user==null){
                returnObject.setCode(Constants.ERROR_CODE);
                returnObject.setMessage("用户名或密码错误");
                return returnObject;
            }else {
                System.out.println(user.getFinanceAccount());
                //把用户信息保存到session中
                session.setAttribute("sessionUser", user);
                returnObject.setCode(Constants.SUCCESS_CODE);
                returnObject.setMessage("登录成功");
                return returnObject;
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Constants.ERROR_CODE);
            returnObject.setMessage("系统忙，请稍后重试...");
            return returnObject;
        }
    }

    @RequestMapping("/verifyRealName")
    public @ResponseBody Object verifyRealName(@RequestParam("realName")String realName,
                                               @RequestParam("idCard")String idCard,
                                               @RequestParam("replayIdCard")String replayIdCard,
                                               @RequestParam("captcha")String captcha,
                                               HttpSession session){
        ReturnObject returnObject =  new ReturnObject();
        if (StringUtils.isEmpty(realName)){
            returnObject.setCode(Constants.ERROR_CODE);
            returnObject.setMessage("请输入真实姓名");
            return returnObject;
        }
        if (StringUtils.isEmpty(idCard)){
            returnObject.setCode(Constants.ERROR_CODE);
            returnObject.setMessage("请输入身份证号");
            return returnObject;
        }
        if (Pattern.matches("(^\\d{15}$)|(^\\d{17}(\\d|X|x)$)",idCard)){
            returnObject.setCode(Constants.ERROR_CODE);
            returnObject.setMessage("身份证输入不合法");
            return returnObject;
        }
        if (StringUtils.isEmpty(replayIdCard)){
            returnObject.setCode(Constants.ERROR_CODE);
            returnObject.setMessage("请输入确认身份证号");
            return returnObject;
        }
        if (StringUtils.equals(idCard,replayIdCard)){
            returnObject.setCode(Constants.ERROR_CODE);
            returnObject.setMessage("两次输入的身份证号不一样");
            return returnObject;
        }
        if (StringUtils.isEmpty(captcha)){
            returnObject.setCode(Constants.ERROR_CODE);
            returnObject.setMessage("请输入图形验证码");
            return returnObject;
        }
        String sessionCaptcha= (String) session.getAttribute(Constants.SESSION_CAPTCHA);
        if (StringUtils.containsIgnoreCase(captcha,sessionCaptcha)){
            returnObject.setCode(Constants.ERROR_CODE);
            returnObject.setMessage("图形验证码输入错误");
            return returnObject;
        }
        //程序运行到这里，说明上面验证都通过了，此时需要调用真实身份证验证api接口
        User user= (User) session.getAttribute(Constants.SESSION_USER);
        Map<String,Object> map = new HashMap<>();
        map.put("realName", realName);
        map.put("idCard", idCard);
        map.put("id", user.getId());
        boolean ret=userService.doRealNameVerify(map);

        return returnObject;
    }


}
