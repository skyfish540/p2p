package com.bjpowernode.p2p.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bjpowernode.p2p.commons.Constants;
import com.bjpowernode.p2p.mapper.LoanInfoMapper;
import com.bjpowernode.p2p.model.LoanInfo;
import com.bjpowernode.p2p.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@Service
public class LoanServiceImpl implements LoanService {
    @Autowired
    private LoanInfoMapper loanInfoMapper;
    @Resource
    private RedisTemplate redisTemplate;


    @Override
    public List<LoanInfo> queryLoanInfoByType(Map<String, Integer> map) {
        return loanInfoMapper.selectLoanInfoByType(map);
    }

    @Override
    public long queryLoanAll(Integer productType) {

        return loanInfoMapper.selectLoanInfoAll(productType);
    }

    @Override
    public double queryHistoryRate() {
        //数据序列化（如果不是类类型的数据，可以不用序列化）
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //第一次查询时从数据库中获取，把属于保存到redis中
        //以后再次查询时直接从redis中获取
        Double avgHistoryRate= (Double) redisTemplate.opsForValue().get(Constants.AVG_HISTORY_RATE);
        if (avgHistoryRate==null){
            //从数据库里查询
            avgHistoryRate=loanInfoMapper.selectAvgHistoryRate();
            redisTemplate.opsForValue().set(Constants.AVG_HISTORY_RATE,avgHistoryRate,15, TimeUnit.MINUTES);
            System.out.println("***从数据库中取数据***");
        }else {
            System.out.println("===从redis中取数据===");
        }

        return avgHistoryRate;
    }

    @Override
    public LoanInfo queryLoanInfoById(Integer pid) {

        return loanInfoMapper.selectLoanInfoById(pid);
    }
}
