package service;

import dao.ClinicDAO;
import dao.ScheduleDAO;
import dao.WorkingHoursRuleDAO;
import model.Clinic;
import model.Schedule;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * A background job that activates pending schedules on the 1st of each month.
 * Usage:
 *   new Thread(new MonthlyScheduleActivationJob()).start();
 * Or integrate with a scheduler (e.g., java.util.Timer, Quartz).
 */
public class MonthlyScheduleActivationJob implements Runnable {

    private final ClinicDAO clinicDAO = new ClinicDAO();
    private final ScheduleDAO scheduleDAO = new ScheduleDAO();
    private final WorkingHoursRuleDAO ruleDAO = new WorkingHoursRuleDAO();

    @Override
    public void run() {
        try {
            System.out.println("[" + java.time.LocalDateTime.now() + "] 🟢 Running Monthly Schedule Activation Job...");


            List<Clinic> clinics = clinicDAO.getAll();
            for (Clinic clinic : clinics) {
                try {

                    clinicDAO.loadPendingSchedule(clinic);

                    if (clinic.getPendingSchedule() != null) {
                        System.out.println("→ Processing clinic: " + clinic.getName() + " (ID: " + clinic.getID() + ")");


                        Schedule newActive = clinic.getPendingSchedule();


                        clinic.setSchedule(newActive);
                        clinic.setPendingSchedule(null);

                        // تحديث قاعدة البيانات: جعل schedule_id = pending_schedule_id
                        String updateClinicSql = "UPDATE Clinics SET schedule_id = ?, pending_schedule_id = NULL WHERE id = ?";
                        try (var con = database.DBConnection.getConnection();
                             var ps = con.prepareStatement(updateClinicSql)) {
                            ps.setInt(1, newActive.getID());
                            ps.setInt(2, clinic.getID());
                            ps.executeUpdate();
                        }

                        System.out.println("   ✅ Activated pending schedule (ID: " + newActive.getID() + ") for clinic: " + clinic.getName());
                    }

                } catch (Exception e) {
                    System.err.println("❌ Error activating schedule for clinic ID " + clinic.getID() + ": " + e.getMessage());
                }
            }

            System.out.println("[" + java.time.LocalDateTime.now() + "] 🟢 Monthly job completed.");

        } catch (SQLException e) {
            System.err.println("❌ Database error in MonthlyScheduleActivationJob: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Unexpected error in MonthlyScheduleActivationJob: " + e.getMessage());
        }
    }

    public static void scheduleMonthlyActivation() {
        java.util.Timer timer = new java.util.Timer(true); // daemon thread
        timer.scheduleAtFixedRate(new java.util.TimerTask() {
            @Override
            public void run() {
                new MonthlyScheduleActivationJob().run();
            }
        }, calculateNextFirstOfMonth(), 30L * 24 * 60 * 60 * 1000); // كل ~30 يوم
    }

    private static long calculateNextFirstOfMonth() {
        LocalDate now = LocalDate.now();
        LocalDate firstOfNextMonth = now.withDayOfMonth(1).plusMonths(1);
        java.time.LocalDateTime target = firstOfNextMonth.atStartOfDay();
        return java.time.Duration.between(java.time.LocalDateTime.now(), target).toMillis();
    }
}