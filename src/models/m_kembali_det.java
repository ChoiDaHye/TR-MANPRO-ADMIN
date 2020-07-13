/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author dgeda
 */
public class m_kembali_det {
    String id_kembali, id_vcd;
    int jumlah, kondisi_rusak;
    float  denda;

    public m_kembali_det(String id_kembali, String id_vcd, int jumlah, int kondisi_rusak, float denda) {
        this.id_kembali = id_kembali;
        this.id_vcd = id_vcd;
        this.jumlah = jumlah;
        this.kondisi_rusak = kondisi_rusak;
        this.denda = denda;
    }

    public m_kembali_det() {
    }

    public String getId_kembali() {
        return id_kembali;
    }

    public void setId_kembali(String id_kembali) {
        this.id_kembali = id_kembali;
    }

    public String getId_vcd() {
        return id_vcd;
    }

    public void setId_vcd(String id_vcd) {
        this.id_vcd = id_vcd;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public int getKondisi_rusak() {
        return kondisi_rusak;
    }

    public void setKondisi_rusak(int kondisi_rusak) {
        this.kondisi_rusak = kondisi_rusak;
    }

    public float getDenda() {
        return denda;
    }

    public void setDenda(float denda) {
        this.denda = denda;
    }
    
}
