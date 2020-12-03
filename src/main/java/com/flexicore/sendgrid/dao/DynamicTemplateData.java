
package com.flexicore.sendgrid.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "link",
    "name"
})
public class DynamicTemplateData {

    @JsonProperty("link")
    private String link;
    @JsonProperty("name")
    private String name;

    @JsonProperty("link")
    public String getLink() {
        return link;
    }

    @JsonProperty("link")
    public void setLink(String link) {
        this.link = link;
    }

    public DynamicTemplateData withLink(String link) {
        this.link = link;
        return this;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public DynamicTemplateData withName(String name) {
        this.name = name;
        return this;
    }

}
