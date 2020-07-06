package models;

import java.util.List;

public class m_karyawan_result {

    private List<m_karyawan> karyawan;

    public m_karyawan_result(List<m_karyawan> karyawan) {
        this.karyawan = karyawan;
    }

    public List<m_karyawan> getKaryawan() {
        return karyawan;
    }

    public void setKaryawan(List<m_karyawan> karyawan) {
        this.karyawan = karyawan;
    }
}
