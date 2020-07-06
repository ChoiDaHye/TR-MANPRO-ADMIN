package models;

import java.util.List;

public class m_harga_result {
    
    private List<m_harga> harga;

    public m_harga_result(List<m_harga> harga) {
        this.harga = harga;
    }

    public List<m_harga> getHarga() {
        return harga;
    }

    public void setHarga(List<m_harga> harga) {
        this.harga = harga;
    }
}
