package com.bjpowernode.p2p.commons;

/**
 *
 */
public class Constants {
    //账户初始化可用金额
    public static final double INIT_ACCOUNT_MONEY=888.0;

    //url
    public static final String REAL_NAME_VERIFY_URL="http://localhost:8082/p2p-outapi/outapi/realNameVerify";
    //apiKey
    public static final String REAL_NAME_VERIFY_APIKEY="we46g865lnnjk0945njkrt98fso9";
    //投资状态
    public static final Integer BID_STATUS_SUCCESS=1;
    public static final Integer BID_STATUS_FAIL=0;
    //产品状态
    public static final Integer PRODUCT_STATUS_NO=0;
    public static final Integer PRODUCT_STATUS_OK=1;
    public static final Integer PRODUCT_STATUS_PLAN=2;
    //redis中年化收益率key
    public static final String AVG_HISTORY_RATE="avgHistroyRate";
    //产品的类型
    public static final Integer LOAN_TYPE_XINSHOUBAO=0;
    public static final Integer LOAN_TYPE_YOUXUAN=1;
    public static final Integer LOAN_TYPE_SANBIAO=2;
    //收益状态
    public static final Integer INCOME_STATUS_NO=0;
    public static final Integer INCOME_STATUS_OK=1;





}
