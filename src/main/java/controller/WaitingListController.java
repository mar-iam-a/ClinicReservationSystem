// 🌟 WaitingListController.java

package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.Patient;
import model.WaitingList;
import service.WaitingListService;
import java.sql.SQLException;
import java.util.List;
import javafx.geometry.Insets;
import java.time.format.DateTimeFormatter;
import dao.WaitingListDAO;

public class WaitingListController {

    @FXML
    private VBox waitingListContainer;

    private Patient currentPatient;
    private final WaitingListService waitingListService = new WaitingListService();

    public void setPatient(Patient patient) {
        this.currentPatient = patient;
        loadWaitingList();
    }

    private void loadWaitingList() {
        waitingListContainer.getChildren().clear();
        if (currentPatient == null) return;

        try {
            List<WaitingList> requests = waitingListService.getPatientWaitingList(currentPatient.getID());

            if (requests.isEmpty()) {
                waitingListContainer.getChildren().add(new Label("There are no requests in your waiting list at the moment."));
            } else {
                for (WaitingList request : requests) {
                    waitingListContainer.getChildren().add(createWaitingListCard(request));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            waitingListContainer.getChildren().add(new Label("❌ An error occurred while loading the waiting list."));
        }
    }

    private VBox createWaitingListCard(WaitingList request) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color: #F8F9FA; -fx-border-color: #E0E0E0; -fx-border-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 1);");

        Label clinicName = new Label("Clinic: " + request.getClinic().getName());
        clinicName.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1ABC9C;");

        Label doctorName = new Label("Doctor: " + request.getClinic().getDoctorName());
        doctorName.setStyle("-fx-font-size: 14px; -fx-text-fill: #555;");

        String timeStr = request.getRequestTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        Label requestTime = new Label("Registration Time: " + timeStr);
        requestTime.setStyle("-fx-font-size: 12px; -fx-text-fill: #777;");

        Label status = new Label("Status: " + request.getStatus().name());
        status.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #F39C12;");

        card.getChildren().addAll(clinicName, doctorName, requestTime, status);
        return card;
    }
}