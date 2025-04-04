/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import dao.BillDAO;
import model.TableEntity;
import dao.TableDao;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EventListener;
import javax.swing.JButton;
import javax.swing.*;
import model.Billinf;
import dao.BillInfDAO;
import dao.ConnectionProvider;
import model.Menu;
import dao.MenuDAO;
import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;


import org.jfree.chart.ChartFactory; // Để tạo biểu đồ
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.plot.CategoryPlot;   // Lớp CategoryPlot        

import org.jfree.chart.renderer.category.LineAndShapeRenderer;

/**
 *
 * @author nguye
 */
public class JHome extends javax.swing.JFrame {

    public static DefaultTableModel tbill;

    /**
     * Creates new form JHome
     */
    public JHome() {
        initComponents();
        loadTable();

    }
    
    
        //Biểu đồ Đường (Line Chart)
    private void createAndShowChart(DefaultCategoryDataset dataset, String chartTitle, String xAxisLabel, String yAxisLabel) {
        // Tạo biểu đồ đường với dataset
        JFreeChart chart = ChartFactory.createLineChart(
                chartTitle, // Tiêu đề biểu đồ
                xAxisLabel, // Nhãn trục X
                yAxisLabel, // Nhãn trục Y
                dataset, // Dữ liệu
                PlotOrientation.VERTICAL, // Hướng biểu đồ
                true, // Hiển thị chú thích
                true, // Hiển thị tooltip
                false // Không dùng URL
        );

        // Tùy chỉnh biểu đồ đường
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.white);  // Màu nền của biểu đồ
        plot.setDomainGridlinesVisible(true);  // Hiển thị các đường lưới trục X
        plot.setRangeGridlinesVisible(true);   // Hiển thị các đường lưới trục Y
        plot.setDomainGridlinePaint(Color.gray); // Màu các đường lưới

        // Tùy chỉnh các đường vẽ
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, Color.red);  // Màu sắc cho các đường vẽ
        // Tạo panel để hiển thị biểu đồ
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));

        // Tạo cửa sổ JFrame để hiển thị biểu đồ
        JFrame frame = new JFrame(chartTitle);

        // Tạo nút "Quay lại"
        JButton backButton = new JButton("Quay lại");
        backButton.setPreferredSize(new Dimension(100, 30));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Có thể đóng cửa sổ hoặc chuyển về màn hình trước đó
                frame.dispose();  // Đóng cửa sổ hiện tại
            }
        });

        // Tạo một JPanel để chứa biểu đồ và nút "Quay lại"
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(chartPanel, BorderLayout.CENTER);
        panel.add(backButton, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    public void StatisticByDay() {
        // Lọc dữ liệu trong tháng và năm hiện tại
        String sql = "SELECT DAY(timeStart) AS day, SUM(total) AS total_amount FROM bill "
                + "WHERE YEAR(timeStart) = YEAR(CURDATE()) AND MONTH(timeStart) = MONTH(CURDATE()) "
                + "GROUP BY DAY(timeStart)";

        // Tạo dataset cho biểu đồ
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        
        try (Connection conn = ConnectionProvider.getConn(); 
                Statement stmt = conn.createStatement(); 
                ResultSet rs = stmt.executeQuery(sql)) {

            // Lấy dữ liệu từ ResultSet và thêm vào dataset
            while (rs.next()) {
                String day = rs.getString("day");
                double totalAmount = rs.getDouble("total_amount");
                dataset.addValue(totalAmount, "Tổng số tiền", day); // Dữ liệu cho biểu đồ
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Gọi phương thức chung để tạo và hiển thị biểu đồ
        createAndShowChart(dataset, "Thống kê Tổng Số Tiền Theo Ngày", "Ngày", "Tổng Số Tiền");
    }

    public void StatisticsByMonth() {
        // Lọc dữ liệu trong năm hiện tại
        String sql = "SELECT MONTH(timeStart) AS month, SUM(total) AS total_amount FROM bill "
                + "WHERE YEAR(timeStart) = YEAR(CURDATE()) "
                + "GROUP BY MONTH(timeStart)";

        // Tạo dataset cho biểu đồ
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        
        try (Connection conn = ConnectionProvider.getConn(); 
                Statement stmt = conn.createStatement(); 
                ResultSet rs = stmt.executeQuery(sql)) {
            // Lấy dữ liệu từ ResultSet và thêm vào dataset
            while (rs.next()) {
                int month = rs.getInt("month");
                double totalAmount = rs.getDouble("total_amount");
                dataset.addValue(totalAmount, "Tổng số tiền", "Tháng " + month); // Dữ liệu cho biểu đồ
            }
           
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Gọi phương thức chung để tạo và hiển thị biểu đồ
        createAndShowChart(dataset, "Thống kê Tổng Số Tiền Theo Tháng", "Tháng", "Tổng Số Tiền");
    }

    public void StatisticsByYear() {
        // Cập nhật câu lệnh SQL để nhóm theo năm
        String sql = "SELECT YEAR(timeStart) AS year, SUM(total) AS total_amount FROM bill GROUP BY YEAR(timeStart)";

        // Tạo dataset cho biểu đồ
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        
        try (Connection conn = ConnectionProvider.getConn(); 
                Statement stmt = conn.createStatement(); 
                ResultSet rs = stmt.executeQuery(sql)) {

            // Lấy dữ liệu từ ResultSet và thêm vào dataset
            while (rs.next()) {
                int year = rs.getInt("year");
                double totalAmount = rs.getDouble("total_amount");
                dataset.addValue(totalAmount, "Tổng số tiền", "Năm " + year); // Dữ liệu cho biểu đồ
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Gọi phương thức chung để tạo và hiển thị biểu đồ
        createAndShowChart(dataset, "Thống kê Tổng Số Tiền Theo Năm", "Năm", "Tổng Số Tiền");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jRadioButtonMenuItem1 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItem2 = new javax.swing.JRadioButtonMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        flTable = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbBill = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jComboBox3 = new javax.swing.JComboBox<>();
        jPanel7 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jSpinner1 = new javax.swing.JSpinner();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenuStatisticalDay = new javax.swing.JMenuItem();
        jMenuStatisticalMonth = new javax.swing.JMenuItem();
        jMenuStatisticalYear = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();

        jRadioButtonMenuItem1.setSelected(true);
        jRadioButtonMenuItem1.setText("jRadioButtonMenuItem1");

        jRadioButtonMenuItem2.setSelected(true);
        jRadioButtonMenuItem2.setText("jRadioButtonMenuItem2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Home");
        setLocation(new java.awt.Point(100, 50));
        setPreferredSize(new java.awt.Dimension(1000, 750));

        jPanel4.setBackground(new java.awt.Color(204, 204, 255));

        flTable.setBackground(new java.awt.Color(0, 204, 204));
        flTable.setAutoscrolls(true);
        flTable.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(flTable, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(flTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel5.setBackground(new java.awt.Color(102, 102, 255));

        tbBill.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tên món", "Số lượng", "Đơn Giá", "Thành tiền"
            }
        ));
        tbBill.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                tbBillComponentShown(evt);
            }
        });
        jScrollPane2.setViewportView(tbBill);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
        );

        jPanel6.setBackground(new java.awt.Color(0, 51, 204));

        jButton2.setText("Chuyển bàn");

        jButton3.setText("Thanh Toán");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 260, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 6, Short.MAX_VALUE))
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE))
                .addContainerGap())
        );

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton1.setText("Thêm dịch vụ");

        jSpinner1.setValue(1);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(87, 87, 87)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton4.setText("jButton4");

        jButton5.setText("jButton5");

        jMenu1.setText("Quản lý bàn");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Quản lý dịch vụ");
        jMenuBar1.add(jMenu2);

        jMenu3.setText("Thống kê");

        jMenuStatisticalDay.setText("Theo ngày");
        jMenuStatisticalDay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuStatisticalDayActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuStatisticalDay);

        jMenuStatisticalMonth.setText("Theo tháng");
        jMenuStatisticalMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuStatisticalMonthActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuStatisticalMonth);

        jMenuStatisticalYear.setText("Theo năm");
        jMenuStatisticalYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuStatisticalYearActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuStatisticalYear);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Thông tin tài khoản");
        jMenuBar1.add(jMenu4);

        jMenu5.setText("Log out");
        jMenuBar1.add(jMenu5);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void tbBillComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_tbBillComponentShown
        // TODO add your handling code here:F
    }//GEN-LAST:event_tbBillComponentShown

    private void jMenuStatisticalDayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuStatisticalDayActionPerformed
        // TODO add your handling code here:
        StatisticByDay();
    }//GEN-LAST:event_jMenuStatisticalDayActionPerformed

    private void jMenuStatisticalMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuStatisticalMonthActionPerformed
        // TODO add your handling code here:
        StatisticsByMonth();
    }//GEN-LAST:event_jMenuStatisticalMonthActionPerformed

    private void jMenuStatisticalYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuStatisticalYearActionPerformed
        // TODO add your handling code here:
        StatisticsByYear();
    }//GEN-LAST:event_jMenuStatisticalYearActionPerformed
    
    private void loadTable() {
        ArrayList<TableEntity> tableList = (ArrayList<TableEntity>) TableDao.getAllTables();
        if (flTable == null) {
            System.out.println("Error: flTable is not initialized.");
            return;
        }
        // Xóa các nút cũ trước khi tải lại
        flTable.removeAll();
        for (TableEntity table : tableList) {
            String txt = "<html>" + table.getName() + "<br>" + table.getStatus() + "</html>";
            JButton btn = new JButton(txt);
            btn.setPreferredSize(new Dimension(90, 90));
            flTable.add(btn);
            switch (table.getStatus()) {
                case "trống":
                    btn.setBackground(Color.LIGHT_GRAY);
                    break;
                default:
                    btn.setBackground(Color.GREEN);
                    throw new AssertionError();
            }
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Nut Da Duoc Bam");
                    ShowBill(table.getTableID());
                }
            });
        }
        flTable.revalidate();
        flTable.repaint();

    }

    void ShowBill(int id) {
        tbill = (DefaultTableModel) tbBill.getModel();
        tbill.setRowCount(0);
        ArrayList<Menu> listBillInf = MenuDAO.getListMenuByTable(id);
        for (Menu item : listBillInf) {
            System.out.print(item.getServiceName());
            tbill.addRow(new Object[]{item.getServiceName(), item.getCount(), item.getPrice(), item.getTotalPrice()});
        }
        tbBill.revalidate();
        tbBill.repaint();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JHome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JHome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JHome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JHome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JHome().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel flTable;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuStatisticalDay;
    private javax.swing.JMenuItem jMenuStatisticalMonth;
    private javax.swing.JMenuItem jMenuStatisticalYear;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem1;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTable tbBill;
    // End of variables declaration//GEN-END:variables
}
