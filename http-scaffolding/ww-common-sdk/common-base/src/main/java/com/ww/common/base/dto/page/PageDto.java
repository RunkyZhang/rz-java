package com.ww.common.base.dto.page;

import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class PageDto<T> implements IPageDto<T> {
    private static final long serialVersionUID = 1L;

    /**
     * 查询数据列表
     */
    private List<T> records = Collections.emptyList();

    /**
     * 总数
     */
    private long total = 0;
    /**
     * 每页显示条数，默认 10
     */
    private long size = 10;

    /**
     * 当前页
     */
    private long current = 1;

    /**
     * 排序字段信息
     */
    @Setter
    private List<OrderItemDto> orders = new ArrayList<>();

    /**
     * 自动优化 COUNT SQL
     */
    private boolean optimizeCountSql = true;
    /**
     * 是否进行 count 查询
     */
    private boolean searchCount = true;
    /**
     * {@link #optimizeJoinOfCountSql()}
     */
    @Setter
    private boolean optimizeJoinOfCountSql = true;
    /**
     * 单页分页条数限制
     */
    @Setter
    private Long maxLimit;
    /**
     * countId
     */
    @Setter
    private String countId;

    public PageDto() {
    }

    /**
     * 分页构造函数
     *
     * @param current 当前页
     * @param size    每页显示条数
     */
    public PageDto(long current, long size) {
        this(current, size, 0);
    }

    public PageDto(long current, long size, long total) {
        this(current, size, total, true);
    }

    public PageDto(long current, long size, boolean searchCount) {
        this(current, size, 0, searchCount);
    }

    public PageDto(long current, long size, long total, boolean searchCount) {
        if (current > 1) {
            this.current = current;
        }
        this.size = size;
        this.total = total;
        this.searchCount = searchCount;
    }

    /**
     * 是否存在上一页
     *
     * @return true / false
     */
    public boolean hasPrevious() {
        return this.current > 1;
    }

    /**
     * 是否存在下一页
     *
     * @return true / false
     */
    public boolean hasNext() {
        return this.current < this.getPages();
    }

    @Override
    public List<T> getRecords() {
        return this.records;
    }

    @Override
    public PageDto<T> setRecords(List<T> records) {
        this.records = records;
        return this;
    }

    @Override
    public long getTotal() {
        return this.total;
    }

    @Override
    public PageDto<T> setTotal(long total) {
        this.total = total;
        return this;
    }

    @Override
    public long getSize() {
        return this.size;
    }

    @Override
    public PageDto<T> setSize(long size) {
        this.size = size;
        return this;
    }

    @Override
    public long getCurrent() {
        return this.current;
    }

    @Override
    public PageDto<T> setCurrent(long current) {
        this.current = current;
        return this;
    }

    @Override
    public String countId() {
        return this.countId;
    }

    @Override
    public Long maxLimit() {
        return this.maxLimit;
    }

    /**
     * 查找 order 中正序排序的字段数组
     *
     * @param filter 过滤器
     * @return 返回正序排列的字段数组
     */
    private String[] mapOrderToArray(Predicate<OrderItemDto> filter) {
        List<String> columns = new ArrayList<>(orders.size());
        orders.forEach(i -> {
            if (filter.test(i)) {
                columns.add(i.getColumn());
            }
        });
        return columns.toArray(new String[0]);
    }

    /**
     * 移除符合条件的条件
     *
     * @param filter 条件判断
     */
    private void removeOrder(Predicate<OrderItemDto> filter) {
        for (int i = orders.size() - 1; i >= 0; i--) {
            if (filter.test(orders.get(i))) {
                orders.remove(i);
            }
        }
    }

    /**
     * 添加新的排序条件，构造条件可以使用工厂：{@link OrderItemDto#build(String, boolean)}
     *
     * @param items 条件
     * @return 返回分页参数本身
     */
    public PageDto<T> addOrder(OrderItemDto... items) {
        orders.addAll(Arrays.asList(items));
        return this;
    }

    /**
     * 添加新的排序条件，构造条件可以使用工厂：{@link OrderItemDto#build(String, boolean)}
     *
     * @param items 条件
     * @return 返回分页参数本身
     */
    public PageDto<T> addOrder(List<OrderItemDto> items) {
        orders.addAll(items);
        return this;
    }

    @Override
    public List<OrderItemDto> orders() {
        return this.orders;
    }

    @Override
    public boolean optimizeCountSql() {
        return optimizeCountSql;
    }

    public static <T> PageDto<T> of(long current, long size, long total, boolean searchCount) {
        return new PageDto<>(current, size, total, searchCount);
    }

    @Override
    public boolean optimizeJoinOfCountSql() {
        return optimizeJoinOfCountSql;
    }

    public PageDto<T> setSearchCount(boolean searchCount) {
        this.searchCount = searchCount;
        return this;
    }

    public PageDto<T> setOptimizeCountSql(boolean optimizeCountSql) {
        this.optimizeCountSql = optimizeCountSql;
        return this;
    }

    @Override
    public long getPages() {
        // 解决 github issues/3208
        return IPageDto.super.getPages();
    }

    /* --------------- 以下为静态构造方式 --------------- */
    public static <T> PageDto<T> of(long current, long size) {
        return of(current, size, 0);
    }

    public static <T> PageDto<T> of(long current, long size, long total) {
        return of(current, size, total, true);
    }

    public static <T> PageDto<T> of(long current, long size, boolean searchCount) {
        return of(current, size, 0, searchCount);
    }

    @Override
    public boolean searchCount() {
        if (total < 0) {
            return false;
        }
        return searchCount;
    }

    @Override
    public String toString() {
        return "Page{" +
                "records=" + records +
                ", total=" + total +
                ", size=" + size +
                ", current=" + current +
                ", orders=" + orders +
                ", optimizeCountSql=" + optimizeCountSql +
                ", searchCount=" + searchCount +
                ", optimizeJoinOfCountSql=" + optimizeJoinOfCountSql +
                ", maxLimit=" + maxLimit +
                ", countId='" + countId + '\'' +
                '}';
    }

}
