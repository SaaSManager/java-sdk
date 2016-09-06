package com.saasmgr.starter.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.saasmgr.starter.auth.SaaSMgrTenantTokenProvider;
import com.saasmgr.starter.model.Grant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaaSManagerAPI {

    Logger logger = LoggerFactory.getLogger(SaaSManagerAPI.class);
    private final SaaSMgrTenantTokenProvider tokenProvider;

    @Autowired
    public SaaSManagerAPI(SaaSMgrTenantTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    public HttpResponse<String> validateUser(String userToken) throws UnirestException {
        String tenantToken = tokenProvider.getToken().getAccessToken();

        return Unirest
                .get("https://app.dev.saasmgr.com/userapp-auth/appUsers/oauth/tokeninfo?access_token={tenant_token}&token={user_token}")
                .routeParam("tenant_token", tenantToken)
                .routeParam("user_token", userToken)
                .header("content-type", "application/json")
                .header("cache-control", "no-cache")
                .asString();
    }

    public HttpResponse<String> logInUser(Grant grant) throws UnirestException, JsonProcessingException {
        String tenantToken = tokenProvider.getToken().getAccessToken();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(grant);

        return Unirest
                .post("https://app.dev.saasmgr.com/userapp-auth/appUsers/oauth/token?access_token={tenant_token}")
                .header("content-type", "application/json")
                .header("cache-control", "no-cache")
                .routeParam("tenant_token", tenantToken)
                .body(json)
                .asString();
    }
}
