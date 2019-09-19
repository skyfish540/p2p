package com.bjpowernode.p2p.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bjpowernode.p2p.commons.Constants;
import com.bjpowernode.p2p.commons.HttpRequestClients;
import com.bjpowernode.p2p.excepiton.MyException;
import com.bjpowernode.p2p.mapper.FinanceAccountMapper;
import com.bjpowernode.p2p.mapper.UserMapper;
import com.bjpowernode.p2p.model.FinanceAccount;
import com.bjpowernode.p2p.model.User;
import com.bjpowernode.p2p.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FinanceAccountMapper financeAccountMapper;


    @Override
    public long queryCountAllUsers() {
        return userMapper.selectCountAllUsers();
    }

    @Override
    public User queryUserByPhone(String phone) {
        return userMapper.selectUserByPhone(phone);
    }

    @Override
    public void addUser(Map<String, Object> map) {
        User user= new User();
        FinanceAccount financeAccount= null;
        user.setPhone((String) map.get("phone"));
        user.setLoginPassword((String) map.get("loginPassword"));
        user.setAddTime(new Date());
        //像用户表中太添加数据
        int userCount=userMapper.insertUser(user);

        System.out.println("1:"+user);

        if (userCount>0){   //说明用户添加成功，此时需要向用户账户表里添加数据
            financeAccount= new FinanceAccount();
            financeAccount.setUid(user.getId());
            financeAccount.setAvailableMoney(Constants.INIT_ACCOUNT_MONEY);//初始化账户资金
            int accountCount=financeAccountMapper.insertFinanceAccount(financeAccount);
            if (accountCount==0){
                throw new MyException();
            }

        }else {
            throw  new MyException();
        }
    }

    @Override
    public User doLoginByPhoneAndLoginPassword(Map<String, Object> map) {
        User tempUser=userMapper.selectUserByPhoneAndLoginPassword(map);
        if (tempUser!=null){
           tempUser.setLastLoginTime(new Date());
            int updataUserCount = userMapper.updateByPrimaryKey(tempUser);
            if (updataUserCount==0){
                throw  new MyException();
            }
        }
        return tempUser;
    }

    @Override
    public boolean doRealNameVerify(Map<String, Object> map) {
        Map<String,Object> verifyMap= new HashMap<>();
        verifyMap.put("apiKey", Constants.REAL_NAME_VERIFY_APIKEY);
        verifyMap.put("realName", map.get("realName"));
        verifyMap.put("idCard", map.get("idCard"));

        String repStr = HttpRequestClients.postOutApi(Constants.REAL_NAME_VERIFY_URL, verifyMap);
        JSONObject jsonObject= (JSONObject) JSON.parse(repStr);
        String code=jsonObject.getString("code");
        String message=jsonObject.getString("message");
        if ("1001".equals(code)){ //表示认证成功，认证信息更新到数据库
            User user=new User();
            user.setId((Integer) map.get("id"));
            user.setName((String) map.get("realName"));
            user.setIdCard((String) map.get("idCard"));
            int result = userMapper.updateUserRealName(user);
            if (result>0){
                return true;
            } else {
                throw new MyException();
            }

        }else {
           return false;
        }
    }

    @Override
    public User queryUserByUid(Integer uid) {
        return userMapper.selectUserByUid(uid);
    }
}
