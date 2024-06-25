package models;

import java.sql.Date;

public class Keuangan {
    private int idTransaksi;
    private java.sql.Date tanggal;
    private String deskripsi;
    private String tipe;
    private double jumlah;

    // Konstruktor default
    public Keuangan() {
    }

    // Konstruktor untuk auto-increment (tanpa idTransaksi)
    public Keuangan(Date tanggal, String deskripsi, String tipe, double jumlah) {
        this.tanggal = tanggal;
        this.deskripsi = deskripsi;
        this.tipe = tipe;
        this.jumlah = jumlah;
    }

    // Konstruktor lengkap (dengan idTransaksi)
    public Keuangan(int idTransaksi, Date tanggal, String deskripsi, String tipe, double jumlah) {
        this.idTransaksi = idTransaksi;
        this.tanggal = tanggal;
        this.deskripsi = deskripsi;
        this.tipe = tipe;
        this.jumlah = jumlah;
    }

    // Getter dan Setter
    public int getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(int idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public double getJumlah() {
        return jumlah;
    }

    public void setJumlah(double jumlah) {
        this.jumlah = jumlah;
    }
}
