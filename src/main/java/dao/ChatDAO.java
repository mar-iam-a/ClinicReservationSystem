package dao;

import database.DBConnection;
import model.Chat;
import model.Message;
import model.Patient;
import model.Practitioner;

import java.sql.*;
import java.util.*;

public class ChatDAO implements GenericDAO<Chat> {

    private final PatientDAO patientDAO = new PatientDAO();
    private final PractitionerDAO practitionerDAO = new PractitionerDAO();

    private Chat extractChatFromResultSet(ResultSet rs) throws SQLException {
        int patientId = rs.getInt("patient_id");
        int practitionerId = rs.getInt("practitioner_id");
        int lastReadByPatient = rs.getInt("last_read_by_patient");
        int lastReadByPractitioner = rs.getInt("last_read_by_practitioner");

        Patient patient = patientDAO.getById(patientId);
        Practitioner practitioner = practitionerDAO.getById(practitionerId);

        return new Chat(
                rs.getInt("id"),
                patient,
                practitioner,
                lastReadByPatient,
                lastReadByPractitioner
        );
    }

    public Chat getChatByParticipants(int patientId, int practitionerId) throws SQLException {
        String sql = "SELECT * FROM Chats WHERE (patient_id = ? AND practitioner_id = ?) LIMIT 1";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, patientId);
            ps.setInt(2, practitionerId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractChatFromResultSet(rs);
                }
                return null;
            }
        }
    }

    public List<Chat> getChatsByPatientId(int patientId) throws SQLException {
        List<Chat> chats = new ArrayList<>();
        String sql = "SELECT * FROM Chats WHERE patient_id = ? ORDER BY id DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    chats.add(extractChatFromResultSet(rs));
                }
            }
        }
        return chats;
    }

    public List<Chat> getChatsByPractitionerId(int practitionerId) throws SQLException {
        List<Chat> chats = new ArrayList<>();
        String sql = "SELECT * FROM Chats WHERE practitioner_id = ? ORDER BY id DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, practitionerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    chats.add(extractChatFromResultSet(rs));
                }
            }
        }
        return chats;
    }

    // Insert
    @Override
    public void add(Chat chat) throws SQLException {
        String sql = "INSERT INTO Chats (patient_id, practitioner_id) VALUES (?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, chat.getPatient().getID());
            ps.setInt(2, chat.getPractitioner().getID());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        chat.setId(rs.getInt(1));
                    }
                }
            }
        }
    }

    // Get by ID
    @Override
    public Chat getById(int id) throws SQLException {
        String sql = "SELECT * FROM Chats WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractChatFromResultSet(rs);
                }
                return null;
            }
        }
    }

    // Get All
    @Override
    public List<Chat> getAll() throws SQLException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    // Update
    @Override
    public void update(Chat chat) throws SQLException {
        throw new UnsupportedOperationException("Chat entity is usually not updated, only messages are added.");
    }

    // Delete
    @Override
    public void delete(int id) throws SQLException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void addMessage(Message message) throws SQLException {
        String sql = "INSERT INTO Messages (chat_id, sender_type, sender_id, receiver_id, message_text, timestamp) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, message.getChatId());
            ps.setString(2, message.getSenderType().name());
            ps.setInt(3, message.getSenderId());
            ps.setInt(4, message.getReceiverId());
            ps.setString(5, message.getMessageText());
            ps.setTimestamp(6, Timestamp.valueOf(message.getTimestamp()));

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        message.setId(rs.getInt(1));
                    }
                }
            }
        }
    }

    /**
     * Retrieves all chats between a specific patient and practitioner.
     */
    public List<Chat> getChatsByPatientIdAndPractitionerId(int patientId, int practitionerId) throws SQLException {
        List<Chat> chats = new ArrayList<>();
        String sql = "SELECT * FROM Chats WHERE patient_id = ? AND practitioner_id = ? ORDER BY id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, patientId);
            ps.setInt(2, practitionerId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    chats.add(extractChatFromResultSet(rs));
                }
            }
        }
        return chats;
    }
    public void updateLastReadByPatient(int chatId, int messageId) throws SQLException {
        String sql = "UPDATE Chats SET last_read_by_patient = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, messageId);
            ps.setInt(2, chatId);
            ps.executeUpdate();
        }
    }

    public void updateLastReadByPractitioner(int chatId, int messageId) throws SQLException {
        String sql = "UPDATE Chats SET last_read_by_practitioner = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, messageId);
            ps.setInt(2, chatId);
            ps.executeUpdate();
        }
    }
}