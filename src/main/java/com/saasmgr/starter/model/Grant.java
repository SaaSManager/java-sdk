package com.saasmgr.starter.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Grant {
    @JsonProperty("username")
    private final String username;

    @JsonProperty("password")
    private final String password;

    @JsonCreator
    public Grant(@JsonProperty("username") String username, @JsonProperty("password") String password){
        this.password = password;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
