package com.flexicore.sendgrid.config;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.CrossLoaderResolver;
import com.flexicore.events.PluginsLoadedEvent;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.sendgrid.model.*;
import com.flexicore.sendgrid.request.SendGridServerCreate;
import com.flexicore.sendgrid.request.SendGridServerUpdate;
import com.flexicore.sendgrid.request.SendGridTemplateCreate;
import com.flexicore.sendgrid.request.SendGridTemplateUpdate;
import com.flexicore.service.BaseclassService;
import org.pf4j.Extension;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@PluginInfo(version = 1)
@Component
@Extension
public class SendGridConfig implements ServicePlugin {

    @EventListener
    public void init(PluginsLoadedEvent event){
        BaseclassService.registerFilterClass(SendGridTemplateFiltering.class, SendGridTemplate.class);
        BaseclassService.registerFilterClass(SendGridServerFiltering.class, SendGridServer.class);
        CrossLoaderResolver.registerClass(SendGridServerCreate.class, SendGridServerUpdate.class,
                SendGridTemplateCreate.class, SendGridTemplateUpdate.class, SendMailRequest.class,ImportTemplatesRequest.class);


    }
}
