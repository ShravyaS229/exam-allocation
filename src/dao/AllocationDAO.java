package src.dao;

import src.DBConnection;
import src.models.AllocationResult;

import java.sql.*;
import java.util.*;

public class AllocationDAO {

    public void saveAllocation(AllocationResult ar) {
        String sql = """
            INSERT INTO allocation_result
            (exam_date,time,room_no,semester,subject,faculty_name,designation)
            VALUES (?,?,?,?,?,?,?)
        """;

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, ar.getExamDate());
            ps.setString(2, ar.getTime());
            ps.setInt(3, ar.getRoomNo());
            ps.setString(4, ar.getSemester());
            ps.setString(5, ar.getSubject());
            ps.setString(6, ar.getFacultyName());
            ps.setString(7, ar.getDesignation());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<AllocationResult> getAllocationsForFaculty(String name) {
        List<AllocationResult> list = new ArrayList<>();
        String sql = "SELECT * FROM allocation_result WHERE faculty_name=?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new AllocationResult(
                        rs.getDate("exam_date").toString(),
                        rs.getString("time"),
                        rs.getInt("room_no"),
                        rs.getString("semester"),
                        rs.getString("subject"),
                        rs.getString("faculty_name"),
                        rs.getString("designation")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
