package com.bjpowernode.p2p.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.bjpowernode.p2p.mapper.BidInfoMapper;
import com.bjpowernode.p2p.model.BidInfo;
import com.bjpowernode.p2p.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 */
@Service(interfaceClass = BidService.class)
@Component
public class BidServiceImpl implements BidService {
    @Autowired
    private BidInfoMapper bidInfoMapper;

    @Override
    public double queryAllBidMoney() {
        return bidInfoMapper.selectAllBidMoney();

    }

    @Override
    public List<BidInfo> queryBidInfoById(Integer pid) {
        return bidInfoMapper.selectBidInfoById(pid);
    }
}
