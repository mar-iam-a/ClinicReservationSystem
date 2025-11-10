
package com.mycompany.clinicsystem;
import java.util.*;


public class Practitioner extends User{
    private Clinic clinic;
    public Practitioner(int ID, String name, String phone, String email, String password, Clinic clinic) {
        super(ID, name, phone, email, password);
        this.clinic = clinic;
    }
    public List<Appointment> getAppointments() {
        if (clinic == null || clinic.getSchedule() == null) {
            return null;
        }
        return clinic.getAppointments();
    }
    

    public List<Rating> getRatings() {
        return clinic != null ? clinic.getRatings() : null;
    }

    public Clinic getClinic() {
        return clinic;
    }

    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }

    public void updateClinicInfo(String name, String address, double price) {
        if (clinic != null) {
            clinic.setName(name);
            clinic.setAddress(address);
            clinic.setPrice(price);
            
        }
    }
}
