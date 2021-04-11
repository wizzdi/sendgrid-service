package com.flexicore.sendgrid.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.interfaces.dynamic.IdRefFieldInfo;
import com.flexicore.model.dynamic.ExecutionParametersHolder;

import javax.persistence.Entity;
import javax.persistence.Transient;


public class ImportTemplatesRequest {


    @JsonIgnore
    private SendGridServer sendGridServer;


    @IdRefFieldInfo(refType = SendGridServer.class,list = false,mandatory = true)
    private String sendGridServerId;

    public String getSendGridServerId() {
        return sendGridServerId;
    }

    public <T extends ImportTemplatesRequest> T setSendGridServerId(String externalServerId) {
        this.sendGridServerId = externalServerId;
        return (T) this;
    }

    
    @JsonIgnore
    public SendGridServer getSendGridServer() {
        return sendGridServer;
    }

    public <T extends ImportTemplatesRequest> T setSendGridServer(SendGridServer sendGridServer) {
        this.sendGridServer = sendGridServer;
        return (T) this;
    }

}
