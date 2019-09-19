package com.bjpowernode.p2p.service.impl;

import com.bjpowernode.p2p.commons.Constants;
import com.bjpowernode.p2p.excepiton.MyException;
import com.bjpowernode.p2p.mapper.BidInfoMapper;
import com.bjpowernode.p2p.mapper.FinanceAccountMapper;
import com.bjpowernode.p2p.mapper.LoanInfoMapper;
import com.bjpowernode.p2p.model.BidInfo;
import com.bjpowernode.p2p.model.LoanInfo;
import com.bjpowernode.p2p.model.User;
import com.bjpowernode.p2p.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
public class BidServiceImpl implements BidService {
    @Autowired
    private BidInfoMapper bidInfoMapper;
    @Autowired
    private FinanceAccountMapper financeAccountMapper;
    @Autowired
    private LoanInfoMapper loanInfoMapper;

    @Override
    public Double queryAllBidMoney() {
        return bidInfoMapper.selectAllBidMoney();

    }

    @Override
    public List<BidInfo> queryBidInfoById(Integer pid)
    {
        return bidInfoMapper.selectBidInfoById(pid);
    }

    @Override
    public List<BidInfo> queryBidInfoByUid(Map<String, Object> map) {

        return bidInfoMapper.selectBidInfoByUid(map);
    }

    @Override
    public long queryAllBidInfoByUid(Map<String, Object> map) {

        return bidInfoMapper.selectAllBidInfoByUid(map);
    }

    @Override
    public void doBidInfoRecord(Double bidMoney, User user, LoanInfo loanInfo) {
        BidInfo bidInfo = new BidInfo();
        bidInfo.setLoanId(loanInfo.getId());
        bidInfo.setUid(user.getId());
        bidInfo.setBidMoney(bidMoney);
        bidInfo.setBidTime(new Date());
        bidInfo.setBidStatus(Constants.BID_STATUS_SUCCESS);
        int insertCount = bidInfoMapper.insert(bidInfo);

        if (insertCount>0){
            //更新账户可用余额
            Map<String,Object> BidMap = new HashMap<>();
            BidMap.put("uid", user.getId());
            BidMap.put("bidMoney", bidMoney);
            int updateAvailMoneyCount = financeAccountMapper.updateAvailMoneyByUidForBid(BidMap);
            if (updateAvailMoneyCount>0){
                //更新产品可投金额
                Map<String,Object>  loanMap = new HashMap<>();
                loanMap.put("id", loanInfo.getId());
                loanMap.put("bidMoney", bidMoney);
                loanMap.put("version",loanInfo.getVersion());
                int updateLoanLeftMoneyCount = loanInfoMapper.updateLoanLeftMoneyByIdForBid(loanMap);
                if (updateLoanLeftMoneyCount>0){
                    // Double.doubleToLongBits将double类型数据转换成long类型数据,可以使double类型数据按照long的方法判断大小
                    long leftProductMoney=Double.doubleToLongBits(loanInfo.getLeftProductMoney());
                    //判断是否满标，如果满标则更新产品状态（0未满标，1已满标，2满标已生成收益计划）
                    if (leftProductMoney==Double.doubleToLongBits(bidMoney)){
                        loanMap.put("productStatus",Constants.PRODUCT_STATUS_OK);
                        int updateProductStatusCount= loanInfoMapper.updateProductStatusById(loanMap);
                        if (updateProductStatusCount==0){
                            throw new MyException();
                        }
                    }
                }else {
                    throw new MyException();
                }

            }else {
                throw  new MyException();
            }
        }else {
            throw  new MyException();
        }
    }
}
