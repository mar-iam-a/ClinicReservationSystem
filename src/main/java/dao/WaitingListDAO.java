package dao;

import database.DBConnection;
import model.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class WaitingListDAO implements GenericDAO<WaitingList> {

    private final PatientDAO patientDAO = new PatientDAO();
    private final ClinicDAO clinicDAO = new ClinicDAO();

    private WaitingList extractWaitingListFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int patientId = rs.getInt("patient_id");
        int clinicId = rs.getInt("clinic_id");
        LocalDate date = rs.getDate("date").toLocalDate(); // ← مهم
        LocalDateTime requestTime = rs.getTimestamp("request_time").toLocalDateTime();
        String statusStr = rs.getString("status");
        WaitingStatus status = WaitingStatus.valueOf(statusStr); // ← استخدم WaitingStatus

        Patient patient = patientDAO.getById(patientId);
        Clinic clinic = clinicDAO.getById(clinicId);

        return new WaitingList(id, patient, clinic, date, requestTime, status);
    }

    public WaitingList getFirstPendingForDate(int clinicId, LocalDate date) throws SQLException {
        String sql = """
            SELECT * FROM WaitingList 
            WHERE clinic_id = ? AND date = ? AND status = 'PENDING' 
            ORDER BY request_time ASC 
            LIMIT 1
            """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, clinicId);
            ps.setDate(2, Date.valueOf(date));

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? extractWaitingListFromResultSet(rs) : null;
            }
        }
    }

    public boolean existsPendingRequest(int patientId, int clinicId, LocalDate date) throws SQLException {
        String sql = """
            SELECT 1 FROM WaitingList 
            WHERE patient_id = ? AND clinic_id = ? AND date = ? AND status = 'PENDING' 
            LIMIT 1
            """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, patientId);
            ps.setInt(2, clinicId);
            ps.setDate(3, Date.valueOf(date));

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public List<WaitingList> getPatientPendingRequests(int patientId) throws SQLException {
        String sql = """
            SELECT * FROM WaitingList 
            WHERE patient_id = ? AND status IN ('PENDING', 'OFFERED') 
            ORDER BY request_time DESC
            """;
        List<WaitingList> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractWaitingListFromResultSet(rs));
                }
            }
        }
        return list;
    }

    @Override
    public void add(WaitingList item) throws SQLException {
        String sql = """
            INSERT INTO WaitingList (patient_id, clinic_id, date, request_time, status) 
            VALUES (?, ?, ?, ?, ?)
            """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, item.getPatient().getID());
            ps.setInt(2, item.getClinic().getID());
            ps.setDate(3, Date.valueOf(item.getDate())); // ← مهم
            ps.setTimestamp(4, Timestamp.valueOf(item.getRequestTime()));
            ps.setString(5, item.getStatus().name());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        item.setId(rs.getInt(1));
                    }
                }
            }
        }
    }

    @Override
    public void update(WaitingList item) throws SQLException {
        String sql = """
            UPDATE WaitingList 
            SET patient_id = ?, clinic_id = ?, date = ?, request_time = ?, status = ? 
            WHERE id = ?
            """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, item.getPatient().getID());
            ps.setInt(2, item.getClinic().getID());
            ps.setDate(3, Date.valueOf(item.getDate()));
            ps.setTimestamp(4, Timestamp.valueOf(item.getRequestTime()));
            ps.setString(5, item.getStatus().name());
            ps.setInt(6, item.getId());

            ps.executeUpdate();
        }
    }

    @Override
    public WaitingList getById(int id) throws SQLException {
        String sql = "SELECT * FROM WaitingList WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? extractWaitingListFromResultSet(rs) : null;
            }
        }
    }
    // في الـ DAO
    public void updateStatus(int id, WaitingStatus status) throws SQLException {
        String sql = "UPDATE WaitingList SET status = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status.name()); // ← يحول CANCELLED لـ "CANCELLED"
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }
    @Override
    public List<WaitingList> getAll() throws SQLException {
        String sql = "SELECT * FROM WaitingList ORDER BY request_time DESC";
        List<WaitingList> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(extractWaitingListFromResultSet(rs));
            }
        }
        return list;
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM WaitingList WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    public List<WaitingList> getAllByClinicId(int clinicId) throws SQLException {
        String sql = "SELECT * FROM WaitingList WHERE clinic_id = ? ORDER BY request_time DESC";
        List<WaitingList> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, clinicId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractWaitingListFromResultSet(rs));
                }
            }
        }
        return list;
    }
    public List<WaitingList> findByClinicId(int clinicId) throws SQLException {
        String sql = "SELECT * FROM WaitingList WHERE clinic_id = ? AND status = 'PENDING' ORDER BY request_time ASC";
        List<WaitingList> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, clinicId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractWaitingListFromResultSet(rs));
                }
            }
        }
        return list;
    }
    // dao/WaitingListDAO.java
    public void expireOldRequests(int clinicId) throws SQLException {
        String sql = """
        UPDATE WaitingList 
        SET status = ?
        WHERE clinic_id = ? 
          AND status = ?
          AND request_time < NOW() - INTERVAL 10 MINUTE
        """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, WaitingStatus.EXPIRED.name());
            ps.setInt(2, clinicId);
            ps.setString(3, WaitingStatus.PENDING.name());
            ps.executeUpdate();
        }
    }
    public void expireAllOfferedOlderThan(LocalDateTime threshold) throws SQLException {
        String sql = """
            UPDATE WaitingList 
            SET status = 'EXPIRED' 
            WHERE status = 'OFFERED' AND request_time < ?
            """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(threshold));
            ps.executeUpdate();
        }
    }
}