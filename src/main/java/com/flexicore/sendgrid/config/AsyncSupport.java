package com.flexicore.sendgrid.config;

import com.flexicore.interfaces.ServicePlugin;
import org.pf4j.Extension;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@Extension
@EnableAsync(proxyTargetClass = true)
public class AsyncSupport implements ServicePlugin {

}