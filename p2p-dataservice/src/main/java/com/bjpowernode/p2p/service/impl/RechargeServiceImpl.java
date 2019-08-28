package com.bjpowernode.p2p.service.impl;

import com.bjpowernode.p2p.commons.Constants;
import com.bjpowernode.p2p.commons.UUIDUtils;
import com.bjpowernode.p2p.mapper.RechargeRecordMapper;
import com.bjpowernode.p2p.model.RechargeRecord;
import com.bjpowernode.p2p.service.RechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    public void saveRechargeRecord(Integer uid, double alipayMoney) {
        RechargeRecord rechargeRecord =new RechargeRecord();
        rechargeRecord.setUid(uid);
        rechargeRecord.setRechargeNo(UUIDUtils.getUUID());
        rechargeRecord.setRechargeMoney(alipayMoney);
        rechargeRecord.setRechargeStatus(Constants.RECHARGE_STATUS_ING);
        rechargeRecord.setRechargeTime(new Date());
        rechargeRecord.setRechargeDesc("支付宝充值");

    }
}
