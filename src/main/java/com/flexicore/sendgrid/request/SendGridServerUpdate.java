package com.flexicore.sendgrid.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.sendgrid.model.SendGridServer;

public class SendGridServerUpdate extends SendGridServerCreate{

    private String id;
    @JsonIgnore
    private SendGridServer sendGridServer;

    public String getId() {
        return id;
    }

    public <T extends SendGridServerUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public SendGridServer getSendGridServer() {
        return sendGridServer;
    }

    public <T extends SendGridServerUpdate> T setSendGridServer(SendGridServer sendGridServer) {
        this.sendGridServer = sendGridServer;
        return (T) this;
    }
}
