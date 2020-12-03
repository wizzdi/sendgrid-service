
package com.flexicore.sendgrid.dao;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SendMailRequest {

    private From from;
    private String subject;
    private String templateId;
    private List<Personalization> personalizations = null;
    private Integer sendAt;

    @JsonProperty("from")
    public From getFrom() {
        return from;
    }

    public void setFrom(From from) {
        this.from = from;
    }

    public SendMailRequest withFrom(From from) {
        this.from = from;
        return this;
    }

    @JsonProperty("subject")
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public SendMailRequest withSubject(String subject) {
        this.subject = subject;
        return this;
    }

    @JsonProperty("template_id")
    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public SendMailRequest withTemplateId(String templateId) {
        this.templateId = templateId;
        return this;
    }

    @JsonProperty("personalizations")
    public List<Personalization> getPersonalizations() {
        return personalizations;
    }

    public void setPersonalizations(List<Personalization> personalizations) {
        this.personalizations = personalizations;
    }

    public SendMailRequest withPersonalizations(List<Personalization> personalizations) {
        this.personalizations = personalizations;
        return this;
    }

    @JsonProperty("sendAt")
    public Integer getSendAt() {
        return sendAt;
    }

    public void setSendAt(Integer sendAt) {
        this.sendAt = sendAt;
    }

    public SendMailRequest withSendAt(Integer sendAt) {
        this.sendAt = sendAt;
        return this;
    }

}
