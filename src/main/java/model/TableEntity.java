/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author nguye
 */
public class TableEntity {

    private int tableID;
    private String name;
    private int price;
    private String status;
    private String type;

    // Constructor không tham số (nếu cần)
    public TableEntity() {
    }

    // Constructor có tham số (nếu cần)
    public TableEntity(int tableID, String name, int price, String status, String type) {
        this.tableID = tableID;
        this.name = name;
        this.price = price;
        this.status = status;
        this.type = type;
    }

    // Getters và Setters
    public int getTableID() {
        return tableID;
    }

    public void setTableID(int tableID) {
        this.tableID = tableID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
