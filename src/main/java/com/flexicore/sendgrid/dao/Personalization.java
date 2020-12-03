
package com.flexicore.sendgrid.dao;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Personalization {

    @JsonProperty("to")
    private List<To> to = null;
    @JsonProperty("dynamic_template_data")
    private Object dynamicTemplateData;

    @JsonProperty("to")
    public List<To> getTo() {
        return to;
    }

    @JsonProperty("to")
    public void setTo(List<To> to) {
        this.to = to;
    }

    public Personalization withTo(List<To> to) {
        this.to = to;
        return this;
    }

    @JsonProperty("dynamic_template_data")
    public Object getDynamicTemplateData() {
        return dynamicTemplateData;
    }

    @JsonProperty("dynamic_template_data")
    public void setDynamicTemplateData(Object dynamicTemplateData) {
        this.dynamicTemplateData = dynamicTemplateData;
    }

    public Personalization withDynamicTemplateData(Object dynamicTemplateData) {
        this.dynamicTemplateData = dynamicTemplateData;
        return this;
    }

}
