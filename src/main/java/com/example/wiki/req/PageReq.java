package com.example.wiki.req;

import java.util.Optional;

public class PageReq {
    private int page;

    private int size;

    /**
     * 默认当前页码为1，每页显示10条
     */
    public PageReq() {
        this.page = 1;
        this.size = 10;
    }
    public int getPage() {
        return Optional.ofNullable(page).orElse(1);
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return Optional.ofNullable(size).orElse(10);
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PageReq{");
        sb.append("page=").append(page);
        sb.append(", size=").append(size);
        sb.append('}');
        return sb.toString();
    }
}