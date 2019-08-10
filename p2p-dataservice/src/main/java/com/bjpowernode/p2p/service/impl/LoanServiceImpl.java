package com.bjpowernode.p2p.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.bjpowernode.p2p.mapper.LoanInfoMapper;
import com.bjpowernode.p2p.model.LoanInfo;
import com.bjpowernode.p2p.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
/**
 *
 */
@Service(interfaceClass = LoanService.class)
@Component
public class LoanServiceImpl implements LoanService {
    @Autowired
    private LoanInfoMapper loanInfoMapper;

    @Override
    public List<LoanInfo> queryLoanInfoByType(Map<String, Integer> map) {
        return loanInfoMapper.selectLoanInfoByType(map);
    }

    @Override
    public List<LoanInfo> queryLoanAll(Integer productType) {

        return loanInfoMapper.selectLoanInfoAll(productType);
    }


}
