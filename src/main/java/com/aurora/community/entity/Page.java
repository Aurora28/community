package com.aurora.community.entity;

// devide many pages
public class Page {
    // current page
    private int current = 1;
    // limit
    private int limit = 10;
    // total amount of data to calculate total pages number
    private int rows;
    // search path to reuse page devide link
    private String path;

    public int getCurrent() {
        return this.current;
    }

    public void setCurrent(int current) {
        if (current >= 1) {
            this.current = current;
        }
    }

    public int getLimit() {
        return this.limit;
    }

    public void setLimit(int limit) {
        if (limit >= 1 && limit <= 100) {
            this.limit = limit;
        }
    }

    public int getRows() {
        return this.rows;
    }

    public void setRows(int rows) {
        if (rows >= 0) {
            this.rows = rows;
        }
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    // get current page's begin position
    public int getOffset() {
        return (current - 1) * limit;
    }

    // to get total pages number
    public int getTotal() {
        return rows % limit == 0? (rows / limit) : (rows / limit + 1);
    }

    // get start page number
    public int getFrom() {
        return current - 2 < 1? 1 : (current - 2);
    }

    public int getTo() {
        return current + 2 > getTotal()? getTotal() : (current + 2);
    }
}