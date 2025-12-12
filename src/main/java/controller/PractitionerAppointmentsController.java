package controller;

import dao.AppointmentDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.Appointment;
import model.Practitioner;
import service.PractitionerService;

import java.time.LocalDate;
import java.util.List;

public class PractitionerAppointmentsController {

    @FXML private DatePicker datePicker;
    @FXML private Button cancelAllBtn;
    @FXML private Label dateHeader;
    @FXML private TableView<Appointment> appointmentsTable;
    @FXML private TableColumn<Appointment, String> timeCol;
    @FXML private TableColumn<Appointment, String> patientCol;
    @FXML private TableColumn<Appointment, String> statusCol;
    @FXML private TableColumn<Appointment, Void> actionCol;

    private Practitioner currentPractitioner;
    private final PractitionerService practitionerService = new PractitionerService();


    public void setPractitioner(Practitioner practitioner) {
        this.currentPractitioner = practitioner;
        datePicker.setValue(LocalDate.now());
        loadAppointmentsForDate();
    }

    @FXML
    private void initialize() {

        datePicker.setValue(LocalDate.now());
        datePicker.valueProperty().addListener((obs, oldVal, newVal) -> loadAppointmentsForDate());
    }

    @FXML
    private void loadAppointmentsForDate() {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate == null || currentPractitioner == null) return;

        String formattedDate = selectedDate.format(
                java.time.format.DateTimeFormatter.ofPattern("EEEE، d MMMM yyyy", java.util.Locale.forLanguageTag("ar"))
        );
        dateHeader.setText("Today's Appointments" + formattedDate);

        try {
            List<Appointment> apps = practitionerService.getAppointmentsByDate(currentPractitioner, selectedDate);
            appointmentsTable.getItems().setAll(apps);
            cancelAllBtn.setDisable(apps.isEmpty());

            timeCol.setCellValueFactory(data ->
                    new javafx.beans.property.SimpleStringProperty(
                            data.getValue().getAppointmentDateTime().getStartTime()
                                    .format(java.time.format.DateTimeFormatter.ofPattern("hh:mm a"))
                    )
            );

            patientCol.setCellValueFactory(data ->
                    new javafx.beans.property.SimpleStringProperty(
                            data.getValue().getPatient().getName()
                    )
            );

            statusCol.setCellValueFactory(data ->
                    new javafx.beans.property.SimpleStringProperty(
                            data.getValue().getStatus().name()
                    )
            );

            actionCol.setCellFactory(col -> new TableCell<>() {
                private final Button cancelBtn = new Button("Cancel");
                {
                    cancelBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 3 10; -fx-font-size: 12px;");
                    cancelBtn.setOnAction(e -> {
                        Appointment appt = getTableView().getItems().get(getIndex());
                        confirmSingleCancel(appt);
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : cancelBtn);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load appointments");
        }
    }

    @FXML
    private void handleCancelAll() {
        LocalDate date = datePicker.getValue();
        if (date == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancellation Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you've cancelled **all** appointments for today?\n" +
                date.format(java.time.format.DateTimeFormatter.ofPattern("EEEE، d MMMM yyyy", java.util.Locale.forLanguageTag("ar"))) +
                "؟\nNotifications will be sent to the patient.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    int count = practitionerService.cancelAllAppointmentsForDate(
                            currentPractitioner, date, "Apology from the clinic"
                    );
                    showAlert(Alert.AlertType.INFORMATION, "Success", "The Appointment is Cancelled" + count + "Appointments");
                    loadAppointmentsForDate();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Error", "Cancellation failed:" + ex.getMessage());
                }
            }
        });
    }

    private void confirmSingleCancel(Appointment appt) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Appointment");
        alert.setHeaderText("Cancel an appointment with" + appt.getPatient().getName());
        alert.setContentText("Do you want to cancel this appointment? A notification will be sent to the patient.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    practitionerService.cancelAppointmentAsPractitioner(appt.getId(), "Apology from the clinic");
                    showAlert(Alert.AlertType.INFORMATION, "Done", "The Appointment is Cancelled");
                    loadAppointmentsForDate();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
                }
            }
        });
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}