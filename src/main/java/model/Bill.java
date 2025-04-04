/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 *
 * @author nguye
 */
public class Bill {
    private int id;
    private int tableId;
    private Date timeStart;
    private Date timeEnd;
    private int status; // "pending", "paid"
    private double total;

    public Bill(int id, int tableId, Date timeStart, Date timeEnd, int status, double total) {
        this.id = id;
        this.tableId = tableId;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.status = status;
        this.total = total;
    }
    
    public Bill(ResultSet row) throws SQLException {
        this.id = row.getInt("id");
        this.timeStart = new Date(row.getTimestamp("timeStart").getTime());
        this.timeEnd = new Date (row.getTimestamp("timeEnd").getTime());
        this.status = row.getInt("status");
    }

    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getTableId() { return tableId; }
    public void setTableId(int tableId) { this.tableId = tableId; }
    public Date getTimeStart() { return timeStart; }
    public void setTimeStart(Date timeStart) { this.timeStart = timeStart; }
    public Date getTimeEnd() { return timeEnd; }
    public void setTimeEnd(Date timeEnd) { this.timeEnd = timeEnd; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}
