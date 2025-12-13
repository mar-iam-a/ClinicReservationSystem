package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class WaitingList {
    private int id;
    private Patient patient;
    private Clinic clinic;
    private LocalDate date;
    private LocalDateTime requestTime;
    private WaitingStatus status;

    public WaitingList(int id, Patient patient, Clinic clinic, LocalDate date, LocalDateTime requestTime, WaitingStatus status) {
        this.id = id;
        this.patient = patient;
        this.clinic = clinic;
        this.date = date;
        this.requestTime = requestTime;
        this.status = status;
    }

    public WaitingList(Patient patient, Clinic clinic, LocalDate date) {
        this(0, patient, clinic, date, LocalDateTime.now(), WaitingStatus.PENDING);
    }


    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Clinic getClinic() { return clinic; }
    public void setClinic(Clinic clinic) { this.clinic = clinic; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalDateTime getRequestTime() { return requestTime; }
    public void setRequestTime(LocalDateTime requestTime) { this.requestTime = requestTime; }

    public WaitingStatus getStatus() { return status; }
    public void setStatus(WaitingStatus status) { this.status = status; }

    @Override
    public String toString() {
        return "WaitingList{id=" + id +
                ", patient=" + (patient != null ? patient.getName() : "?") +
                ", clinic=" + (clinic != null ? clinic.getName() : "?") +
                ", date=" + date +
                ", status=" + status +
                "}";
    }
}