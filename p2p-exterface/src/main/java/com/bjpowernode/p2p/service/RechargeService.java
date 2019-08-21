package com.bjpowernode.p2p.service;

import com.bjpowernode.p2p.model.RechargeRecord;

import java.util.List;
import java.util.Map;

public interface RechargeService {

    List<RechargeRecord> queryRechargeByUid(Map<String, Object> map);

    long queryRechargeCountByUid(Integer id);
}
