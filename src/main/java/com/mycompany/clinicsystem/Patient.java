package com.mycompany.clinicsystem;
import java.util.*;

public class Patient extends User{
    private List<Appointment> patientAppointments ;
        
    public Patient(int ID,String name,String phone,String email,String password){
        super(ID,name,phone,email,password);
    }
    public void bookAppointment(TimeSlot selectedSlot, Clinic clinic) {
        Appointment appointment = new Appointment(this, clinic, selectedSlot);
        clinic.getAppointments().add(appointment);
        patientAppointments.add(appointment);
    }
    public void cancelAppointment(Appointment appointment, Clinic clinic) {
        clinic.getAppointments().remove(appointment);
        patientAppointments.remove(appointment);
    }
    public void addRating(Clinic clinic, int score, String comment) {
        Rating rating = new Rating(this, clinic, score, comment);
        clinic.getRatings().add(rating);
    }
    public List<Appointment> getAppointmentList() {
        return patientAppointments;
    }
}