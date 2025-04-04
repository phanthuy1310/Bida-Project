/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import model.TableEntity;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import dao.TableDao;
/**
 *
 * @author ADMIN
 */
public class TableJFrame extends javax.swing.JFrame {

    /**
     * Creates new form TableJFrame
     */
    private DefaultTableModel model;
    private int selectedRow = -1;

    public TableJFrame() {
        initComponents();
        // chế độ chọn nhiều bản ghi
        tableTable.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        setupComboBoxes();
        model = (DefaultTableModel) tableTable.getModel();
        loadData();
        tableTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                fillText();
            }
        });
    }
     
    private void setupComboBoxes() {
        // Cho phép người dùng nhập giá trị mới (gợi ý)
        comboStatus.setEditable(true);
        comboType.setEditable(true);
        // Gán giá trị mặc định cho các combobox
        String[] statusOptions = {"", "Hoạt động", "Không hoạt động", "Bảo trì"};
        comboStatus.setModel(new DefaultComboBoxModel<>(statusOptions));
        String[] typeOptions = {"", "Thường", "Vip"};
        comboType.setModel(new DefaultComboBoxModel<>(typeOptions));
    }

    private void loadData() {
        model.setRowCount(0);
        List<TableEntity> tables = TableDao.getAllTables();
        for (TableEntity table : tables) {
            model.addRow(new Object[]{
                table.getTableID(),
                table.getName(),
                table.getPrice(),
                table.getStatus(),
                table.getType()
            });
        }
    }

     private void fillText() {
        selectedRow = tableTable.getSelectedRow();
        if (selectedRow != -1) {
            textName.setText(tableTable.getValueAt(selectedRow, 1).toString());
            textPrice.setText(tableTable.getValueAt(selectedRow, 2).toString());
            comboStatus.setSelectedItem(tableTable.getValueAt(selectedRow, 3).toString());
            comboType.setSelectedItem(tableTable.getValueAt(selectedRow, 4).toString());
        }
    }
     
    // Hàm kiểm tra và thêm giá trị vào combobox nếu chưa có
    private boolean addComboBoxItemIfNotExist(JComboBox<String> comboBox, String item) {
        DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) comboBox.getModel();
        for (int i = 0; i < model.getSize(); i++) {
            if (model.getElementAt(i).equalsIgnoreCase(item)) {
                return false;
            }
        }
        model.addElement(item);
        return true;
    }
    
     // Hàm kiểm tra xem giá trị có tồn tại trong table (dữ liệu hiển thị) không
    private boolean existsInTable(String value, int colIndex) {
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, colIndex).toString().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }

    // Hàm loại bỏ giá trị khỏi combobox
    private void removeComboBoxItem(JComboBox<String> comboBox, String item) {
        DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) comboBox.getModel();
        for (int i = 0; i < model.getSize(); i++) {
            if (model.getElementAt(i).equalsIgnoreCase(item)) {
                model.removeElementAt(i);
                break;
            }
        }
    }
    
    private void createTable() {
        String name = textName.getText().trim();
        String priceStr = textPrice.getText().trim();
        String status = comboStatus.getEditor().getItem().toString().trim();
        String type = comboType.getEditor().getItem().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty() || status.isEmpty() || type.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bạn cần điền đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int price;
        try {
            price = Integer.parseInt(priceStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá phải là số", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Kiểm tra trùng lặp trước khi tạo mới
        if (TableDao.existsTable(name, type)) {
            JOptionPane.showMessageDialog(this, "Tên và loại đã tồn tại, vui lòng nhập thông tin khác!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        TableEntity table = new TableEntity();
        table.setName(name);
        table.setPrice(price);
        table.setStatus(status);
        table.setType(type);

        if (TableDao.addTable(table)) {
            JOptionPane.showMessageDialog(this, "Đã thêm bản ghi thành công");
            loadData();
            // Cập nhật gợi ý cho combobox nếu giá trị mới chưa tồn tại
            addComboBoxItemIfNotExist(comboStatus, status);
            addComboBoxItemIfNotExist(comboType, type);
        } else {
            JOptionPane.showMessageDialog(this, "Thêm bản ghi thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateTable() {
        int selected = tableTable.getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một bản ghi để cập nhật", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String name = textName.getText().trim();
        String priceStr = textPrice.getText().trim();
        String status = comboStatus.getEditor().getItem().toString().trim();
        String type = comboType.getEditor().getItem().toString().trim();
        if (name.isEmpty() || priceStr.isEmpty() || status.isEmpty() || type.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bạn cần điền đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int price;
        try {
            price = Integer.parseInt(priceStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá phải là số", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int tableID = Integer.parseInt(tableTable.getValueAt(selected, 0).toString());
        
        // Kiểm tra trùng lặp (loại trừ bản ghi hiện tại)
        if (TableDao.existsTableExcludingId(name, type, tableID)) {
            JOptionPane.showMessageDialog(this, "Tên và loại đã tồn tại trong một bản ghi khác!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        TableEntity table = new TableEntity(tableID, name, price, status, type);
        if (TableDao.updateTable(table)) {
            JOptionPane.showMessageDialog(this, "Cập nhật bản ghi thành công");
            loadData();
            // Cập nhật lại gợi ý cho combobox
            addComboBoxItemIfNotExist(comboStatus, status);
            addComboBoxItemIfNotExist(comboType, type);
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteTable() {
        int selected = tableTable.getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một bản ghi để xóa", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Lấy giá trị cần kiểm tra trước khi xóa
        String deletedStatus = tableTable.getValueAt(selected, 3).toString();
        String deletedType = tableTable.getValueAt(selected, 4).toString();
        
        int tableID = Integer.parseInt(tableTable.getValueAt(selected, 0).toString());
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa bản ghi này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (TableDao.deleteTable(tableID)) {
                JOptionPane.showMessageDialog(this, "Bản ghi đã được xóa thành công!");
                loadData();
                // Kiểm tra xem giá trị của combobox có còn xuất hiện trong dữ liệu không,
                // nếu không có thì loại bỏ khỏi gợi ý
                if (!existsInTable(deletedStatus, 3)) {
                    removeComboBoxItem(comboStatus, deletedStatus);
                }
                if (!existsInTable(deletedType, 4)) {
                    removeComboBoxItem(comboType, deletedType);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Xóa bản ghi thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void deleteMultipleTables() {
        int[] selectedRows = tableTable.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một bản ghi để xóa", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa các bản ghi đã chọn?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // Lưu lại các giá trị status và type của các bản ghi sắp bị xóa
        java.util.List<String> statusesToCheck = new java.util.ArrayList<>();
        java.util.List<String> typesToCheck = new java.util.ArrayList<>();

        // Duyệt qua các hàng được chọn (chú ý chuyển đổi chỉ số hàng từ view sang model nếu có sắp xếp)
        for (int viewRow : selectedRows) {
            int modelRow = tableTable.convertRowIndexToModel(viewRow);
            String status = model.getValueAt(modelRow, 3).toString();
            String type = model.getValueAt(modelRow, 4).toString();
            statusesToCheck.add(status);
            typesToCheck.add(type);

            int tableID = Integer.parseInt(model.getValueAt(modelRow, 0).toString());
            TableDao.deleteTable(tableID);
        }

        JOptionPane.showMessageDialog(this, "Các bản ghi đã được xóa thành công!");
        loadData(); // Cập nhật lại bảng

        // Cập nhật lại gợi ý cho combobox
        for (String status : statusesToCheck) {
            if (!existsInTable(status, 3)) {
                removeComboBoxItem(comboStatus, status);
            }
        }
        for (String type : typesToCheck) {
            if (!existsInTable(type, 4)) {
                removeComboBoxItem(comboType, type);
            }
        }
    }
    
    private void deleteSelectedTables() {
        int[] selectedRows = tableTable.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một bản ghi để xóa", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (selectedRows.length == 1) {
            deleteTable();
        } else {
            deleteMultipleTables();
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

        jScrollPane4 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textName = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        textPrice = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        AddButton = new javax.swing.JButton();
        UpdateButton = new javax.swing.JButton();
        DeleteButton = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        tableTable = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        comboStatus = new javax.swing.JComboBox<>();
        comboType = new javax.swing.JComboBox<>();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Tên bàn");

        textName.setColumns(20);
        textName.setRows(5);
        jScrollPane1.setViewportView(textName);

        textPrice.setColumns(20);
        textPrice.setRows(5);
        jScrollPane2.setViewportView(textPrice);

        jLabel2.setText("Trạng thái");

        jLabel3.setText("Giá");

        AddButton.setText("Thêm");
        AddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddButtonActionPerformed(evt);
            }
        });

        UpdateButton.setText("Sửa");
        UpdateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UpdateButtonActionPerformed(evt);
            }
        });

        DeleteButton.setText("Xóa");
        DeleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteButtonActionPerformed(evt);
            }
        });

        tableTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Tên bàn", "Giá", "Trạng thái", "Loại bàn"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane5.setViewportView(tableTable);

        jLabel4.setText("Loại bàn");

        comboStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboStatusActionPerformed(evt);
            }
        });

        comboType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboTypeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(comboType, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(AddButton, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(UpdateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(14, 14, 14)
                                    .addComponent(DeleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(comboStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(comboStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comboType, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AddButton)
                    .addComponent(UpdateButton)
                    .addComponent(DeleteButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void AddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddButtonActionPerformed
        // TODO add your handling code here:
        createTable();
    }//GEN-LAST:event_AddButtonActionPerformed

    private void UpdateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateButtonActionPerformed
        // TODO add your handling code here:
        updateTable();
    }//GEN-LAST:event_UpdateButtonActionPerformed

    private void DeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteButtonActionPerformed
        // TODO add your handling code here:
        deleteSelectedTables();
    }//GEN-LAST:event_DeleteButtonActionPerformed

    private void comboStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboStatusActionPerformed

    private void comboTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboTypeActionPerformed



   
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
            java.util.logging.Logger.getLogger(TableJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TableJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TableJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TableJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new TableJFrame().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddButton;
    private javax.swing.JButton DeleteButton;
    private javax.swing.JButton UpdateButton;
    private javax.swing.JComboBox<String> comboStatus;
    private javax.swing.JComboBox<String> comboType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable tableTable;
    private javax.swing.JTextArea textName;
    private javax.swing.JTextArea textPrice;
    // End of variables declaration//GEN-END:variables
}
