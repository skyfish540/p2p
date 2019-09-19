package com.bjpowernode.p2p.schedule;

import com.bjpowernode.p2p.service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class IncomeScheduler {

    @Autowired
    @Qualifier("incomeService")
    private IncomeService incomeService;

    //生成收益计划
    @Scheduled(cron = "0/5 * * * * *")
    public void generateIncomePlan(){
        System.out.println("generateIncomePlan开始执行");
        try {
            incomeService.doGenerateIncomePlan();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("生成收益计划失败！");
        }
    }

    //收益计划返还
    @Scheduled(cron = "0/5 * * * * *")
    public void generateIncomeBack(){
        System.out.println("===generateIncomeBack开始执行===");
        try {
            incomeService.doGenerateIncomeBack();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("收益返还失败！");
        }
    }
}
