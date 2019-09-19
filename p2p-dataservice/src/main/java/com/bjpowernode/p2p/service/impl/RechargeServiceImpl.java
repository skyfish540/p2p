package com.bjpowernode.p2p.service.impl;

import com.bjpowernode.p2p.excepiton.MyException;
import com.bjpowernode.p2p.mapper.FinanceAccountMapper;
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
public class RechargeServiceImpl implements RechargeService {
    @Autowired
    private RechargeRecordMapper rechargeRecordMapper;
    @Autowired
    private FinanceAccountMapper financeAccountMapper;
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

    @Override
    public RechargeRecord queryRechargeByRechargeNo(String out_trade_no) {
        return rechargeRecordMapper.selectRechargeByRechargeNo(out_trade_no);
    }

    @Override
    public void updateRechargeRecordByRechargeNo(Map<String, Object> map) {
        int updateRechargeRecordCount=rechargeRecordMapper.updateRechargeRecordByRechargeNo(map);
        if (updateRechargeRecordCount>0){
            //更新账户余额
            int updateAvailMoneyCount=financeAccountMapper.updateAvailMoneyByUidForRecharge(map);
            if (updateAvailMoneyCount==0){
                throw  new MyException();
            }
        }else {
            throw new MyException();
        }

    }
}
