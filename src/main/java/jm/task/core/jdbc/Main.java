package jm.task.core.jdbc;

//import com.mysql.jdbc.FabricMySQLDriver;

import com.mysql.cj.jdbc.Driver;

import java.sql.*;


public class Main {

    private static final String URL = "jdbc:mysql://localhost:3306/usersdb";
    private static final String USER = "root";
    private static final String PASSWORD = "root";


    public static void main(String[] args) {


        Connection connection = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            //Driver driver = new FabricMySQLDriver();
            //DriverManager.registerDriver(driver);
            if (connection != null && !connection.isClosed()) {
                System.out.println("Соединение с БД установлено!");


            }
        } catch (ClassNotFoundException e) {
            System.err.println("Драйвер БД не найден: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Не удалось загрузить класс драйвера");
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("Соединение с БД закрыто.");
                } catch (SQLException e) {
                    System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
                }
            }
        }

    }
}
