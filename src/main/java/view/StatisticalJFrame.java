/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

/**
 *
 * @author ASUS
 */
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//dùng cho csdl
import java.sql.*;
import dao.ConnectionProvider;

import org.jfree.chart.ChartFactory; // Để tạo biểu đồ
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.plot.CategoryPlot;   // Lớp CategoryPlot        

import javax.swing.*;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;

public class StatisticalJFrame extends javax.swing.JFrame {

    /**
     * Creates new form StatisticalJFrame
     */
    private DefaultTableModel model;

    public StatisticalJFrame() {
        initComponents();

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

        // Thủy Lưu ý
        // ConnectionProvider.getConn() = hàm DatabaseConnection.connect() cũ m đã viết;
        try (Connection conn = ConnectionProvider.getConn(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

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

        // Thủy Lưu ý
        // ConnectionProvider.getConn() = hàm DatabaseConnection.connect() cũ m đã viết;
        try (Connection conn = ConnectionProvider.getConn(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
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

        // Thủy Lưu ý
        // ConnectionProvider.getConn() = hàm DatabaseConnection.connect() cũ m đã viết;
        try (Connection conn = ConnectionProvider.getConn(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

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

        jMenu2 = new javax.swing.JMenu();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenuStatistic = new javax.swing.JMenu();
        jMenuStatisticByDay = new javax.swing.JMenuItem();
        jMenuStatisticByMonth = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();

        jMenu2.setText("jMenu2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jMenu1.setText("File");

        jMenu3.setText("jMenu3");
        jMenu1.add(jMenu3);

        jMenuBar1.add(jMenu1);

        jMenuStatistic.setText("Thống kê");

        jMenuStatisticByDay.setText("Theo ngày");
        jMenuStatisticByDay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuStatisticByDayActionPerformed(evt);
            }
        });
        jMenuStatistic.add(jMenuStatisticByDay);

        jMenuStatisticByMonth.setText("Theo tháng");
        jMenuStatisticByMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuStatisticByMonthActionPerformed(evt);
            }
        });
        jMenuStatistic.add(jMenuStatisticByMonth);

        jMenuItem3.setText("Theo năm");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenuStatistic.add(jMenuItem3);

        jMenuBar1.add(jMenuStatistic);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 277, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuStatisticByDayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuStatisticByDayActionPerformed
        // TODO add your handling code here:
        StatisticByDay();
    }//GEN-LAST:event_jMenuStatisticByDayActionPerformed

    private void jMenuStatisticByMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuStatisticByMonthActionPerformed
        // TODO add your handling code here:
        StatisticsByMonth();
    }//GEN-LAST:event_jMenuStatisticByMonthActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        StatisticsByYear();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

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
            java.util.logging.Logger.getLogger(StatisticalJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StatisticalJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StatisticalJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StatisticalJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StatisticalJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenu jMenuStatistic;
    private javax.swing.JMenuItem jMenuStatisticByDay;
    private javax.swing.JMenuItem jMenuStatisticByMonth;
    // End of variables declaration//GEN-END:variables
}
