package src.dao;

import src.models.Subject;
import java.sql.*;
import java.util.*;
import src.DBConnection;
public class SubjectDAO {
    public List<Subject> getSubjectsBySemester(String semester) {
        List<Subject> subjects = new ArrayList<>();
        String query = "SELECT * FROM subjects WHERE semester = ? ORDER BY subject_name";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, semester);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                subjects.add(new Subject(
                    rs.getString("subject_code"),
                    rs.getString("subject_name"),
                    rs.getString("semester")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjects;
    }

    // NEW METHOD: CRITICAL for AllocationLogic
    public Subject getSubjectByCode(String subjectCode) {
        String query = "SELECT * FROM subjects WHERE subject_code = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, subjectCode);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Subject(
                    rs.getString("subject_code"),
                    rs.getString("subject_name"),
                    rs.getString("semester")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Subject(subjectCode, "Unknown Subject", "Unknown");
    }
}
