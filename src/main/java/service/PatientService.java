package service;

import dao.AppointmentDAO;
import dao.ClinicDAO;
import dao.PatientDAO;
import dao.RatingDAO;
import dao.TimeSlotDAO;
import dao.WaitingListDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import model.Appointment;
import model.Clinic;
import model.Patient;
import model.Rating;
import model.Status;
import model.TimeSlot;
import model.WaitingList;

public class PatientService {

    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final TimeSlotDAO timeSlotDAO = new TimeSlotDAO();
    private final ClinicDAO clinicDAO = new ClinicDAO();
    private final RatingDAO ratingDAO = new RatingDAO();
    private final RatingService ratingService = new RatingService();
    private final WaitingListDAO waitingListDAO = new WaitingListDAO();
    private final PatientDAO patientDAO = new PatientDAO();
    private ClinicService clinicService;


    public void setClinicService(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    // ==============================
    //      AUTHENTICATION
    // ==============================

    // Register new patient
    public boolean registerPatient(Patient patient) {
        try {
            // Check duplicate email
            if (isEmailTaken(patient.getEmail())) {
                return false;
            }

            patientDAO.add(patient);
            return true;
        } catch (SQLException e) {
            System.err.println("Register failed: " + e.getMessage());
            return false;
        }
    }

    // Login
    public Patient login(String email, String password) {
        try {
            List<Patient> all = patientDAO.getAll();

            for (Patient p : all) {
                if (p.getEmail().equalsIgnoreCase(email)
                        && p.getPassword().equals(password)) {
                    return p;
                }
            }
            return null;

        } catch (SQLException e) {
            System.err.println("Login error: " + e.getMessage());
            return null;
        }
    }

    // Check if email already exists
    public boolean isEmailTaken(String email) {
        try {
            List<Patient> all = patientDAO.getAll();
            for (Patient p : all) {
                if (p.getEmail().equalsIgnoreCase(email)) {
                    return true;
                }
            }
            return false;

        } catch (SQLException e) {
            return false;
        }
    }

    // Get all patients (useful for debug or UI lists)
    public List<Patient> getAllPatients() {
        try {
            return patientDAO.getAll();
        } catch (SQLException e) {
            System.err.println("Error fetching patients: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean bookAppointment(Patient patient, Clinic clinic, TimeSlot slot) throws SQLException {
        TimeSlotDAO timeSlotDAO = new TimeSlotDAO();
        AppointmentDAO appointmentDAO = new AppointmentDAO();
        TimeSlotDAO slotDAO = new TimeSlotDAO();
        if (!slotDAO.isSlotAvailable(slot.getId())) {
            return false; // Someone booked it already
        }

        slot.setClinicId(clinic.getID());


        if (timeSlotDAO.getById(slot.getId()) == null) {
            timeSlotDAO.add(slot);
        }


        List<Appointment> existingAppointments = appointmentDAO.getAppointmentsByClinicId(clinic.getID());
        boolean isBooked = existingAppointments.stream()
                .anyMatch(a -> a.getAppointmentDateTime().getId() == slot.getId());
        if (isBooked) {
            return false;
        }


        slot.markAsBooked();
        timeSlotDAO.update(slot);


        Appointment appointment = new Appointment(patient, clinic, slot);
        appointmentDAO.add(appointment);


        patient.getAppointmentList().add(appointment);

        return true;
    }



    public void cancelAppointment(Patient patient, Appointment appointment) throws SQLException {
        appointment.setStatus(Status.Cancelled_by_Patient);

        TimeSlot slot = appointment.getAppointmentDateTime();
        slot.markAsAvailable();
        timeSlotDAO.update(slot);

        patient.getAppointmentList().remove(appointment);

        Clinic clinic = appointment.getClinic();
        clinic.getAppointments().remove(appointment);

        appointmentDAO.delete(appointment.getId());

        if (clinicService != null) {
            clinicService.notifyWaitingList(clinic, slot);

        } else {
            System.err.println("Cannot notify waiting list: ClinicService is not set.");
        }
    }


    // ==============================
    //          RATING LOGIC
    // ==============================

    public boolean addRating(Patient patient, Clinic clinic, Rating rating) throws SQLException {
        if (ratingService.isDuplicateRating(patient, clinic)) {
            return false;
        }

        ratingDAO.add(rating);
        clinic.getRatings().add(rating);
        return true;
    }

    public void deleteRating(Patient patient, Clinic clinic) throws SQLException {
        List<Rating> rAll = ratingDAO.getRatingsByClinicId(clinic.getID());
        for (Rating r : rAll) {
            if (r.getPatient() == patient) {
                ratingService.deleteRating(r);
                break;
            }
        }
    }


    public void addPatient(Patient patient) throws SQLException {
        patientDAO.add(patient);
    }
    //final AppointmentDAO appointmentDAO = new AppointmentDAO();
    public List<Appointment> getPatientAppointments(Patient patient) throws SQLException {
        return appointmentDAO.getAppointmentsByPatientId(patient.getID());
    }



}
