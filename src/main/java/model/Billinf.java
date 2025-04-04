/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author nguye
 */
public class Billinf {
    private int billinfId;
    private int billId;
    private int serviceId;
    private int count;

    public Billinf(int billinfId, int billId, int serviceId, int count) {
        this.billinfId = billinfId;
        this.billId = billId;
        this.serviceId = serviceId;
        this.count = count;
    }
    public Billinf(ResultSet row) throws SQLException{
        this.billinfId = row.getInt("billinfId");
        this.billId = row.getInt("billId");
        this.serviceId = row.getInt("serviceId");
    }

    // Getters và Setters
    public int getBillInfId() { return billinfId; }
    public void setBillInfId(int billInfId) { this.billinfId = billInfId; }
    public int getBillId() { return billId; }
    public void setBillId(int billId) { this.billId = billId; }
    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }
    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
}
