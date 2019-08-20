package com.bjpowernode.p2p.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bjpowernode.p2p.service.IncomeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 */
@Controller
@RequestMapping("/income")
public class IncomeController {
    @Reference
    private IncomeService incomeService;

    @RequestMapping("/myIncome")
    public String incomeRecord(){


        return "myIncome";
    }

}
