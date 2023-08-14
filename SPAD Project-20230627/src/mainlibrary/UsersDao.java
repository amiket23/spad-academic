/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainlibrary;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Logger;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author bikash
 */
public class UsersDao {

    public static boolean validate(String name, String password) {
        boolean status = false;
        byte[] storedpass = null;
        byte[] salt = null;
        try {
            Connection con = DB.getConnection();
            String select = "select * from Users where UserName= '" + name +"'";
            String select2 = "select * from salt where UserName= '" + name +"'";
            Statement selectStatement = con.createStatement();
            Statement selectStatement2 = con.createStatement();
            ResultSet rs = selectStatement.executeQuery(select);
            ResultSet rs2 = selectStatement2.executeQuery(select2);
            rs.absolute(1);
            rs2.absolute(1);
            storedpass = rs.getBytes("UserPass");
            salt = rs2.getBytes("salt");
            con.close();
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] UserPass = factory.generateSecret(spec).getEncoded();
            if (Arrays.equals(UserPass,storedpass)) {
                status = true;
            }
        } catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            Logger.getLogger(UsersDao.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }
        return status;
    }

    public static boolean CheckIfAlready(String UserName) {
        boolean status = false;
        try {
            Connection con = DB.getConnection();
            String select = "select * from Users where UserName= '" + UserName +"'";
            Statement selectStatement = con.createStatement();
            ResultSet rs = selectStatement.executeQuery(select);
            status = rs.next();
            con.close();
        } catch (Exception e) {
            Logger.getLogger(UsersDao.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }
        return status;

    }

    public static int AddUser(String User, byte[] UserPass, byte[] salt, String UserEmail, String Date) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        int status = 0;
        try {

            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement("insert into Users(UserPass,RegDate,UserName,Email) values(?,?,?,?)");
            ps.setBytes(1, UserPass);
            ps.setString(2, Date);
            ps.setString(3, User);
            ps.setString(4, UserEmail);
            PreparedStatement ps2 = con.prepareStatement("insert into salt(UserName,salt) values(?,?)");
            ps2.setString(1, User);
            ps2.setBytes(2, salt);
            status = ps2.executeUpdate();
            status = ps.executeUpdate();
            con.close();
        } catch (SQLException e) {
            Logger.getLogger(UsersDao.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }
        return status;

    }

}
