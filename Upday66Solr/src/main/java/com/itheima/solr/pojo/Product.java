package com.itheima.solr.pojo;

import org.apache.solr.client.solrj.beans.Field;

public class Product {

    @Field("pid")
    private String pid;

    @Field
    private String name;

    @Field("catalog_name")
    private String catalogName;

    @Field
    private Double price;

    @Field
    private String  description;

    @Field
    private String picture;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String pictrue) {
        this.picture = pictrue;
    }
}
