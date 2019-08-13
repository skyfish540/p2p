package com.bjpowernode.p2p.mapper;

import com.bjpowernode.p2p.model.BidInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BidInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BidInfo record);

    int insertSelective(BidInfo record);

    BidInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BidInfo record);

    int updateByPrimaryKey(BidInfo record);

    double selectAllBidMoney();

    List<BidInfo> selectBidInfoById(Integer pid);
}