package com.flexicore.sendgrid.rest;


import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.ProtectedREST;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.security.SecurityContext;
import com.flexicore.sendgrid.model.SendGridServer;
import com.flexicore.sendgrid.model.SendGridServerFiltering;
import com.flexicore.sendgrid.model.SendMailRequest;
import com.flexicore.sendgrid.request.SendGridServerCreate;
import com.flexicore.sendgrid.request.SendGridServerUpdate;
import com.flexicore.sendgrid.response.SendMailResponse;
import com.flexicore.sendgrid.service.SendGridServerService;
import com.flexicore.sendgrid.service.SendGridService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;

/**
 * Created by Asaf on 04/06/2017.
 */

@PluginInfo(version = 1)
@OperationsInside
@ProtectedREST
@Path("plugins/SendGridServer")
@OpenAPIDefinition(tags = {@Tag(name = "SendGridServer", description = "SendGridServer Services"),})
@Tag(name = "SendGridServer")
@Extension
@Component
public class SendGridServerRESTService implements RestServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private SendGridServerService service;

	@PluginInfo(version = 1)
	@Autowired
	private SendGridService sendGridService;

	@POST
	@Produces("application/json")
	@Operation(summary = "updateSendGridServer", description = "update SendGridServer ")
	@Path("updateSendGridServer")
	public SendGridServer updateSendGridServer(
			@HeaderParam("authenticationKey") String authenticationKey,
			SendGridServerUpdate sendGridServerUpdate,
			@Context SecurityContext securityContext) {
		String id = sendGridServerUpdate.getId();
		SendGridServer sendGridServer = id != null ? service
				.getByIdOrNull(id, SendGridServer.class, null,
						securityContext) : null;
		if (sendGridServer == null) {
			throw new BadRequestException("No SendGridServer with id "
					+ id);
		}
		sendGridServerUpdate.setSendGridServer(sendGridServer);
		service.validate(sendGridServerUpdate, securityContext);

		return service.updateSendGridServer(sendGridServerUpdate,
				securityContext);
	}

	@POST
	@Produces("application/json")
	@Operation(summary = "sendMail", description = "sends mail")
	@Path("sendMail")
	public SendMailResponse sendMail(
			@HeaderParam("authenticationKey") String authenticationKey,
			SendMailRequest sendGridServerUpdate,
			@Context SecurityContext securityContext) {

		sendGridService.validate(sendGridServerUpdate, securityContext);
		return sendGridService.sendMail(sendGridServerUpdate, securityContext);
	}

	@POST
	@Produces("application/json")
	@Operation(summary = "createSendGridServer", description = "create SendGridServer ")
	@Path("createSendGridServer")
	public SendGridServer createSendGridServer(
			@HeaderParam("authenticationKey") String authenticationKey,
			SendGridServerCreate sendGridServerCreate,
			@Context SecurityContext securityContext) {
		service.validate(sendGridServerCreate, securityContext);

		return service.createSendGridServer(sendGridServerCreate,
				securityContext);
	}

	@POST
	@Produces("application/json")
	@Operation(summary = "getAllSendGridServers", description = "Gets All SendGridServers Filtered")
	@Path("getAllSendGridServers")
	public PaginationResponse<SendGridServer> getAllSendGridServers(
			@HeaderParam("authenticationKey") String authenticationKey,
			SendGridServerFiltering filtering,
			@Context SecurityContext securityContext) {
		service.validateFiltering(filtering, securityContext);

		return service.getAllSendGridServers(securityContext, filtering);
	}

}
