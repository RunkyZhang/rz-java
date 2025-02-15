package com.ww.common.base.dto.page;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Getter
@Setter
public class OrderItemDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 需要进行排序的字段
     */
    private String column;
    /**
     * 是否正序排列，默认 true
     */
    private boolean asc = true;

    public static OrderItemDto asc(String column) {
        return build(column, true);
    }

    public static OrderItemDto desc(String column) {
        return build(column, false);
    }

    public static List<OrderItemDto> ascs(String... columns) {
        return Arrays.stream(columns).map(OrderItemDto::asc).collect(Collectors.toList());
    }

    public static List<OrderItemDto> descs(String... columns) {
        return Arrays.stream(columns).map(OrderItemDto::desc).collect(Collectors.toList());
    }

    private static OrderItemDto build(String column, boolean asc) {
        return new OrderItemDto().setColumn(column).setAsc(asc);
    }

    public OrderItemDto setColumn(String column) {
        this.column = OrderItemDto.replaceAllBlank(column);
        return this;
    }

    public OrderItemDto setAsc(boolean asc) {
        this.asc = asc;
        return this;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "column='" + column + '\'' +
                ", asc=" + asc +
                '}';
    }

    private static final Pattern REPLACE_BLANK = Pattern.compile("'|\"|\\<|\\>|&|\\*|\\+|=|#|-|;|\\s*|\t|\r|\n");

    public static String replaceAllBlank(String str) {
        Matcher matcher = REPLACE_BLANK.matcher(str);
        return matcher.replaceAll("");
    }
}
