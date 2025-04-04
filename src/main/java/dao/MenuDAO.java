/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author nguye
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model.Menu;

public class MenuDAO {

    // Method to get list of Menu by table ID
    public static ArrayList<Menu> getListMenuByTable(int id) {
        ArrayList<Menu> listMenu = new ArrayList<>();
        
        String query = "SELECT s.serviceName, bi.count, s.price, s.price * bi.count AS totalPrice "
                + "FROM billInf AS bi, Bill AS b, serviceitem AS s "
                + "WHERE bi.billId = b.billId AND bi.serviceId = s.serviceId AND b.status = 1 AND b.tableId = "+id;
        
        try (ResultSet resultSet = DbOperations.getData(query)) {
            while (resultSet.next()) {
                
                String serviceName = resultSet.getString("serviceName");
                int count = resultSet.getInt("count");
                float price = resultSet.getFloat("price");
                float totalPrice = resultSet.getFloat("totalPrice");

                // Create a Menu object and add it to the list
                Menu menu = new Menu(serviceName, count, price, totalPrice);
                listMenu.add(menu);
            }
        } catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
            e.printStackTrace(); // In chi tiết lỗi ra console
        }
        
        return listMenu;
    }
}
