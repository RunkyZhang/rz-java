package com.ww.common.base;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtility {
    /*
     * yyyy-MM-dd
     */
    public final static String DATE_FORMAT1 = "yyyy-MM-dd";
    /*
     * yyyy-MM-dd HH:mm:ss
     */
    public final static String DATE_FORMAT2 = "yyyy-MM-dd HH:mm:ss";
    /*
     * yyyy-MM-dd HH:mm:ss SSS
     */
    public final static String DATE_FORMAT3 = "yyyy-MM-dd HH:mm:ss SSS";

    /*
     * yyyyMM
     */
    public final static String DATE_FORMAT4 = "yyyyMMdd";

    public static Date toDate(int year, int month, int day) {
        return DateTimeUtility.toDate(year, month, day, 0, 0, 0, 0);
    }

    public static Date toDate(int year, int month, int day, int hour, int minute, int second, int millisecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hour, minute, second);
        calendar.set(Calendar.MILLISECOND, millisecond);

        return calendar.getTime();
    }

    public static Date parse(String dateString) throws ParseException {
        return DateTimeUtility.parse(dateString, DateTimeUtility.DATE_FORMAT1);
    }

    public static Date parse(String dateString, String dateFormat) throws ParseException {
        return new SimpleDateFormat(dateFormat).parse(dateString);
    }

    public static String toString(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

        return simpleDateFormat.format(date);
    }

    public static Date toYear(Date date) {
        return DateTimeUtility.toDate(DateTimeUtility.getYear(date), 1, 1);
    }

    public static Date toMonth(Date date) {
        return DateTimeUtility.toDate(DateTimeUtility.getYear(date), DateTimeUtility.getMonth(date), 1);
    }

    public static Date toDay(Date date) {
        return DateTimeUtility.toDate(DateTimeUtility.getYear(date), DateTimeUtility.getMonth(date), DateTimeUtility.getDay(date));
    }

    public static Date getNow() {
        return new Date();
    }

    public static long compare(Date date1, Date date2) {
        return date1.getTime() - date2.getTime();
    }

    public static Date getToday() {
        Date now = DateTimeUtility.getNow();

        return DateTimeUtility.toDate(DateTimeUtility.getYear(now), DateTimeUtility.getMonth(now), DateTimeUtility.getDay(now));
    }

    public static Date getMinDate() {
        return DateTimeUtility.toDate(1, 1, 1);
    }

    public static Date getMaxDate() {
        return DateTimeUtility.toDate(9999, 12, 31, 23, 59, 59, 999);
    }

    public static double totalDays(Date date1, Date date2) {
        double difference = date1.getTime() - date2.getTime();

        return difference / (1000 * 60 * 60 * 24);
    }

    public static double totalHours(Date date1, Date date2) {
        double difference = date1.getTime() - date2.getTime();

        return difference / (1000 * 60 * 60);
    }

    public static double totalMinutes(Date date1, Date date2) {
        double difference = date1.getTime() - date2.getTime();

        return difference / (1000 * 60);
    }

    public static double totalSeconds(Date date1, Date date2) {
        double difference = date1.getTime() - date2.getTime();

        return difference / 1000;
    }

    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.YEAR);
    }

    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.DATE);
    }

    public static int getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.MINUTE);
    }

    public static int getSecond(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.SECOND);
    }

    public static int getMillisecond(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.MILLISECOND);
    }

    public static Date addYear(Date date, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, year);

        return calendar.getTime();
    }

    public static Date addMonth(Date date, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);

        return calendar.getTime();
    }

    public static Date addDay(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);

        return calendar.getTime();
    }

    public static Date addHour(Date date, int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, hour);

        return calendar.getTime();
    }

    public static Date addMinute(Date date, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minute);

        return calendar.getTime();
    }

    public static Date addSecond(Date date, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, second);

        return calendar.getTime();
    }

    public static Date addMillisecond(Date date, int millisecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MILLISECOND, millisecond);

        return calendar.getTime();
    }

    public static int dayOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    public static int dayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static Date firstDayOfWeek(Date date) {
        date = DateTimeUtility.toDay(date);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());

        return DateTimeUtility.addDay(calendar.getTime(), 1);
    }

    public static Date lastTimeOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 将时间设置为当天最后一秒
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * 获取上个月最后一天
     * @param now
     * @param minusCount 0上月最后一天，1上上月
     * @return
     */
    public static LocalDateTime lastDayOfPreviousMonth(LocalDateTime now, int minusCount) {
        Month previousMonth = LocalDateTime.now().minusMonths(minusCount).getMonth();
        // 获取上上个月的最后一天
        return now.withMonth(previousMonth.getValue())
                // 将日期设为1号
                .withDayOfMonth(1)
                // 减去一天，得到上个月的最后一天
                .minusDays(1)
                // 设置时钟为最后一小时
                .withHour(23)
                // 设置分钟为最后一分钟
                .withMinute(59)
                // 设置秒钟为最后一秒
                .withSecond(59);
    }

    /**
     * 将LocalDateTime转换为Date
     */
    public static Date convert2Date(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
