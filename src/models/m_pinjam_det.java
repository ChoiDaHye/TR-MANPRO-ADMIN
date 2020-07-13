package models;

public class m_pinjam_det {
    private String id_pinjam, id_vcd;
    private float subtotal;
    private int jumlah, kembali;

    public m_pinjam_det(String id_pinjam, String id_vcd, int jumlah, float subtotal, int kembali) {
        this.id_pinjam = id_pinjam;
        this.id_vcd = id_vcd;
        this.subtotal = subtotal;
        this.jumlah = jumlah;
        this.kembali = kembali;
    }

    public m_pinjam_det() {
    }

    public String getId_pinjam() {
        return id_pinjam;
    }

    public void setId_pinjam(String id_pinjam) {
        this.id_pinjam = id_pinjam;
    }

    public String getId_vcd() {
        return id_vcd;
    }

    public void setId_vcd(String id_vcd) {
        this.id_vcd = id_vcd;
    }

    public float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public int getKembali() {
        return kembali;
    }

    public void setKembali(int kembali) {
        this.kembali = kembali;
    }
}
