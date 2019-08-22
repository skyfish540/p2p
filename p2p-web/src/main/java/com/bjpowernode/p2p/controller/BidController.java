package com.bjpowernode.p2p.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bjpowernode.p2p.commons.Constants;
import com.bjpowernode.p2p.commons.ReturnObject;
import com.bjpowernode.p2p.model.BidInfo;
import com.bjpowernode.p2p.model.LoanInfo;
import com.bjpowernode.p2p.model.User;
import com.bjpowernode.p2p.service.BidService;
import com.bjpowernode.p2p.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Controller
@RequestMapping("/bid")
public class BidController {
    @Reference
    private BidService bidService;
    @Reference
    private LoanService loanService;

    @RequestMapping("/myBid")
    public String BidRecord(@RequestParam(name = "pageNo",required = false) Integer pageNo,
                            Model model,
                            HttpSession session){
        User user= (User) session.getAttribute(Constants.SESSION_USER);
        Map<String,Object> map =new HashMap<>();
        if (pageNo==null){
            pageNo=1;
        }
        map.put("uid", user.getId());
        //计算startRow limit start pageSize;
        //start:就是当前页的起始索引,pageSize就是每页的条数,currentPage:就是当前页
        Integer startRow=(pageNo-1)*Constants.LIST_PAGE_SIZE;
        map.put("startRow", startRow);
        map.put("pageSize", Constants.LIST_PAGE_SIZE);
        //查询投资记录
        List<BidInfo> bidInfoList=bidService.queryBidInfoByUid(map);
        //查询总条数
        long totalRows=bidService.queryAllBidInfoByUid(map);
        //计算总页数
        long totalPages=1;
        if (totalRows%Constants.LIST_PAGE_SIZE==0){
           totalPages= totalRows/Constants.LIST_PAGE_SIZE;
        }else {
            totalPages= totalRows/Constants.LIST_PAGE_SIZE+1;
        }
        //把数据保存到model中,返回给页面
        model.addAttribute("bidInfoList",bidInfoList);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("totalRows",totalRows);
        model.addAttribute("totalPages",totalPages);
        return "myBid";
    }

    @RequestMapping("/invest")
    public @ResponseBody Object invest(@RequestParam("bidMoney")Double bidMoney,
                                       @RequestParam("loanId")Integer loanId,
                                       HttpSession session){
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        ReturnObject returnObject = new ReturnObject();
        //再次验证投资金额,先查询出该产品信息
        LoanInfo loanInfo =loanService.queryLoanInfoById(loanId);
        if (bidMoney==null){
            returnObject.setCode(Constants.ERROR_CODE);
            returnObject.setMessage("请输入投资金额");
            return returnObject;
        }
        if (bidMoney<=0){
            returnObject.setCode(Constants.ERROR_CODE);
            returnObject.setMessage("投资金额必须大于0");
            return returnObject;
        }
        if (bidMoney%100!=0){
            returnObject.setCode(Constants.ERROR_CODE);
            returnObject.setMessage("投资金额应为100的整数倍");
            return returnObject;
        }
        if (bidMoney<loanInfo.getBidMinLimit()){
            returnObject.setCode(Constants.ERROR_CODE);
            returnObject.setMessage("投资金额不能低于产品起投金额");
            return returnObject;
        }
        if (bidMoney>loanInfo.getBidMaxLimit()){
            returnObject.setCode(Constants.ERROR_CODE);
            returnObject.setMessage("投资金额不能超过最大投资限额");
            return returnObject;
        }

        if (bidMoney>user.getFinanceAccount().getAvailableMoney()){
            returnObject.setCode(Constants.ERROR_CODE);
            returnObject.setMessage("账户余额不足，请充值");
            return returnObject;
        }
        //添加投资记录
        try {
            bidService.doBidInfoRecord(bidMoney, user, loanInfo);
            //更新session中的用户账户信息
            double availableMoney=user.getFinanceAccount().getAvailableMoney()-bidMoney;
            user.getFinanceAccount().setAvailableMoney(availableMoney);
            returnObject.setCode(Constants.SUCCESS_CODE);
            returnObject.setMessage("投资成功");
            return returnObject;
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Constants.ERROR_CODE);
            returnObject.setMessage("投资失败");
            return returnObject;
        }
    }
}
