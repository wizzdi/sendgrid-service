package com.flexicore.sendgrid.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.events.PluginsLoadedEvent;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.security.SecurityContext;
import com.flexicore.sendgrid.dao.*;
import com.flexicore.sendgrid.model.*;
import com.flexicore.sendgrid.model.SendMailRequest;
import com.flexicore.sendgrid.request.SendGridServerCreate;
import com.flexicore.sendgrid.request.SendGridTemplateCreate;
import com.flexicore.sendgrid.response.ImportTemplatesResponse;
import com.flexicore.sendgrid.response.SendMailResponse;
import com.flexicore.service.SecurityService;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.ws.rs.BadRequestException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@PluginInfo(version = 1)
@Extension
@Component
public class SendGridService implements ServicePlugin {

    @Value("${flexicore.sendgrid.apiKey:@null}")
    private String sendGridApiKey;

    private static final ObjectMapper objectMapper=new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    private static final Logger logger= LoggerFactory.getLogger(SendGridService.class);


    @Autowired
    @PluginInfo(version = 1)
    private SendGridServerService sendGridServerService;

    @Autowired
    @PluginInfo(version = 1)
    private SendGridTemplateService templateService;

    @Autowired
    private SecurityService securityService;

    private final Map<String,SendGrid> sendGridMap=new ConcurrentHashMap<>();



    @Async
    @EventListener
    public void init(PluginsLoadedEvent event){
        SecurityContext securityContext=securityService.getAdminUserSecurityContext();

        List<SendGridServer> servers=sendGridServerService.listAllSendGridServers(securityContext,new SendGridServerFiltering().setApiKeys(Collections.singleton(new ApiKeyRef(sendGridApiKey))));
        SendGridServerCreate sendGridServerCreate=new SendGridServerCreate()
                .setApiKey(sendGridApiKey)
                .setName("Default SendGrid Server");
        SendGridServer sendGridServer;
        if(servers.isEmpty()){
            sendGridServer=sendGridServerService.createSendGridServer(sendGridServerCreate,securityContext);
        }
        else{
            sendGridServer=servers.get(0);
           if(sendGridServerService.updateSendGridServerNoMerge(sendGridServerCreate,sendGridServer)){
                sendGridServerService.merge(sendGridServer);
           }
        }
        importSendGridTemplates(securityContext,new ImportTemplatesRequest().setSendGridServer(sendGridServer));
    }



    public SendMailResponse sendMail(SendMailRequest sendMailRequestModel, SecurityContext securityContext) {
        SendMailResponse sendMailResponse=new SendMailResponse();
        SendGridTemplate sendGridTemplate=sendMailRequestModel.getSendGridTemplate();
        SendGridServer sendGridServer=sendGridTemplate.getSendGridServer();
        SendGrid sendGrid= getSendGrid(sendGridServer);
        com.flexicore.sendgrid.dao.SendMailRequest sendMailRequest=new com.flexicore.sendgrid.dao.SendMailRequest()
                .withFrom(new From().withEmail(sendMailRequestModel.getEmailFrom()).withName(sendMailRequestModel.getEmailFrom()))
                .withSendAt(0)
                .withTemplateId(sendGridTemplate.getExternalId())
                .withPersonalizations(Collections.singletonList(
                        new Personalization()
                                .withTo(sendMailRequestModel.getEmailRefs().stream().map(f->new To().withEmail(f.getId()).withName(f.getId())).collect(Collectors.toList()))
                                .withDynamicTemplateData(sendMailRequestModel.any())
                ));


        Request request = new Request();
        try {
            String body=objectMapper.writeValueAsString(sendMailRequest);

            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(body);
            Response response = sendGrid.api(request);
            logger.info("forgot password mail response: " + response.getStatusCode() + "," + response.getBody());
            sendMailResponse.setOk(response.getStatusCode()==200).setMessage(response.getBody());

        } catch (IOException e) {
            logger.error("failed sending forgot password mail", e);
        }
        return sendMailResponse;
    }

    private SendGrid getSendGrid(SendGridServer sendGridServer) {
        return sendGridMap.computeIfAbsent(sendGridServer.getId(),f->new SendGrid(sendGridServer.getApiKey()));
    }

    public void validate(SendMailRequest sendMailRequest, SecurityContext securityContext) {
        String sendGridTemplateId=sendMailRequest.getSendGridTemplateId();
        SendGridTemplate sendGridServer=sendGridTemplateId!=null?sendGridServerService.getByIdOrNull(sendGridTemplateId,SendGridTemplate.class,null,securityContext):null;
        if(sendGridServer==null){
            throw new BadRequestException("No SendGridTemplate with id "+sendGridTemplateId);
        }
        sendMailRequest.setSendGridTemplate(sendGridServer);
        if(sendMailRequest.getEmailFrom()==null){
            throw new BadRequestException("Email from is mandatory");
        }
        if(sendMailRequest.getEmailRefs()==null || sendMailRequest.getEmailRefs().isEmpty()){
            throw new BadRequestException("Emails must be non null and not empty");
        }

    }

    public ImportTemplatesResponse importSendGridTemplates(SecurityContext securityContext, ImportTemplatesRequest importTemplatesRequest) {
        ImportTemplatesResponse importTemplatesResponse=new ImportTemplatesResponse();
        SendGridServer sendGridServer=importTemplatesRequest.getSendGridServer();
        try {
            SendGrid sendGrid = getSendGrid(sendGridServer);
            Request request = new Request();
            request.setMethod(Method.GET);
            request.setEndpoint("templates");
            request.addQueryParam("generations","dynamic");
            Response response = sendGrid.api(request);
            int statusCode = response.getStatusCode();

            if (statusCode == 200) {
                String body = response.getBody();
                Templates templates = objectMapper.readValue(body, Templates.class);
                importTemplatesResponse.setOk(true);
                Map<String,SendGridTemplate> existing=templateService.listAllSendGridTemplates(securityContext,new SendGridTemplateFiltering().setSendGridServers(Collections.singletonList(sendGridServer))).stream().collect(Collectors.toMap(f->f.getExternalId(),f->f,(a,b)->a));
                List<Object> toMerge=new ArrayList<>();
                Map<String,Template> templateMap = templates.getTemplates().stream().collect(Collectors.toMap(f->f.getId(),f->f,(a,b)->a));
                for (Template template : templateMap.values()) {
                    String id = template.getId();
                    SendGridTemplateCreate sendGridTemplateCreate=new SendGridTemplateCreate().setSendGridServer(sendGridServer).setExternalId(id).setName(template.getName());
                    SendGridTemplate sendGridTemplate=existing.get(id);
                    if(sendGridTemplate==null){
                        sendGridTemplate=templateService.createSendGridTemplateNoMerge(sendGridTemplateCreate,securityContext);
                        existing.put(sendGridTemplate.getExternalId(),sendGridTemplate);
                        toMerge.add(sendGridTemplate);
                        importTemplatesResponse.setCreated(importTemplatesResponse.getCreated()+1);
                    }
                    else{
                        if(templateService.updateSendGridTemplateNoMerge(sendGridTemplateCreate,sendGridTemplate)){
                            toMerge.add(sendGridTemplate);
                            importTemplatesResponse.setUpdated(importTemplatesResponse.getUpdated()+1);
                        }
                        else{
                            importTemplatesResponse.setUnchanged(importTemplatesResponse.getUnchanged()+1);
                        }
                    }
                }
                for (SendGridTemplate value : existing.values()) {
                    if(!templateMap.containsKey(value.getExternalId())){
                        value.setSoftDelete(true);
                        toMerge.add(value);
                        importTemplatesResponse.setDeleted(importTemplatesResponse.getDeleted()+1);
                    }
                }
                logger.info("Default server template import:" +importTemplatesResponse);
                templateService.massMerge(toMerge);

            }
            else{
                logger.error("Getting templates for sendgrid server returned "+statusCode +", body: "+response.getBody());
            }
        }
        catch (Exception e){
            logger.error("Unable to fetch templates for default sendgrid server",e);
        }
        return importTemplatesResponse;
    }
}
