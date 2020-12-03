package com.flexicore.sendgrid.rest;


import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.ProtectedREST;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.security.SecurityContext;
import com.flexicore.sendgrid.model.ImportTemplatesRequest;
import com.flexicore.sendgrid.model.SendGridTemplate;
import com.flexicore.sendgrid.model.SendGridTemplateFiltering;
import com.flexicore.sendgrid.request.SendGridTemplateCreate;
import com.flexicore.sendgrid.request.SendGridTemplateUpdate;
import com.flexicore.sendgrid.response.ImportTemplatesResponse;
import com.flexicore.sendgrid.service.SendGridService;
import com.flexicore.sendgrid.service.SendGridTemplateService;
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
@Path("plugins/SendGridTemplate")
@OpenAPIDefinition(tags = {@Tag(name = "SendGridTemplate", description = "SendGridTemplate Services"),})
@Tag(name = "SendGridTemplate")
@Extension
@Component
public class SendGridTemplateRESTService implements RestServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private SendGridTemplateService service;

	@PluginInfo(version = 1)
	@Autowired
	private SendGridService sendGridService;


	@POST
	@Produces("application/json")
	@Operation(summary = "getAllSendGridTemplates", description = "Gets All SendGridTemplates Filtered")
	@Path("getAllSendGridTemplates")
	public PaginationResponse<SendGridTemplate> getAllSendGridTemplates(
			@HeaderParam("authenticationKey") String authenticationKey,
			SendGridTemplateFiltering filtering,
			@Context SecurityContext securityContext) {
		service.validateFiltering(filtering, securityContext);

		return service.getAllSendGridTemplates(securityContext, filtering);
	}

	@POST
	@Produces("application/json")
	@Operation(summary = "importSendGridTemplates", description = "imports send grid templates")
	@Path("importSendGridTemplates")
	public ImportTemplatesResponse importSendGridTemplates(
			@HeaderParam("authenticationKey") String authenticationKey,
			ImportTemplatesRequest importTemplatesRequest,
			@Context SecurityContext securityContext) {
		service.validate(importTemplatesRequest, securityContext);
		return sendGridService.importSendGridTemplates(securityContext, importTemplatesRequest);
	}

}
