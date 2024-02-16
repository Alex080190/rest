package com.example.rest.Controller;

import com.example.rest.Beans.Credentials;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class ApiController {


    @GetMapping("/get")
    public String getPage() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        Credentials credentials = new Credentials("TestLogin", "TestPassword");

        return objectMapper.writeValueAsString(credentials);
    }
    @PostMapping("/post")
    public String getData(@RequestBody Credentials receivedCredentials) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        String login = receivedCredentials.getLogin();
        String password = receivedCredentials.getPassword();

        if (login == null || password == null) {
            throw new SecurityException("The entered data does not match the format, " +
                    "the correct format - " +
                    "login : Your login, " +
                    "password : Your password");
        }

        Credentials credentials = new Credentials(login, password);

        Date current = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String date = dateFormat.format(current);

        credentials.setDate(date);

        return objectMapper.writeValueAsString(credentials);
    }
}
