package src.dao;

import src.DBConnection;
import src.models.Subject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectDAO {

    // Existing method (unchanged)
    public Subject getByCode(String code) {
        String sql = "SELECT * FROM subjects WHERE subject_code = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Subject(
                        rs.getString("subject_code"),
                        rs.getString("subject_name"),
                        rs.getString("semester")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ✅ REQUIRED by AllocationLogic
    public List<Subject> getSubjectsBySemester(String semester) {
        List<Subject> subjects = new ArrayList<>();

        String sql = "SELECT * FROM subjects WHERE semester = ?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, semester);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                subjects.add(new Subject(
                        rs.getString("subject_code"),
                        rs.getString("subject_name"),
                        rs.getString("semester")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return subjects;
    }
}
