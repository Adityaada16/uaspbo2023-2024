package models;

import java.sql.Date;

public class Peminjaman {
    private int idPeminjaman;
    private int idAnggota;
    private int idBarang;
    private Date tanggalPeminjaman;
    private Date tanggalPengembalian;

    // Informasi dari tabel anggota
    private String namaAnggota;
    private String nim;
    private String prodi;

    // Informasi dari tabel inventaris
    private String namaBarang;

    // Konstruktor baru (tanpa informasi tambahan)
    public Peminjaman(int idAnggota, int idBarang, Date tanggalPeminjaman, Date tanggalPengembalian) {
        this.idAnggota = idAnggota;
        this.idBarang = idBarang;
        this.tanggalPeminjaman = tanggalPeminjaman;
        this.tanggalPengembalian = tanggalPengembalian;
    }

    // Constructor untuk kasus auto-increment (tanpa idPeminjaman)
    public Peminjaman(int idAnggota, int idBarang, Date tanggalPeminjaman, Date tanggalPengembalian, 
                      String namaAnggota, String nim, String prodi, String namaBarang) {
        this.idAnggota = idAnggota;
        this.idBarang = idBarang;
        this.tanggalPeminjaman = tanggalPeminjaman;
        this.tanggalPengembalian = tanggalPengembalian;
        this.namaAnggota = namaAnggota;
        this.nim = nim;
        this.prodi = prodi;
        this.namaBarang = namaBarang;
    }

    // Constructor lengkap (dengan idPeminjaman)
    public Peminjaman(int idPeminjaman, int idAnggota, int idBarang, Date tanggalPeminjaman, Date tanggalPengembalian, 
                      String namaAnggota, String nim, String prodi, String namaBarang) {
        this.idPeminjaman = idPeminjaman;
        this.idAnggota = idAnggota;
        this.idBarang = idBarang;
        this.tanggalPeminjaman = tanggalPeminjaman;
        this.tanggalPengembalian = tanggalPengembalian;
        this.namaAnggota = namaAnggota;
        this.nim = nim;
        this.prodi = prodi;
        this.namaBarang = namaBarang;
    }

    // Getters dan Setters
    public int getIdPeminjaman() {
        return idPeminjaman;
    }

    public void setIdPeminjaman(int idPeminjaman) {
        this.idPeminjaman = idPeminjaman;
    }

    public int getIdAnggota() {
        return idAnggota;
    }

    public void setIdAnggota(int idAnggota) {
        this.idAnggota = idAnggota;
    }

    public int getIdBarang() {
        return idBarang;
    }

    public void setIdBarang(int idBarang) {
        this.idBarang = idBarang;
    }

    public Date getTanggalPeminjaman() {
        return tanggalPeminjaman;
    }

    public void setTanggalPeminjaman(Date tanggalPeminjaman) {
        this.tanggalPeminjaman = tanggalPeminjaman;
    }

    public Date getTanggalPengembalian() {
        return tanggalPengembalian;
    }

    public void setTanggalPengembalian(Date tanggalPengembalian) {
        this.tanggalPengembalian = tanggalPengembalian;
    }

    public String getNamaAnggota() {
        return namaAnggota;
    }

    public void setNamaAnggota(String namaAnggota) {
        this.namaAnggota = namaAnggota;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getProdi() {
        return prodi;
    }

    public void setProdi(String prodi) {
        this.prodi = prodi;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }
}
