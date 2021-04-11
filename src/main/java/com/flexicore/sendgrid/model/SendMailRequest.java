package com.flexicore.sendgrid.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.converters.JsonConverter;
import com.flexicore.interfaces.dynamic.FieldInfo;
import com.flexicore.interfaces.dynamic.IdRefFieldInfo;
import com.flexicore.interfaces.dynamic.ListFieldInfo;
import com.flexicore.model.dynamic.ExecutionParametersHolder;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class SendMailRequest  {

    
    @JsonIgnore
    private SendGridTemplate sendGridTemplate;

    @JsonIgnore
    private Map<String, Object> templateParameters =new HashMap<>();



    private Set<String> emailRefs=new HashSet<>();

    @FieldInfo(displayName = "from",mandatory = true)
    private String emailFrom;
    @IdRefFieldInfo(refType = SendGridTemplate.class,list = false,mandatory = true)
    private String sendGridTemplateId;

    public String getSendGridTemplateId() {
        return sendGridTemplateId;
    }

    public <T extends SendMailRequest> T setSendGridTemplateId(String externalServerId) {
        this.sendGridTemplateId = externalServerId;
        return (T) this;
    }

    
    @JsonIgnore
    public SendGridTemplate getSendGridTemplate() {
        return sendGridTemplate;
    }

    public <T extends SendMailRequest> T setSendGridTemplate(SendGridTemplate sendGridTemplate) {
        this.sendGridTemplate = sendGridTemplate;
        return (T) this;
    }

    @JsonIgnore
    public Map<String, Object> getTemplateParameters() {
        return templateParameters;
    }

    public <T extends SendMailRequest> T setTemplateParameters(Map<String, Object> genericPredicates) {
        this.templateParameters = genericPredicates;
        return (T) this;
    }

    @JsonAnyGetter
    public Map<String, Object> any() {
        return templateParameters;
    }

    @JsonIgnore
    public boolean supportingDynamic() {
        return false;
    }

    @JsonAnySetter
    public void set(final String name, final Object value) {
        templateParameters.put(name, value);
    }

    
    public Set<String> getEmailRefs() {
        return emailRefs;
    }

    public <T extends SendMailRequest> T setEmailRefs(Set<String> emailRefs) {
        this.emailRefs = emailRefs;
        return (T) this;
    }

    public String getEmailFrom() {
        return emailFrom;
    }

    public <T extends SendMailRequest> T setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
        return (T) this;
    }

}
