package cn.mybatis.mp.routing.datasource;

import java.util.Deque;
import java.util.LinkedList;

public class DataSourceHolder {

    private static final ThreadLocal<Deque<String>> dataSource = new ThreadLocal<>();

    public static void add(String type) {
        Deque<String> list = dataSource.get();
        if (list == null) {
            list = new LinkedList<>();
        }
        list.addFirst(type);
        dataSource.set(list);
    }

    public static String getCurrent() {
        Deque<String> list = dataSource.get();
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.getFirst();
    }

    public static void remove() {
        Deque<String> list = dataSource.get();
        if (list == null || list.isEmpty()) {
            dataSource.remove();
            return;
        }
        int size = list.size();
        if (size == 1) {
            list.clear();
            dataSource.remove();
            return;
        }
        list.removeFirst();
    }
}
