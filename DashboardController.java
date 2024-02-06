package Controller;

import Model.Absensi;
import Model.NewAbsensi;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class DashboardController {

    @FXML
    private TableView<Absensi> tabelabsensi;

    @FXML
    private TableColumn<Absensi, Integer> colIdAbsensi;

    @FXML
    private TableColumn<Absensi, String> colNIM;

    @FXML
    private TableColumn<Absensi, String> colNama;

    @FXML
    private TableColumn<Absensi, String> colJenisKelamin;

    @FXML
    private TableColumn<Absensi, String> colKelas;

    @FXML
    private TableColumn<Absensi, String> colWaktu;

    @FXML
    private TextField tfcari;
    
    @FXML
    private Button btntambah;
    
    @FXML
    private Button btnupdate;
    
    @FXML
    private Button btnhapus;
    
    @FXML
    private Button btncari;
    
    @FXML
    private Button btnlogout;

    private ObservableList<Absensi> dataAbsensi = FXCollections.observableArrayList();
    
    @FXML
    private void handleLogout(ActionEvent event) {
        Stage stage = (Stage) btnlogout.getScene().getWindow();
        stage.close();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Login.fxml"));
            Parent root = loader.load();

            LoginController loginController = loader.getController();

            Stage loginStage = new Stage();
            loginStage.setTitle("Login");
            loginStage.setScene(new Scene(root));
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal membuka tampilan login.");
        }
    }

    @FXML
    private void initialize() {
        colIdAbsensi.setCellValueFactory(cellData -> cellData.getValue().idAbsensiProperty().asObject());
        colNIM.setCellValueFactory(cellData -> cellData.getValue().nimProperty());
        colNama.setCellValueFactory(cellData -> cellData.getValue().namaProperty());
        colJenisKelamin.setCellValueFactory(cellData -> cellData.getValue().jenisKelaminProperty());
        colKelas.setCellValueFactory(cellData -> cellData.getValue().kelasProperty());
        colWaktu.setCellValueFactory(cellData -> cellData.getValue().waktuProperty());

        btntambah.setOnAction(this::handleTambah);
        btnupdate.setOnAction(this::handleUpdate);
        btnhapus.setOnAction(this::handleHapus);
        btncari.setOnAction(this::handleCari);
        btnlogout.setOnAction(this::handleLogout);

        loadDataFromDatabase();
    }
    
    @FXML
    private void handleCari(ActionEvent event) {
        String kataKunci = tfcari.getText().trim();

        if (!kataKunci.isEmpty()) {
            cariDataDariDatabase(kataKunci);
        } else {
            loadDataFromDatabase();
        }
    }

    private void cariDataDariDatabase(String kataKunci) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM absensi WHERE nim LIKE ? OR nama_siswa LIKE ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, "%" + kataKunci + "%");
                preparedStatement.setString(2, "%" + kataKunci + "%");

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    dataAbsensi.clear(); 

                    while (resultSet.next()) {
                        int idAbsensi = resultSet.getInt("id_absensi");
                        String nim = resultSet.getString("nim");
                        String nama = resultSet.getString("nama_siswa");
                        String jenisKelamin = resultSet.getString("jenis_kelamin");
                        String kelas = resultSet.getString("kelas");
                        String waktu = resultSet.getString("waktu");

                        Absensi absensi = new Absensi(idAbsensi, nim, nama, jenisKelamin, kelas, waktu);
                        dataAbsensi.add(absensi);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal melakukan pencarian data di database.");
        }
        tabelabsensi.setItems(dataAbsensi);
    }

    
    @FXML
    private void handleUpdate(ActionEvent event) {
        Absensi selectedAbsensi = tabelabsensi.getSelectionModel().getSelectedItem();

        if (selectedAbsensi == null) {
            showAlert("Peringatan", "Pilih data yang ingin diubah.");
            return;
        }

        Dialog<NewAbsensi> dialog = new Dialog<>();
        dialog.setTitle("Edit Data Absensi");

        ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        
        TextField tfNIM = new TextField(selectedAbsensi.nimProperty().get());
        TextField tfNama = new TextField(selectedAbsensi.namaProperty().get());
        TextField tfJenisKelamin = new TextField(selectedAbsensi.jenisKelaminProperty().get());
        TextField tfKelas = new TextField(selectedAbsensi.kelasProperty().get());

        DatePicker datePicker = new DatePicker(LocalDate.parse(selectedAbsensi.waktuProperty().get()));

        grid.add(new Label("NIM:"), 0, 0);
        grid.add(tfNIM, 1, 0);
        grid.add(new Label("Nama:"), 0, 1);
        grid.add(tfNama, 1, 1);
        grid.add(new Label("Jenis Kelamin:"), 0, 2);
        grid.add(tfJenisKelamin, 1, 2);
        grid.add(new Label("Kelas:"), 0, 3);
        grid.add(tfKelas, 1, 3);
        grid.add(new Label("Waktu:"), 0, 4);
        grid.add(datePicker, 1, 4);

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> tfNIM.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeOk) {
                return new NewAbsensi(
                        tfNIM.getText(),
                        tfNama.getText(),
                        tfJenisKelamin.getText(),
                        tfKelas.getText(),
                        datePicker.getValue()
                );
            }
            return null;
        });

        Optional<NewAbsensi> result = dialog.showAndWait();
        result.ifPresent(newAbsensi -> {
            updateDataKeDatabase(selectedAbsensi, newAbsensi);
        });
    }

    private void updateDataKeDatabase(Absensi selectedAbsensi, NewAbsensi newAbsensi) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "UPDATE absensi SET nim=?, nama_siswa=?, jenis_kelamin=?, kelas=?, waktu=? WHERE id_absensi=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, newAbsensi.getNim());
                preparedStatement.setString(2, newAbsensi.getNama());
                preparedStatement.setString(3, newAbsensi.getJenisKelamin());
                preparedStatement.setString(4, newAbsensi.getKelas());
                preparedStatement.setDate(5, java.sql.Date.valueOf(newAbsensi.getWaktu()));
                preparedStatement.setInt(6, selectedAbsensi.idAbsensiProperty().get());

                preparedStatement.executeUpdate();
                loadDataFromDatabase();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal mengupdate data ke database.");
        } finally {
            DatabaseConnection.closeConnection();
        }
    }

    private void loadDataFromDatabase() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM absensi";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                dataAbsensi.clear();

                while (resultSet.next()) {
                    int idAbsensi = resultSet.getInt("id_absensi");
                    String nim = resultSet.getString("nim");
                    String nama = resultSet.getString("nama_siswa");
                    String jenisKelamin = resultSet.getString("jenis_kelamin");
                    String kelas = resultSet.getString("kelas");
                    String waktu = resultSet.getString("waktu");

                    Absensi absensi = new Absensi(idAbsensi, nim, nama, jenisKelamin, kelas, waktu);
                    dataAbsensi.add(absensi);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tabelabsensi.setItems(dataAbsensi);
    }

    @FXML
    private void handleTambah(ActionEvent event) {
        Dialog<NewAbsensi> dialog = new Dialog<>();
        dialog.setTitle("Tambah Data Absensi");

        ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        TextField tfNIM = new TextField();
        TextField tfNama = new TextField();
        TextField tfJenisKelamin = new TextField();
        TextField tfKelas = new TextField();

        DatePicker datePicker = new DatePicker();

        grid.add(new Label("NIM:"), 0, 0);
        grid.add(tfNIM, 1, 0);
        grid.add(new Label("Nama:"), 0, 1);
        grid.add(tfNama, 1, 1);
        grid.add(new Label("Jenis Kelamin:"), 0, 2);
        grid.add(tfJenisKelamin, 1, 2);
        grid.add(new Label("Kelas:"), 0, 3);
        grid.add(tfKelas, 1, 3);
        grid.add(new Label("Waktu:"), 0, 4);
        grid.add(datePicker, 1, 4);

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> tfNIM.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeOk) {
                return new NewAbsensi(
                        tfNIM.getText(),
                        tfNama.getText(),
                        tfJenisKelamin.getText(),
                        tfKelas.getText(),
                        datePicker.getValue()
                );
            }
            return null;
        });

        Optional<NewAbsensi> result = dialog.showAndWait();
        result.ifPresent(newAbsensi -> {
            tambahDataKeDatabase(newAbsensi);
        });
    }
    
    private void tambahDataKeDatabase(NewAbsensi newAbsensi) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO absensi (nim, nama_siswa, jenis_kelamin, kelas, waktu) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, newAbsensi.getNim());
                preparedStatement.setString(2, newAbsensi.getNama());
                preparedStatement.setString(3, newAbsensi.getJenisKelamin());
                preparedStatement.setString(4, newAbsensi.getKelas());
                preparedStatement.setDate(5, java.sql.Date.valueOf(newAbsensi.getWaktu()));

                preparedStatement.executeUpdate();
                loadDataFromDatabase();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal menambahkan data ke database.");
        } finally {
            DatabaseConnection.closeConnection();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    @FXML
    private void handleHapus(ActionEvent event) {
        // Mendapatkan data yang dipilih dari tabel
        Absensi selectedAbsensi = tabelabsensi.getSelectionModel().getSelectedItem();

        if (selectedAbsensi == null) {
            showAlert("Peringatan", "Pilih data yang ingin dihapus.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi");
        alert.setHeaderText("Hapus Data Absensi");
        alert.setContentText("Apakah Anda yakin ingin menghapus data absensi ini?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            hapusDataDariDatabase(selectedAbsensi);
        }
    }

    private void hapusDataDariDatabase(Absensi selectedAbsensi) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM absensi WHERE id_absensi = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, selectedAbsensi.idAbsensiProperty().get());
                preparedStatement.executeUpdate();

                loadDataFromDatabase();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal menghapus data dari database.");
        } finally {
            DatabaseConnection.closeConnection();
        }
    }

    
}
