package src.dao;

import src.DBConnection;
import src.models.Slot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SlotDAO {

    // Existing method (UNCHANGED)
    public List<Slot> getAllSlots() {
        List<Slot> list = new ArrayList<>();
        String sql = "SELECT * FROM slots ORDER BY exam_date, time";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Slot(
                        rs.getDate("exam_date").toString(),
                        rs.getString("semester"),
                        rs.getString("subject_code"),
                        rs.getString("time")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ✅ REQUIRED by AllocationLogic
    public List<Slot> getSlotsBySemester(String semester) {
        List<Slot> list = new ArrayList<>();
        String sql = "SELECT * FROM slots WHERE semester = ? ORDER BY exam_date, time";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, semester);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Slot(
                        rs.getDate("exam_date").toString(),
                        rs.getString("semester"),
                        rs.getString("subject_code"),
                        rs.getString("time")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
