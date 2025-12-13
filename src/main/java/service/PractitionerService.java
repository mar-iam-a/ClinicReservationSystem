package service;

import com.sun.nio.sctp.Notification;
import dao.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import model.*;

public class PractitionerService {
   // private static final String TABLE_NAME = "practitioners";
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final PractitionerDAO practitionerDAO = new PractitionerDAO();
    private final ClinicDAO clinicDAO = new ClinicDAO();
    private final RatingDAO ratingDAO = new RatingDAO();
    private final PatientDAO patientDAO = new PatientDAO();

    // =======================
    //     GET CLINIC
    // =======================
    public List<Practitioner> getAllPractitioners() throws SQLException {
        return practitionerDAO.getAll();
    }

    public void addPractitioner(Practitioner practitioner) throws SQLException {
        practitionerDAO.add(practitioner);
    }

    public Clinic getClinic(Practitioner practitioner) {
        try {
            if (practitioner == null) {
                System.err.println("Practitioner cannot be null.");
                return null;
            }

            if (practitioner.getClinic() == null) {
                System.err.println("Practitioner has no clinic assigned.");
                return null;
            }

            return clinicDAO.getById(practitioner.getClinic().getID());

        } catch (SQLException e) {
            System.err.println("Failed to get clinic: " + e.getMessage());
            return null;
        }
    }


    // =======================
    //     SET CLINIC
    // =======================
    public boolean setClinic(Practitioner practitioner, Clinic clinic) {
        try {
            if (practitioner == null || clinic == null) {
                System.err.println("Practitioner or Clinic cannot be null.");
                return false;
            }


            if (clinic.getID() == 0) {
                clinicDAO.add(clinic);
            }


            practitioner.setClinic(clinic);
            practitionerDAO.update(practitioner);

            return true;
        } catch (SQLException e) {
            System.err.println("Failed to set clinic: " + e.getMessage());
            return false;
        }
    }


    // =======================
    //   GET APPOINTMENTS
    // =======================
    public List<Appointment> getAppointments(Practitioner practitioner) {
        try {
            if (practitioner == null || practitioner.getClinic() == null) {
                System.err.println("Practitioner has no clinic assigned.");
                return new ArrayList<>();
            }

            return appointmentDAO.getAppointmentsByClinicId(practitioner.getClinic().getID());

        } catch (SQLException e) {
            System.err.println("Error fetching appointments: " + e.getMessage());
            return new ArrayList<>();
        }
    }


    // =======================
    //      GET RATINGS
    // =======================
    public List<Rating> getRatings(Practitioner practitioner) {
        try {
            if (practitioner == null || practitioner.getClinic() == null) {
                System.err.println("Practitioner has no clinic assigned.");
                return new ArrayList<>();
            }

            return ratingDAO.getRatingsByClinicId(practitioner.getClinic().getID());

        } catch (SQLException e) {
            System.err.println("Error fetching ratings: " + e.getMessage());
            return new ArrayList<>();
        }
    }


    // =======================
    //    UPDATE CLINIC INFO
    // =======================
    public void updateClinicInfo(Clinic clinic, String name, String address, double price) {

        try {
            if (clinic == null) {
                System.err.println("Clinic cannot be null.");
                return;
            }

            if (name == null || name.isEmpty()) {
                System.err.println("Clinic name cannot be empty.");
                return;
            }

            if (address == null || address.isEmpty()) {
                System.err.println("Clinic address cannot be empty.");
                return;
            }

            if (price < 0) {
                System.err.println("Clinic price cannot be negative.");
                return;
            }

            clinic.setName(name);
            clinic.setAddress(address);
            clinic.setPrice(price);

            clinicDAO.update(clinic);

            System.out.println("Clinic updated successfully.");

        } catch (SQLException e) {
            System.err.println("Error updating clinic info: " + e.getMessage());
        }
    }
    public Practitioner login(String email, String password) {
        try {
            return practitionerDAO.getByEmailAndPassword(email, password);
        } catch (SQLException e) {
            System.err.println("Login error: " + e.getMessage());
            return null;
        }
    }
    public Practitioner getPractitionerById(int practitionerId) throws SQLException {
        if (practitionerId <= 0) {
            System.err.println("Invalid Practitioner ID.");
            return null;
        }

        return practitionerDAO.getById(practitionerId);
    }
    public List<Appointment> getAppointmentsByDate(Practitioner practitioner, LocalDate date) {
        try {
            if (practitioner == null || practitioner.getClinic() == null) {
                return new ArrayList<>();
            }
            int clinicId = practitioner.getClinic().getID();
            return appointmentDAO.getAppointmentsByClinicIdAndDate(clinicId, date);
        } catch (SQLException e) {
            System.err.println("Error fetching appointments for date: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void cancelAppointmentAsPractitioner(int appointmentId, String reason) throws SQLException {
        Appointment appt = appointmentDAO.getById(appointmentId);
        if (appt == null || appt.getStatus() == Status.Cancelled_by_Doctor) {
            throw new IllegalArgumentException("Appointment not found or already cancelled.");
        }

        // 1. غيّري الحالة في قاعدة البيانات
        appointmentDAO.updateStatus(appointmentId, Status.Cancelled_by_Doctor);

        // 2. عدّل الـobject محليًا (لو حابّة تستخدمه بعد كده)
        appt.setStatus(Status.Cancelled_by_Doctor);
        sendCancellationNotification(appt, reason);
    }
    public int cancelAllAppointmentsForDate(Practitioner practitioner, LocalDate date, String reason) throws SQLException {
        List<Appointment> appointments = getAppointmentsByDate(practitioner, date);
        int count = 0;

        for (Appointment appt : appointments) {
            try {
                cancelAppointmentAsPractitioner(appt.getId(), reason);
                count++;
            } catch (Exception e) {
                System.err.println("Failed to cancel appointment " + appt.getId() + ": " + e.getMessage());
                // نكمل باقي المواعيد حتى لو واحد فشل
            }
        }
        return count;
    }
    private void sendCancellationNotification(Appointment appt, String reason) {
        try {
            Patient patient = patientDAO.getById(appt.getPatient().getID());
            String message = String.format(
                    "مرحبًا %s،\n\nنأسف لإعلامك بأنه تم إلغاء موعدك مع د. %s يوم %s.\nالسبب: %s\n\nيرجى حجز موعد جديد من التطبيق.\nمع خالص التحية.",
                    patient.getName(),
                    appt.getClinic().getDoctorName(),
                    appt.getAppointmentDateTime().getDate().format(java.time.format.DateTimeFormatter.ofPattern("EEEE، d MMMM yyyy", java.util.Locale.forLanguageTag("ar"))),
                    reason.isEmpty() ? "ظرف طارئ" : reason
            );
            System.out.println("📧 Notification to " + patient.getEmail() + ":\n" + message);
            // ✅ هنا تضيفي لاحقًا: EmailService.send(...) أو SMS
        } catch (Exception e) {
            System.err.println("Failed to send notification: " + e.getMessage());
        }
    }
    public void cancelAllAppointmentsForClinic(int clinicId, String reason) throws SQLException {
        AppointmentDAO appointmentDAO = new AppointmentDAO();
        List<Appointment> appointments = appointmentDAO.getAppointmentsByClinicId(clinicId);

        for (Appointment a : appointments) {
            if (a.getStatus() == Status.Booked || a.getStatus() == Status.Completed) {
                a.setStatus(Status.Cancelled_by_Doctor); // أو Cancelled
                appointmentDAO.update(a);

                // ✅ إرسال إشعار للمريض (يمكنكِ تحويله لـ Email/Notification لاحقًا)
                sendCancellationNotification(a.getPatient(), a, reason);
            }
        }
    }

    private void sendCancellationNotification(Patient patient, Appointment appointment, String reason) {
        try {
            if (patient == null || patient.getEmail() == null || patient.getEmail().trim().isEmpty()) {
                System.err.println("⚠️ Cannot send email: Patient email is missing.");
                return;
            }

            Clinic clinic = appointment.getClinic();
            TimeSlot slot = appointment.getAppointmentDateTime();
            if (clinic == null || slot == null) {
                System.err.println("⚠️ Cannot send email: Clinic or time slot is missing.");
                return;
            }

            String subject = "Appointment Cancelled — " + clinic.getName();
            String body = "Dear " + safeString(patient.getName(), "Patient") + ",\n\n" +
                    "Your appointment at " + clinic.getName() +
                    " on " + slot.getDate().format(DateTimeFormatter.ofPattern("EEEE, MMM d, yyyy")) +
                    " at " + slot.getStartTime().format(DateTimeFormatter.ofPattern("hh:mm a")) +
                    " has been cancelled.\n\n" +
                    "Reason: " + safeString(reason, "The clinic has been permanently closed.") + "\n\n" +
                    "We sincerely apologize for the inconvenience.\n\n" +
                    "Best regards,\nClinic Management Team";

            // ✅ استخدمي الدالة اللي عندكِ جاهزة
            sendEmail(patient.getEmail(), subject, body);

            System.out.println("✅ Email sent to: " + patient.getEmail());

        } catch (Exception e) {
            System.err.println("❌ Failed to send cancellation email to: " +
                    (patient != null ? patient.getEmail() : "unknown"));
            e.printStackTrace();
        }
    }

    // دالة مساعدة بسيطة (لو مش موجودة)
    private String safeString(String s, String fallback) {
        return (s != null && !s.trim().isEmpty()) ? s.trim() : fallback;
    }
    public static void sendEmail(String to, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("your_email@gmail.com", "your-app-password");
            }
        });

        try {
            // ✅ استخدم MimeMessage مباشرةً — مش Message
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("noreply@yourclinic.com"));
            message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
            System.out.println("✅ Email sent to: " + to);

        } catch (Exception e) {
            System.err.println("❌ Email failed to: " + to);
            e.printStackTrace();
        }
    }

}
