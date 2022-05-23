package com.flexicore.sendgrid.rest;


import com.flexicore.annotations.OperationsInside;
import com.flexicore.security.SecurityContextBase;
import com.flexicore.sendgrid.model.SendGridServer;
import com.flexicore.sendgrid.model.SendGridServerFiltering;
import com.flexicore.sendgrid.model.SendMailRequest;
import com.flexicore.sendgrid.request.SendGridServerCreate;
import com.flexicore.sendgrid.request.SendGridServerUpdate;
import com.flexicore.sendgrid.response.SendMailResponse;
import com.flexicore.sendgrid.service.SendGridServerService;
import com.flexicore.sendgrid.service.SendGridService;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Created by Asaf on 04/06/2017.
 */


@OperationsInside
@RequestMapping("SendGridServer")
@OpenAPIDefinition(tags = {@Tag(name = "SendGridServer", description = "SendGridServer Services")})
@Tag(name = "SendGridServer")
@Extension
@RestController
public class SendGridServerRESTService implements Plugin {

	
	@Autowired
	private SendGridServerService service;

	
	@Autowired
	private SendGridService sendGridService;

	@Operation(summary = "updateSendGridServer", description = "update SendGridServer ")
	@PutMapping("updateSendGridServer")
	public SendGridServer updateSendGridServer(

			@RequestBody  SendGridServerUpdate sendGridServerUpdate,
			@RequestAttribute SecurityContextBase securityContext) {
		String id = sendGridServerUpdate.getId();
		SendGridServer sendGridServer = id != null ? service
				.getByIdOrNull(id, SendGridServer.class, null,
						securityContext) : null;
		if (sendGridServer == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No SendGridServer with id "
					+ id);
		}
		sendGridServerUpdate.setSendGridServer(sendGridServer);
		service.validate(sendGridServerUpdate, securityContext);

		return service.updateSendGridServer(sendGridServerUpdate,
				securityContext);
	}

	@Operation(summary = "sendMail", description = "sends mail")
	@PostMapping("sendMail")
	public SendMailResponse sendMail(
			
			@RequestBody SendMailRequest sendGridServerUpdate,
			@RequestAttribute SecurityContextBase securityContext) {

		sendGridService.validate(sendGridServerUpdate, securityContext);
		return sendGridService.sendMail(sendGridServerUpdate, securityContext);
	}


	@Operation(summary = "createSendGridServer", description = "create SendGridServer ")
	@PostMapping("createSendGridServer")
	public SendGridServer createSendGridServer(
			
			@RequestBody SendGridServerCreate sendGridServerCreate,
			@RequestAttribute SecurityContextBase securityContext) {
		service.validate(sendGridServerCreate, securityContext);

		return service.createSendGridServer(sendGridServerCreate,
				securityContext);
	}


	@Operation(summary = "getAllSendGridServers", description = "Gets All SendGridServers Filtered")
	@PostMapping("getAllSendGridServers")
	public PaginationResponse<SendGridServer> getAllSendGridServers(
			@RequestBody  SendGridServerFiltering filtering,
			@RequestAttribute SecurityContextBase securityContext) {
		service.validateFiltering(filtering, securityContext);

		return service.getAllSendGridServers(securityContext, filtering);
	}

}
