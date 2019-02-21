package com.bcx.wind.workflow.helper;

import java.text.SimpleDateFormat;
import java.util.*;

public class TimeHelper {

    private static final String DATE_STR = "yyyy-MM-dd";
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final long DAY_TIME = 86400000L;

    public TimeHelper() {
    }

    public static String getNowTime() {
        return FORMAT.format(new Date());
    }

    public static String getNow() {
        return getNowTime("yyyy-MM-dd HH:mm:ss");
    }

    public static String getNowTime(String format) {
        return (new SimpleDateFormat(format)).format(new Date());
    }

    public static long betweenSeconds(String date1, String date2, String... format) {
        try {
            Date start = getFormat(format).parse(date1);
            Date end = getFormat(format).parse(date2);
            return (end.getTime() - start.getTime()) / 1000L;
        } catch (Exception var5) {

            return 0L;
        }
    }

    public static long betweenDays(String date1, String date2, String... format) {
        long seconds = betweenSeconds(date1, date2, format);
        return seconds == 0L ? 0L : seconds / 60L / 60L / 24L + 1L;
    }

    private static boolean isNull(String... objs) {
        return objs == null || objs.length == 0;
    }

    private static SimpleDateFormat getFormat(String... format) {
        return isNull(format) ? FORMAT : new SimpleDateFormat(format[0]);
    }

    public static List<String> betweenWorkDays(String date1, String date2, String... format) {
        List<String> days = getBetweenDays(date1, date2, format);
        List<String> workDays = new ArrayList();
        Iterator var5 = days.iterator();

        while(var5.hasNext()) {
            String day = (String)var5.next();
            int week = getWeek(day);
            if (week != 0 && week != 6) {
                workDays.add(day);
            }
        }

        return workDays;
    }

    public static List<String> getBetweenDays(String date1, String date2, String... format) {
        ArrayList dates = new ArrayList();

        try {
            long d1 = getFormat(format).parse(date1).getTime();

            for(long d2 = getFormat(format).parse(date2).getTime(); d2 >= d1; d1 += 86400000L) {
                String time = getFormat(format).format(d1);
                dates.add(time);
            }

            return dates;
        } catch (Exception var9) {
            var9.printStackTrace();
            return dates;
        }
    }

    public static int getWeek(String date, String... format) {
        return searchTime(date, 7, format) - 1;
    }

    private static int searchTime(String date, Integer search, String... format) {
        try {
            Date d = getFormat(format).parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(d);
            return calendar.get(search);
        } catch (Exception var5) {

            return -1;
        }
    }

    public static int getNowWeek() {
        return searchTime(getNowTime(), 7) - 1;
    }

    public static String getNowWeekSimp() {
        return (new SimpleDateFormat("E")).format(new Date());
    }

    public static int getNowDayOfMonth() {
        return searchTime(getNowTime(), 5);
    }

    public static int getNowMonth() {
        return searchTime(getNowTime(), 2) + 1;
    }

    public static int getNowYear() {
        return searchTime(getNowTime(), 1);
    }

    public static String addLongTime(String now, long milliSecond, String... format) {
        try {
            Date date = getFormat(format).parse(now);
            long time = date.getTime() + milliSecond;
            return getTime(time, format);
        } catch (Exception var7) {

            return null;
        }
    }

    public static String getTime(long time, String... format) {
        Date date = new Date(time);
        return getFormat(format).format(date);
    }

    public static void main(String[] args) throws Exception {
        String ss = addLongTime("2018-08-23 14:44:32", 3600000L, "yyyy-mm-dd HH:mm:ss");
        getTime(System.currentTimeMillis());
        System.out.println();
    }

    public static long getSecond(int time, int type) {
        switch(type) {
            case 1:
                return (long)time;
            case 2:
                return (long)(60 * time);
            case 3:
                return (long)(3600 * time);
            case 4:
                return (long)(86400 * time);
            default:
                return 0L;
        }
    }
}
