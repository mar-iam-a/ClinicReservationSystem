/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.time.LocalDate;
import java.util.*;

public class Patient extends User {
    private List<Appointment> patientAppointments;// List of this patient's appointments
    
    // Constructor: creates a new patient with personal information
    public Patient(int ID, String name, String email, String phone, String password,String gender, LocalDate dateOfBirth) {
        super(ID, name, email, phone, password,gender, dateOfBirth);
        patientAppointments = new ArrayList<>();
    }
    public String getUsername(){
        return name;
    }

    
    // Books a new appointment for this patient at a given clinic and time slot
    public void bookAppointment(TimeSlot selectedSlot, Clinic clinic) {
        Appointment appointment = new Appointment(this, clinic, selectedSlot);
        clinic.getAppointments().add(appointment);
        patientAppointments.add(appointment);
    }

    // Cancels an existing appointment booked by this patient
    public void cancelAppointment(Appointment a, Clinic clinic) {
        a.cancel();
        patientAppointments.remove(a);
        clinic.getAppointments().remove(a);
    }
    public String getFullName() {
        return name;
    }



    // Submits a rating for a specific clinic
    public void addRating(Clinic clinic, int score, String comment) {
        Rating rating = new Rating(this, clinic, score, comment);
        clinic.addToRatings(rating);
    }

    // Returns the list of appointments booked by this patient
    public List<Appointment> getAppointmentList() {
        return patientAppointments;
    }
}
