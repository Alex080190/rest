package com.example.rest.DB;

import com.example.rest.Beans.User;

import java.sql.*;

public class ClientDBFunctions {
public Connection connectToDB(String dbName, String user, String password) {
    Connection conn = null;
    try {
//            Class.forName("org.postgresql.Driver");
//        conn = DriverManager.getConnection("jdbc:postgresql://172.17.0.1:5432/" + dbName, user, password);
        conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbName, user, password);
        if (conn != null) {
            System.out.println("Connection established");
        } else {
            System.out.println("Connection failed");
        }

    } catch (Exception e) {
        System.out.println(e);
    }
    return conn;
}
    public int insertUser(User user) {
        String firstTableQuery = "insert into firsttable (login, password, date) values (?, ?, ?)";
        String secondTableQuery = "insert into secondtable (login, email) values (?, ?)";
        int rows = 0;
        try (Connection connection = connectToDB("task", "user", "pass");
             PreparedStatement preparedStatement_1 = connection.prepareStatement(firstTableQuery);
             PreparedStatement preparedStatement_2 = connection.prepareStatement(secondTableQuery)) {

            preparedStatement_1.setString(1, user.getLogin());
            preparedStatement_1.setString(2, user.getPassword());
            preparedStatement_1.setString(3, user.getDate());

            preparedStatement_2.setString(1, user.getLogin());
            preparedStatement_2.setString(2, user.getEmail());

            rows = preparedStatement_1.executeUpdate() + preparedStatement_2.executeUpdate();
            System.out.println("User added");
        } catch (Exception e) {
            System.out.println(e);
        }
        return rows;
    }
    public User getUserByLogin(String login) throws SQLException {
        Statement statement = null;
        ResultSet rs = null;
        User user = null;
        Connection connection = connectToDB("task", "user", "pass");
        try {
            String query = String.format("select * from firsttable inner join secondtable " +
                    "on firsttable.login = secondtable.login where firsttable.login = '%s'", login);
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            rs.next();
            user = new User(rs.getString("login"), rs.getString("password"),
                    rs.getString("date"), rs.getString("email"));
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            statement.close();
            rs.close();
            connection.close();
        }
        return user;
    }


















//    public void createTableEmployee() {
//        Statement statement;
//        try {
//            String query = "create table client(id SERIAL, firstname varchar(30), lastname varchar(30)," +
//                    " birthdate varchar(30), email varchar(30), login varchar(30) unique, primary key(id));";
//            statement = connection.createStatement();
//            statement.executeUpdate(query);
//            System.out.println("Table created" );
//        }catch (Exception e) {
//            System.out.println(e);
//        }
//    }
//    public void deleteTableEmployee() {
//        Statement statement;
//        try {
//            String query = "drop table client";
//            statement =  connection.createStatement();
//            statement.executeUpdate(query);
//            System.out.println("Table deleted");
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }

}
