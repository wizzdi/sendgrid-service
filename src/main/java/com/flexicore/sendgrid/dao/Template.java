package com.flexicore.sendgrid.dao;

public class Template {

    private String id;
    private String name;


    public String getId() {
        return id;
    }

    public <T extends Template> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    public String getName() {
        return name;
    }

    public <T extends Template> T setName(String name) {
        this.name = name;
        return (T) this;
    }
}
