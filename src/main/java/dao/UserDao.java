/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import model.User;
/**
 *
 * @author nguye
 */
public class UserDao {
    public static void save(User user){
        String query = "insert into user(userName , phoneNum, status, password) "
                + "values('"+user.getName()+"','"+user.getPhoneNum()+"','false','"+user.getPassword()+"')";
        DbOperations.setDataOrDelete(query, "Dang ky thanh cong");
    }
}
