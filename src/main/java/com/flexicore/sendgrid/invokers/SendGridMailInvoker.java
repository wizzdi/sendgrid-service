package com.flexicore.sendgrid.invokers;


import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.dynamic.Invoker;
import com.flexicore.interfaces.dynamic.InvokerInfo;
import com.flexicore.interfaces.dynamic.InvokerMethodInfo;
import com.flexicore.security.SecurityContext;
import com.flexicore.sendgrid.model.SendGridServer;
import com.flexicore.sendgrid.model.SendMailRequest;
import com.flexicore.sendgrid.response.SendMailResponse;
import com.flexicore.sendgrid.service.SendGridService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@PluginInfo(version = 1)
@InvokerInfo(displayName = "SendGridMail Invoker", description = "Invoker for SendGridMail")
@Extension
@Component
public class SendGridMailInvoker implements Invoker {


	@PluginInfo(version = 1)
	@Autowired
	private SendGridService sendGridService;


	@InvokerMethodInfo(displayName = "sendMail", description = "sendMail", relatedClasses = {SendGridServer.class},categories = "ACTION")
	public SendMailResponse sendMail(SendMailRequest sendMailRequest, SecurityContext securityContext) {
		sendGridService.validate(sendMailRequest, securityContext);
		return sendGridService.sendMail(sendMailRequest, securityContext);
	}


	@Override
	public Class<?> getHandlingClass() {
		return SendGridServer.class;
	}
}
