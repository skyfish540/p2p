package com.bjpowernode.p2p.service;

import com.bjpowernode.p2p.model.BidInfo;

import java.util.List;
import java.util.Map;

public interface BidService {
    double queryAllBidMoney();

    List<BidInfo> queryBidInfoById(Integer pid);

    List<BidInfo> queryBidInfoByUid(Map<String, Object> map);
}
