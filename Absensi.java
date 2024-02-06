package Model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Absensi {
    private SimpleIntegerProperty idAbsensi;
    private SimpleStringProperty nim;
    private SimpleStringProperty nama;
    private SimpleStringProperty jenisKelamin;
    private SimpleStringProperty kelas;
    private SimpleStringProperty waktu;

    public Absensi(int idAbsensi, String nim, String nama, String jenisKelamin, String kelas, String waktu) {
        this.idAbsensi = new SimpleIntegerProperty(idAbsensi);
        this.nim = new SimpleStringProperty(nim);
        this.nama = new SimpleStringProperty(nama);
        this.jenisKelamin = new SimpleStringProperty(jenisKelamin);
        this.kelas = new SimpleStringProperty(kelas);
        this.waktu = new SimpleStringProperty(waktu);
    }

    public SimpleIntegerProperty idAbsensiProperty() {
        return idAbsensi;
    }

    public SimpleStringProperty nimProperty() {
        return nim;
    }

    public SimpleStringProperty namaProperty() {
        return nama;
    }

    public SimpleStringProperty jenisKelaminProperty() {
        return jenisKelamin;
    }

    public SimpleStringProperty kelasProperty() {
        return kelas;
    }

    public SimpleStringProperty waktuProperty() {
        return waktu;
    }

}
