package com.flexicore.sendgrid.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.interfaces.dynamic.FieldInfo;
import com.flexicore.interfaces.dynamic.IdRefFieldInfo;
import com.flexicore.product.iot.request.ExternalServerCreate;
import com.flexicore.request.BaseclassCreate;
import com.flexicore.sendgrid.model.SendGridServer;

public class SendGridTemplateCreate extends BaseclassCreate {

    @FieldInfo(mandatory = true,description = "Send Grid Template Id")
    private String externalId;

    @IdRefFieldInfo(refType = SendGridServer.class,list = false,mandatory = true)
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
