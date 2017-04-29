package com.phonecompany.model;

import java.sql.Date;

public class Complaint extends DomainEntity {

    private String status;
    private Date date;
    private String text;
    private String type;
    private User user;
    private String subject;

    public Complaint(){}

    public Complaint(String status, Date date, String text, String type, User user, String subject) {
        this.status = status;
        this.date = date;
        this.text = text;
        this.type = type;
        this.user = user;
        this.subject = subject;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSubject() { return subject; }

    public void setSubject(String subject) { this.subject = subject; }

    @Override
    public String toString() {
        return "Complaint{" +
                "status='" + status + '\'' +
                ", date=" + date +
                ", text='" + text + '\'' +
                ", type='" + type + '\'' +
                ", user=" + user +
                ", subject=" + subject +
                '}';
    }
}
