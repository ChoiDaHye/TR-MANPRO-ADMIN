package models;

public class m_pinjam {
    private String id_pinjam, status, id_customer, id_karyawan, tgl_pinjam, jatuh_tempo;
    private float harga_total;

    public m_pinjam() {
    }

    public m_pinjam(String id_pinjam, String tgl_pinjam, String jatuh_tempo, float harga_total, String status, String id_customer, String id_karyawan) {
        this.id_pinjam = id_pinjam;
        this.status = status;
        this.id_customer = id_customer;
        this.id_karyawan = id_karyawan;
        this.tgl_pinjam = tgl_pinjam;
        this.jatuh_tempo = jatuh_tempo;
        this.harga_total = harga_total;
    }

    public String getId_pinjam() {
        return id_pinjam;
    }

    public void setId_pinjam(String id_pinjam) {
        this.id_pinjam = id_pinjam;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId_customer() {
        return id_customer;
    }

    public void setId_customer(String id_customer) {
        this.id_customer = id_customer;
    }

    public String getId_karyawan() {
        return id_karyawan;
    }

    public void setId_karyawan(String id_karyawan) {
        this.id_karyawan = id_karyawan;
    }

    public String getTgl_pinjam() {
        return tgl_pinjam;
    }

    public void setTgl_pinjam(String tgl_pinjam) {
        this.tgl_pinjam = tgl_pinjam;
    }

    public String getJatuh_tempo() {
        return jatuh_tempo;
    }

    public void setJatuh_tempo(String jatuh_tempo) {
        this.jatuh_tempo = jatuh_tempo;
    }

    public float getHarga_total() {
        return harga_total;
    }

    public void setHarga_total(float harga_total) {
        this.harga_total = harga_total;
    }
}
