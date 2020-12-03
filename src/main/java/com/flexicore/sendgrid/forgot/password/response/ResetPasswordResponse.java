package com.flexicore.sendgrid.forgot.password.response;

public class ResetPasswordResponse {
    private boolean sent;

    public boolean isSent() {
        return sent;
    }

    public <T extends ResetPasswordResponse> T setSent(boolean sent) {
        this.sent = sent;
        return (T) this;
    }
}
