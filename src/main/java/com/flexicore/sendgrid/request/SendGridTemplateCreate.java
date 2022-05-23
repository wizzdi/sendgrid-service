package com.flexicore.sendgrid.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.sendgrid.model.SendGridServer;
import com.wizzdi.flexicore.security.request.BasicCreate;

public class SendGridTemplateCreate extends BasicCreate {

    private String externalId;

    private String sendGridServerId;

    @JsonIgnore
    private SendGridServer sendGridServer;

    public String getExternalId() {
        return externalId;
    }

    public <T extends SendGridTemplateCreate> T setExternalId(String externalId) {
        this.externalId = externalId;
        return (T) this;
    }

    public String getSendGridServerId() {
        return sendGridServerId;
    }

    public <T extends SendGridTemplateCreate> T setSendGridServerId(String sendGridServerId) {
        this.sendGridServerId = sendGridServerId;
        return (T) this;
    }

    @JsonIgnore
    public SendGridServer getSendGridServer() {
        return sendGridServer;
    }

    public <T extends SendGridTemplateCreate> T setSendGridServer(SendGridServer sendGridServer) {
        this.sendGridServer = sendGridServer;
        return (T) this;
    }
}
