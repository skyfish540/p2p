package com.bjpowernode.p2p.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bjpowernode.p2p.commons.Constants;
import com.bjpowernode.p2p.commons.ReturnObject;
import com.bjpowernode.p2p.model.BidInfo;
import com.bjpowernode.p2p.model.User;
import com.bjpowernode.p2p.service.BidService;
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
                                       @RequestParam("loanId")String loanId,HttpSession session){
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        ReturnObject returnObject = new ReturnObject();

        if (bidMoney>user.getFinanceAccount().getAvailableMoney()){
            returnObject.setCode(Constants.ERROR_CODE);
            returnObject.setMessage("账户余额不足，请充值");
            return returnObject;
        }


        return returnObject;
    }
}
