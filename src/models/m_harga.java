package models;

public class m_harga {
    private String idHarga, nama;
    private float harga;
    
    public m_harga(){}
    
    public m_harga(String idHarga, String nama, float harga){
        this.idHarga = idHarga;
        this.nama = nama;
        this.harga = harga;
    }

    public String getIdHarga() {
        return idHarga;
    }

    public void setIdHarga(String idHarga) {
        this.idHarga = idHarga;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public float getHarga() {
        return harga;
    }

    public void setHarga(float harga) {
        this.harga = harga;
    }
}
