package com.example.rest.Controller;

import com.example.rest.Beans.User;
import com.example.rest.DB.ClientDBFunctions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class ApiController {


    @GetMapping("/get")
    public String getPage(@RequestParam String login) throws JsonProcessingException, SQLException {
        ObjectMapper objectMapper = new ObjectMapper();
        ClientDBFunctions userFromDB = new ClientDBFunctions();

        User user = userFromDB.getUserByLogin(login);
        System.out.println(user);
        if (user == null) {
            return "No such login in DB \n" + new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return objectMapper.writeValueAsString(user);
    }
    @PostMapping("/post")
    public String getData(@RequestBody User receivedUser) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        String login = receivedUser.getLogin();
        String password = receivedUser.getPassword();
        String email = receivedUser.getEmail();

        if (login == null || password == null || email == null) {
            return "The entered data does not match the format, " +
                    "the correct format - " +
                    "login : Your login, " +
                    "password : Your password " +
                    "email: Your email\n" + new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user = new User(login, password, email);

        Date current = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String date = dateFormat.format(current);

        user.setDate(date);

        ClientDBFunctions userToDB = new ClientDBFunctions();

        userToDB.insertUser(user);

        return objectMapper.writeValueAsString(user);
    }
}
