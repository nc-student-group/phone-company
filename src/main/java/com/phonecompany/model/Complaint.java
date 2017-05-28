package com.phonecompany.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.phonecompany.config.datetime_config.LocalDateTimeDeserializer;
import com.phonecompany.config.datetime_config.LocalDateTimeSerializer;
import com.phonecompany.model.enums.ComplaintCategory;
import com.phonecompany.model.enums.ComplaintStatus;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class Complaint extends DomainEntity {

    private ComplaintStatus status;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDate date;
    @Size(max = 255, message = "Problem description should not be longer than 255 symbols")
    @NotNull(message = "There should be some message describing the problem")
    private String text;
    private ComplaintCategory type;
    private User user;
    @Size(max = 150, message = "Complaint subject should not be longer than 150 symbols")
    @NotNull(message = "Subject must not be null")
    private String subject;
    private User responsiblePmg;
    private String comment;

    public Complaint() {
    }

    public Complaint(ComplaintStatus status, LocalDate date, String text, ComplaintCategory type, User user, String subject, User responsiblePmg, String comment) {
        this.status = status;
        this.date = date;
        this.text = text;
        this.type = type;
        this.user = user;
        this.subject = subject;
        this.responsiblePmg = responsiblePmg;
        this.comment = comment;
    }

    public ComplaintStatus getStatus() {
        return status;
    }

    public void setStatus(ComplaintStatus status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public User getResponsiblePmg() {
        return responsiblePmg;
    }

    public void setResponsiblePmg(User responsiblePmg) {
        this.responsiblePmg = responsiblePmg;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Complaint{" +
                "status='" + status + '\'' +
                ", date='" + date + '\'' +
                ", text='" + text + '\'' +
                ", type='" + type + '\'' +
                ", user='" + user + '\'' +
                ", subject='" + subject + "\'" +
                ", responsiblePmg='" + responsiblePmg + '\'' +
                ", comment='" + comment + "\'" +
                '}';
    }
}
