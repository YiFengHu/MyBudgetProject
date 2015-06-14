package com.dreamer.mybudget;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table CATEGORY.
 */
public class Category {

    private Long cid;
    /** Not-null value. */
    private String category_name;
    /** Not-null value. */
    private String category_type;

    public Category() {
    }

    public Category(Long cid) {
        this.cid = cid;
    }

    public Category(Long cid, String category_name, String category_type) {
        this.cid = cid;
        this.category_name = category_name;
        this.category_type = category_type;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    /** Not-null value. */
    public String getCategory_name() {
        return category_name;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    /** Not-null value. */
    public String getCategory_type() {
        return category_type;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setCategory_type(String category_type) {
        this.category_type = category_type;
    }

}