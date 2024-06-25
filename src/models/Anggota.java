package models;

public class Anggota {
    private int idAnggota;
    private String nama;
    private String nim;
    private String prodi;
    private String jenisKelamin;
    private String alamatKos;
    private String alamatAsal;
    private int angkatan;

    // Konstruktor dengan idAnggota
    public Anggota(int idAnggota, String nama, String nim, String prodi, String 
            jenisKelamin, String alamatKos, String alamatAsal, int angkatan) {
        this.idAnggota = idAnggota;
        this.nama = nama;
        this.nim = nim;
        this.prodi = prodi;
        this.jenisKelamin = jenisKelamin;
        this.alamatKos = alamatKos;
        this.alamatAsal = alamatAsal;
        this.angkatan = angkatan;
    }

    // Konstruktor tanpa idAnggota untuk penambahan data baru
    public Anggota(String nama, String nim, String prodi, String jenisKelamin, 
            String alamatKos, String alamatAsal, int angkatan) {
        this.nama = nama;
        this.nim = nim;
        this.prodi = prodi;
        this.jenisKelamin = jenisKelamin;
        this.alamatKos = alamatKos;
        this.alamatAsal = alamatAsal;
        this.angkatan = angkatan;
    }

    // Konstruktor default
    public Anggota() {
    }

    // Getter dan Setter
    public int getIdAnggota() {
        return idAnggota;
    }

    public void setIdAnggota(int idAnggota) {
        this.idAnggota = idAnggota;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
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

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String getAlamatKos() {
        return alamatKos;
    }

    public void setAlamatKos(String alamatKos) {
        this.alamatKos = alamatKos;
    }

    public String getAlamatAsal() {
        return alamatAsal;
    }

    public void setAlamatAsal(String alamatAsal) {
        this.alamatAsal = alamatAsal;
    }
    
    public int getAngkatan() {
        return angkatan;
    }
    
    public void setAngkatan(int angkatan) {
        this.angkatan = angkatan;
    }
}
