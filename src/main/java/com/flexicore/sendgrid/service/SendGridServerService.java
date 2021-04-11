package com.flexicore.sendgrid.service;


import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.model.User;
import com.flexicore.product.interfaces.IEquipmentService;
import com.flexicore.product.interfaces.IExternalServerService;
import com.flexicore.security.SecurityContext;
import com.flexicore.sendgrid.dao.DynamicTemplateData;
import com.flexicore.sendgrid.dao.From;
import com.flexicore.sendgrid.dao.Personalization;
import com.flexicore.sendgrid.dao.To;
import com.flexicore.sendgrid.data.SendGridServerRepository;
import com.flexicore.sendgrid.model.SendGridServer;
import com.flexicore.sendgrid.model.SendGridServerFiltering;
import com.flexicore.sendgrid.model.SendGridTemplate;
import com.flexicore.sendgrid.model.SendMailRequest;
import com.flexicore.sendgrid.request.SendGridServerCreate;
import com.flexicore.sendgrid.request.SendGridServerUpdate;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.BadRequestException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@PluginInfo(version = 1)
@Extension
@Component
public class SendGridServerService implements ServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private SendGridServerRepository sendGridServerRepository;

	@PluginInfo(version = 1)
	@Autowired
	private IExternalServerService externalServerService;

	@PluginInfo(version = 1)
	@Autowired
	private IEquipmentService equipmentService;


	private static final Logger logger= LoggerFactory.getLogger(SendGridServerService.class);

	public PaginationResponse<SendGridServer> getAllSendGridServers(
			SecurityContext securityContext,
			SendGridServerFiltering filtering) {
		long count = countAllSendGridServers(securityContext, filtering);
		List<SendGridServer> list = listAllSendGridServers(
				securityContext, filtering);
		return new PaginationResponse<>(list, filtering, count);
	}

	public List<SendGridServer> listAllSendGridServers(
			SecurityContext securityContext,
			SendGridServerFiltering filtering) {
		return sendGridServerRepository.getAllSendGridServers(
				filtering, securityContext);
	}

	public Long countAllSendGridServers(SecurityContext securityContext,
			SendGridServerFiltering filtering) {
		return sendGridServerRepository.countAllSendGridServers(
				filtering, securityContext);
	}

	public boolean updateSendGridServerNoMerge(
			SendGridServerCreate createSendGridServer,
			SendGridServer sendGridServer) {
		boolean update = externalServerService.updateExternalServerNoMerge(
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



	public void validate(SendGridServerCreate createSendGridServer, SecurityContext securityContext) {
		equipmentService.validateEquipmentCreate(createSendGridServer, securityContext);

	}



	public void validateFiltering(SendGridServerFiltering filtering,
			SecurityContext securityContext) {
		equipmentService.validateFiltering(filtering, securityContext);

	}

	public SendGridServer createSendGridServer(
			SendGridServerCreate sendGridServerCreate,
			SecurityContext securityContext) {
		SendGridServer sendGridServer = createSendGridServerNoMerge(
				sendGridServerCreate, securityContext);
		sendGridServerRepository.merge(sendGridServer);
		return sendGridServer;
	}

	public SendGridServer createSendGridServerNoMerge(
			SendGridServerCreate sendGridServerCreate,
			SecurityContext securityContext) {
		SendGridServer sendGridServer = new SendGridServer(
				sendGridServerCreate.getName(), securityContext);
		updateSendGridServerNoMerge(sendGridServerCreate,
				sendGridServer);
		return sendGridServer;

	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c,
			List<String> batchString, SecurityContext securityContext) {
		return sendGridServerRepository.getByIdOrNull(id, c, batchString,
				securityContext);
	}

	public SendGridServer updateSendGridServer(
			SendGridServerUpdate sendGridServerUpdate,
			SecurityContext securityContext) {
		SendGridServer sendGridServer = sendGridServerUpdate
				.getSendGridServer();
		if (updateSendGridServerNoMerge(sendGridServerUpdate,
				sendGridServer)) {
			sendGridServerRepository.merge(sendGridServer);
		}
		return sendGridServer;
	}

	@Transactional
	public void merge(Object base) {
		sendGridServerRepository.merge(base);
	}


}
