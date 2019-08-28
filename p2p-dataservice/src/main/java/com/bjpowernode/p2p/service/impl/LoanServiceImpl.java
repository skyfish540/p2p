package com.bjpowernode.p2p.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bjpowernode.p2p.mapper.LoanInfoMapper;
import com.bjpowernode.p2p.model.LoanInfo;
import com.bjpowernode.p2p.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
/**
 *
 */
@Service
@Component
public class LoanServiceImpl implements LoanService {
    @Autowired
    private LoanInfoMapper loanInfoMapper;


    @Override
    public List<LoanInfo> queryLoanInfoByType(Map<String, Integer> map) {
        return loanInfoMapper.selectLoanInfoByType(map);
    }

    @Override
    public long queryLoanAll(Integer productType) {
        return loanInfoMapper.selectLoanInfoAll(productType);
    }

    @Override
    public double queryHistoryRate() {
        return loanInfoMapper.selectAvgHistoryRate();
    }

    @Override
    public LoanInfo queryLoanInfoById(Integer pid) {
        return loanInfoMapper.selectLoanInfoById(pid);
    }
}
