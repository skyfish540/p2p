package com.bjpowernode.p2p.service;

import com.bjpowernode.p2p.model.BidInfo;
import com.bjpowernode.p2p.model.LoanInfo;
import com.bjpowernode.p2p.model.User;

import java.util.List;
import java.util.Map;

public interface BidService {
    Double queryAllBidMoney();

    List<BidInfo> queryBidInfoById(Integer pid);

    List<BidInfo> queryBidInfoByUid(Map<String, Object> map);

    long queryAllBidInfoByUid(Map<String, Object> map);

    void doBidInfoRecord(Double bidMoney, User user, LoanInfo loanInfo);
}
