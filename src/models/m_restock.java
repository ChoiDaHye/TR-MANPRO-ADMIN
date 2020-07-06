package models;

public class m_restock {
    private String id_restock, tanggal, id_vcd, id_karyawan;
    private int jumlah;

    public m_restock(String id_restock, String tanggal, String id_vcd, String id_karyawan, int jumlah) {
        this.id_restock = id_restock;
        this.tanggal = tanggal;
        this.id_vcd = id_vcd;
        this.id_karyawan = id_karyawan;
        this.jumlah = jumlah;
    }

    public m_restock() {}

    public String getId_restock() {
        return id_restock;
    }

    public void setId_restock(String id_restock) {
        this.id_restock = id_restock;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
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

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }
}
