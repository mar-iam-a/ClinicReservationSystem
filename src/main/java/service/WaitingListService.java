package service;

import dao.WaitingListDAO;
import model.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import jakarta.mail.MessagingException;

public class WaitingListService {

    private final WaitingListDAO waitingListDAO = new WaitingListDAO();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    private final NotificationService notificationService = new NotificationService();

    public void onAppointmentCancelled(Appointment cancelledAppointment) throws SQLException {
        if (cancelledAppointment == null) return;
        TimeSlot slot = cancelledAppointment.getAppointmentDateTime();
        if (slot == null) return;

        Clinic clinic = cancelledAppointment.getClinic();
        if (clinic == null) return;

        LocalDate date = slot.getDate();
        WaitingList next = waitingListDAO.getFirstPendingForDate(clinic.getID(), date);
        if (next != null) {
            offerSlotTo(next, slot);
        }
    }

    public void offerSlotTo(WaitingList entry, TimeSlot freedSlot) throws SQLException {
        try {
            entry.setStatus(WaitingStatus.OFFERED);
            entry.setRequestTime(LocalDateTime.now());
            waitingListDAO.update(entry);

            Patient p = entry.getPatient();
            Clinic clinic = entry.getClinic();
            if (p != null && p.getEmail() != null && clinic != null) {
                String subject = "🔔 A Slot Is Available!";
                String body = String.format(
                        "<h3>Dear %s,</h3>" +
                                "<p>A slot just opened on <strong>%s</strong> at <strong>%s</strong> in Dr. %s's clinic.</p>" +
                                "<p>⏳ You have <strong>10 minutes</strong> to confirm your booking.</p>" +
                                "<p><a href='#' style='display:inline-block;background:#2ecc71;color:white;padding:10px 20px;text-decoration:none;border-radius:4px;'>✅ Confirm Now</a></p>" +
                                "<p>If no action is taken, this offer will expire automatically.</p>" +
                                "<p>Best regards,<br><em>Clinic Management</em></p>",
                        p.getName(),
                        entry.getDate(),
                        freedSlot.getStartTime().format(java.time.format.DateTimeFormatter.ofPattern("hh:mm a")),
                        clinic.getDoctorName()
                );
                try {
                    notificationService.sendEmail(p.getEmail(), subject, body);
                } catch (MessagingException e) {
                    System.err.println("❌ Failed to send email to: " + p.getEmail());
                    e.printStackTrace();
                }
            }

            int entryId = entry.getId();
            scheduler.schedule(() -> {
                try {
                    WaitingList refreshed = waitingListDAO.getById(entryId);
                    if (refreshed != null && refreshed.getStatus() == WaitingStatus.OFFERED) {
                        // لم يُ confirm → EXPIRED
                        refreshed.setStatus(WaitingStatus.EXPIRED);
                        waitingListDAO.update(refreshed);

                        offerNextInQueue(refreshed.getClinic().getID(), refreshed.getDate());
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }, 1, TimeUnit.MINUTES);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to offer slot to waiting patient", e);
        }
    }

    // ★ عرض على اللي بعده
    public void offerNextInQueue(int clinicId, LocalDate date) throws SQLException {
        WaitingList next = waitingListDAO.getFirstPendingForDate(clinicId, date);
        if (next != null) {

            TimeSlot slot = new TimeSlot(date, java.time.LocalTime.of(10, 0));
            offerSlotTo(next, slot);
        }
    }
    // service/WaitingListService.java
    public void updateStatus(int requestId, WaitingStatus newStatus) throws SQLException{
        if (requestId <= 0) {
            throw new IllegalArgumentException("Invalid request ID");
        }
        if (newStatus == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        waitingListDAO.updateStatus(requestId, newStatus);
    }
    public void addPatient(WaitingList item) throws SQLException {
        waitingListDAO.add(item);
    }
    public void expireOldRequests(int clinicId) throws SQLException {
        waitingListDAO.expireOldRequests(clinicId);
    }
    public boolean existsPendingRequest(int patientId, int clinicId, LocalDate date) throws SQLException {
        return waitingListDAO.existsPendingRequest(patientId, clinicId, date);
    }
    public List<WaitingList> getWaitingListByClinicId(int clinicId) throws SQLException {
        return waitingListDAO.findByClinicId(clinicId);
    }
    public List<WaitingList> getPatientWaitingList(int patientId) throws SQLException {
        return waitingListDAO.getPatientPendingRequests(patientId);
    }
}