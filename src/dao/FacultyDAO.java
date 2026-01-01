package src.dao;

import src.DBConnection;
import src.models.Faculty;

import java.sql.*;
import java.util.*;

public class FacultyDAO {

    public List<Faculty> getAllFaculties() {
        List<Faculty> list = new ArrayList<>();
        String sql = "SELECT * FROM faculty";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Faculty(
                        rs.getInt("faculty_id"),
                        rs.getString("name"),
                        rs.getString("designation"),
                        rs.getBoolean("is_absent"),
                        rs.getBoolean("is_senior")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void markAbsent(int id, boolean absent) {
        String sql = "UPDATE faculty SET is_absent=? WHERE faculty_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setBoolean(1, absent);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
