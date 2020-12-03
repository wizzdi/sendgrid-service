package com.flexicore.sendgrid.response;

public class SendMailResponse {

    private boolean ok;
    private String message;

    public boolean isOk() {
        return ok;
    }

    public <T extends SendMailResponse> T setOk(boolean ok) {
        this.ok = ok;
        return (T) this;
    }

    public String getMessage() {
        return message;
    }

    public <T extends SendMailResponse> T setMessage(String message) {
        this.message = message;
        return (T) this;
    }
}
