package src.dao;

import src.DBConnection;
import src.models.Room;

import java.sql.*;
import java.util.*;

public class RoomDAO {

    public List<Room> getAllRooms() {
        List<Room> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM rooms")) {

            while (rs.next()) {
                list.add(new Room(
                        rs.getInt("room_no"),
                        rs.getInt("capacity")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
