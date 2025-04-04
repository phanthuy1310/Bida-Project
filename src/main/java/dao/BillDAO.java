/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.ResultSet;
import javax.swing.JOptionPane;
import model.TableEntity;
import model.Bill;

/**
 *
 * @author nguye
 */
public class BillDAO {

    public static int getUncheckBillIdByTableId(int id) {
        try {
            ResultSet rs = DbOperations.getData("select * from bill where tableID = " + id + " and status = 0");
            while (rs != null && rs.next()) {
                Bill bill = new Bill(rs);
                return bill.getId();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi lấy thông tin hóa đơn: " + e.getMessage());
            e.printStackTrace(); // In chi tiết lỗi ra console

        }
        return -1;
    }
}
