package com.saasmgr.starter.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;

public class Token {
    @JsonIgnore
    private final ZonedDateTime expireDate;

    @JsonProperty("access_token")
    private final String accessToken;

    @JsonProperty("token_type")
    private final String tokenType;

    @JsonProperty("expires_in")
    private final Integer expiresIn;

    @JsonCreator
    public Token(@JsonProperty("token_type") String tokenType,
                 @JsonProperty("access_token") String accessToken,
                 @JsonProperty("expires_in") Integer expiresIn) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.expireDate = ZonedDateTime.now().plusSeconds(expiresIn);
    }

    private Token(Builder builder){
        this(builder.tokenType, builder.accessToken, builder.expiresIn);
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getTokenType() {
        return this.tokenType;
    }

    public Integer getExpiresIn() {
        return this.expiresIn;
    }

    public ZonedDateTime getExpireDate() {
        return expireDate;
    }

    public boolean isExpired(){
        return ZonedDateTime.now().isAfter(this.expireDate);
    }

    public static Builder newBuilder(){
        return new Builder();
    }

    public static class Builder {
        private String accessToken;
        private String tokenType;
        private Integer expiresIn;

        public Builder setAccessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder setTokenType(String tokenType) {
            this.tokenType = tokenType;
            return this;
        }

        public Builder setExpiresIn(Integer expiresIn) {
            this.expiresIn = expiresIn;
            return this;
        }

        public Builder setExpiresNow() {
            return this.setExpiresIn((int)ZonedDateTime.now().toInstant().getEpochSecond());
        }

        private Builder(){}

        public Token build(){
            return new Token(this);
        }
    }
}
