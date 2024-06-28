package com.HotelApp.domain.models.service;

public class UserRoleChangeRequest {
    private String command;
    private String encrypted;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getEncrypted() {
        return encrypted;
    }

    public void setEncrypted(String encrypted) {
        this.encrypted = encrypted;
    }
}