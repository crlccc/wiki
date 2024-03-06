package com.example.wiki.req;

import jakarta.validation.constraints.NotNull;

public class EbookQueryReq extends PageReq {

    private Long id;

    private String name;

    private long categoryId2;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCategoryId2() {
        return categoryId2;
    }

    public void setCategoryId2(long categoryId2) {
        this.categoryId2 = categoryId2;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("EbookQueryReq{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", categoryId2=").append(categoryId2);
        sb.append('}');
        return sb.toString();
    }
}