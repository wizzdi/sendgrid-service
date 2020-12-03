package com.flexicore.sendgrid.request;

import com.flexicore.interfaces.dynamic.FieldInfo;
import com.flexicore.product.iot.request.ExternalServerCreate;

public class SendGridServerCreate extends ExternalServerCreate {

    @FieldInfo(mandatory = true,description = "Send Grid Api Key")
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }

    public <T extends SendGridServerCreate> T setApiKey(String apiKey) {
        this.apiKey = apiKey;
        return (T) this;
    }
}
