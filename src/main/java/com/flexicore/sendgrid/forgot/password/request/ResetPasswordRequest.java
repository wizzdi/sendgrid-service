package com.flexicore.sendgrid.forgot.password.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.User;

public class ResetPasswordRequest {

    private String email;
    @JsonIgnore
    private User user;

    @JsonIgnore
    private String templateId;

    private String returnLink;

    public String getEmail() {
        return email;
    }

    public <T extends ResetPasswordRequest> T setEmail(String email) {
        this.email = email;
        return (T) this;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public <T extends ResetPasswordRequest> T setUser(User user) {
        this.user = user;
        return (T) this;
    }

    @JsonIgnore
    public String getTemplateId() {
        return templateId;
    }

    public <T extends ResetPasswordRequest> T setTemplateId(String templateId) {
        this.templateId = templateId;
        return (T) this;
    }

    public String getReturnLink() {
        return returnLink;
    }

    public <T extends ResetPasswordRequest> T setReturnLink(String returnLink) {
        this.returnLink = returnLink;
        return (T) this;
    }
}
