package src.dao;

import src.models.AllocationResult;
import src.DBConnection;
import java.sql.*;
import java.util.*;

public class AllocationDAO {
    public List<AllocationResult> getAllocationsForFaculty(String facultyName) {
        List<AllocationResult> results = new ArrayList<>();
        String query = "SELECT * FROM allocation_result WHERE faculty_name LIKE ? ORDER BY exam_date, time";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "%" + facultyName.trim() + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                results.add(new AllocationResult(
                    rs.getString("exam_date"),
                    rs.getString("time"),
                    rs.getInt("room_no"),
                    rs.getString("semester"),
                    rs.getString("subject"),
                    rs.getString("faculty_name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
    
    public void clearAllAllocations() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM allocation_result");
            System.out.println("Cleared all allocations");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // FIXED: String date parameter for AllocationLogic compatibility
    public void saveAllocation(String examDateStr, String time, int roomNo, String semester, 
                              String subject, String facultyName, String designation) {
        String sql = "INSERT INTO allocation_result (exam_date, time, room_no, semester, subject, faculty_name, designation) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, examDateStr);  // String date from Slot model
            ps.setString(2, time);
            ps.setInt(3, roomNo);
            ps.setString(4, semester);
            ps.setString(5, subject);
            ps.setString(6, facultyName);
            ps.setString(7, designation);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
