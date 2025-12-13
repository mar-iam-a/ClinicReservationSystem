package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import java.io.IOException;
import java.util.Objects;
import javafx.scene.Node;
import javafx.stage.Stage;


public class HomeController {

    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private Button exitButton;

    @FXML
    public void initialize() {
        System.out.println("HomeController initialized");
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Login - Clinic Reservation System");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load Login page.");
        }
    }


    @FXML
    private void handleRegister(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/register.fxml")));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Create Account");
           // stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load Register page.");
        }
    }



    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleExit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Confirmation");
        alert.setContentText("Do you want to exit the system?");
        if (alert.showAndWait().get() == javafx.scene.control.ButtonType.OK) {
            System.exit(0);
        }
    }

    @FXML
    private void onHoverIn(javafx.scene.input.MouseEvent event) {
        Button btn = (Button) event.getSource();
        String style = btn.getStyle();
        if (style.contains("#15BF8F")) {
            btn.setStyle(style.replace("#15BF8F", "#12A075"));
        } else if (style.contains("#ff5555")) {
            btn.setStyle(style.replace("#ff5555", "#e04444"));
        }
    }

    @FXML
    private void onHoverOut(javafx.scene.input.MouseEvent event) {
        Button btn = (Button) event.getSource();
        String style = btn.getStyle();
        if (style.contains("#12A075")) {
            btn.setStyle(style.replace("#12A075", "#15BF8F"));
        } else if (style.contains("#e04444")) {
            btn.setStyle(style.replace("#e04444", "#ff5555"));
        }
    }
}