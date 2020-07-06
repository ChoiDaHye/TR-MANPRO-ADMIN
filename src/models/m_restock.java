package models;

import java.util.Date;

public class m_restock {
    private String id_restock, id_vcd, id_karyawan;
    private Date tanggal;
    private int jumlah;

    public String getId_restock() {
        return id_restock;
    }

    public void setId_restock(String id_restock) {
        this.id_restock = id_restock;
    }

    public String getId_vcd() {
        return id_vcd;
    }

    public void setId_vcd(String id_vcd) {
        this.id_vcd = id_vcd;
    }

    public String getId_karyawan() {
        return id_karyawan;
    }

    public void setId_karyawan(String id_karyawan) {
        this.id_karyawan = id_karyawan;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }
}
