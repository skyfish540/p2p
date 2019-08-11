package com.bjpowernode.p2p.service;


import com.bjpowernode.p2p.model.LoanInfo;

import java.util.List;
import java.util.Map;

public interface LoanService {

    List<LoanInfo> queryLoanInfoByType(Map<String, Integer> map);

    List<LoanInfo> queryLoanAll(Integer productType);

    double queryHistoryRate();
}
