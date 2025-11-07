package com.mycompany.clinicsystem;
import java.util.*;

public class Patient extends User{
    
    
    public Patient(int ID,String name,String phone,String email,String password){
        super(ID,name,phone,email,password);
    }
    public void bookAppointment(TimeSlot selectedSlot, Clinic clinic) {
        Appointment appointment = new Appointment(this, clinic, selectedSlot);
        clinic.addAppointment(appointment);
    }
    public void cancelAppointment(Appointment appointment, Clinic clinic) {
        clinic.removeAppointment(appointment);
    }
    public void addRating(Clinic clinic, int score, String comment) {
        Rating rating = new Rating(this, clinic, score, comment);
        clinic.addRating(rating);
    }
    public List<Appointment> getAppointmentList(Clinic clinic) {
        return clinic.getAppointmentsForPatient(this);
    }
}