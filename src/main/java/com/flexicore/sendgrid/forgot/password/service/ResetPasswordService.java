package com.flexicore.sendgrid.forgot.password.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.ResetUserPasswordRequest;
import com.flexicore.sendgrid.forgot.password.request.ResetPasswordRequest;
import com.flexicore.sendgrid.forgot.password.response.ResetPasswordResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.User;
import com.flexicore.request.UserFiltering;
import com.flexicore.sendgrid.dao.DynamicTemplateData;
import com.flexicore.sendgrid.dao.From;
import com.flexicore.sendgrid.dao.Personalization;
import com.flexicore.sendgrid.dao.SendMailRequest;
import com.flexicore.sendgrid.dao.To;
import com.flexicore.service.UserService;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.ws.rs.BadRequestException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;


@Component
@Extension
public class ResetPasswordService implements Plugin {

    private static final ObjectMapper objectMapper=new ObjectMapper();

    @Value("${flexicore.sendgrid.templateId:#{null}")
    private String templateId;

    @Value("${flexicore.sendgrid.mailSenderEmail:help@flexicore.io}")
    private String mailSenderEmail;
    @Value("${flexicore.sendgrid.mailSenderName:help@flexicore.io}")
    private String mailSenderName;
    @Value("${flexicore.sendgrid.apiKey:@null}")
    private String sendGridApiKey;

    private static final Logger logger= LoggerFactory.getLogger(ResetPasswordService.class);
    private SendGrid sendGrid=null;

    @Autowired
    private UserService userService;

    private SendGrid getSendGrid(){
        if(sendGrid==null){
            sendGrid=new SendGrid(sendGridApiKey);
        }
        return sendGrid;
    }


    private boolean sendResetPasswordMail(ResetPasswordRequest resetPasswordRequest) {
        User user = resetPasswordRequest.getUser();
        SendGrid sendGrid = getSendGrid();
        SendMailRequest sendMailRequest=new SendMailRequest()
                .withFrom(new From().withEmail(mailSenderEmail).withName(mailSenderName))
                .withSendAt(0)
                .withSubject("FlexiCore Password Reset")
                .withTemplateId(templateId)
                .withPersonalizations(Collections.singletonList(
                        new Personalization()
                                .withTo(Collections.singletonList(new To().withEmail(user.getEmail()).withName(user.getName())))
                        .withDynamicTemplateData(new DynamicTemplateData().withLink(resetPasswordRequest.getReturnLink()).withName(user.getName()))
                        ));


        Request request = new Request();
        try {
            String body=objectMapper.writeValueAsString(sendMailRequest);

            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(body);
            Response response = sendGrid.api(request);
            logger.info("forgot password mail response: " + response.getStatusCode() + "," + response.getBody());
            return true;

        } catch (IOException e) {
            logger.error("failed sending forgot password mail", e);
        }
        return false;
    }

    public void validate(ResetPasswordRequest resetPasswordRequest) {
        if(resetPasswordRequest.getEmail()==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"email must be provided");
        }
        if(resetPasswordRequest.getReturnLink()==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"return link must be provided");
        }
        List<User> users=userService.listAllUsers(new UserFiltering().setEmails(Collections.singleton(resetPasswordRequest.getEmail())),null);
        if(!users.isEmpty()){
            resetPasswordRequest.setUser(users.get(0));
        }

    }

    private String getResetPasswordLink(String returnLink,String verificationToken) {
        return returnLink+"?token="+verificationToken;
    }

    public ResetPasswordResponse resetPasswordStart(ResetPasswordRequest resetPasswordRequest) {
        ResetPasswordResponse resetPasswordResponse = new ResetPasswordResponse().setSent(true);
        if(resetPasswordRequest.getUser()==null){
            return resetPasswordResponse;
        }

        ResetPasswordResponse prepareResponse=userService.resetPasswordViaMailPrepare(new ResetUserPasswordRequest().setUser(resetPasswordRequest.getUser()));
        resetPasswordRequest.setReturnLink(getResetPasswordLink(resetPasswordRequest.getReturnLink(),prepareResponse.getVerificationToken()));
        sendResetPasswordMail(resetPasswordRequest);
        return resetPasswordResponse;
    }
}
