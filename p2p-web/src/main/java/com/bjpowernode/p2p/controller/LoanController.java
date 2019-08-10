package com.bjpowernode.p2p.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bjpowernode.p2p.model.LoanInfo;
import com.bjpowernode.p2p.service.LoanService;
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
public class LoanController {
    @Reference
    private LoanService loanService;

    @RequestMapping("/index")    //斜杠(/)表示根目录
    public String index(Model model){
        System.out.println("=========LoanController的index方法========");

        Map<String,Integer> map = new HashMap<>();
        //查询新手宝
        map.put("productType",0);
        map.put("pageSize", 1);
        map.put("startRow", 0);
        //List<LoanInfo> xloanInfoList=loanService.queryLoanInfoByType(map);
        List<LoanInfo> xloanInfoList=loanService.queryLoanAll(0);
        System.out.println(xloanInfoList);
        model.addAttribute("xloanInfoList",xloanInfoList);
        return "index";
    }
}
