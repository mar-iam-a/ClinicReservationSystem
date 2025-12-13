package dao;

import database.DBConnection;
import model.Schedule;
import model.WorkingHoursRule;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScheduleDAO implements GenericDAO<Schedule> {

    private final WorkingHoursRuleDAO workingHoursRuleDAO = new WorkingHoursRuleDAO();

    private Schedule extractScheduleFromResultSet(ResultSet rs) throws SQLException {
        return new Schedule(
                rs.getInt("id"),
                rs.getInt("slot_duration_in_minutes")
        );
    }

    // Insert
    @Override
    public void add(Schedule schedule) throws SQLException {
        String sql = "INSERT INTO Schedules (slot_duration_in_minutes) VALUES (?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, schedule.getSlotDurationInMinutes());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    schedule.setID(rs.getInt(1));
                }
            }
        }
    }

    // Create schedule and return ID
    public int createSchedule(int slotDuration) throws SQLException {
        Schedule s = new Schedule(0, slotDuration);
        add(s);
        return s.getID();
    }

    @Override
    public Schedule getById(int id) throws SQLException {
        String sql = "SELECT * FROM Schedules WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Schedule s = extractScheduleFromResultSet(rs);
                    s.setWeeklyRules(workingHoursRuleDAO.getByScheduleId(id));
                    return s;
                }
            }
        }
        return null;
    }

    @Override
    public List<Schedule> getAll() throws SQLException {
        String sql = "SELECT * FROM Schedules";
        List<Schedule> schedules = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                schedules.add(extractScheduleFromResultSet(rs));
            }
        }
        return schedules;
    }

    @Override
    public void update(Schedule schedule) throws SQLException {
        String sql = "UPDATE Schedules SET slot_duration_in_minutes = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, schedule.getSlotDurationInMinutes());
            ps.setInt(2, schedule.getID());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Schedules WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public void deleteScheduleById(int id) throws SQLException {
        String sql = "DELETE FROM Schedules WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
