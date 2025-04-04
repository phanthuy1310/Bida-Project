/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import java.sql.*;
/**
 *
 * @author nguye
 */
public class ConnectionProvider {
    private static final String URL = "jdbc:mysql://localhost:3306/billiards";
    private static final String USER = "root"; // User mặc định của XAMPP MySQL
    private static final String PASSWORD = ""; // Mật khẩu mặc định của XAMPP MySQL là rỗng
    public static Connection getConn(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            return conn;
        } catch (Exception e) {
            System.out.println("Loi ket noi " + e.getMessage());
            return null;
        }
    }
}
