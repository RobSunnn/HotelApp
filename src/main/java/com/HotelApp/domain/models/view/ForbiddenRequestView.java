package com.HotelApp.domain.models.view;

import java.time.LocalDateTime;

public class ForbiddenRequestView {
    private String url;
    private String method;
    private String ip;
    private String username;
    private LocalDateTime timestamp;
    private boolean isChecked;

    public ForbiddenRequestView() {
    }

    public String getUrl() {
        return url;
    }

    public ForbiddenRequestView setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public ForbiddenRequestView setMethod(String method) {
        this.method = method;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public ForbiddenRequestView setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public ForbiddenRequestView setUsername(String username) {
        this.username = username;
        return this;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public ForbiddenRequestView setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public ForbiddenRequestView setChecked(boolean checked) {
        isChecked = checked;
        return this;
    }
}
