package models;

import java.util.List;

public class m_pinjam_result {

    private List<m_pinjam> pinjam;

    public m_pinjam_result(List<m_pinjam> pinjam) {
        this.pinjam = pinjam;
    }

    public List<m_pinjam> getPinjam() {
        return pinjam;
    }

    public void setPinjam(List<m_pinjam> pinjam) {
        this.pinjam = pinjam;
    }
}
