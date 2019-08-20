package com.bjpowernode.p2p.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bjpowernode.p2p.service.RechargeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 */
@Controller
@RequestMapping("/recharge")
public class RecharegeController {
    @Reference
    private RechargeService rechargeService;

    @RequestMapping("/myRecharge")
    public String rechargeRecord(){


        return "myRecharge";
    }

}
