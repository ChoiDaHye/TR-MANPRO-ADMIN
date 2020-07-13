/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.List;

/**
 *
 * @author dgeda
 */
public class m_kembali_det_result {

    private List<m_kembali_det> kembali_det;

    public m_kembali_det_result(List<m_kembali_det> kembali_det) {
        this.kembali_det = kembali_det;
    }

    public List<m_kembali_det> getKembali_det() {
        return kembali_det;
    }

    public void setKembali_det(List<m_kembali_det> kembali_det) {
        this.kembali_det = kembali_det;
    }
    
}
