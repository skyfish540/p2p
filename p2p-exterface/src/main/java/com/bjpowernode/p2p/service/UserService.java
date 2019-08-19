package com.bjpowernode.p2p.service;

import com.bjpowernode.p2p.model.User;

import java.util.Map;

public interface UserService {
    long queryCountAllUsers();

    User queryUserByPhone(String phone);

    void addUser(Map<String, Object> map);

    User queryUserByPhoneAndLoginPassword(Map<String, Object> map);

     boolean doRealNameVerify(Map<String, Object> map);
}
