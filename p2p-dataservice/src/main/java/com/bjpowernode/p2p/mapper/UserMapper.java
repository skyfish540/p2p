package com.bjpowernode.p2p.mapper;

import com.bjpowernode.p2p.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    long selectCountAllUsers();

    User selectUserByPhone(String phone);

    int insertUser(User user);

    User selectUserByPhoneAndLoginPassword(Map<String,Object> map);

    int updateUserRealName(User user);
}