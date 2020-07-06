package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class m_karyawan {

    private String idKaryawan, nama, noKtp, kontak, alamat, level, username, password, createdAt, editedAt;

    public m_karyawan() {}

    public m_karyawan(String idKaryawan, String nama, String noKtp, String kontak, String alamat, String level, String username, String password, String createdAt, String editedAt) {
        this.idKaryawan = idKaryawan;
        this.nama = nama;
        this.noKtp = noKtp;
        this.kontak = kontak;
        this.alamat = alamat;
        this.level = level;
        this.username = username;
        this.password = password;
        this.createdAt = createdAt;
        this.editedAt = editedAt;
    }

    public String getIdKaryawan() {
        return idKaryawan;
    }

    public void setIdKaryawan(String idKaryawan) {
        this.idKaryawan = idKaryawan;
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
