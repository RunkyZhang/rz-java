package com.ww.common.architecture.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class LocalDateTimeUtils {
    public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static final String yyyy_MM_dd_hh_mm_ss = "yyyy-MM-dd hh:mm:ss";
    public static final String yyyy_MM_dd = "yyyy-MM-dd";
    public static final String yyyy_MM = "yyyy-MM";

    public LocalDateTimeUtils() {
    }

    public static LocalDateTime convertDateToLDT(Date date) {
        return null == date ? null : LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static Date convertLDTToDate(LocalDateTime time) {
        return null == time ? null : Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Long getMilliByTime(LocalDateTime time) {
        return null == time ? null : time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static Long getSecondsByTime(LocalDateTime time) {
        return null == time ? null : time.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }

    public static String formatTime(LocalDateTime time, String pattern) {
        return null == time ? "" : time.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String formatNow(String pattern) {
        return formatTime(LocalDateTime.now(), pattern);
    }

    public static LocalDateTime plus(LocalDateTime time, long number, TemporalUnit field) {
        return time.plus(number, field);
    }

    public static LocalDateTime minu(LocalDateTime time, long number, TemporalUnit field) {
        return time.minus(number, field);
    }

    public static long betweenTwoTime(LocalDateTime startTime, LocalDateTime endTime, ChronoUnit field) {
        Period period = Period.between(LocalDate.from(startTime), LocalDate.from(endTime));
        if (field == ChronoUnit.YEARS) {
            return (long) period.getYears();
        } else {
            return field == ChronoUnit.MONTHS ? (long) (period.getYears() * 12 + period.getMonths()) : field.between(startTime, endTime);
        }
    }

    public static LocalDateTime getDayStart(LocalDateTime time) {
        return time.withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    public static LocalDateTime getDayEnd(LocalDateTime time) {
        return time.withHour(23).withMinute(59).withSecond(59).withNano(99);
    }

    public static String formatOldDate(Date date, String formatType) {
        return convertDateToLDT(date).format(DateTimeFormatter.ofPattern(formatType));
    }

    public static boolean isOverlapping(LocalDate startDate1, LocalDate endDate1, LocalDate startDate2, LocalDate endDate2) {
        // 检查 [startDate1, endDate1] 和 [startDate2, endDate2] 是否重叠
        if (startDate1 != null && startDate2 != null) {
            // 检查 startDate1 是否在 [startDate2, endDate2] 之间
            if ((endDate2 == null || !startDate1.isAfter(endDate2)) && (endDate1 == null || !startDate2.isAfter(endDate1))) {
                return true;
            }
            // 检查 startDate2 是否在 [startDate1, endDate1] 之间
            if ((endDate1 == null || !startDate2.isAfter(endDate1)) && (endDate2 == null || !startDate1.isAfter(endDate2))) {
                return true;
            }
        }
        // 检查 [startDate1, endDate1] 是否完全包含 [startDate2, endDate2]
        if (startDate1 == null || (startDate2 != null && !startDate1.isAfter(startDate2))) {
            if (endDate1 == null || (endDate2 != null && !endDate1.isBefore(endDate2))) {
                return true;
            }
        }
        // 检查 [startDate2, endDate2] 是否完全包含 [startDate1, endDate1]
        if (startDate2 == null || (startDate1 != null && !startDate2.isAfter(startDate1))) {
            if (endDate2 == null || (endDate1 != null && !endDate2.isBefore(endDate1))) {
                return true;
            }
        }
        return false;
    }


    // 获取指定日期到当月最后一天的所有日期
    public static List<LocalDate> getDatesUntilEndOfMonth(LocalDate startDate) {
        if (startDate == null) {
            return new ArrayList<>();
        }

        List<LocalDate> dates = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(startDate);
        LocalDate endDate = yearMonth.atEndOfMonth();

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            dates.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }

        return dates;
    }

    // 根据多个不连续的日期，找出这些日期之间的所有连续区间
    public static List<DateRangeDto> getDateRanges(List<LocalDate> list) {
        List<DateRangeDto> ranges = new ArrayList<>();
        if (list == null || list.isEmpty()) {
            return ranges;
        }

        Collections.sort(list);
        LocalDate startDate = list.get(0);
        LocalDate endDate = list.get(0);

        for (int i = 1; i < list.size(); i++) {
            LocalDate current = list.get(i);
            if (ChronoUnit.DAYS.between(endDate, current) == 1) {
                endDate = current;
            } else {
                DateRangeDto dateRange = new DateRangeDto();
                dateRange.setStartDate(startDate);
                dateRange.setEndDate(endDate);
                ranges.add(dateRange);
                startDate = current;
                endDate = current;
            }
        }
        DateRangeDto dateRange = new DateRangeDto();
        dateRange.setStartDate(startDate);
        dateRange.setEndDate(endDate);
        ranges.add(dateRange);

        return ranges;
    }

    @Data
    @ApiModel(value = "日期区间")
    public static class DateRangeDto {

        @ApiModelProperty(value = "开始日期")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate startDate;

        @ApiModelProperty(value = "结束日期")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate endDate;

    }
}
