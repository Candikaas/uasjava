package Controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField tfusername;

    @FXML
    private PasswordField tfpassword;

    @FXML
    private Button btnlogin;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = tfusername.getText();
        String password = tfpassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Login Gagal", "Username dan Password harus diisi.");
            return;
        }

        if (username.equals("Admin") && password.equals("Admin")) {
            showDashboard();
        } else {
            showAlert("Login Gagal", "Username atau Password salah. Coba lagi.");
        }
    }

    private void showDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Dashboard.fxml"));
            Stage stage = (Stage) btnlogin.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Terjadi kesalahan saat membuka Dashboard.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
