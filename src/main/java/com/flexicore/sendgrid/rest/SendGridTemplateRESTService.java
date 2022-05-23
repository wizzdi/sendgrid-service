package com.flexicore.sendgrid.rest;


import com.flexicore.annotations.OperationsInside;
import com.flexicore.security.SecurityContextBase;
import com.flexicore.sendgrid.model.ImportTemplatesRequest;
import com.flexicore.sendgrid.model.SendGridTemplate;
import com.flexicore.sendgrid.model.SendGridTemplateFiltering;
import com.flexicore.sendgrid.response.ImportTemplatesResponse;
import com.flexicore.sendgrid.service.SendGridService;
import com.flexicore.sendgrid.service.SendGridTemplateService;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Asaf on 04/06/2017.
 */


@OperationsInside
@RequestMapping("SendGridTemplate")
@OpenAPIDefinition(tags = {@Tag(name = "SendGridTemplate", description = "SendGridTemplate Services"),})
@Tag(name = "SendGridTemplate")
@Extension
@Component
public class SendGridTemplateRESTService implements Plugin {

	
	@Autowired
	private SendGridTemplateService service;

	
	@Autowired
	private SendGridService sendGridService;


	@Operation(summary = "getAllSendGridTemplates", description = "Gets All SendGridTemplates Filtered")
	@PostMapping("getAllSendGridTemplates")
	public PaginationResponse<SendGridTemplate> getAllSendGridTemplates(
			
			@RequestBody SendGridTemplateFiltering filtering,
			@RequestAttribute SecurityContextBase securityContext) {
		service.validateFiltering(filtering, securityContext);

		return service.getAllSendGridTemplates(securityContext, filtering);
	}

	@Operation(summary = "importSendGridTemplates", description = "imports send grid templates")
	@PostMapping("importSendGridTemplates")
	public ImportTemplatesResponse importSendGridTemplates(
			
			@RequestBody  ImportTemplatesRequest importTemplatesRequest,
			@RequestAttribute SecurityContextBase securityContext) {
		service.validate(importTemplatesRequest, securityContext);
		return sendGridService.importSendGridTemplates(securityContext, importTemplatesRequest);
	}

}
