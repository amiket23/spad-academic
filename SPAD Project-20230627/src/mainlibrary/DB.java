/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainlibrary;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 *
 * @author bikash
 */
public class DB {

    public static String user = ConfigReader.getUsername();
    public static String host = ConfigReader.getHost();
    public static String port = ConfigReader.getPort();
    public static String connection = String.format("jdbc:mysql://%s:%s/library?autoReconnect=true&useSSL=True", host, port);
    
    public static Connection getConnection() {
        Connection con = null;
        try {
            Properties props = new Properties();
            String username = ConfigReader.getUsername();
            String password = ConfigReader.getPassword();
            props.put("user", username);
     //change the password to the password  ↓↓↓↓↓↓↓↓↓↓↓   you enteredwhen setting up mysql
            props.put("password", password);
            props.put("useUnicode", "true");
            props.put("useServerPrepStmts", "false"); // use client-side prepared statement
            props.put("characterEncoding", "UTF-8"); // ensure charset is utf8 here

            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(connection, props);
        } catch (SQLException | ClassNotFoundException e) {
            Logger.getLogger(DB.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }
        return con;
    }

}
