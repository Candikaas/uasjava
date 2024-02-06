package Model;

import java.time.LocalDate;

public class NewAbsensi {
    private String nim;
    private String nama;
    private String jenisKelamin;
    private String kelas;
    private LocalDate waktu;

    public NewAbsensi(String nim, String nama, String jenisKelamin, String kelas, LocalDate waktu) {
        this.nim = nim;
        this.nama = nama;
        this.jenisKelamin = jenisKelamin;
        this.kelas = kelas;
        this.waktu = waktu;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public LocalDate getWaktu() {
        return waktu;
    }

    public void setWaktu(LocalDate waktu) {
        this.waktu = waktu;
    }
}
