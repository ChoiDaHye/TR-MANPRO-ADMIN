package models;

public class m_keranjang {
    private String id, judul;
    private int jumlah;
    private float subtotal;

    public m_keranjang(String id, String judul, int jumlah, float subtotal) {
        this.id = id;
        this.judul = judul;
        this.jumlah = jumlah;
        this.subtotal = subtotal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }
}
