package com.flexicore.sendgrid.invokers;


import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.dynamic.InvokerInfo;
import com.flexicore.interfaces.dynamic.InvokerMethodInfo;
import com.flexicore.interfaces.dynamic.ListingInvoker;
import com.flexicore.security.SecurityContext;
import com.flexicore.sendgrid.model.SendGridServer;
import com.flexicore.sendgrid.model.SendGridServerFiltering;
import com.flexicore.sendgrid.model.SendMailRequest;
import com.flexicore.sendgrid.request.SendGridServerCreate;
import com.flexicore.sendgrid.request.SendGridServerUpdate;
import com.flexicore.sendgrid.response.SendMailResponse;
import com.flexicore.sendgrid.service.SendGridServerService;
import com.flexicore.sendgrid.service.SendGridService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.BadRequestException;

@PluginInfo(version = 1)
@InvokerInfo(displayName = "SendGridServer Invoker", description = "Invoker for SendGridServer")
@Extension
@Component
public class SendGridServerInvoker implements ListingInvoker<SendGridServer, SendGridServerFiltering> {

	@PluginInfo(version = 1)
	@Autowired
	private SendGridServerService sendGridServerService;


	@Override
	@InvokerMethodInfo(displayName = "listAllSendGridServers", description = "lists all SendGridServers", relatedClasses = {SendGridServer.class})
	public PaginationResponse<SendGridServer> listAll(SendGridServerFiltering sendGridServerFiltering, SecurityContext securityContext) {
		sendGridServerService.validateFiltering(sendGridServerFiltering, securityContext);
		return sendGridServerService.getAllSendGridServers(securityContext, sendGridServerFiltering);
	}


	@InvokerMethodInfo(displayName = "createSendGridServer", description = "create SendGridServer", relatedClasses = {SendGridServer.class},categories = "TYPE_ACTION")
	public SendGridServer create(SendGridServerCreate sendGridServerCreate, SecurityContext securityContext) {
		sendGridServerService.validate(sendGridServerCreate, securityContext);
		return sendGridServerService.createSendGridServer(
				sendGridServerCreate, securityContext);
	}

	@InvokerMethodInfo(displayName = "updateSendGridServer", description = "update SendGridServer", relatedClasses = {SendGridServer.class},categories = "ACTION")
	public SendGridServer update(SendGridServerUpdate sendGridServerUpdate, SecurityContext securityContext) {
		String id = sendGridServerUpdate.getId();
		SendGridServer sendGridServer = id != null ? sendGridServerService.getByIdOrNull(id, SendGridServer.class, null, securityContext) : null;
		if (sendGridServer == null) {
			throw new BadRequestException("No SendGridServer with id " + id);
		}
		sendGridServerUpdate.setSendGridServer(sendGridServer);
		sendGridServerService.validate(sendGridServerUpdate, securityContext);
		return sendGridServerService.updateSendGridServer(sendGridServerUpdate, securityContext);
	}

	@Override
	public Class<SendGridServerFiltering> getFilterClass() {
		return SendGridServerFiltering.class;
	}

	@Override
	public Class<?> getHandlingClass() {
		return SendGridServer.class;
	}
}
