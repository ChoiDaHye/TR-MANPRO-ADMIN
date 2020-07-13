package models;

import java.util.List;

public class m_pinjam_det_result {
    private List<m_pinjam_det> pinjam_det;

    public m_pinjam_det_result(List<m_pinjam_det> pinjam_det) {
        this.pinjam_det = pinjam_det;
    }

    public List<m_pinjam_det> getPinjam_det() {
        return pinjam_det;
    }

    public void setPinjam_det(List<m_pinjam_det> pinjam_det) {
        this.pinjam_det = pinjam_det;
    }
}
