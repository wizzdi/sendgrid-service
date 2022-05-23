package com.flexicore.sendgrid.service;


import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.flexicore.security.SecurityContextBase;
import com.flexicore.sendgrid.data.SendGridServerRepository;
import com.flexicore.sendgrid.model.SendGridServer;
import com.flexicore.sendgrid.model.SendGridServerFiltering;
import com.flexicore.sendgrid.request.SendGridServerCreate;
import com.flexicore.sendgrid.request.SendGridServerUpdate;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.BaseclassService;
import com.wizzdi.flexicore.security.service.BasicService;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.metamodel.SingularAttribute;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@Extension
@Component
public class SendGridServerService implements Plugin {

	
	@Autowired
	private SendGridServerRepository sendGridServerRepository;
	@Autowired
	private BasicService basicService;


	private static final Logger logger= LoggerFactory.getLogger(SendGridServerService.class);

	public PaginationResponse<SendGridServer> getAllSendGridServers(
			SecurityContextBase securityContext,
			SendGridServerFiltering filtering) {
		long count = countAllSendGridServers(securityContext, filtering);
		List<SendGridServer> list = listAllSendGridServers(
				securityContext, filtering);
		return new PaginationResponse<>(list, filtering, count);
	}

	public List<SendGridServer> listAllSendGridServers(
			SecurityContextBase securityContext,
			SendGridServerFiltering filtering) {
		return sendGridServerRepository.getAllSendGridServers(
				filtering, securityContext);
	}

	public Long countAllSendGridServers(SecurityContextBase securityContext,
			SendGridServerFiltering filtering) {
		return sendGridServerRepository.countAllSendGridServers(
				filtering, securityContext);
	}

	public boolean updateSendGridServerNoMerge(
			SendGridServerCreate createSendGridServer,
			SendGridServer sendGridServer) {
		boolean update = basicService.updateBasicNoMerge(
				createSendGridServer, sendGridServer);

		if (createSendGridServer.getApiKey() != null
				&& !createSendGridServer.getApiKey().equals(
						sendGridServer.getApiKey())) {
			sendGridServer
					.setApiKey(createSendGridServer.getApiKey());
			update = true;
		}

		return update;

	}



	public void validate(SendGridServerCreate createSendGridServer, SecurityContextBase securityContext) {
		basicService.validate(createSendGridServer, securityContext);

	}



	public void validateFiltering(SendGridServerFiltering filtering,
			SecurityContextBase securityContext) {
		basicService.validate(filtering, securityContext);

	}

	public SendGridServer createSendGridServer(
			SendGridServerCreate sendGridServerCreate,
			SecurityContextBase securityContext) {
		SendGridServer sendGridServer = createSendGridServerNoMerge(
				sendGridServerCreate, securityContext);
		sendGridServerRepository.merge(sendGridServer);
		return sendGridServer;
	}

	public SendGridServer createSendGridServerNoMerge(
			SendGridServerCreate sendGridServerCreate,
			SecurityContextBase securityContext) {
		SendGridServer sendGridServer = new SendGridServer();
		sendGridServer.setId(UUID.randomUUID().toString());
		updateSendGridServerNoMerge(sendGridServerCreate, sendGridServer);
		BaseclassService.createSecurityObjectNoMerge(sendGridServer,securityContext);
		return sendGridServer;

	}

	public SendGridServer updateSendGridServer(
			SendGridServerUpdate sendGridServerUpdate,
			SecurityContextBase securityContext) {
		SendGridServer sendGridServer = sendGridServerUpdate
				.getSendGridServer();
		if (updateSendGridServerNoMerge(sendGridServerUpdate,
				sendGridServer)) {
			sendGridServerRepository.merge(sendGridServer);
		}
		return sendGridServer;
	}


	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContextBase securityContext) {
		return sendGridServerRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
		return sendGridServerRepository.getByIdOrNull(id, c, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return sendGridServerRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return sendGridServerRepository.listByIds(c, ids, baseclassAttribute, securityContext);
	}

	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return sendGridServerRepository.findByIds(c, ids, idAttribute);
	}

	public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
		return sendGridServerRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return sendGridServerRepository.findByIdOrNull(type, id);
	}

	@Transactional
	public void merge(Object base) {
		sendGridServerRepository.merge(base);
	}

	@Transactional
	public void massMerge(List<?> toMerge) {
		sendGridServerRepository.massMerge(toMerge);
	}
}
