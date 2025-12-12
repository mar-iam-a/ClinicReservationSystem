package dao;

import database.DBConnection;
import model.Clinic;
import model.Schedule;
import model.WorkingHoursRule;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClinicDAO implements GenericDAO<Clinic> {

    private final ScheduleDAO scheduleDAO = new ScheduleDAO();

    private Clinic extractClinicFromResultSet(ResultSet rs) throws SQLException {
        int scheduleId = rs.getInt("schedule_id");
        Schedule schedule = null;
        if (!rs.wasNull() && scheduleId > 0) {
            try {
                schedule = scheduleDAO.getById(scheduleId);
            } catch (SQLException e) {
                System.err.println("Failed to load schedule for Clinic ID " + rs.getInt("id"));
            }
        }

        int doctorID = rs.getInt("doctor_id");
        String doctorName = rs.getString("doctor_name");

        Clinic clinic = new Clinic(
                rs.getInt("id"),
                rs.getInt("department_id"),
                rs.getString("name"),
                rs.getString("address"),
                rs.getDouble("price"),
                schedule
        );

        clinic.setDoctorID(doctorID);
        clinic.setDoctorName(doctorName != null ? doctorName : "—");
        clinic.setConsultationPrice(rs.getDouble("consultation_price"));
        clinic.setConsultationDurationDays(rs.getInt("consultation_duration_days"));

        clinic.setDoctorID(doctorID);
        clinic.setDoctorName(doctorName != null ? doctorName : "—");
        double cp = rs.getObject("consultation_price") != null ?
                rs.getDouble("consultation_price") : 0.0;
        clinic.setConsultationPrice(cp);

        int cd = rs.getObject("consultation_duration_days") != null ?
                rs.getInt("consultation_duration_days") : 0;
        clinic.setConsultationDurationDays(cd);
        return clinic;
    }

    @Override
    public void add(Clinic clinic) throws SQLException {
        String sql = """
        INSERT INTO Clinics 
        (department_id, name, address, price, consultation_price, consultation_duration_days, schedule_id, doctor_id, doctor_name) 
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, clinic.getDepartmentID());
            ps.setString(2, clinic.getName());
            ps.setString(3, clinic.getAddress());
            ps.setDouble(4, clinic.getPrice());
            ps.setDouble(5, clinic.getConsultationPrice());           // ✅ consultation_price
            ps.setInt(6, clinic.getConsultationDurationDays());       // ✅ consultation_duration_days
            if (clinic.getSchedule() == null) {
                ps.setNull(7, Types.INTEGER);
            } else {
                ps.setInt(7, clinic.getSchedule().getID());
            }
            ps.setInt(8, clinic.getDoctorID());
            ps.setString(9, clinic.getDoctorName());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        clinic.setID(rs.getInt(1));
                    }
                }
            }
        }
    }

    @Override
    public Clinic getById(int id) throws SQLException {
        String sql = "SELECT * FROM Clinics WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? extractClinicFromResultSet(rs) : null;
            }
        }
    }

    @Override
    public List<Clinic> getAll() throws SQLException {
        // ✅ نجيب doctor_name فقط — مش department_name
        String sql = """
            SELECT 
                c.*,
                p.name AS doctor_name
            FROM Clinics c
            LEFT JOIN Practitioners p ON c.doctor_id = p.id
            """;
        List<Clinic> clinics = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                clinics.add(extractClinicFromResultSet(rs));
            }
        }
        return clinics;
    }

    @Override
    public void update(Clinic clinic) throws SQLException {
        String sql = """
        UPDATE Clinics 
        SET department_id = ?, name = ?, address = ?, price = ?, 
            consultation_price = ?, consultation_duration_days = ?, 
            schedule_id = ?, doctor_id = ?, doctor_name = ?
        WHERE id = ?
        """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, clinic.getDepartmentID());
            ps.setString(2, clinic.getName());
            ps.setString(3, clinic.getAddress());
            ps.setDouble(4, clinic.getPrice());
            ps.setDouble(5, clinic.getConsultationPrice());
            ps.setInt(6, clinic.getConsultationDurationDays());
            if (clinic.getSchedule() == null) {
                ps.setNull(7, Types.INTEGER);
            } else {
                ps.setInt(7, clinic.getSchedule().getID());
            }
            ps.setInt(8, clinic.getDoctorID());
            ps.setString(9, clinic.getDoctorName());
            ps.setInt(10, clinic.getID());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Clinics WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public Clinic getClinicByPractitionerId(int practitionerId) throws SQLException {
        String sql = """
            SELECT 
                c.*,
                p.name AS doctor_name
            FROM Clinics c
            JOIN Practitioners p ON c.doctor_id = p.id
            WHERE p.id = ?
            """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, practitionerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Clinic clinic = extractClinicFromResultSet(rs);
                    if (clinic.getSchedule() == null && rs.getInt("schedule_id") > 0) {
                        clinic.setSchedule(scheduleDAO.getById(rs.getInt("schedule_id")));
                    }
                    return clinic;
                }
                return null;
            }
        }
    }

    public List<Clinic> getByDepartmentId(int departmentId) throws SQLException {
        String sql = "SELECT c.*, p.name AS doctor_name " +
                "FROM Clinics c " +
                "LEFT JOIN Practitioners p ON c.doctor_id = p.id " +
                "WHERE c.department_id = ?";
        List<Clinic> clinics = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, departmentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    clinics.add(extractClinicFromResultSet(rs));
                }
            }
        }
        return clinics;
    }

    public void loadPendingSchedule(Clinic clinic) throws SQLException {
        if (clinic == null || clinic.getID() <= 0) return;

        String sql = "SELECT pending_schedule_id FROM Clinics WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, clinic.getID());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int pendingScheduleId = rs.getInt("pending_schedule_id");
                    if (!rs.wasNull() && pendingScheduleId > 0) {
                        Schedule pending = new ScheduleDAO().getById(pendingScheduleId);
                        if (pending != null) {
                            List<WorkingHoursRule> rules = new WorkingHoursRuleDAO().getByScheduleId(pendingScheduleId);
                            pending.setWeeklyRules(rules);
                            clinic.setPendingSchedule(pending);
                        } else {
                            clinic.setPendingSchedule(null);
                        }
                    } else {
                        clinic.setPendingSchedule(null);
                    }
                }
            }
        }
    }

    public void updatePendingScheduleId(int clinicId, Integer pendingScheduleId) throws SQLException {
        String sql = "UPDATE Clinics SET pending_schedule_id = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (pendingScheduleId == null) {
                ps.setNull(1, java.sql.Types.INTEGER);
            } else {
                ps.setInt(1, pendingScheduleId);
            }
            ps.setInt(2, clinicId);
            ps.executeUpdate();
        }
    }
}