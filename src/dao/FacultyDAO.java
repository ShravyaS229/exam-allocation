package src.dao;
import src.models.Faculty;
import src.DBConnection;
import java.sql.*;
import java.util.*;

public class FacultyDAO {
    public List<Faculty> getAllFaculties() {
        List<Faculty> faculties = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM faculty ORDER BY faculty_id")) {
            while (rs.next()) {
                faculties.add(new Faculty(
                    rs.getInt("faculty_id"), rs.getString("name"), 
                    rs.getString("designation"), rs.getBoolean("is_senior"), rs.getBoolean("is_absent")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return faculties;
    }
    
    public List<Faculty> getAllFaculty() { return getAllFaculties(); } // FacultyPage needs this
    
    public void updateFacultyStatus(int facultyId, boolean isAbsent) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE faculty SET is_absent = ? WHERE faculty_id = ?")) {
            ps.setBoolean(1, isAbsent); ps.setInt(2, facultyId); ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    public void clearAllAllocations() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM allocation_result");
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
