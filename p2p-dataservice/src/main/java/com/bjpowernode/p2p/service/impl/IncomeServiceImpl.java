package com.bjpowernode.p2p.service.impl;

import com.bjpowernode.p2p.commons.Constants;
import com.bjpowernode.p2p.excepiton.MyException;
import com.bjpowernode.p2p.mapper.BidInfoMapper;
import com.bjpowernode.p2p.mapper.FinanceAccountMapper;
import com.bjpowernode.p2p.mapper.IncomeRecordMapper;
import com.bjpowernode.p2p.mapper.LoanInfoMapper;
import com.bjpowernode.p2p.model.BidInfo;
import com.bjpowernode.p2p.model.IncomeRecord;
import com.bjpowernode.p2p.model.LoanInfo;
import com.bjpowernode.p2p.service.IncomeService;
import com.bjpowernode.p2p.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Service("incomeService")
public class IncomeServiceImpl implements IncomeService {
    @Autowired
    private IncomeRecordMapper incomeRecordMapper;
    @Autowired
    private LoanInfoMapper loanInfoMapper;
    @Autowired
    private BidInfoMapper bidInfoMapper;
    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Override
    public List<IncomeRecord> queryIncomeByUid(Map<String, Object> map) {
        return incomeRecordMapper.selectIncomeByUid(map);
    }

    @Override
    public long queryIncomeCountByUid(Integer uid) {

        return incomeRecordMapper.selectIncomeCountByUid(uid);
    }

    @Override
    public void doGenerateIncomePlan() {
        //查询所有满标的产品（product_status=1)
        List<LoanInfo> loanInfoList=loanInfoMapper.queryLoanByStatus(Constants.PRODUCT_STATUS_OK);
        if (loanInfoList!=null){
            //查询该产品下所有投资记录
            for (LoanInfo loanInfo:loanInfoList){
                List<BidInfo> bidInfoList=bidInfoMapper.queryBidByPidForIncome(loanInfo.getId());
                if (bidInfoList!=null){
                    IncomeRecord incomeRecord=null;
                    //生成收益计划
                    for (BidInfo bidInfo:bidInfoList){
                        incomeRecord=new IncomeRecord();
                        incomeRecord.setUid(bidInfo.getUid());
                        incomeRecord.setLoanId(loanInfo.getId());
                        incomeRecord.setBidId(bidInfo.getId());
                        incomeRecord.setBidMoney(bidInfo.getBidMoney());
                        incomeRecord.setIncomeStatus(Constants.INCOME_STATUS_NO);
                        if (loanInfo.getProductType()==Constants.LOAN_TYPE_XINSHOUBAO){
                            //从当前时间+loanInfo.getCycle()天
                            incomeRecord.setIncomeDate(DateUtils.getDateFromDateAddDays(new Date(), loanInfo.getCycle()));
                        }else {
                            //从当前时间+loanInfo.getCycle()月
                            incomeRecord.setIncomeDate(DateUtils.getDateFromDateByAddMouths(new Date(), loanInfo.getCycle()));
                        }
                        //收益金额=投资金额*年化收益率*投资周期（注意，年化率要转化成天收益率）rate/100/365
                        Double incomeMoney;
                        if (loanInfo.getProductType()==Constants.LOAN_TYPE_XINSHOUBAO){
                            incomeMoney=bidInfo.getBidMoney()*loanInfo.getRate()/100/DateUtils.getDaysByTheDayOfYear(new Date())*loanInfo.getCycle();
                        }else {
                            incomeMoney=bidInfo.getBidMoney()*loanInfo.getRate()/100/DateUtils.getDaysByTheDayOfYear(new Date())*DateUtils.getDaysBetweenTwoDays(new Date(), incomeRecord.getIncomeDate());
                        }
                        //四舍五入，保留两位小数（先扩大100倍，在同比例缩小）
                        incomeMoney=Math.round(incomeMoney*Math.pow(10, 2))/Math.pow(10, 2);
                        incomeRecord.setIncomeMoney(incomeMoney);
                        int insertIncomeCount=incomeRecordMapper.insert(incomeRecord);
                        if (insertIncomeCount==0){
                            throw new MyException();
                        }
                    }
                    //更新产品状态为满标已生成收益计划（product_status=2)
                    Map<String,Object> map= new HashMap<>();
                    map.put("id", loanInfo.getId());
                    map.put("productStatus", Constants.PRODUCT_STATUS_PLAN);
                    int updateLoanStatusCount=loanInfoMapper.updateProductStatusById(map);
                    if (updateLoanStatusCount==0){
                        throw new MyException();
                    }
                }
            }
        }
    }

    @Override
    public void doGenerateIncomeBack() {
        //定时扫描收益表里状态为0，且收益已到期的所有产品
        List<IncomeRecord> incomeRecordList=incomeRecordMapper.queryIncomeByStatusAndIncomeDate(Constants.INCOME_STATUS_NO);
        //更新账户余额=账户可用余额+投资金额+收益金额
        if(incomeRecordList!=null){
            for (IncomeRecord incomeRecord:incomeRecordList){
                Map<String,Object> map=new HashMap<>();
                map.put("bidMoney", incomeRecord.getBidMoney());
                map.put("incomeMoney", incomeRecord.getIncomeMoney());
                map.put("uid", incomeRecord.getUid());
                int updateAvailMoneyCount=financeAccountMapper.updateAvailMoneyByUidForIncome(map);
                if (updateAvailMoneyCount>0){
                    //更新收益状态为1，表示收益已返
                    Map<String,Object> incomeMap=new HashMap<>();
                    map.put("incomeStatus", Constants.INCOME_STATUS_OK);
                    map.put("id", incomeRecord.getId());
                    int updateIncomeStatusCount=incomeRecordMapper.updateIncomeStatusById(map);
                    if (updateIncomeStatusCount==0){
                        throw new MyException();
                    }
                }
            }
        }
    }
}
