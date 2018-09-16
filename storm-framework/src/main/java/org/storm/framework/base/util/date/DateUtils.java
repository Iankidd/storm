/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.storm.framework.base.util.date;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static final long SECOND = 1000L;

    public static final long MINUTE = 60L * SECOND;

    public static final long HOUR = 60L * MINUTE;

    public static final String DATE_PATTERN = "yyyy-MM-dd";

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final long DAY = 24L * HOUR;

    public static final String DAY_FORMAT = "yyyy-MM-dd";

    public static final String HOUR_FORMAT = "yyyy-MM-dd HH";

    public static final String MINUTE_FORMAT = "yyyy-MM-dd HH:mm";

    public static final String MONTH_FORMAT = "yyyy-MM";

    public static final String SECOND_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String MILSECOND_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static final String MS_SECOND_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    public static final long WEEK = 7L * DAY;

    public static final String YEAR_FORMAT = "yyyy";

    public static final String YEAR_MONTH_DAY_FORMAT = "yyyyMMdd";

    /**
     * 转成yyyy-MM-dd HH:mm:ss.SSS时间
     *
     * @param time
     * @return
     */
    public static Date toMsDate(String time) {
        SimpleDateFormat df = new SimpleDateFormat(MS_SECOND_FORMAT);
        try {
            return df.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toMsDateString(Date d) {
        SimpleDateFormat df = new SimpleDateFormat(MS_SECOND_FORMAT);
        return df.format(d);
    }

    /**
     * 判断某个时间time2是否在另一个时间time1之前
     *
     * @param time1
     * @param time2
     * @return
     */
    public static boolean beforeTime(Date time1, Date time2) {
        if (time1.getTime() < time2.getTime()) {
            return true;
        } else {
            return false;
        }
    }

    public static BigDecimal getCurrentTimeAsNumber() {
        String returnStr = null;
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        returnStr = f.format(date);
        return new BigDecimal(returnStr);
    }

    /**
     * 根据年月取每月的天数
     *
     * @param year
     * @param month
     * @return
     */
    public static int getYearMonthDays(int year, int month) {
        int days = 31;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                days = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                days = 30;
                break;
            case 2:
                if (year % 4 == 0 || year % 100 == 0 || year % 400 == 0) {
                    days = 29;
                    break;
                }
                days = 28;
                break;
        }
        return days;
    }

    /**
     * 按指定格式将java.util.Date日期转换为字符串 例如:2009-10-01
     *
     * @param date
     * @return
     */
    public static String getDate(Date date) {
        return getDateTime(DATE_PATTERN, date);
    }

    /**
     * 按指定格式将java.util.Date日期转换为字符串 例如:2009-10-01
     *
     * @param date
     * @return
     */
    public static String getDateMonth(Date date) {
        return getDateTime(MONTH_FORMAT, date);
    }

    /**
     * 按指定格式将java.util.Date日期转换为字符串 例如:2009-01-01 15:02:01
     *
     * @param date
     * @return
     */
    public static String getDateTime(Date date) {
        return getDateTime(DATE_TIME_PATTERN, date);
    }

    /**
     * 按给定格式转换java.util.Date日期为字符串
     *
     * @param pattern
     * @param date
     * @return
     */
    public static String getDateTime(String pattern, Date date) {
        if (date == null) {
            return "";
        }
        if (pattern == null) {
            pattern = DATE_TIME_PATTERN;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.getDefault());
        String ret = formatter.format(date);
        return ret;
    }

    /**
     * 获取两个日期之间的天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getDaysBetween(Calendar startDate, Calendar endDate) {
        if (startDate.after(endDate)) {
            Calendar swap = startDate;
            startDate = endDate;
            endDate = swap;
        }
        int days = endDate.get(Calendar.DAY_OF_YEAR) - startDate.get(Calendar.DAY_OF_YEAR);
        int y2 = endDate.get(Calendar.YEAR);
        if (startDate.get(Calendar.YEAR) != y2) {
            startDate = (Calendar) startDate.clone();
            do {
                days += startDate.getActualMaximum(Calendar.DAY_OF_YEAR);
                startDate.add(Calendar.YEAR, 1);
            } while (startDate.get(Calendar.YEAR) != y2);
        }
        return days;
    }

    /**
     * 获取2个时间的秒数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static long getSecondsBetween(Date startDate, Date endDate) {
        long s = startDate.getTime() / 1000;
        long e = endDate.getTime() / 1000;
        long t = e - s;
        return t;
    }

    /**
     * 获取两个日期之间的天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getDaysBetween(Date startDate, Date endDate) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(startDate);
        cal2.setTime(endDate);
        return getDaysBetween(cal1, cal2);
    }

    /**
     * 计算两个日期之间的假期天数（仅仅是正常休息日即周六周日）
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getHolidays(Calendar startDate, Calendar endDate) {
        return getDaysBetween(startDate, endDate) - getWorkingDay(startDate, endDate);
    }

    /**
     * 获得指定日期的下一个星期一的日期
     *
     * @param date
     * @return
     */
    public static Calendar getNextMonday(Calendar date) {
        Calendar result = date;
        do {
            result = (Calendar) result.clone();
            result.add(Calendar.DATE, 1);
        } while (result.get(Calendar.DAY_OF_WEEK) != 2);
        return result;
    }

    public static int getNowYearMonth() {
        String returnStr = null;
        SimpleDateFormat f = new SimpleDateFormat("yyyyMM");
        Date date = new Date();
        returnStr = f.format(date);
        return Integer.parseInt(returnStr);
    }

    public static int getNowYearMonthDay() {
        String returnStr = null;
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        returnStr = f.format(date);
        return Integer.parseInt(returnStr);
    }

    /**
     * 计算两个日期之间的工作天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getWorkingDay(Calendar startDate, Calendar endDate) {
        int result = -1;
        if (startDate.after(endDate)) {
            Calendar swap = startDate;
            startDate = endDate;
            endDate = swap;
        }

        int charge_start_date = 0;// 开始日期的日期偏移量
        int charge_end_date = 0;// 结束日期的日期偏移量
        // 日期不在同一个日期内
        int stmp;
        int etmp;
        stmp = 7 - startDate.get(Calendar.DAY_OF_WEEK);
        etmp = 7 - endDate.get(Calendar.DAY_OF_WEEK);
        if (stmp != 0 && stmp != 6) {// 开始日期为星期六和星期日时偏移量为0
            charge_start_date = stmp - 1;
        }
        if (etmp != 0 && etmp != 6) {// 结束日期为星期六和星期日时偏移量为0
            charge_end_date = etmp - 1;
        }

        result = (getDaysBetween(getNextMonday(startDate), getNextMonday(endDate)) / 7) * 5 + charge_start_date
                - charge_end_date;
        return result;
    }

    /**
     * 计算两个日期之间的工作天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getWorkingDay(Date startDate, Date endDate) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(startDate);
        cal2.setTime(endDate);
        return getWorkingDay(cal1, cal2);
    }

    public static int getYearMonth(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM", Locale.getDefault());
        String ret = formatter.format(date);
        return Integer.parseInt(ret);
    }

    public static int getYearMonthDay(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String ret = formatter.format(date);
        return Integer.parseInt(ret);
    }

    public static void main(String[] args) {
        System.out.println(DateUtils.getDate(new Date()));
        System.out.println(DateUtils.getDateTime(new Date()));
        System.out.println(DateUtils.getYearMonthDay(new Date()));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year, month, day - 7);
        Date dateBefore = calendar.getTime();
        System.out.println("day:" + year + "-" + month + "-" + day);
        System.out.println(DateUtils.getDate(dateBefore));

        calendar.set(year - 1, month, day);
        dateBefore = calendar.getTime();
        System.out.println(DateUtils.getDate(dateBefore));

        calendar.set(year, month - 15, day);
        dateBefore = calendar.getTime();
        System.out.println(">>" + DateUtils.getDate(dateBefore));

        calendar.set(year, month, day - 365);
        dateBefore = calendar.getTime();
        System.out.println(">>>>" + DateUtils.getDate(dateBefore));

        String[] parsePatterns = {DateUtils.DATE_TIME_PATTERN, DateUtils.DATE_PATTERN};
        System.out.println(DateUtils.parseDate("2009-12-25", parsePatterns));
        System.out.println(DateUtils.parseDate("2009-12-25 12:00:05", parsePatterns));
        System.out.println(DateUtils.parseDate("2009-12-25 23:59:59", parsePatterns));
        System.out.println(DateUtils.parseDate("2009-12-25 00:00:00", parsePatterns));
        System.out.println(Locale.getDefault());
        System.out.println(DateUtils.toDate("2009"));
        System.out.println(DateUtils.toDate("2009-12"));
        System.out.println(DateUtils.toDate("2009-12-25"));
        System.out.println(DateUtils.toDate("2009-12-25 13"));
        System.out.println(DateUtils.toDate("2009-12-25 10:45"));
        System.out.println(DateUtils.toDate("2009-12-25 22:45:50"));

        System.out.println(dateDiff(DateUtils.toDate("2013-10-25"), DateUtils.toDate("2013-10-29")));
        Date toDate = DateUtils.getDateAfter(DateUtils.toDate("2013-03-21"), 60);
        long daysDiff = DateUtils.dateDiff(new Date(), toDate);
        System.out.println(daysDiff);
    }

    public static Date parseDate(String str, String[] parsePatterns) {
        if (str == null || parsePatterns == null) {
            throw new IllegalArgumentException("Date and Patterns must not be null");
        }
        SimpleDateFormat parser = null;
        ParsePosition pos = new ParsePosition(0);
        for (int i = 0; i < parsePatterns.length; i++) {
            if (i == 0) {
                parser = new SimpleDateFormat(parsePatterns[0]);
            } else {
                parser.applyPattern(parsePatterns[i]);
            }
            pos.setIndex(0);
            Date date = parser.parse(str, pos);
            if (date != null && pos.getIndex() == str.length()) {
                return date;
            }
        }
        throw new RuntimeException("Unable to parse the date: " + str);
    }

    public static Date toDate(long ts) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ts);
        return calendar.getTime();
    }

    public static Date toDate(String dateString) {
        String[] parsePatterns = new String[1];
        if (dateString.length() == 4) {
            parsePatterns[0] = YEAR_FORMAT;
            return DateUtils.parseDate(dateString, parsePatterns);
        } else if (dateString.length() == 7) {
            parsePatterns[0] = MONTH_FORMAT;
            return DateUtils.parseDate(dateString, parsePatterns);
        } else if (dateString.length() == 10) {
            parsePatterns[0] = DAY_FORMAT;
            return DateUtils.parseDate(dateString, parsePatterns);
        } else if (dateString.length() == 13) {
            parsePatterns[0] = HOUR_FORMAT;
            return DateUtils.parseDate(dateString, parsePatterns);
        } else if (dateString.length() == 16) {
            parsePatterns[0] = MINUTE_FORMAT;
            return DateUtils.parseDate(dateString, parsePatterns);
        } else if (dateString.length() == 19) {
            parsePatterns[0] = SECOND_FORMAT;
            return DateUtils.parseDate(dateString, parsePatterns);
        } else if (dateString.length() == 24) {
            parsePatterns[0] = MILSECOND_FORMAT;
            return DateUtils.parseDate(dateString, parsePatterns);
        }
        throw new RuntimeException("Input is not valid date string: " + dateString);
    }

    public static java.sql.Timestamp toTimestamp(Date date) {
        if (date != null) {
            return new java.sql.Timestamp(date.getTime());
        } else {
            return null;
        }
    }

    public static java.sql.Timestamp toTimestamp(String dateTime) {
        Date newDate = toDate(dateTime);
        if (newDate != null) {
            return new java.sql.Timestamp(newDate.getTime());
        } else {
            return null;
        }
    }

    /**
     * 取两日期差异天数
     *
     * @param fromDate
     * @param toDate
     * @return
     * @author key
     * @date Sep 12, 2013
     */
    public static long dateDiff(Date fromDate, Date toDate) {
        return dateDiff(getDateTime(DATE_PATTERN, fromDate), getDateTime(DATE_PATTERN, toDate));
    }

    public static long dateDiff(String beginDateStr, String endDateStr) {
        long day = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate;
        Date endDate;
        try {
            beginDate = format.parse(beginDateStr);
            endDate = format.parse(endDateStr);
            day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day;
    }

    /**
     * 得到几天前的时间
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateBefore(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();
    }

    /**
     * 得到几天后的时间
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    /**
     * 根据时间字符串返回Date对象
     *
     * @param dateStr,可以接受3种格式分别是:yyyy-MM-dd,yyyy-MM-dd HH:mm,yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Date getDateByStr(String dateStr) {
        SimpleDateFormat formatter = null;
        if (dateStr.length() == 10)
            formatter = new SimpleDateFormat("yyyy-MM-dd");
        else if (dateStr.length() == 16)
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        else if (dateStr.length() == 19)
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        else {
            System.out.println("日期字符串格式错误!");
            return null;
        }
        try {
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 返回日期的字符串
     *
     * @param date   Date对象
     * @param format 例如:yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getStrByDate(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    /**
     * 返回日期的字符串,年-月-日
     *
     * @param date
     * @return yyyy-MM-dd
     */
    public static String getStrYMDByDate(Date date) {
        return getStrByDate(date, "yyyy-MM-dd");
    }

    /**
     * 返回日期的字符串,时:分:秒
     *
     * @param date
     * @return HH:mm:ss
     */
    public static String getStrHMSByDate(Date date) {
        return getStrByDate(date, "HH:mm:ss");
    }

    /**
     * 返回日期的字符串,年-月-日 时:分:秒
     *
     * @param date
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getStrYMDHMSByDate(Date date) {
        return getStrByDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 返回日期的字符串,年月日 时:分
     *
     * @param date
     * @return yyyy-MM-dd HH:mm
     */
    public static String getStrYMDHMByDate(Date date) {
        return getStrByDate(date, "yyyy-MM-dd HH:mm");
    }

    /**
     * 对天数进行加减运算
     *
     * @param date 原来的时间
     * @param days 正数为加,负数为减
     * @return 返回运算后的时间
     */
    public static Date addDay(Date date, Integer days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    /**
     * 对月数进行加减运算
     *
     * @param date   原来的时间
     * @param months 正数为加,负数为减
     * @return 返回运算后的时间
     */
    public static Date addMonth(Date date, Integer months) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, months);
        return calendar.getTime();
    }

    /**
     * 对秒数进行加减运算
     *
     * @param date    原来的时间
     * @param seconds 正数为加,负数为减
     * @return 返回运算后的时间
     */
    public static Date addSecond(Date date, Integer seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }

    /**
     * 返回中文时间格式
     *
     * @param object 可以为Date对象或2007-06-12格式的字符串
     * @return
     */
    public static String toChinese(Object object) {
        String dateStr = null;
        if (object instanceof Date)
            dateStr = getStrYMDByDate((Date) object);
        else if (object instanceof String)
            dateStr = (String) object;
        else
            return dateStr;
        String[] cnArray = {"〇", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        String year = dateStr.split("-")[0];
        String month = dateStr.split("-")[1];
        String date = dateStr.split("-")[2];
        dateStr = "";
        for (int i = 0; i < year.length(); i++)
            dateStr += cnArray[Integer.valueOf(String.valueOf(year.charAt(i)))];
        dateStr += "年";
        if ("10".equals(month))
            dateStr += "十";
        else {
            int num = Integer.valueOf(String.valueOf(month.charAt(1)));
            if ("0".equals(String.valueOf(month.charAt(0))))
                dateStr += cnArray[num];
            else
                dateStr += "十" + cnArray[num];
        }
        dateStr += "月";
        if ("10".equals(date))
            dateStr += "十";
        else {
            String temp = String.valueOf(date.charAt(0));
            if ("1".equals(temp))
                dateStr += "十";
            else if ("2".equals(temp))
                dateStr += "二十";
            else if ("3".equals(temp))
                dateStr += "三十";
            if (!"0".equals(String.valueOf(date.charAt(1))))
                dateStr += cnArray[Integer.valueOf(String.valueOf(date.charAt(1)))];
        }
        dateStr += "日";
        return dateStr;
    }

    /**
     * 返回星期几
     *
     * @param object Date对象或者字符串,yyyy-MM-dd
     * @return 星期五
     */
    @SuppressWarnings("deprecation")
    public static String getWeek(Object object) {
        Date date = null;
        if (object instanceof Date)
            date = (Date) object;
        else if (object instanceof String)
            date = getDateByStr((String) object);
        else
            return "";
        String[] cnWeek = {"日", "一", "二", "三", "四", "五", "六"};
        return "星期" + cnWeek[date.getDay()];
    }

    public static Date get00_00_00Date(Date date) {
        return getDateByStr(getStrYMDByDate(date));
    }

    public static Date get23_59_59Date(Date date) {
        return getDateByStr(getStrYMDHMSByDate(date).substring(0, 10) + " 23:59:59");
    }

    public static Date getDateFirstDayOfMonth(Date date) {
        Date d1 = new Date(date.getYear(), date.getMonth(), 0);
        return getDateByStr(getStrYMDHMSByDate(date).substring(0, 8) + "01 00:00:00");
    }

    public static Date getDateLastDayOfMonth(Date date) {
        Date d1 = new Date(date.getYear(), date.getMonth(), 0);
        return getDateByStr(getStrYMDHMSByDate(date).substring(0, 8) + d1.getDate() + " 23:59:59");
    }

    public static Date getDateFirstDayOfYear(Date date) {
        return getDateByStr(getStrYMDHMSByDate(date).substring(0, 4) + "-01-01 00:00:00");
    }

    public static Date getDateLastDayOfYear(Date date) {
        return getDateByStr(getStrYMDHMSByDate(date).substring(0, 4) + "-12-31 23:59:59");
    }

    public static Integer changeSecond(String hms) {
        if (hms == null || "".equals(hms)) {
            return null;
        }

        String[] t = hms.split(":");
        int hour = Integer.valueOf("0" + t[0]);
        int min = Integer.valueOf("0" + t[1]);
        int sec = Integer.valueOf("0" + t[2]);

        return hour * 3600 + min * 60 + sec;
    }

    /**
     * 返回多少时间前
     *
     * @param date
     * @return
     */
    public static String getPreTime(Date date) {
        long time = date.getTime();
        Long result = 0L;
        result = (new Date().getTime() - time);
        result = result / 1000L;
        if (result < 60L) {
            return result + "秒前";
        }
        if (result >= 60L && result < 3600L) {
            result = result / 60;
            return result + "分钟前";
        }
        if (result >= 3600L && result < 86400L) {
            result = result / 3600;
            return result + "小时前";
        } else {
            result = result / 3600 / 24;
            return result + "天前";
        }
    }

    /**
     * 取传入日期后的第一个星期一(包括传入的日期)
     *
     * @param startTime
     * @return
     */
    public static Date getFirstMondayAfter(Date startTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startTime);
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            cal.add(Calendar.DATE, 1);
        }
        return cal.getTime();
    }

    /*
     * 毫秒转化时分秒毫秒
     */
    public static String formatTimeSec(Long sec) {
        Long ms = sec * 1000;
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        String strTime = "";
        if (day > 0) {
            strTime += day + "天";
        }
        if (hour > 0 || (day > 0 && (minute > 0 || second > 0))) {
            strTime += hour + "小时";
        }
        if (minute > 0 || (hour > 0 || day > 0 && second > 0)) {
            strTime += minute + "分";
        }
        if (second > 0) {
            strTime += second + "秒";
        }
        if (strTime.length() <= 0) {
            strTime = "0秒";
        }
        return strTime;
    }

    /**
     * 获取上个月第一天
     *
     * @return
     */
    public static Date getLastMonthFirstDay() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    /**
     * 获取上个月最后一天
     *
     * @return
     */
    public static Date getLastMonthLastDay() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 0);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return c.getTime();
    }

    private DateUtils() {

    }

}