package com.bjpowernode.p2p.service;

import com.bjpowernode.p2p.model.IncomeRecord;

import java.util.List;
import java.util.Map;

public interface IncomeService {

    List<IncomeRecord> queryIncomeByUid(Map<String, Object> map);
}
