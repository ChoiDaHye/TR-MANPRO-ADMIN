package models;

import java.util.Date;

public class m_vcd {
    private String id_vcd, judul, genre, bahasa, poster, id_harga;
    private Date rilis;
    private int kondisi_baik, kondisi_buruk;

    public String getId_vcd() {
        return id_vcd;
    }

    public void setId_vcd(String id_vcd) {
        this.id_vcd = id_vcd;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getBahasa() {
        return bahasa;
    }

    public void setBahasa(String bahasa) {
        this.bahasa = bahasa;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getId_harga() {
        return id_harga;
    }

    public void setId_harga(String id_harga) {
        this.id_harga = id_harga;
    }

    public Date getRilis() {
        return rilis;
    }

    public void setRilis(Date rilis) {
        this.rilis = rilis;
    }

    public int getKondisi_baik() {
        return kondisi_baik;
    }

    public void setKondisi_baik(int kondisi_baik) {
        this.kondisi_baik = kondisi_baik;
    }

    public int getKondisi_buruk() {
        return kondisi_buruk;
    }

    public void setKondisi_buruk(int kondisi_buruk) {
        this.kondisi_buruk = kondisi_buruk;
    }
}
