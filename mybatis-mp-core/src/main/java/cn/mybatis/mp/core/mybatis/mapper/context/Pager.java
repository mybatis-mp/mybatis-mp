package cn.mybatis.mp.core.mybatis.mapper.context;

import java.util.List;

public class Pager<T> {

    private boolean optimize = true;

    private boolean executeCount = true;

    private List<T> results;

    private Integer total;

    private int number = 1;

    private int size = 20;

    public Pager() {

    }

    public Pager(int size) {
        this(1, size);
    }

    public Pager(int number, int size) {
        this.number = number;
        this.size = size;
    }

    public static <T> Pager<T> of(int size) {
        return new Pager<>(size);
    }

    public static <T> Pager<T> of(Class<T> type, int size) {
        return of(size);
    }

    public static <T> Pager<T> of(int number, int size) {
        return of(number, size);
    }

    public static <T> Pager<T> of(Class<T> type, int number, int size) {
        return of(number, size);
    }

    public int getOffset() {
        return (number - 1) * size;
    }

    public boolean isExecuteCount() {
        return executeCount;
    }

    public <T> Pager<T> setExecuteCount(boolean executeCount) {
        this.executeCount = executeCount;
        return (Pager<T>) this;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isOptimize() {
        return optimize;
    }

    public <T> Pager<T> setOptimize(boolean optimize) {
        this.optimize = optimize;
        return (Pager<T>) this;
    }

    public Integer getTotalPage() {
        if (total == null) {
            total = 1;
        } else if (total < 1) {
            return 0;
        }
        return this.total / this.size + (this.total % this.size == 0 ? 0 : 1);
    }

    @Override
    public String toString() {
        return "Pager{" +
                "optimize=" + optimize +
                ", executeCount=" + executeCount +
                ", results=" + results +
                ", total=" + total +
                ", number=" + number +
                ", size=" + size +
                '}';
    }
}
