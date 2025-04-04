/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import model.Billinf;
import model.TableEntity;

/**
 *
 * @author nguye
 */
public class BillInfDAO {

    public static ArrayList<Billinf> getListBillInf(int id) {
        ArrayList<Billinf> listBillinf = new ArrayList<>();
        ResultSet rs = DbOperations.getData("select * from billinf where billID = "+id);

        try {
            while (rs.next()) {
                Billinf billinf = new Billinf(rs);
                listBillinf.add(billinf);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            e.printStackTrace(); // In chi tiết lỗi ra console
        }

        return listBillinf;
    }
}
