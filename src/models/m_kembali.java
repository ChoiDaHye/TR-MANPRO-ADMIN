package models;

import java.util.Date;

public class m_kembali {
    private String id_kembali, id_pinjam, id_karyawan, tgl_kembali;
    private float denda_total;

    public m_kembali(String id_kembali, String id_pinjam, String id_karyawan, String tgl_kembali, float denda_total) {
        this.id_kembali = id_kembali;
        this.id_pinjam = id_pinjam;
        this.id_karyawan = id_karyawan;
        this.tgl_kembali = tgl_kembali;
        this.denda_total = denda_total;
    }

    public m_kembali() {
    }

    public String getId_kembali() {
        return id_kembali;
    }

    public void setId_kembali(String id_kembali) {
        this.id_kembali = id_kembali;
    }

    public String getId_pinjam() {
        return id_pinjam;
    }

    public void setId_pinjam(String id_pinjam) {
        this.id_pinjam = id_pinjam;
    }

    public String getId_karyawan() {
        return id_karyawan;
    }

    public void setId_karyawan(String id_karyawan) {
        this.id_karyawan = id_karyawan;
    }

    public String getTgl_kembali() {
        return tgl_kembali;
    }

    public void setTgl_kembali(String tgl_kembali) {
        this.tgl_kembali = tgl_kembali;
    }

    public float getDenda_total() {
        return denda_total;
    }

    public void setDenda_total(float denda_total) {
        this.denda_total = denda_total;
    }
}
