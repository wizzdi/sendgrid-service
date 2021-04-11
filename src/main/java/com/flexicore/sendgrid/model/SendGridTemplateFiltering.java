package com.flexicore.sendgrid.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.interfaces.dynamic.IdRefFieldInfo;
import com.flexicore.interfaces.dynamic.ListFieldInfo;
import com.flexicore.model.FilteringInformationHolder;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class SendGridTemplateFiltering extends FilteringInformationHolder {


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

}
