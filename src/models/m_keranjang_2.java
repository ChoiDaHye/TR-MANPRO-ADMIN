package models;

public class m_keranjang_2 {
    private String id;
    private int baik, buruk;
    private float subdenda;

    public m_keranjang_2(String id, int baik, int buruk, float subdenda) {
        this.id = id;
        this.baik = baik;
        this.buruk = buruk;
        this.subdenda = subdenda;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getBaik() {
        return baik;
    }

    public void setBaik(int baik) {
        this.baik = baik;
    }

    public int getBuruk() {
        return buruk;
    }

    public void setBuruk(int buruk) {
        this.buruk = buruk;
    }

    public float getSubdenda() {
        return subdenda;
    }

    public void setSubdenda(float subdenda) {
        this.subdenda = subdenda;
    }
}
