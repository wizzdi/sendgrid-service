package com.flexicore.sendgrid.service;


import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.product.interfaces.IEquipmentService;
import com.flexicore.security.SecurityContext;
import com.flexicore.sendgrid.data.SendGridTemplateRepository;
import com.flexicore.sendgrid.model.ImportTemplatesRequest;
import com.flexicore.sendgrid.model.SendGridServer;
import com.flexicore.sendgrid.model.SendGridTemplate;
import com.flexicore.sendgrid.model.SendGridTemplateFiltering;
import com.flexicore.sendgrid.request.SendGridTemplateCreate;
import com.flexicore.sendgrid.request.SendGridTemplateUpdate;
import com.flexicore.service.BaseclassNewService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.BadRequestException;
import java.util.List;
import java.util.logging.Logger;

@PluginInfo(version = 1)
@Extension
@Component
public class SendGridTemplateService implements ServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private SendGridTemplateRepository sendGridTemplateRepository;

	@Autowired
	private BaseclassNewService baseclassNewService;

	@Autowired
	private Logger logger;

	public PaginationResponse<SendGridTemplate> getAllSendGridTemplates(
			SecurityContext securityContext,
			SendGridTemplateFiltering filtering) {
		long count = countAllSendGridTemplates(securityContext, filtering);
		List<SendGridTemplate> list = listAllSendGridTemplates(
				securityContext, filtering);
		return new PaginationResponse<>(list, filtering, count);
	}

	public List<SendGridTemplate> listAllSendGridTemplates(
			SecurityContext securityContext,
			SendGridTemplateFiltering filtering) {
		return sendGridTemplateRepository.getAllSendGridTemplates(
				filtering, securityContext);
	}

	public Long countAllSendGridTemplates(SecurityContext securityContext,
			SendGridTemplateFiltering filtering) {
		return sendGridTemplateRepository.countAllSendGridTemplates(
				filtering, securityContext);
	}

	public boolean updateSendGridTemplateNoMerge(SendGridTemplateCreate createSendGridTemplate, SendGridTemplate sendGridTemplate) {
		boolean update = baseclassNewService.updateBaseclassNoMerge(createSendGridTemplate, sendGridTemplate);

		if (createSendGridTemplate.getExternalId() != null && !createSendGridTemplate.getExternalId().equals(sendGridTemplate.getExternalId())) {
			sendGridTemplate.setExternalId(createSendGridTemplate.getExternalId());
			update = true;
		}

		if (createSendGridTemplate.getSendGridServer() != null && (sendGridTemplate.getSendGridServer()==null||!createSendGridTemplate.getSendGridServer().getId().equals(sendGridTemplate.getSendGridServer().getId()))) {
			sendGridTemplate.setSendGridServer(createSendGridTemplate.getSendGridServer());
			update = true;
		}
		return update;

	}

	public void validate(ImportTemplatesRequest importTemplatesRequest,
						 SecurityContext securityContext) {
		String sendGridServerId=importTemplatesRequest.getSendGridServerId();
		SendGridServer sendGridServer=sendGridServerId!=null?getByIdOrNull(sendGridServerId,SendGridServer.class,null,securityContext):null;
		if(sendGridServer==null){
			throw new BadRequestException("No SendGridServer with id "+sendGridServerId);
		}
		importTemplatesRequest.setSendGridServer(sendGridServer);

	}

	public void validate(SendGridTemplateCreate createSendGridTemplate,
			SecurityContext securityContext) {
		baseclassNewService.validate(createSendGridTemplate,
				securityContext);
		String sendGridServerId=createSendGridTemplate.getSendGridServerId();
		SendGridServer sendGridServer=sendGridServerId!=null?getByIdOrNull(sendGridServerId,SendGridServer.class,null,securityContext):null;
		if(sendGridServer==null){
			throw new BadRequestException("No Sendgrid server with id "+sendGridServerId);
		}
		createSendGridTemplate.setSendGridServer(sendGridServer);

	}



	public void validateFiltering(SendGridTemplateFiltering filtering,
			SecurityContext securityContext) {
		baseclassNewService.validateFilter(filtering, securityContext);

	}

	public SendGridTemplate createSendGridTemplate(
			SendGridTemplateCreate sendGridTemplateCreate,
			SecurityContext securityContext) {
		SendGridTemplate sendGridTemplate = createSendGridTemplateNoMerge(
				sendGridTemplateCreate, securityContext);
		sendGridTemplateRepository.merge(sendGridTemplate);
		return sendGridTemplate;
	}

	public SendGridTemplate createSendGridTemplateNoMerge(
			SendGridTemplateCreate sendGridTemplateCreate,
			SecurityContext securityContext) {
		SendGridTemplate sendGridTemplate = new SendGridTemplate(
				sendGridTemplateCreate.getName(), securityContext);
		updateSendGridTemplateNoMerge(sendGridTemplateCreate,
				sendGridTemplate);
		return sendGridTemplate;

	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c,
			List<String> batchString, SecurityContext securityContext) {
		return sendGridTemplateRepository.getByIdOrNull(id, c, batchString,
				securityContext);
	}

	public SendGridTemplate updateSendGridTemplate(
			SendGridTemplateUpdate sendGridTemplateUpdate,
			SecurityContext securityContext) {
		SendGridTemplate sendGridTemplate = sendGridTemplateUpdate
				.getSendGridTemplate();
		if (updateSendGridTemplateNoMerge(sendGridTemplateUpdate,
				sendGridTemplate)) {
			sendGridTemplateRepository.merge(sendGridTemplate);
		}
		return sendGridTemplate;
	}

	@Transactional
	public void massMerge(List<?> toMerge) {
		sendGridTemplateRepository.massMerge(toMerge);
	}
}
