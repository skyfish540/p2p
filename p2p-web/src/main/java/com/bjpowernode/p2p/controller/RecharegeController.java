package com.bjpowernode.p2p.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bjpowernode.p2p.commons.Constants;
import com.bjpowernode.p2p.model.RechargeRecord;
import com.bjpowernode.p2p.model.User;
import com.bjpowernode.p2p.service.RechargeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Controller
@RequestMapping("/recharge")
public class RecharegeController {
    @Reference
    private RechargeService rechargeService;

    @RequestMapping("/myRecharge")
    public String rechargeRecord(@RequestParam(name = "pageNo",required = false) Integer pageNo,
                                 HttpSession session,
                                 Model model){
        User user= (User) session.getAttribute(Constants.SESSION_USER);
        //计算startRow
        if (pageNo==null){
            pageNo=1;
        }
        //startRow=(pageNo-1)*pageSize
        Integer startRow=(pageNo-1)*Constants.LIST_PAGE_SIZE;
        //将变量保存到map中
        Map<String,Object> map = new HashMap();
        map.put("uid", user.getId());
        map.put("pageNo", pageNo);
        map.put("pageSize", Constants.LIST_PAGE_SIZE);
        map.put("startRow", startRow);

        //查询所有充值记录
        List<RechargeRecord> rechargeRecordList=rechargeService.queryRechargeByUid(map);

        //查询总条数
        long totalRows=rechargeService.queryRechargeCountByUid(user.getId());

        //计算总页数
        long totalPages=1;
        if (totalRows%totalPages==0){
            totalPages=totalRows/Constants.LIST_PAGE_SIZE;
        }else {
            totalPages=totalRows/Constants.LIST_PAGE_SIZE+1;
        }
        //把数据保存到model中,返回给页面
        model.addAttribute("rechargeRecordList",rechargeRecordList);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("totalRows",totalRows);
        model.addAttribute("totalPages",totalPages);

        return "myRecharge";
    }

}
