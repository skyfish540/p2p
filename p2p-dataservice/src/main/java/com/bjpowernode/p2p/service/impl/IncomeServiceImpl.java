package com.bjpowernode.p2p.service.impl;

import com.bjpowernode.p2p.mapper.IncomeRecordMapper;
import com.bjpowernode.p2p.model.IncomeRecord;
import com.bjpowernode.p2p.service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
@Component
public class IncomeServiceImpl implements IncomeService {
    @Autowired
    private IncomeRecordMapper incomeRecordMapper;

    @Override
    public List<IncomeRecord> queryIncomeByUid(Map<String, Object> map) {
        return incomeRecordMapper.selectIncomeByUid(map);
    }

    @Override
    public long queryIncomeCountByUid(Integer uid) {
        return incomeRecordMapper.selectIncomeCountByUid(uid);
    }
}
