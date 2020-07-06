package models;

import java.util.List;

public class m_customer_result {

    private List<m_customer> customer;

    public m_customer_result(List<m_customer> customer) {
        this.customer = customer;
    }

    public List<m_customer> getCustomer() {
        return customer;
    }

    public void setCustomer(List<m_customer> customer) {
        this.customer = customer;
    }
}
