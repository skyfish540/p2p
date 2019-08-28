package com.bjpowernode.p2p.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.support.Parameter;
import com.bjpowernode.p2p.commons.Constants;
import com.bjpowernode.p2p.model.BidInfo;
import com.bjpowernode.p2p.model.LoanInfo;
import com.bjpowernode.p2p.service.BidService;
import com.bjpowernode.p2p.service.LoanService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 */
@Controller
@RequestMapping("/loan")
public class LoanController {
    @Resource
    private LoanService loanService;
    @Resource
    private BidService bidService;

    @RequestMapping("/queryListLoanByTypeForPage")
    public String showLoan(@RequestParam("productType") Integer productType,
                           @RequestParam(name="curPage",required =false) Integer curPage,//required=false表示可选参数
                           Model model){
        /*
          分页查询，后台计算出页码、页数(页大小)
           int curPage = 前端传过来，即当前页。当首次访问网页时，当前页为null;
           int pageSize = 需求指定，即每页要显示的个数;
           int startRow = (curPage - 1) * pageSize;
         */
        Map<String,Integer> map = new HashMap<>();
        map.put("productType",productType);
        map.put("pageSize", Constants.LIST_PAGE_SIZE);
        if (curPage==null){
            curPage=1;
        }
        //计算startRow,
        Integer startRow=(curPage-1)* Constants.LIST_PAGE_SIZE;
        map.put("startRow", startRow);
        //查询产品
        List<LoanInfo> loanInfoList = loanService.queryLoanInfoByType(map);
        //计算总条数
        long totalRows=loanService.queryLoanAll(productType);
        //计算总页数
        long totalPages;
        if (totalRows% Constants.LIST_PAGE_SIZE==0){
            totalPages=totalRows/Constants.LIST_PAGE_SIZE;
        }else {
            totalPages=totalRows/Constants.LIST_PAGE_SIZE+1;
        }
        //把数据都保存到model中
        model.addAttribute("loanInfoList",loanInfoList);
        model.addAttribute("productType",productType);
        model.addAttribute("totalRows",totalRows);
        model.addAttribute("totalPages",totalPages);
        model.addAttribute("curPage",curPage);

        return "loanMore";
    }

    @RequestMapping("/loanInfo")
    public String showLoanDedails(@RequestParam(name = "id",required = false) Integer pid,Model model){

        //查询产品信息(也可以同时连接投资表和用户表），一般情况连表查询不要查过2张
        LoanInfo loanInfo=loanService.queryLoanInfoById(pid);
        model.addAttribute("loanInfo",loanInfo);

        //查询投资信息（需要连表查询，显示投资信息的时候需要显示投资人手机号）
        List<BidInfo> bidInfoList=bidService.queryBidInfoById(pid);
        model.addAttribute("bidInfoList",bidInfoList);

        return "loanDetails";
    }



}
