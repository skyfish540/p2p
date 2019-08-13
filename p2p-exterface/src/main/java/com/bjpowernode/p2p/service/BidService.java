package com.bjpowernode.p2p.service;

import com.bjpowernode.p2p.model.BidInfo;

import java.util.List;

public interface BidService {
    double queryAllBidMoney();

    List<BidInfo> queryBidInfoById(Integer pid);
}
