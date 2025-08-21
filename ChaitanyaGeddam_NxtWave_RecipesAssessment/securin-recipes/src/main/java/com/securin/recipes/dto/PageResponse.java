package com.securin.recipes.dto;

import java.util.List;

public class PageResponse<T> {
    private int page;
    private int limit;
    private long total;
    private List<T> data;

    public PageResponse() {}

    public PageResponse(int page, int limit, long total, List<T> data) {
        this.page = page;
        this.limit = limit;
        this.total = total;
        this.data = data;
    }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    public int getLimit() { return limit; }
    public void setLimit(int limit) { this.limit = limit; }
    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }
    public List<T> getData() { return data; }
    public void setData(List<T> data) { this.data = data; }
}
