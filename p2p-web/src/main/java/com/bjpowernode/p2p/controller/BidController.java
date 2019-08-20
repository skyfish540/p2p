package com.bjpowernode.p2p.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bjpowernode.p2p.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 */
@Controller
@RequestMapping("/bid")
public class BidController {
    @Reference
    private BidService bidService;

    @RequestMapping("/myBid")
    public String BidRecord(){

        return "myBid";
    }
}
