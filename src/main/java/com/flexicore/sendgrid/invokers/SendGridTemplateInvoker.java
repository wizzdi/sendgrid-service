package com.flexicore.sendgrid.invokers;


import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.dynamic.InvokerInfo;
import com.flexicore.interfaces.dynamic.InvokerMethodInfo;
import com.flexicore.interfaces.dynamic.ListingInvoker;
import com.flexicore.security.SecurityContext;
import com.flexicore.sendgrid.model.ImportTemplatesRequest;
import com.flexicore.sendgrid.model.SendGridTemplate;
import com.flexicore.sendgrid.model.SendGridTemplateFiltering;
import com.flexicore.sendgrid.request.SendGridTemplateCreate;
import com.flexicore.sendgrid.request.SendGridTemplateUpdate;
import com.flexicore.sendgrid.response.ImportTemplatesResponse;
import com.flexicore.sendgrid.service.SendGridService;
import com.flexicore.sendgrid.service.SendGridTemplateService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.BadRequestException;

@PluginInfo(version = 1)
@InvokerInfo(displayName = "SendGridTemplate Invoker", description = "Invoker for SendGridTemplate")
@Extension
@Component
public class SendGridTemplateInvoker implements ListingInvoker<SendGridTemplate, SendGridTemplateFiltering> {

	@PluginInfo(version = 1)
	@Autowired
	private SendGridTemplateService sendGridTemplateService;

	@PluginInfo(version = 1)
	@Autowired
	private SendGridService sendGridService;



	@Override
	@InvokerMethodInfo(displayName = "listAllSendGridTemplates", description = "lists all SendGridTemplates", relatedClasses = {SendGridTemplate.class})
	public PaginationResponse<SendGridTemplate> listAll(SendGridTemplateFiltering sendGridTemplateFiltering, SecurityContext securityContext) {
		sendGridTemplateService.validateFiltering(sendGridTemplateFiltering, securityContext);
		return sendGridTemplateService.getAllSendGridTemplates(securityContext, sendGridTemplateFiltering);
	}

	@InvokerMethodInfo(displayName = "importSendGridTemplates", description = "importSendGridTemplates", relatedClasses = {SendGridTemplate.class},categories = "TYPE_ACTION")
	public ImportTemplatesResponse importSendGridTemplates(ImportTemplatesRequest sendGridTemplateFiltering, SecurityContext securityContext) {
		sendGridTemplateService.validate(sendGridTemplateFiltering, securityContext);
		return sendGridService.importSendGridTemplates(securityContext, sendGridTemplateFiltering);
	}


	@Override
	public Class<SendGridTemplateFiltering> getFilterClass() {
		return SendGridTemplateFiltering.class;
	}

	@Override
	public Class<?> getHandlingClass() {
		return SendGridTemplate.class;
	}
}
