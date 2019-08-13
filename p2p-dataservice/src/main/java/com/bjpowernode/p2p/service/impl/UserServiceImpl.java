package com.bjpowernode.p2p.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.bjpowernode.p2p.mapper.UserMapper;
import com.bjpowernode.p2p.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 */
@Service(interfaceClass = UserService.class)
@Component
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public long queryCountAllUsers() {
        return userMapper.selectCountAllUsers();
    }
}
