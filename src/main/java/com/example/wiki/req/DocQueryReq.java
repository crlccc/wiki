package com.example.wiki.req;

public class DocQueryReq extends PageReq {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DocQueryReq{super=${super.toString()}}";
    }
}