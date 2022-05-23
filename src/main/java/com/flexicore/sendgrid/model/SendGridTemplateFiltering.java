package com.flexicore.sendgrid.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.flexicore.security.request.BasicPropertiesFilter;
import com.wizzdi.flexicore.security.request.PaginationFilter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class SendGridTemplateFiltering extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;


    private Set<String> externalTemplateIds=new HashSet<>();


    private Set<String> sendGridServerIds =new HashSet<>();

    @JsonIgnore
    
    private List<SendGridServer> sendGridServers;

    
    public Set<String> getExternalTemplateIds() {
        return externalTemplateIds;
    }



    public <T extends SendGridTemplateFiltering> T setExternalTemplateIds(Set<String> externalTemplateIds) {
        this.externalTemplateIds = externalTemplateIds;
        return (T) this;
    }

    
    public Set<String> getSendGridServerIds() {
        return sendGridServerIds;
    }

    public <T extends SendGridTemplateFiltering> T setSendGridServerIds(Set<String> sendGridIds) {
        this.sendGridServerIds = sendGridIds;
        return (T) this;
    }

    @JsonIgnore
    
    public List<SendGridServer> getSendGridServers() {
        return sendGridServers;
    }

    public <T extends SendGridTemplateFiltering> T setSendGridServers(List<SendGridServer> sendGridServers) {
        this.sendGridServers = sendGridServers;
        return (T) this;
    }

    public BasicPropertiesFilter getBasicPropertiesFilter() {
        return basicPropertiesFilter;
    }

    public <T extends SendGridTemplateFiltering> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
        this.basicPropertiesFilter = basicPropertiesFilter;
        return (T) this;
    }
}
