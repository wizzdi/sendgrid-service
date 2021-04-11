package com.flexicore.sendgrid.model;

import com.flexicore.interfaces.dynamic.ListFieldInfo;
import com.flexicore.product.containers.request.ExternalServerFiltering;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;


public class SendGridServerFiltering extends ExternalServerFiltering {

    @ListFieldInfo(listType = String.class)
    private Set<String> apiKeys=new HashSet<>();

    
    public Set<String> getApiKeys() {
        return apiKeys;
    }

    public <T extends SendGridServerFiltering> T setApiKeys(Set<String> apiKeys) {
        this.apiKeys = apiKeys;
        return (T) this;
    }
}
