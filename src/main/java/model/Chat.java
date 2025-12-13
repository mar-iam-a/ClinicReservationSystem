package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a unique chat session between a Patient and a Practitioner.
 */
public class Chat {
    private int id;
    private Patient patient;
    private Practitioner practitioner;
    private List<Message> messages;
    private int lastReadByPatient;
    private int lastReadByPractitioner;

    public Chat(int id, Patient patient, Practitioner practitioner, List<Message> messages) {
        this.id = id;
        this.patient = patient;
        this.practitioner = practitioner;
        this.messages = messages != null ? new ArrayList<>(messages) : new ArrayList<>();
    }

    public Chat(Patient patient, Practitioner practitioner) {
        this(0, patient, practitioner, new ArrayList<>());
    }

    public Chat(int id, Patient patient, Practitioner practitioner) {
        this(id, patient, practitioner, new ArrayList<>());
    }
    // constructors
    public Chat(int id, Patient patient, Practitioner practitioner,
                int lastReadByPatient, int lastReadByPractitioner) {
        this(id, patient, practitioner);
        this.lastReadByPatient = lastReadByPatient;
        this.lastReadByPractitioner = lastReadByPractitioner;
    }

    // getters & setters
    public int getLastReadByPatient() { return lastReadByPatient; }
    public void setLastReadByPatient(int lastReadByPatient) { this.lastReadByPatient = lastReadByPatient; }

    public int getLastReadByPractitioner() { return lastReadByPractitioner; }
    public void setLastReadByPractitioner(int lastReadByPractitioner) { this.lastReadByPractitioner = lastReadByPractitioner; }

    // Getters and Setters:
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Practitioner getPractitioner() { return practitioner; }
    public void setPractitioner(Practitioner practitioner) { this.practitioner = practitioner; }

    public List<Message> getMessages() { return messages; }
    public void setMessages(List<Message> messages) { this.messages = messages; }

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    @Override
    public String toString() {
        return "Chat{" +
                "id=" + id +
                ", patientId=" + (patient != null ? patient.getID() : "N/A") +
                ", practitionerId=" + (practitioner != null ? practitioner.getID() : "N/A") +
                '}';
    }

    public boolean hasUnreadMessagesForPatient() {
        return messages != null &&
                !messages.isEmpty() &&
                messages.get(messages.size() - 1).getId() > lastReadByPatient;
    }

    public boolean hasUnreadMessagesForPractitioner() {
        return messages != null &&
                !messages.isEmpty() &&
                messages.get(messages.size() - 1).getId() > lastReadByPractitioner;
    }
}
