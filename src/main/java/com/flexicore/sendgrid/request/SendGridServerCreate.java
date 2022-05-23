package com.flexicore.sendgrid.request;

import com.wizzdi.flexicore.security.request.BasicCreate;

public class SendGridServerCreate extends BasicCreate {

    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }

    public <T extends SendGridServerCreate> T setApiKey(String apiKey) {
        this.apiKey = apiKey;
        return (T) this;
    }
}
