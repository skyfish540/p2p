package com.bjpowernode.p2p.mapper;

import com.bjpowernode.p2p.model.LoanInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface LoanInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LoanInfo record);

    int insertSelective(LoanInfo record);

    LoanInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LoanInfo record);

    int updateByPrimaryKeyWithBLOBs(LoanInfo record);

    int updateByPrimaryKey(LoanInfo record);

    List<LoanInfo> selectLoanInfoByType(Map<String, Integer> map);

    List<LoanInfo> selectLoanInfoAll(Integer productType);

    double selectAvgHistoryRate();

}