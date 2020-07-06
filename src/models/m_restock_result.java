package models;

import java.util.List;

public class m_restock_result {
    private List<m_restock> restock;

    public m_restock_result(List<m_restock> restock) {
        this.restock = restock;
    }

    public List<m_restock> getRestock() {
        return restock;
    }

    public void setRestock(List<m_restock> restock) {
        this.restock = restock;
    }
}
