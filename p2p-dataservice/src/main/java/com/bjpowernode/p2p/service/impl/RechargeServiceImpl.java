package com.bjpowernode.p2p.service.impl;

import com.bjpowernode.p2p.mapper.RechargeRecordMapper;
import com.bjpowernode.p2p.model.RechargeRecord;
import com.bjpowernode.p2p.service.RechargeService;
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
public class RechargeServiceImpl implements RechargeService {
    @Autowired
    private RechargeRecordMapper rechargeRecordMapper;
    @Override
    public List<RechargeRecord> queryRechargeByUid(Map<String, Object> map) {
        return rechargeRecordMapper.selectRechargeByUid(map);
    }

    @Override
    public long queryRechargeCountByUid(Integer id) {
        return rechargeRecordMapper.selectRechargeCountByUid(id);
    }

    @Override
    public int saveRechargeRecord(RechargeRecord rechargeRecord) {

         return rechargeRecordMapper.insert(rechargeRecord);
    }
}
