package com.bcx.wind.workflow.helper;

import org.junit.Test;

import java.util.Date;
import java.util.List;

public class TimeHelperTest {

    private String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Test
    public void getNow(){
        //当前时间 ****-**-** **:**:**
        System.out.println(TimeHelper.getNow());

        //当前时间 ****-**-**
        System.out.println(TimeHelper.getNowTime());

        //获取时间
        TimeHelper.getTime(new Date().getTime(),TIME_FORMAT);

        //两个日期之间的天数
        List<String> betweenDays = TimeHelper.getBetweenDays("2018-02-03 12:31:31","2018-03-02 23:31:32",TIME_FORMAT);
        System.out.println(betweenDays.size());

        //获得指定日期为周几
        int week = TimeHelper.getWeek("2019-02-15 09:34:21",TIME_FORMAT);
        System.out.println(week);

        //获得当前日期是当月的第几天
        System.out.println(TimeHelper.getNowDayOfMonth());

        //获得当前月份为第几月
        System.out.println(TimeHelper.getNowMonth());

        //获取当前日期为周几，中文
        System.out.println(TimeHelper.getNowWeekSimp());

        //获得当前年份
        System.out.println(TimeHelper.getNowYear());

        //获得当前日期为周几
        System.out.println(TimeHelper.getNowWeek());

        //在指定日期后添加时间毫秒，计算添加后的时间
        System.out.println(TimeHelper.addLongTime("2019-02-15 12:21:31",1213414,TIME_FORMAT));

        //获得两个日期之间的秒数
        System.out.println(TimeHelper.betweenSeconds("2019-02-15 09:21:13","2019-02-15 09:22:15",TIME_FORMAT));

        //获得两个日期之间的工作日
        List<String> workDays = TimeHelper.betweenWorkDays("2019-02-15 09:12:31","2019-02-20 09:12:31",TIME_FORMAT);
        System.out.println(workDays.size());

    }


}
