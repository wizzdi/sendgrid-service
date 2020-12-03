package com.flexicore.sendgrid.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.interfaces.dynamic.IdRefFieldInfo;
import com.flexicore.sendgrid.model.SendGridTemplate;

public class SendGridTemplateUpdate extends SendGridTemplateCreate{

    @IdRefFieldInfo(mandatory = true,refType = SendGridTemplate.class,list = false)
    private String id;
    @JsonIgnore
    private SendGridTemplate sendGridTemplate;

    public String getId() {
        return id;
    }

    public <T extends SendGridTemplateUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public SendGridTemplate getSendGridTemplate() {
        return sendGridTemplate;
    }

    public <T extends SendGridTemplateUpdate> T setSendGridTemplate(SendGridTemplate sendGridTemplate) {
        this.sendGridTemplate = sendGridTemplate;
        return (T) this;
    }
}
