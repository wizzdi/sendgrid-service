package com.flexicore.sendgrid.config;

import com.flexicore.interfaces.ServicePlugin;
import org.pf4j.Extension;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@Extension
@EnableAsync(proxyTargetClass = true)
@EnableTransactionManagement(proxyTargetClass = true)
public class AsyncSupport implements ServicePlugin {

}