package models;

public class m_pendapatan {
    private String id_trans, tanggal;
    private Float harga, denda, total;

    public m_pendapatan(String id_trans, String tanggal, Float harga, Float denda, Float total) {
        this.id_trans = id_trans;
        this.tanggal = tanggal;
        this.harga = harga;
        this.denda = denda;
        this.total = total;
    }

    public m_pendapatan() {
    }

    public String getId_trans() {
        return id_trans;
    }

    public void setId_trans(String id_trans) {
        this.id_trans = id_trans;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public Float getHarga() {
        return harga;
    }

    public void setHarga(Float harga) {
        this.harga = harga;
    }

    public Float getDenda() {
        return denda;
    }

    public void setDenda(Float denda) {
        this.denda = denda;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }
    
}
