package swing;

import dao.dao_awal;

public class launcher {
    public static void main(String[] args) {
        dao_awal dao = new dao_awal();
        boolean cek_induk = dao.cek_induk();

        if (!cek_induk) {
            Awal_karyawan ak = new Awal_karyawan();
            ak.setVisible(true);
        } else {
            Login lg = new Login();
            lg.setVisible(true);
        }
    }
}
