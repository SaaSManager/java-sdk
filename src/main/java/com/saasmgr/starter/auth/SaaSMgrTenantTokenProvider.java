package com.saasmgr.starter.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.saasmgr.starter.model.Grant;
import com.saasmgr.starter.model.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SaaSMgrTenantTokenProvider {

    private final Grant grant;
    public Token token;
    Logger logger = LoggerFactory.getLogger(SaaSMgrTenantTokenProvider.class);

    @Autowired
    private SaaSMgrTenantTokenProvider(@Value("${username}") String username,
                                       @Value("${password}") String password) {
        grant = new Grant(username, password);

        token = setToken();
    }

    public Token setToken() {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;
        Token tokenTemp = null;

        try {
            json = objectMapper.writeValueAsString(grant);
        } catch (JsonProcessingException e) {
            logger.error("Cannot make JSON {}", e);
        }

        try {
            HttpResponse<String> stringHttpResponse = Unirest
                    .post("https://app.dev.saasmgr.com/saasmgr-auth/tenantAuth/oauth/token")
                    .header("content-type", "application/json")
                    .header("cache-control", "no-cache")
                    .body(json)
                    .asString();

            String stringHttpResponseBody = stringHttpResponse.getBody();

            try {
                tokenTemp = objectMapper.readValue(stringHttpResponseBody, Token.class);
            } catch (IOException e) {
                logger.error("Cannot map into the object {}", e);
            }
        } catch (UnirestException e) {
            logger.error("Cannot connect with endpoint {}", e);
        }

        return tokenTemp;
    }

    public Token getToken() {
        synchronized (token){
            if(this.token == null) {
                this.token = setToken();
            }
            else if(this.token.isExpired()) {
                this.token = setToken();
            }
        }
        return token;
    }
}
