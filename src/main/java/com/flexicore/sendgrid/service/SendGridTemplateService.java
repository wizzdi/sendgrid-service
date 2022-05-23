package com.flexicore.sendgrid.service;


import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.flexicore.model.SecuredBasic_;
import com.flexicore.security.SecurityContextBase;
import com.flexicore.sendgrid.data.SendGridTemplateRepository;
import com.flexicore.sendgrid.model.ImportTemplatesRequest;
import com.flexicore.sendgrid.model.SendGridServer;
import com.flexicore.sendgrid.model.SendGridTemplate;
import com.flexicore.sendgrid.model.SendGridTemplateFiltering;
import com.flexicore.sendgrid.request.SendGridTemplateCreate;
import com.flexicore.sendgrid.request.SendGridTemplateUpdate;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.BaseclassService;
import com.wizzdi.flexicore.security.service.BasicService;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.metamodel.SingularAttribute;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@Extension
@Component
public class SendGridTemplateService implements Plugin {

	
	@Autowired
	private SendGridTemplateRepository sendGridTemplateRepository;

	@Autowired
	private BasicService basicService;

	private static final Logger logger = LoggerFactory.getLogger(SendGridTemplateService.class);

	public PaginationResponse<SendGridTemplate> getAllSendGridTemplates(
			SecurityContextBase securityContext,
			SendGridTemplateFiltering filtering) {
		long count = countAllSendGridTemplates(securityContext, filtering);
		List<SendGridTemplate> list = listAllSendGridTemplates(
				securityContext, filtering);
		return new PaginationResponse<>(list, filtering, count);
	}

	public List<SendGridTemplate> listAllSendGridTemplates(
			SecurityContextBase securityContext,
			SendGridTemplateFiltering filtering) {
		return sendGridTemplateRepository.getAllSendGridTemplates(
				filtering, securityContext);
	}

	public Long countAllSendGridTemplates(SecurityContextBase securityContext,
										  SendGridTemplateFiltering filtering) {
		return sendGridTemplateRepository.countAllSendGridTemplates(
				filtering, securityContext);
	}

	public boolean updateSendGridTemplateNoMerge(SendGridTemplateCreate createSendGridTemplate, SendGridTemplate sendGridTemplate) {
		boolean update = basicService.updateBasicNoMerge(createSendGridTemplate, sendGridTemplate);

		if (createSendGridTemplate.getExternalId() != null && !createSendGridTemplate.getExternalId().equals(sendGridTemplate.getExternalId())) {
			sendGridTemplate.setExternalId(createSendGridTemplate.getExternalId());
			update = true;
		}

		if (createSendGridTemplate.getSendGridServer() != null && (sendGridTemplate.getSendGridServer() == null || !createSendGridTemplate.getSendGridServer().getId().equals(sendGridTemplate.getSendGridServer().getId()))) {
			sendGridTemplate.setSendGridServer(createSendGridTemplate.getSendGridServer());
			update = true;
		}
		return update;

	}

	public void validate(ImportTemplatesRequest importTemplatesRequest,
						 SecurityContextBase securityContext) {
		String sendGridServerId = importTemplatesRequest.getSendGridServerId();
		SendGridServer sendGridServer = sendGridServerId != null ? getByIdOrNull(sendGridServerId, SendGridServer.class,SecuredBasic_.security,  securityContext) : null;
		if (sendGridServer == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No SendGridServer with id " + sendGridServerId);
		}
		importTemplatesRequest.setSendGridServer(sendGridServer);

	}

	public void validate(SendGridTemplateCreate createSendGridTemplate,
						 SecurityContextBase securityContext) {
		basicService.validate(createSendGridTemplate,
				securityContext);
		String sendGridServerId = createSendGridTemplate.getSendGridServerId();
		SendGridServer sendGridServer = sendGridServerId != null ? getByIdOrNull(sendGridServerId, SendGridServer.class, SecuredBasic_.security, securityContext) : null;
		if (sendGridServer == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No Sendgrid server with id " + sendGridServerId);
		}
		createSendGridTemplate.setSendGridServer(sendGridServer);

	}


	public void validateFiltering(SendGridTemplateFiltering filtering,
								  SecurityContextBase securityContext) {
		basicService.validate(filtering, securityContext);

	}

	public SendGridTemplate createSendGridTemplate(
			SendGridTemplateCreate sendGridTemplateCreate,
			SecurityContextBase securityContext) {
		SendGridTemplate sendGridTemplate = createSendGridTemplateNoMerge(
				sendGridTemplateCreate, securityContext);
		sendGridTemplateRepository.merge(sendGridTemplate);
		return sendGridTemplate;
	}

	public SendGridTemplate createSendGridTemplateNoMerge(
			SendGridTemplateCreate sendGridTemplateCreate,
			SecurityContextBase securityContext) {
		SendGridTemplate sendGridTemplate = new SendGridTemplate();
		sendGridTemplate.setId(UUID.randomUUID().toString());
		updateSendGridTemplateNoMerge(sendGridTemplateCreate,
				sendGridTemplate);
		BaseclassService.createSecurityObjectNoMerge(sendGridTemplate,securityContext);
		return sendGridTemplate;

	}

	public SendGridTemplate updateSendGridTemplate(
			SendGridTemplateUpdate sendGridTemplateUpdate,
			SecurityContextBase securityContext) {
		SendGridTemplate sendGridTemplate = sendGridTemplateUpdate
				.getSendGridTemplate();
		if (updateSendGridTemplateNoMerge(sendGridTemplateUpdate,
				sendGridTemplate)) {
			sendGridTemplateRepository.merge(sendGridTemplate);
		}
		return sendGridTemplate;
	}


	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContextBase securityContext) {
		return sendGridTemplateRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
		return sendGridTemplateRepository.getByIdOrNull(id, c, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return sendGridTemplateRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return sendGridTemplateRepository.listByIds(c, ids, baseclassAttribute, securityContext);
	}

	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return sendGridTemplateRepository.findByIds(c, ids, idAttribute);
	}

	public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
		return sendGridTemplateRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return sendGridTemplateRepository.findByIdOrNull(type, id);
	}

	@Transactional
	public void merge(Object base) {
		sendGridTemplateRepository.merge(base);
	}

	@Transactional
	public void massMerge(List<?> toMerge) {
		sendGridTemplateRepository.massMerge(toMerge);
	}
}
