package src.dao;
import src.models.Slot;
import java.sql.*;
import java.util.*;
import src.DBConnection; 

public class SlotDAO {
    public List<Slot> getSlotsBySemester(String semester) {
        List<Slot> slots = new ArrayList<>();
        String query = "SELECT slot_id, exam_date, semester, subject_code, time FROM slots WHERE semester = ? ORDER BY exam_date, time";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, semester);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                slots.add(new Slot(
                    rs.getInt("slot_id"),
                    rs.getString("exam_date"),
                    rs.getString("semester"),
                    rs.getString("subject_code"),
                    rs.getString("time")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return slots;
    }
    
    // ADD THIS METHOD - FIXES "getAllSlots() undefined"
    public List<Slot> getAllSlots() {
        List<Slot> slots = new ArrayList<>();
        String query = "SELECT slot_id, exam_date, semester, subject_code, time FROM slots ORDER BY exam_date, time";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                slots.add(new Slot(
                    rs.getInt("slot_id"),
                    rs.getString("exam_date"),
                    rs.getString("semester"),
                    rs.getString("subject_code"),
                    rs.getString("time")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return slots;
    }
}
