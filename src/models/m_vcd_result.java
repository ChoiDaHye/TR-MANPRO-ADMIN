package models;

import java.util.List;

public class m_vcd_result {

    private List<m_vcd> vcd;

    public m_vcd_result(List<m_vcd> vcd) {
        this.vcd = vcd;
    }

    public List<m_vcd> getVcd() {
        return vcd;
    }

    public void setVcd(List<m_vcd> vcd) {
        this.vcd = vcd;
    }

}
