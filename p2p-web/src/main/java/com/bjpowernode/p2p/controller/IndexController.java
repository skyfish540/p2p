package com.bjpowernode.p2p.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bjpowernode.p2p.commons.Constants;
import com.bjpowernode.p2p.model.LoanInfo;
import com.bjpowernode.p2p.service.BidService;
import com.bjpowernode.p2p.service.LoanService;
import com.bjpowernode.p2p.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Controller
public class IndexController {
    @Resource
    private LoanService loanService;
    @Resource
    private UserService userService;
    @Resource
    private BidService bidService;

    @RequestMapping("/")    //斜杠(/)表示根目录
    public String index(Model model) {
        System.out.println("=========LoanController的index方法========");

        Map<String, Integer> map = new HashMap<>();
        //查询新手宝
        map.put("productType", Constants.LOAN_TYPE_XINSHOUBAO);
        map.put("pageSize", Constants.INDEX_PAGE_XINSHOUBAO);
        map.put("startRow", 0);
        List<LoanInfo> xloanInfoList = loanService.queryLoanInfoByType(map);
        model.addAttribute("xloanInfoList", xloanInfoList);
        //查询优选
        map.put("productType", Constants.LOAN_TYPE_YOUXUAN);
        map.put("pageSize", Constants.INDEX_PAGE_YOUXUAN);
        List<LoanInfo> uloanInfoList =loanService.queryLoanInfoByType(map);
        model.addAttribute("uloanInfoList", uloanInfoList);
        //查询散标
        map.put("productType", Constants.LOAN_TYPE_SANBIAO);
        map.put("pageSize", Constants.INDEX_PAGE_SANBIAO);
        List<LoanInfo> sloanInfoList =loanService.queryLoanInfoByType(map);
        model.addAttribute("sloanInfoList",sloanInfoList);

        //查询历史年化收益率
        double avgHistoryRate=loanService.queryHistoryRate();
        model.addAttribute("avgHistoryRate",avgHistoryRate);

        //查询平台总用户数
        long countAllUsers=userService.queryCountAllUsers();
        model.addAttribute("countAllUsers",countAllUsers);

        //查询历史总投资金额
        Double allBidMoney=bidService.queryAllBidMoney();
        if (allBidMoney==null){
            allBidMoney=0.0;
        }
        model.addAttribute("allBidMoney",allBidMoney);

        return "index";
    }
}
