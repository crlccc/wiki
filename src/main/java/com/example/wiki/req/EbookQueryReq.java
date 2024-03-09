package com.example.wiki.req;


public class EbookQueryReq extends PageReq {

    private Long id;

    private String name;

    private Integer CategoryId2;

    public EbookQueryReq(){
        this.CategoryId2 = null;
    }
    public Integer getCategoryId2() {
        return CategoryId2;
    }

    public void setCategoryId2(Integer categoryId2) {
        this.CategoryId2 = CategoryId2 == null ? null : CategoryId2;
    }

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


    @Override
    public String toString() {
        return "EbookReq{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}