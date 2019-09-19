package com.bjpowernode.p2p.utils;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 */
public class DateUtils {

    //返回指定时间加上days天之后的日期
    public static Date getDateFromDateAddDays(Date date,int days){
        Calendar calendar =Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        Date calendarTime = calendar.getTime();
        return calendarTime;
    }

    //返回指定时间加上mouths月之后的日期
    public static Date getDateFromDateByAddMouths(Date date,int mouths){
        Calendar cal=Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, mouths);
        return  cal.getTime();
    }
    //返回指定时间所在年份的总天数
    public static int getDaysByTheDayOfYear(Date date){
        Calendar cal=Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int daysOfYear;
        if (year%4==0 && year%100!=0){
            daysOfYear=366;
        }else {
            daysOfYear=365;
        }
        return daysOfYear;
    }
    //返回两个时间点之间的天数
    public static int getDaysBetweenTwoDays(Date startDate,Date endDate){

        long ms=(endDate.getTime()-startDate.getTime());
        return (int) (ms/1000/60/60/24);
    }

    @Test
    public void testDate() throws ParseException {

        Date date = DateUtils.getDateFromDateAddDays(new Date(), 7);
        SimpleDateFormat simp=new SimpleDateFormat("yyyy-MM-dd");//"yyyy-MM-dd HH:mm:ss"
        System.out.println("指定时间加上days天之后的日期="+simp.format(date));

        System.out.println("指定时间加上mouths月之后的日期:"+simp.format(DateUtils.getDateFromDateByAddMouths(new Date(),1)));

        System.out.println("定时间所在年份的总天数="+DateUtils.getDaysByTheDayOfYear(simp.parse("2018-8-19")));
        Date d=new Date();
        System.out.println("距离1970年1月1日0点0分0秒的毫秒数="+d.getTime());
        int days = DateUtils.getDaysBetweenTwoDays(simp.parse("2019-8-18"), simp.parse("2019-9-1"));
        System.out.println("两个日期之间相差的天数="+days);
    }
}
