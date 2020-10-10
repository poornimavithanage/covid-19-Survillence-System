package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB_connection {
    private static DB_connection db_connection;
    private Connection connection;

    private DB_connection(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/covid19","root","mysql");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static DB_connection getInstance(){
        return (db_connection==null) ? db_connection = new DB_connection():db_connection;

    }

    public Connection getConnection(){
        return connection;
    }
}
