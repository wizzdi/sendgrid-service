package com.flexicore.sendgrid.dao;

import java.util.List;

public class Templates {

    private List<Template> templates;

    public List<Template> getTemplates() {
        return templates;
    }

    public <T extends Templates> T setTemplates(List<Template> templates) {
        this.templates = templates;
        return (T) this;
    }
}
