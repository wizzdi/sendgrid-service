package com.flexicore.sendgrid.response;

public class ImportTemplatesResponse {

    private boolean ok;
    private int created;
    private int updated;
    private int deleted;
    private int unchanged;

    public int getCreated() {
        return created;
    }

    public <T extends ImportTemplatesResponse> T setCreated(int created) {
        this.created = created;
        return (T) this;
    }

    public int getUpdated() {
        return updated;
    }

    public <T extends ImportTemplatesResponse> T setUpdated(int updated) {
        this.updated = updated;
        return (T) this;
    }

    public int getDeleted() {
        return deleted;
    }

    public <T extends ImportTemplatesResponse> T setDeleted(int deleted) {
        this.deleted = deleted;
        return (T) this;
    }

    public int getUnchanged() {
        return unchanged;
    }

    public <T extends ImportTemplatesResponse> T setUnchanged(int unchanged) {
        this.unchanged = unchanged;
        return (T) this;
    }

    public boolean isOk() {
        return ok;
    }

    public <T extends ImportTemplatesResponse> T setOk(boolean ok) {
        this.ok = ok;
        return (T) this;
    }

    @Override
    public String toString() {
        return "ImportTemplatesResponse{" +
                "ok=" + ok +
                ", created=" + created +
                ", updated=" + updated +
                ", deleted=" + deleted +
                ", unchanged=" + unchanged +
                '}';
    }
}
