package com.example.rest.Controller;

import com.example.rest.Beans.User;
import com.example.rest.DB.ClientDBFunctions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.stream.Stream;

@RestController
public class ApiController {


    @GetMapping("/get")
    public String getPage(@RequestParam String login) throws IOException, SQLException {
        ObjectMapper objectMapper = new ObjectMapper();
        ClientDBFunctions userFromDB = new ClientDBFunctions();

        User user = userFromDB.getUserByLogin(login);
        if (user == null) {
            return "No such login in DB \n" + new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        File file = new File("users.csv");
        FileWriter writer = new FileWriter(file, true);

//        if (file.length() == 0) {
//            String firstLine = "login,password,date,email\n";
//            writer.write(firstLine);
//        }

        String stringToWrite = user.getLogin() + "," + user.getPassword() + "," + user.getDate() + "," + user.getEmail() + "\n";
        writer.write(stringToWrite);
        writer.flush();
        writer.close();

        return objectMapper.writeValueAsString(user);
    }
    @GetMapping("/get/random")
    public String getRandomString() throws IOException, SQLException {

        Random random = new Random();
        String readedString;

        if(!(new File("users.csv").exists())) {
            return "File is empty";
        }

        try(Stream<String> lines = Files.lines(Paths.get("users.csv"))) {
            int countLines = (int) Files.lines(Paths.get("users.csv")).count();
            int randomString = random.nextInt(1, countLines + 1);
            readedString = lines.skip(randomString - 1).findFirst().get();
        }
        return readedString;
    }

    @PostMapping("/post")
    public String getData(@RequestBody User receivedUser) throws JsonProcessingException, SQLException {
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(current);

        user.setDate(date);
        System.out.println(user);
        ClientDBFunctions userToDB = new ClientDBFunctions();

        int isAdded =  userToDB.insertUser(user);

        if (isAdded == 0) {
            return "User with such id already exists";
        }

        return objectMapper.writeValueAsString(user);
    }
}
