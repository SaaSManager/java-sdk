package com.saasmgr.starter.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.saasmgr.starter.model.Grant;
import com.saasmgr.starter.model.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController("helloWorld")
public class HelloWorldAPI {

    private static final Logger LOG = LoggerFactory.getLogger(HelloWorldAPI.class);
    private final SaaSManagerAPI saaSManagerAPI;

    @Autowired
    public HelloWorldAPI(SaaSManagerAPI saaSManagerAPI) {
        this.saaSManagerAPI = saaSManagerAPI;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String helloWorld(@RequestHeader("Authorization") String userToken) throws UnirestException {
        LOG.trace("Authorization header: {}", userToken);
        Integer errorNumber = saaSManagerAPI.validateUser(userToken).getStatus();

        if(errorNumber == 200)
            return "Token is valid.";
        else
            return "Token is invalid.";
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Token logIn(@RequestBody Grant grant) throws IOException, UnirestException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(saaSManagerAPI.logInUser(grant).getBody(), Token.class);
    }
}
