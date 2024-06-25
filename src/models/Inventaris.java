package models;

public class Inventaris {
    private int idBarang;
    private String namaBarang;
    private int quantity;
    private String keterangan;

    // Konstruktor dengan idBarang
    public Inventaris(int idBarang, String namaBarang, int quantity, String keterangan) {
        this.idBarang = idBarang;
        this.namaBarang = namaBarang;
        this.quantity = quantity;
        this.keterangan = keterangan;
    }

    // Konstruktor tanpa idBarang untuk penambahan data baru
    public Inventaris(String namaBarang, int quantity, String keterangan) {
        this.namaBarang = namaBarang;
        this.quantity = quantity;
        this.keterangan = keterangan;
    }

    // Konstruktor default
    public Inventaris() {
    }

    // Getter dan Setter
    public int getIdBarang() {
        return idBarang;
    }

    public void setIdBarang(int idBarang) {
        this.idBarang = idBarang;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}
