package com.HotelApp.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "forbidden_requests")
public class ForbiddenRequestEntity extends BaseEntity {

    private String url;
    private String method;
    private String ip;
    private String username;
    private LocalDateTime timestamp;
    private boolean isChecked;

    public ForbiddenRequestEntity() {
    }

    public String getUrl() {
        return url;
    }

    public ForbiddenRequestEntity setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public ForbiddenRequestEntity setMethod(String method) {
        this.method = method;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public ForbiddenRequestEntity setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public ForbiddenRequestEntity setUsername(String username) {
        this.username = username;
        return this;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public ForbiddenRequestEntity setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public ForbiddenRequestEntity setChecked(boolean checked) {
        isChecked = checked;
        return this;
    }
}