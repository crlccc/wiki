package com.example.wiki.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Validated
public class PageReq {

    @NotNull(message = "【页码】不能为空")
    private int page;
    @NotNull(message = "【页码】不能为空")
    @Max(value = 1000, message = "【每页条数】不能超过1000")
    private int size;

    /**
     * 默认当前页码为1，每页显示10条
     */

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
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