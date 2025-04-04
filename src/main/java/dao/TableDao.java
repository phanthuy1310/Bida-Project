/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.ArrayList;
import java.sql.*;
import java.util.List;
import javax.swing.JOptionPane;
import model.TableEntity;
import dao.ConnectionProvider;

/**
 *
 * @author nguye
 */
public class TableDao {

    public static List<TableEntity> getAllTables() {
        ArrayList<TableEntity> tables = new ArrayList<>();
        String sql = "SELECT * FROM tableentity ORDER BY tableID ASC";

        // Linh Lưu ý
        // ConnectionProvider.getConn() = hàm DBConfig.getConnection() cũ m đã viết
        try (Connection conn = ConnectionProvider.getConn(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                TableEntity table = new TableEntity();
                table.setTableID(rs.getInt("tableID"));
                table.setName(rs.getString("name"));
                table.setPrice(rs.getInt("price"));
                table.setStatus(rs.getString("status"));
                table.setType(rs.getString("type"));
                tables.add(table);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }

    public static boolean addTable(TableEntity table) {
        String insertQuery = "INSERT INTO tableentity(name, price, status, type) VALUES (?, ?, ?, ?)";

        // Linh Lưu ý
        // ConnectionProvider.getConn() = hàm DBConfig.getConnection() cũ m đã viết
        try (Connection conn = ConnectionProvider.getConn(); PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
            stmt.setString(1, table.getName());
            stmt.setInt(2, table.getPrice());
            stmt.setString(3, table.getStatus());
            stmt.setString(4, table.getType());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateTable(TableEntity table) {
        String updateQuery = "UPDATE tableentity SET name=?, price=?, status=?, type=? WHERE tableID=?";

        // Linh Lưu ý
        // ConnectionProvider.getConn() = hàm DBConfig.getConnection() cũ m đã viết
        try (Connection conn = ConnectionProvider.getConn(); PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setString(1, table.getName());
            stmt.setInt(2, table.getPrice());
            stmt.setString(3, table.getStatus());
            stmt.setString(4, table.getType());
            stmt.setInt(5, table.getTableID());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteTable(int tableID) {
        String deleteQuery = "DELETE FROM tableentity WHERE tableID=?";

        // Linh Lưu ý
        // ConnectionProvider.getConn() = hàm DBConfig.getConnection() cũ m đã viết
        try (Connection conn = ConnectionProvider.getConn(); PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
            stmt.setInt(1, tableID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Kiểm tra xem đã tồn tại bản ghi với name và type chưa (dùng cho create)
    public static boolean existsTable(String name, String type) {
        String sql = "SELECT COUNT(*) FROM tableentity WHERE name = ? AND type = ?";

        // Linh Lưu ý
        // ConnectionProvider.getConn() = hàm DBConfig.getConnection() cũ m đã viết
        try (Connection conn = ConnectionProvider.getConn(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, type);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Kiểm tra khi cập nhật (loại trừ bản ghi hiện tại)
    public static boolean existsTableExcludingId(String name, String type, int tableID) {
        String sql = "SELECT COUNT(*) FROM tableentity WHERE name = ? AND type = ? AND tableID != ?";
        try (Connection conn = ConnectionProvider.getConn(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, type);
            stmt.setInt(3, tableID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
