package util;

import model.Appointment;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HTMLReportGenerator {

    public static String generateAppointmentsHTML(List<Appointment> appointments, String doctorName) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n");
        html.append("<meta charset=\"UTF-8\">\n");
        html.append("<title>Appointments Report</title>\n");
        html.append("<style>\n");
        html.append("body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 30px; background: #f9fbfb; }\n");
        html.append(".container { max-width: 1000px; margin: 0 auto; background: white; padding: 25px; border-radius: 10px; box-shadow: 0 0 15px rgba(0,0,0,0.08); }\n");
        html.append(".header { text-align: center; margin-bottom: 25px; border-bottom: 2px solid #15BF8F; padding-bottom: 15px; }\n");
        html.append("h1 { color: #2c3e50; margin: 0; font-weight: 600; }\n");
        html.append("p.subtitle { color: #7f8c8d; margin: 5px 0 0; }\n");
        html.append("table { width: 100%; border-collapse: collapse; margin-top: 20px; font-size: 14px; }\n");
        html.append("th, td { padding: 12px 10px; text-align: left; border-bottom: 1px solid #eee; }\n");
        html.append("th { background-color: #f8f9fa; font-weight: 600; color: #34495e; }\n");
        html.append("tr:hover { background-color: #f5faf9; }\n");
        html.append(".footer { margin-top: 30px; text-align: center; color: #95a5a6; font-size: 13px; }\n");
        html.append("</style>\n");
        html.append("</head>\n<body>\n");
        html.append("<div class=\"container\">\n");
        html.append("  <div class=\"header\">\n");
        html.append("    <h1>📅 Appointments Report</h1>\n");
        html.append("    <p class=\"subtitle\">Generated on: ");
        html.append(java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, MMM d, yyyy 'at' h:mm a")));
        html.append("</p>\n");
        html.append("  </div>\n");

        // الجدول
        html.append("<table>\n");
        html.append("<thead><tr>");
        html.append("<th>Date</th>");
        html.append("<th>Time</th>");
        html.append("<th>Patient</th>");
        html.append("<th>Status</th>");
        html.append("</tr></thead>\n<tbody>\n");

        if (appointments == null || appointments.isEmpty()) {
            html.append("<tr><td colspan='4' style='text-align:center; color:#e74c3c;'>No appointments available</td></tr>\n");
        } else {
            for (Appointment appt : appointments) {
                String date = (appt.getAppointmentDateTime() != null && appt.getAppointmentDateTime().getDate() != null)
                        ? appt.getAppointmentDateTime().getDate().format(DateTimeFormatter.ofPattern("MMM d, yyyy"))
                        : "—";
                String time = (appt.getAppointmentDateTime() != null)
                        ? appt.getAppointmentDateTime().getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")) + "–"
                        + appt.getAppointmentDateTime().getEndTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                        : "—";
                String patient = (appt.getPatient() != null && appt.getPatient().getName() != null)
                        ? appt.getPatient().getName()
                        : "Unknown";
                String status = (appt.getStatus() != null) ? appt.getStatus().toString() : "—";

                html.append("<tr>");
                html.append("<td>").append(date).append("</td>");
                html.append("<td>").append(time).append("</td>");
                html.append("<td>").append(patient).append("</td>");
                html.append("<td>").append(status).append("</td>");
                html.append("</tr>\n");
            }
        }

        html.append("</tbody>\n</table>\n");

        // التذييل
        html.append("<div class=\"footer\">\n");
        html.append("<p>Total: ").append(appointments != null ? appointments.size() : 0).append(" appointment(s)</p>\n");
        if (doctorName != null && !doctorName.trim().isEmpty()) {
            html.append("<p>Dr. ").append(doctorName).append(" — DoCC Medical System</p>\n");
        } else {
            html.append("<p>DoCC Medical System</p>\n");
        }
        html.append("</div>\n");
        html.append("</div>\n");
        html.append("</body>\n</html>");

        return html.toString();
    }
}