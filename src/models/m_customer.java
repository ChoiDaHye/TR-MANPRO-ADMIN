package models;

import java.util.Date;

public class m_customer {
    private String idCustomer, nama, noKtp, kontak, alamat, level, username, password, createdAt, editedAt;

    public m_customer(){}
    
    public m_customer(String idCustomer, String nama, String noKtp, String kontak, String alamat, String username, String password, String createdAt, String editedAt) {
        this.idCustomer = idCustomer;
        this.nama = nama;
        this.noKtp = noKtp;
        this.kontak = kontak;
        this.alamat = alamat;
        this.username = username;
        this.password = password;
        this.createdAt = createdAt;
        this.editedAt = editedAt;
    }

    public String getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNoKtp() {
        return noKtp;
    }

    public void setNoKtp(String noKtp) {
        this.noKtp = noKtp;
    }

    public String getKontak() {
        return kontak;
    }

    public void setKontak(String kontak) {
        this.kontak = kontak;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getEditedAt() {
        return editedAt;
    }

    public void setEditedAt(String editedAt) {
        this.editedAt = editedAt;
    }
}
