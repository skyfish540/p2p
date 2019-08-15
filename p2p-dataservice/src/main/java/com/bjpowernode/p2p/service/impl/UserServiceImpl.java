package com.bjpowernode.p2p.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.bjpowernode.p2p.commons.Constants;
import com.bjpowernode.p2p.mapper.FinanceAccountMapper;
import com.bjpowernode.p2p.mapper.UserMapper;
import com.bjpowernode.p2p.model.FinanceAccount;
import com.bjpowernode.p2p.model.User;
import com.bjpowernode.p2p.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 *
 */
@Service(interfaceClass = UserService.class)
@Component
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
        user.setPhone((String) map.get("phone"));
        user.setLoginPassword((String) map.get("loginPassword"));
        user.setAddTime(new Date());
        int userCount=userMapper.insertUser(user);

        if (userCount>0){   //说明用户添加成功，此时需要向用户账户表里添加数据
            FinanceAccount financeAccount= new FinanceAccount();
            financeAccount.setId(user.getId());
            financeAccount.setAvailableMoney(Constants.INIT_ACCOUNT_MONEY);//初始化账户资金
            int accountCount=financeAccountMapper.insertFinanceAccount(financeAccount);

        }

    }
}
