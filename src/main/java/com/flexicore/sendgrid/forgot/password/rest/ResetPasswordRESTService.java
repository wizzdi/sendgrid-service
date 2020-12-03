package com.flexicore.sendgrid.forgot.password.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.UnProtectedREST;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.sendgrid.forgot.password.request.ResetPasswordRequest;
import com.flexicore.sendgrid.forgot.password.response.ResetPasswordResponse;
import com.flexicore.sendgrid.forgot.password.service.ResetPasswordService;
import com.flexicore.interfaces.RestServicePlugin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by Asaf on 04/06/2017.
 */

@PluginInfo(version = 1)
@OperationsInside
@UnProtectedREST
@Path("plugins/resetPassword")
@Tag(name = "resetPassword")
@Extension
@Component
public class ResetPasswordRESTService implements RestServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private ResetPasswordService resetPasswordService;



	@POST
	@Produces("application/json")
	@Path("resetPasswordStart")
	@Operation(summary = "resetPasswordStart", description = "reset password start")
	public ResetPasswordResponse resetPasswordStart(
			ResetPasswordRequest resetPasswordRequest) {
		resetPasswordService.validate(resetPasswordRequest);
		return resetPasswordService.resetPasswordStart(resetPasswordRequest);
	}



}
