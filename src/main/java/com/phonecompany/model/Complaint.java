package com.phonecompany.model;

import com.phonecompany.model.enums.ComplaintCategory;
import com.phonecompany.model.enums.ComplaintStatus;

import java.sql.Date;

public class Complaint extends DomainEntity {

    private ComplaintStatus status;
    private Date date;
    private String text;
    private ComplaintCategory type;
    private User user;
    private String subject;

    public Complaint(){}

    public Complaint(ComplaintStatus status, Date date, String text, ComplaintCategory type, User user, String subject) {
        this.status = status;
        this.date = date;
        this.text = text;
        this.type = type;
        this.user = user;
        this.subject = subject;
    }

    public ComplaintStatus getStatus() {
        return status;
    }

    public void setStatus(ComplaintStatus status) {
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

    public ComplaintCategory getType() {
        return type;
    }

    public void setType(ComplaintCategory type) {
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
