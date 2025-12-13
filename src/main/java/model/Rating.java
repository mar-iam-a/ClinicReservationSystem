/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.time.LocalDateTime;


public class Rating {
    private int id;
    private Patient patient;
    private Clinic clinic;
    private int score;       // Numerical score (1–5)
    private String comment;
    private LocalDateTime createdAt;

    // Constructor: creates a new rating with patient, clinic, score, and comment
    public Rating(int id, Patient patient, Clinic clinic, int score, String comment, LocalDateTime createdAt) {
        this.id = id;
        this.patient = patient;
        this.clinic = clinic;
        this.score = score;
        this.comment = comment;
        this.createdAt = createdAt;
    }
    public Rating(Patient patient, Clinic clinic, int score, String comment, LocalDateTime createdAt) {
        this.id = 0; // temporary
        this.patient = patient;
        this.clinic = clinic;
        this.score = score;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public Rating(Patient patient, Clinic clinic, int score, String comment) {
        this(0, patient, clinic, score, comment, LocalDateTime.now());
    }

    public Rating() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public Clinic getClinic() {
        return clinic;
    }

    public int getScore() {
        return score;
    }

    public String getComment() {
        return comment;
    }

    public void setScore(int score) {
        if (score >= 1 && score <= 5)
            this.score = score;
        else
            System.out.println("Please score must be between 1 to 5.");
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    // Returns a string representation of the rating
    @Override
    public String toString() {
        return "Rating{ patient=" + patient + ", clinic=" + clinic + ", score=" + score + ", comment=" + comment + '}';
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }
    // Getter
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


}
