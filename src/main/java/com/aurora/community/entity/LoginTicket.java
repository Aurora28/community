package com.aurora.community.entity;

import java.util.Date;

public class LoginTicket {
    
    private int id;
    private int userId;
    private String ticket;
    private int status;
    private Date expired;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTicket() {
        return this.ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getExpired() {
        return this.expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }


    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", userId='" + getUserId() + "'" +
            ", ticket='" + getTicket() + "'" +
            ", status='" + getStatus() + "'" +
            ", expired='" + getExpired() + "'" +
            "}";
    }

}