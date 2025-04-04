/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author nguye
 */
import java.sql.ResultSet;
import java.sql.SQLException;

public class Menu {
    private String serviceName;
    private int count;
    private float price;
    private float totalPrice;

    // Constructor
    public Menu(String serviceName, int count, float price, float totalPrice) {
        this.serviceName = serviceName;
        this.count = count;
        this.price = price;
        this.totalPrice = totalPrice;
    }

    // Constructor using ResultSet
    public Menu(ResultSet resultSet) throws SQLException {
        this.serviceName = resultSet.getString("serviceName");
        this.count = resultSet.getInt("count");
        this.price = resultSet.getFloat("price");
        this.totalPrice = resultSet.getFloat("totalPrice");
    }

    // Getters and Setters
    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
