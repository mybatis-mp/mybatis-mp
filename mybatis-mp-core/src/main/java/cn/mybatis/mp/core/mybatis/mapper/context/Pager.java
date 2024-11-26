/*
 *  Copyright (c) 2024-2024, Ai东 (abc-127@live.cn).
 *
 *  Licensed under the Apache License, Version 2.0 (the "License").
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package cn.mybatis.mp.core.mybatis.mapper.context;

import java.util.List;

public class Pager<T> {

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

    public static <T> Pager<T> of(int number, int size) {
        return new Pager<>(number, size);
    }

    public int getOffset() {
        return (number - 1) * size;
    }

    public boolean isExecuteCount() {
        return executeCount;
    }

    public Pager<T> setExecuteCount(boolean executeCount) {
        this.executeCount = executeCount;
        return this;
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
                "executeCount=" + executeCount +
                ", results=" + results +
                ", total=" + total +
                ", number=" + number +
                ", size=" + size +
                '}';
    }
}
