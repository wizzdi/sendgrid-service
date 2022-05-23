package com.flexicore.sendgrid.model;

import com.flexicore.annotations.TypeRetention;
import com.wizzdi.flexicore.security.request.BasicPropertiesFilter;
import com.wizzdi.flexicore.security.request.PaginationFilter;

import java.util.HashSet;
import java.util.Set;


public class SendGridServerFiltering extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;

    @TypeRetention(String.class)
    private Set<String> apiKeys=new HashSet<>();

    
    public Set<String> getApiKeys() {
        return apiKeys;
    }

    public <T extends SendGridServerFiltering> T setApiKeys(Set<String> apiKeys) {
        this.apiKeys = apiKeys;
        return (T) this;
    }

    public BasicPropertiesFilter getBasicPropertiesFilter() {
        return basicPropertiesFilter;
    }

    public <T extends SendGridServerFiltering> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
        this.basicPropertiesFilter = basicPropertiesFilter;
        return (T) this;
    }
}
