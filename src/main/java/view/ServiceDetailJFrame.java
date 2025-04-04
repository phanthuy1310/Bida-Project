/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;
import javax.swing.table.DefaultTableModel;//dùng cho DeafaultTableModel
import java.awt.event.MouseAdapter;
import javax.swing.JOptionPane;//alert message box
import java.awt.*;
import java.awt.event.*;//dùng khi click chuột
import java.util.List;
//su dung cho csdl
import java.sql.*;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
/**
 *
 * @author dungx
 */
public class ServiceDetailJFrame extends javax.swing.JFrame {
    private int categoryId;

    //khai bao thông tin kết nối csdl mysql
    private static final String URL = "jdbc:mysql://localhost:3306/billiards";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    //---
    private DefaultTableModel model;
    private int selectedRow = -1;//biến lưu dòng được chọn
    /**
     * Creates new form ServiceDetailJFrame
     */
    //khai bao thông tin kết nối csdl mysql
  

    public ServiceDetailJFrame(int categoryId) {
        this.categoryId = categoryId;
        initComponents();
        // Load dữ liệu các sản phẩm có cùng categoryId từ DB
        model = (DefaultTableModel) tableServiceItem.getModel();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        read2();
        tableServiceItem.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            fillText();
        }
    });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Hiển thị lại CategoryJFrame khi đóng ServiceDetailJFrame
                new CategoryJFrames().setVisible(true);
            }
        });
    }

    private ServiceDetailJFrame() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    private void read2() {
        // Kết nối tới cơ sở dữ liệu và thực hiện truy vấn
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT serviceID, serviceName, price, unit, stock FROM serviceitem WHERE categoryID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                // Gán giá trị categoryId cố định vào truy vấn
                stmt.setInt(1, categoryId);
                try (ResultSet rs = stmt.executeQuery()) {
                    // Lấy model của bảng giao diện dịch vụ
                    DefaultTableModel model = (DefaultTableModel) tableServiceItem.getModel();
                    model.setRowCount(0); // Xóa dữ liệu cũ

                    // Duyệt qua ResultSet và thêm dữ liệu vào model của bảng
                    while (rs.next()) {
                        int serviceId = rs.getInt("serviceID");
                        String serviceName = rs.getString("serviceName");
                        double price = rs.getDouble("price");
                        String unit = rs.getString("unit");
                        int stock = rs.getInt("stock");

                        model.addRow(new Object[]{serviceId, serviceName, price, unit, stock});
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu dịch vụ: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fillText(){
        selectedRow = tableServiceItem.getSelectedRow();
        if (selectedRow != -1){
            textServiceName.setText(tableServiceItem.getValueAt(selectedRow,1).toString());
            textPrice.setText(tableServiceItem.getValueAt(selectedRow,2).toString());
            textUnit.setText(tableServiceItem.getValueAt(selectedRow, 3).toString());
            textStock.setText(tableServiceItem.getValueAt(selectedRow, 4).toString());
        }   
    }
    

    //hàm hiển thị danh sách sinh viên
    public void read(){
        //clear toàn bộ dòng trong tableStudents
        model.setRowCount(0);
        //---
        String sql = "select * from category order by categoryID asc";
        try{
            Connection conn = DriverManager.getConnection(URL,USER,PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            //đưa dữ liệu vào tableCategory
            while(rs.next()){
                model.addRow(new Object[]{ rs.getInt("serviceID"),rs.getString("serviceName"), rs.getInt("price"), rs.getString("unit"), rs.getInt("stock")});
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    
    
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textServiceName = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        textPrice = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        textUnit = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        textStock = new javax.swing.JTextArea();
        btn_Add = new javax.swing.JButton();
        btn_Update = new javax.swing.JButton();
        btn_Delete = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        tableServiceItem = new javax.swing.JTable();
        btn_Search = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        textSearch = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Tên sản phẩm");

        jLabel2.setText("Giá tiền");

        jLabel3.setText("Đơn vị ");

        jLabel4.setText("Số lượng");

        textServiceName.setColumns(20);
        textServiceName.setRows(5);
        jScrollPane1.setViewportView(textServiceName);

        textPrice.setColumns(20);
        textPrice.setRows(5);
        jScrollPane2.setViewportView(textPrice);

        textUnit.setColumns(20);
        textUnit.setRows(5);
        jScrollPane3.setViewportView(textUnit);

        textStock.setColumns(20);
        textStock.setRows(5);
        jScrollPane4.setViewportView(textStock);

        btn_Add.setText("Thêm");
        btn_Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_AddActionPerformed(evt);
            }
        });

        btn_Update.setText("Sửa");
        btn_Update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_UpdateActionPerformed(evt);
            }
        });

        btn_Delete.setText("Xóa");
        btn_Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_DeleteActionPerformed(evt);
            }
        });

        tableServiceItem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID sản phẩm", "Tên sản phẩm", "Giá", "Đơn vị", "Số lượng"
            }
        ));
        jScrollPane5.setViewportView(tableServiceItem);

        btn_Search.setText("Tìm kiếm");
        btn_Search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SearchActionPerformed(evt);
            }
        });

        textSearch.setColumns(20);
        textSearch.setRows(5);
        jScrollPane6.setViewportView(textSearch);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 627, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btn_Add)
                                        .addGap(112, 112, 112)
                                        .addComponent(btn_Update)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btn_Delete))))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                                    .addComponent(btn_Search, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jScrollPane6))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_Search)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Add)
                    .addComponent(btn_Delete)
                    .addComponent(btn_Update))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AddActionPerformed
        // TODO add your handling code here:
        Create();
    }//GEN-LAST:event_btn_AddActionPerformed

    private void Create() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Kiểm tra xem đã có sản phẩm nào có cùng serviceName và unit chưa
            String checkSql = "SELECT COUNT(*) FROM serviceitem WHERE serviceName = ? AND unit = ? AND categoryID = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, textServiceName.getText().trim());
                checkStmt.setString(2, textUnit.getText().trim());
                checkStmt.setInt(3, categoryId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        JOptionPane.showMessageDialog(this, "Sản phẩm đã tồn tại!");
                        return;
                    }
                }
            }

            // Nếu chưa tồn tại, tiến hành thêm sản phẩm mới
            String sql = "INSERT INTO serviceitem(serviceName, price, unit, stock, categoryID) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, textServiceName.getText().trim());
                ps.setInt(2, Integer.parseInt(textPrice.getText().trim()));
                ps.setString(3, textUnit.getText().trim());
                ps.setInt(4, Integer.parseInt(textStock.getText().trim()));
                ps.setInt(5, categoryId);
                int row = ps.executeUpdate();
                if (row > 0) {
                    JOptionPane.showMessageDialog(this, "Thêm dịch vụ thành công!");
                    read2(); // Cập nhật lại bảng hiển thị
                    textServiceName.setText("");
                    textPrice.setText("");
                    textUnit.setText("");
                    textStock.setText("");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm dịch vụ: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void btn_UpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_UpdateActionPerformed
        // TODO add your handling code here:
        Update();
    }//GEN-LAST:event_btn_UpdateActionPerformed
    private void Update() {
        // Kiểm tra xem có dòng nào được chọn không
        selectedRow = tableServiceItem.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần sửa!");
            return;
        }

        // Lấy serviceID từ dòng đang sửa (cột 0 chứa serviceID)
        int serviceId = Integer.parseInt(tableServiceItem.getValueAt(selectedRow, 0).toString());

        // Lấy dữ liệu từ các trường nhập
        String serviceName = textServiceName.getText().trim();
        String priceText = textPrice.getText().trim();
        String unit = textUnit.getText().trim();
        String stockText = textStock.getText().trim();

        // Kiểm tra nếu có ô nào trống
        if (serviceName.isEmpty() || priceText.isEmpty() || unit.isEmpty() || stockText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        try {
            // Chuyển đổi dữ liệu số (price và stock là kiểu int)
            int price = Integer.parseInt(priceText);
            int stock = Integer.parseInt(stockText);

            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                // Kiểm tra xem có sản phẩm nào khác có cùng serviceName và unit không
                String checkSql = "SELECT COUNT(*) FROM serviceitem WHERE serviceName = ? AND unit = ? AND categoryID = ? AND serviceID <> ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                    checkStmt.setString(1, serviceName);
                    checkStmt.setString(2, unit);
                    checkStmt.setInt(3, categoryId);
                    checkStmt.setInt(4, serviceId); // Bỏ qua chính nó

                    try (ResultSet rs = checkStmt.executeQuery()) {
                        if (rs.next() && rs.getInt(1) > 0) {
                            JOptionPane.showMessageDialog(this, "Sản phẩm đã tồn tại!");
                            return;
                        }
                    }
                }

                // Nếu không trùng, tiến hành cập nhật
                String sql = "UPDATE serviceitem SET serviceName = ?, price = ?, unit = ?, stock = ? WHERE serviceID = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, serviceName);
                    ps.setInt(2, price);
                    ps.setString(3, unit);
                    ps.setInt(4, stock);
                    ps.setInt(5, serviceId);

                    int row = ps.executeUpdate();
                    if (row > 0) {
                        JOptionPane.showMessageDialog(this, "Sửa thông tin dịch vụ thành công!");
                        read2(); // Cập nhật lại bảng hiển thị
                        textServiceName.setText("");
                        textPrice.setText("");
                        textUnit.setText("");
                        textStock.setText("");
                    } else {
                        JOptionPane.showMessageDialog(this, "Không có bản ghi nào được cập nhật. Vui lòng kiểm tra lại.");
                    }
                }
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Giá và trạng thái sản phẩm phải là số!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi sửa dịch vụ: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void btn_DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_DeleteActionPerformed
        // TODO add your handling code here:
        Delete();
    }//GEN-LAST:event_btn_DeleteActionPerformed
    // Hàm delete để xóa service
    private void Delete() {
        // Kiểm tra xem có dòng nào được chọn không
        selectedRow = tableServiceItem.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa!");
            return;
        }
        // Lấy serviceID của dòng được chọn
        int serviceId = Integer.parseInt(tableServiceItem.getValueAt(selectedRow, 0).toString());
        // Yêu cầu xác nhận xóa
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa sản phẩm này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String sql = "DELETE FROM serviceitem WHERE serviceID = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, serviceId);
                    int row = ps.executeUpdate();
                    if (row > 0) {
                        JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công!");
                        read2(); // cập nhật lại bảng hiển thị
                        textServiceName.setText("");
                        textPrice.setText("");
                        textUnit.setText("");
                        textStock.setText("");
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa dịch vụ: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
   
    private void btn_SearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SearchActionPerformed
        // TODO add your handling code here:
        searchProduct();
    }//GEN-LAST:event_btn_SearchActionPerformed
    private void searchProduct() {
        // Lấy từ khóa tìm kiếm từ textSearch
        String searchTerm = textSearch.getText().trim();

        // Nếu từ khóa trống, hiển thị lại danh sách sản phẩm ban đầu
        if (searchTerm.isEmpty()) {
            read2(); // Gọi hàm read2() để load dữ liệu ban đầu
            return;
        }

        // Kết nối tới CSDL và thực hiện truy vấn tìm kiếm sản phẩm theo tên
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Truy vấn tìm các sản phẩm thuộc category hiện tại có tên chứa searchTerm
            String sql = "SELECT serviceID, serviceName, price, unit, stock FROM serviceitem WHERE categoryID = ? AND serviceName LIKE ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, categoryId);
                stmt.setString(2, "%" + searchTerm + "%");
                try (ResultSet rs = stmt.executeQuery()) {
                    // Lấy model của bảng và xóa dữ liệu cũ
                    DefaultTableModel model = (DefaultTableModel) tableServiceItem.getModel();
                    model.setRowCount(0);
                    // Duyệt qua kết quả và thêm vào bảng
                    while (rs.next()) {
                        int serviceId = rs.getInt("serviceID");
                        String serviceName = rs.getString("serviceName");
                        double price = rs.getDouble("price");
                        String unit = rs.getString("unit");
                        int stock = rs.getInt("stock");
                        model.addRow(new Object[]{serviceId, serviceName, price, unit, stock});
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm sản phẩm: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }



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
            java.util.logging.Logger.getLogger(ServiceDetailJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ServiceDetailJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ServiceDetailJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ServiceDetailJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ServiceDetailJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Add;
    private javax.swing.JButton btn_Delete;
    private javax.swing.JButton btn_Search;
    private javax.swing.JButton btn_Update;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTable tableServiceItem;
    private javax.swing.JTextArea textPrice;
    private javax.swing.JTextArea textSearch;
    private javax.swing.JTextArea textServiceName;
    private javax.swing.JTextArea textStock;
    private javax.swing.JTextArea textUnit;
    // End of variables declaration//GEN-END:variables
}
