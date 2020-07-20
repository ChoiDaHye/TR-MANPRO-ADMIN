package swing;

import javax.swing.RowFilter;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import dao.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.TableRowSorter;
import jxl.write.WriteException;
import models.m_customer;
import models.m_harga;
import models.m_karyawan;
import models.m_pinjam;
import models.m_pinjam_det;
import models.m_vcd;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;

public class Admin_dashboard extends javax.swing.JFrame {

    int state = 0;

    //for param
    String d_id_vcd, d_id_customer, d_id_karyawan, d_id_harga, p_id_customer;

    //for indentity
    String log_id, log_name, log_level, booking_id, status;

    // METHOD PINJAM
    void pinjam_tampil() {
        Object[] baris = {"KODE BOOKING", "CUSTOMER", "KARYAWAN", "TANGGAL", "JATUH TEMPO", "TOTAL HARGA"};
        DefaultTableModel dt = new DefaultTableModel(null, baris);
        tb_pinjam.setModel(dt);

        try {
            dao_pinjam dao = new dao_pinjam();
            List<m_pinjam> data = dao.readPinjam();

            for (m_pinjam d : data) {
                String total = Float.toString(d.getHarga_total());
                String[] isi = {d.getId_pinjam(), dao.getCus(d.getId_customer()), dao.getKar(d.getId_karyawan()), d.getTgl_pinjam(), d.getJatuh_tempo(), total};
                dt.addRow(isi);
            }
        } catch (Exception e) {
        }
    }

    void tb_pinjam_klik() {
        int row = tb_pinjam.getSelectedRow();
        String id_pinjam = tb_pinjam.getModel().getValueAt(row, 0).toString();

        Object[] baris = {"ID VCD", "JUDUL", "JUMLAH", "SUBTOTAL"};
        DefaultTableModel dt = new DefaultTableModel(null, baris);
        tb_pinjam_det.setModel(dt);
        dt.setRowCount(0);

        try {
            dao_pinjam dao = new dao_pinjam();
            List<m_pinjam_det> data = dao.readDetail(id_pinjam);

            for (m_pinjam_det d : data) {
                String subtotal = Float.toString(d.getSubtotal());
                String jumlah = Integer.toString(d.getJumlah());
                String[] isi = {d.getId_vcd(), dao.getJudul(d.getId_vcd()), jumlah, subtotal};
                dt.addRow(isi);
            }
        } catch (Exception e) {
        }
    }

    // METHOD KEMBALI
    void kembali_tampil() {
        Object[] baris = {"KODE BOOKING", "CUSTOMER", "KARYAWAN", "TANGGAL", "JATUH TEMPO", "TOTAL HARGA", "STATUS"};
        DefaultTableModel dt = new DefaultTableModel(null, baris);
        tb_kembali.setModel(dt);

        try {
            dao_kembali dao = new dao_kembali();
            List<m_pinjam> data = dao.readPinjam();

            for (m_pinjam d : data) {
                String total = Float.toString(d.getHarga_total());
                String[] isi = {d.getId_pinjam(), dao.getCus(d.getId_customer()), dao.getKar(d.getId_karyawan()), d.getTgl_pinjam(), d.getJatuh_tempo(), total, d.getStatus()};
                dt.addRow(isi);
            }
        } catch (Exception e) {
        }
    }

    void tb_kembali_klik() {
        int row = tb_kembali.getSelectedRow();
        String id_pinjam = tb_kembali.getModel().getValueAt(row, 0).toString();
        booking_id = tb_kembali.getModel().getValueAt(row, 0).toString();
        status = tb_kembali.getModel().getValueAt(row, 6).toString();

        Object[] baris = {"ID VCD", "JUDUL", "JUMLAH", "KEMBALI"};
        DefaultTableModel dt = new DefaultTableModel(null, baris);
        jTable2.setModel(dt);
        dt.setRowCount(0);

        try {
            dao_kembali dao = new dao_kembali();
            List<m_pinjam_det> data = dao.readDetail(id_pinjam);

            for (m_pinjam_det d : data) {
                String kembali = Integer.toString(d.getKembali());
                String jumlah = Integer.toString(d.getJumlah());
                String[] isi = {d.getId_vcd(), dao.getJudul(d.getId_vcd()), jumlah, kembali};
                dt.addRow(isi);
            }
        } catch (Exception e) {
        }
    }

    // METHOD PROFIL
    void profil_tampil() {
        try {
            dao_karyawan dao = new dao_karyawan();
            List<m_karyawan> data = dao.readKaryawan();

            for (m_karyawan d : data) {
                if (d.getIdKaryawan().equals(log_id)) {
                    profile_txt_ktp.setText(d.getNoKtp());
                    profile_txt_nama.setText(d.getNama());
                    profile_txt_kontak.setText(d.getKontak());
                    profile_txt_alamat.setText(d.getAlamat());
                    profile_txt_level.setText(d.getLevel());
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void profil_edit(String nama, String kontak, String alamat) {
        try {
            dao_karyawan dao = new dao_karyawan();
            m_karyawan data = new m_karyawan();
            String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

            data.setIdKaryawan(log_id);
            data.setNama(nama);
            data.setKontak(kontak);
            data.setAlamat(alamat);
            data.setEditedAt(date);

            if (dao.editProfile(data)) {
                JOptionPane.showMessageDialog(null, "Data berhasil tersimpan!", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Maaf, data gagal tersimpan!", "Gagal", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menyimpan data!\n" + e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    void ganti_password(String old, String now) {
        try {
            dao_karyawan dao = new dao_karyawan();
            m_karyawan data = new m_karyawan();
            String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

            data.setIdKaryawan(log_id);
            data.setPassword(old);
            data.setEditedAt(date);

            if (dao.ganti_password(data, now)) {
                JOptionPane.showMessageDialog(null, "Data berhasil tersimpan!", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Maaf, data gagal tersimpan!", "Gagal", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menyimpan data!\n" + e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // METHOD VCD
    void vcd_tampil() {
        Object[] baris = {"ID", "JUDUL", "RILIS", "GENRE", "BAHASA", "BAIK", "BURUK", "TERPINJAM"};
        DefaultTableModel dt = new DefaultTableModel(null, baris);
        tb_vcd.setModel(dt);

        try {
            dao_vcd dao = new dao_vcd();
            List<m_vcd> data = dao.readVcd();

            for (m_vcd d : data) {
                String baik = Integer.toString(d.getKondisi_baik());
                String buruk = Integer.toString(d.getKondisi_buruk());
                String pinjam = Integer.toString(d.getTerpinjam());
                String[] isi = {d.getId_vcd(), d.getJudul(), d.getRilis(), d.getGenre(), d.getBahasa(), baik, buruk, pinjam};
                dt.addRow(isi);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void tb_vcd_klik() {
        int row = tb_vcd.getSelectedRow();
        d_id_vcd = tb_vcd.getModel().getValueAt(row, 0).toString();
    }

    void vcd_hapus(String param) {
        try {
            dao_vcd dao = new dao_vcd();
            m_vcd data = new m_vcd();
            data.setId_vcd(param);

            if (dao.deleteVcd(data)) {
                JOptionPane.showMessageDialog(null, "Data berhasil dihapus!", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Maaf, data gagal dihapus!", "Gagal", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menghapus data!\n" + e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // METHOD CUSTOMER
    void customer_tampil() {
        Object[] baris = {"ID", "NAMA", "NOMOR KTP", "ALAMAT", "KONTAK"};
        DefaultTableModel dt = new DefaultTableModel(null, baris);
        tb_customer.setModel(dt);

        try {
            dao_customer dao = new dao_customer();
            List<m_customer> data = dao.readCustomer();

            for (m_customer d : data) {
                String[] isi = {d.getIdCustomer(), d.getNama(), d.getNoKtp(), d.getAlamat(), d.getKontak()};
                dt.addRow(isi);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void tb_customer_klik() {
        int row = tb_customer.getSelectedRow();
        d_id_customer = tb_customer.getModel().getValueAt(row, 0).toString();
    }

    void tb_customer_cari(String query) {
    }

    void customer_hapus(String param) {
        try {
            dao_customer dao = new dao_customer();
            m_customer data = new m_customer();
            data.setIdCustomer(param);

            if (dao.deleteKaryawan(data)) {
                JOptionPane.showMessageDialog(null, "Data berhasil dihapus!", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Maaf, data gagal dihapus!", "Gagal", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menghapus data!\n" + e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // METHOD KARYAWAN
    void karyawan_tampil() {
        Object[] baris = {"ID", "NAMA", "NOMOR KTP", "ALAMAT", "KONTAK", "LEVEL"};
        DefaultTableModel dt = new DefaultTableModel(null, baris);
        tb_karyawan.setModel(dt);

        try {
            dao_karyawan dao = new dao_karyawan();
            List<m_karyawan> data = dao.readKaryawan();

            for (m_karyawan d : data) {
                String[] isi = {d.getIdKaryawan(), d.getNama(), d.getNoKtp(), d.getAlamat(), d.getKontak(), d.getLevel()};
                dt.addRow(isi);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void tb_karyawan_klik() {
        int row = tb_karyawan.getSelectedRow();
        d_id_karyawan = tb_karyawan.getModel().getValueAt(row, 0).toString();
    }

    void tb_karyawan_cari(String query) {
        try {
            DefaultTableModel model = (DefaultTableModel) tb_karyawan.getModel();
            TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(model);
            tb_karyawan.setRowSorter(tr);
            tr.setRowFilter(RowFilter.regexFilter(query));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void karyawan_hapus(String param) {
        try {
            dao_karyawan dao = new dao_karyawan();
            m_karyawan data = new m_karyawan();
            data.setIdKaryawan(param);

            if (dao.deleteKaryawan(data)) {
                JOptionPane.showMessageDialog(null, "Data berhasil dihapus!", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Maaf, data gagal dihapus!", "Gagal", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menghapus data!\n" + e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // METHOD HARGA
    void harga_tampil() {
        Object[] baris = {"ID", "NAMA", "HARGA"};
        DefaultTableModel dt = new DefaultTableModel(null, baris);
        tb_harga.setModel(dt);

        try {
            dao_harga dao = new dao_harga();
            List<m_harga> data = dao.readHarga();

            for (m_harga d : data) {
                String[] isi = {d.getIdHarga(), d.getNama(), Float.toString(d.getHarga())};
                dt.addRow(isi);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void tb_harga_klik() {
        int row = tb_harga.getSelectedRow();
        d_id_harga = tb_harga.getModel().getValueAt(row, 0).toString();
    }

    void tb_harga_cari(String query) {
    }

    void harga_hapus(String param) {
        try {
            dao_harga dao = new dao_harga();
            m_harga data = new m_harga();
            data.setIdHarga(param);

            if (dao.deleteHarga(data)) {
                JOptionPane.showMessageDialog(null, "Data berhasil dihapus!", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Maaf, data gagal dihapus!", "Gagal", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menghapus data!\n" + e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //for dashboard
    private void frameEnter(JPanel p) {
        p.setBorder(BorderFactory.createLineBorder(new Color(213, 213, 213), 2));
    }

    private void frameExit(JPanel p) {
        p.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
    }

    //for left-panel
    private void panelEnter(JPanel p) {
        p.setBackground(new Color(227, 227, 227));
        p.setBorder(BorderFactory.createLineBorder(new Color(213, 213, 213), 2));
    }

    private void panelExit(JPanel p) {
        p.setBackground(new Color(244, 244, 244));
        p.setBorder(BorderFactory.createLineBorder(new Color(244, 244, 244), 2));
    }

    private void frameActive(JPanel p) {
        p.setBackground(new Color(227, 227, 227));
        p.setBorder(BorderFactory.createLineBorder(new Color(227, 227, 227), 2));
    }

    private void frameNonactive(JPanel p) {
        p.setBackground(new Color(244, 244, 244));
        p.setBorder(BorderFactory.createLineBorder(new Color(244, 244, 244), 2));
    }

    //goto
    private void dashboard() {
        dashboard.setVisible(true);
        peminjaman.setVisible(false);
        pengembalian.setVisible(false);
        profile.setVisible(false);
        vcd.setVisible(false);
        customer.setVisible(false);
        karyawan.setVisible(false);
        harga.setVisible(false);
        laporan.setVisible(false);

        state = 0;

        frameActive(lp_beranda);
        frameNonactive(lp_peminjaman);
        frameNonactive(lp_pengembalian);
        frameNonactive(lp_profile);
        frameNonactive(lp_vcd);
        frameNonactive(lp_customer);
        frameNonactive(lp_karyawan);
        frameNonactive(lp_harga);
        frameNonactive(lp_laporan);
        frameNonactive(lp_keluar);

        jLabel2.setText("<html>Ayo kelola toko dengan memilih menu yang kamu perlukan dibawah ini<html>");
        jLabel41.setText("<html>Pastikan customer telah<br>terdaftar</html>");
        jLabel99.setText("<html>Sampai jumpa besok</html>");
        jLabel44.setText("<html>Apa yang kita dapatkan<br>sejauh ini</html>");
        jLabel53.setText("<html>Karyawan yang bekerja<br>ditempat ini</html>");
        jLabel56.setText("<html>Siapa saja yang menjadi<br>member kita</html>");
        jLabel59.setText("<html>Kumpulan VCD yang<br>disewakan</html>");
        jLabel62.setText("<html>Lihat data diri anda</html>");
        jLabel65.setText("<html>Periksa kondisi VCD<br>dengan teliti</html>");
        jLabel50.setText("<html>Kelola harga di toko</html>");
    }

    private void peminjaman() {
        dashboard.setVisible(false);
        peminjaman.setVisible(true);
        pengembalian.setVisible(false);
        profile.setVisible(false);
        vcd.setVisible(false);
        customer.setVisible(false);
        karyawan.setVisible(false);
        harga.setVisible(false);
        laporan.setVisible(false);

        state = 1;

        frameNonactive(lp_beranda);
        frameActive(lp_peminjaman);
        frameNonactive(lp_pengembalian);
        frameNonactive(lp_profile);
        frameNonactive(lp_vcd);
        frameNonactive(lp_customer);
        frameNonactive(lp_karyawan);
        frameNonactive(lp_harga);
        frameNonactive(lp_laporan);
        frameNonactive(lp_keluar);

        pinjam_tampil();

        jLabel120.setText("<html>Pastikan kamu sudah memasukkan data pelanggan dengan benar dan <br>periksa semua VCD yang disewa ada di keranjang<html>");
    }

    private void pengembalian() {
        dashboard.setVisible(false);
        peminjaman.setVisible(false);
        pengembalian.setVisible(true);
        profile.setVisible(false);
        vcd.setVisible(false);
        customer.setVisible(false);
        karyawan.setVisible(false);
        harga.setVisible(false);
        laporan.setVisible(false);

        state = 2;

        frameNonactive(lp_beranda);
        frameNonactive(lp_peminjaman);
        frameActive(lp_pengembalian);
        frameNonactive(lp_profile);
        frameNonactive(lp_vcd);
        frameNonactive(lp_customer);
        frameNonactive(lp_karyawan);
        frameNonactive(lp_harga);
        frameNonactive(lp_laporan);
        frameNonactive(lp_keluar);
        kembali_tampil();

        jLabel123.setText("<html>Jangan lupa minta kode booking dari pelanggan dan periksa kondisi VCD yang dipinjam ya<html>");
    }

    private void profile() {
        dashboard.setVisible(false);
        peminjaman.setVisible(false);
        pengembalian.setVisible(false);
        profile.setVisible(true);
        vcd.setVisible(false);
        customer.setVisible(false);
        karyawan.setVisible(false);
        harga.setVisible(false);
        laporan.setVisible(false);

        state = 3;

        frameNonactive(lp_beranda);
        frameNonactive(lp_peminjaman);
        frameNonactive(lp_pengembalian);
        frameActive(lp_profile);
        frameNonactive(lp_vcd);
        frameNonactive(lp_customer);
        frameNonactive(lp_karyawan);
        frameNonactive(lp_harga);
        frameNonactive(lp_laporan);
        frameNonactive(lp_keluar);

        jLabel8.setText("<html>Disini kamu bisa melihat data diri dan akun kamu</html>");
        profil_tampil();
    }

    private void vcd() {
        dashboard.setVisible(false);
        peminjaman.setVisible(false);
        pengembalian.setVisible(false);
        profile.setVisible(false);
        vcd.setVisible(true);
        customer.setVisible(false);
        karyawan.setVisible(false);
        harga.setVisible(false);
        laporan.setVisible(false);

        state = 4;

        frameNonactive(lp_beranda);
        frameNonactive(lp_peminjaman);
        frameNonactive(lp_pengembalian);
        frameNonactive(lp_profile);
        frameActive(lp_vcd);
        frameNonactive(lp_customer);
        frameNonactive(lp_karyawan);
        frameNonactive(lp_harga);
        frameNonactive(lp_laporan);
        frameNonactive(lp_keluar);

        vcd_tampil();
        jLabel10.setText("<html>Toko kita punya banyak VCD, cari tahu apa saja yang kita punya</html>");
    }

    private void customer() {
        dashboard.setVisible(false);
        peminjaman.setVisible(false);
        pengembalian.setVisible(false);
        profile.setVisible(false);
        vcd.setVisible(false);
        customer.setVisible(true);
        karyawan.setVisible(false);
        harga.setVisible(false);
        laporan.setVisible(false);

        state = 5;

        frameNonactive(lp_beranda);
        frameNonactive(lp_peminjaman);
        frameNonactive(lp_pengembalian);
        frameNonactive(lp_profile);
        frameNonactive(lp_vcd);
        frameActive(lp_customer);
        frameNonactive(lp_karyawan);
        frameNonactive(lp_harga);
        frameNonactive(lp_laporan);
        frameNonactive(lp_keluar);

        jLabel12.setText("<html>Daftar pelanggan toko kita ada pada tabel dibawah ini</html>");
        customer_tampil();
    }

    private void karyawan() {
        dashboard.setVisible(false);
        peminjaman.setVisible(false);
        pengembalian.setVisible(false);
        profile.setVisible(false);
        vcd.setVisible(false);
        customer.setVisible(false);
        karyawan.setVisible(true);
        laporan.setVisible(false);

        state = 6;

        frameNonactive(lp_beranda);
        frameNonactive(lp_peminjaman);
        frameNonactive(lp_pengembalian);
        frameNonactive(lp_profile);
        frameNonactive(lp_vcd);
        frameNonactive(lp_customer);
        frameActive(lp_karyawan);
        frameNonactive(lp_harga);
        frameNonactive(lp_laporan);
        frameNonactive(lp_keluar);

        jLabel14.setText("<html>Daftar siapa saja yang bekerja di toko ada pada tabel dibawah ini</html>");
        karyawan_tampil();
    }

    private void harga() {
        dashboard.setVisible(false);
        peminjaman.setVisible(false);
        pengembalian.setVisible(false);
        profile.setVisible(false);
        vcd.setVisible(false);
        customer.setVisible(false);
        karyawan.setVisible(false);
        harga.setVisible(true);
        laporan.setVisible(false);

        state = 7;

        frameNonactive(lp_beranda);
        frameNonactive(lp_peminjaman);
        frameNonactive(lp_pengembalian);
        frameNonactive(lp_profile);
        frameNonactive(lp_vcd);
        frameNonactive(lp_customer);
        frameNonactive(lp_karyawan);
        frameActive(lp_harga);
        frameNonactive(lp_laporan);
        frameNonactive(lp_keluar);

        jLabel101.setText("<html>Kelola harga pada toko</html>");
        harga_tampil();
    }

    private void laporan() {
        dashboard.setVisible(false);
        peminjaman.setVisible(false);
        pengembalian.setVisible(false);
        profile.setVisible(false);
        vcd.setVisible(false);
        customer.setVisible(false);
        karyawan.setVisible(false);
        harga.setVisible(false);
        laporan.setVisible(true);

        state = 8;

        frameNonactive(lp_beranda);
        frameNonactive(lp_peminjaman);
        frameNonactive(lp_pengembalian);
        frameNonactive(lp_profile);
        frameNonactive(lp_vcd);
        frameNonactive(lp_customer);
        frameNonactive(lp_karyawan);
        frameNonactive(lp_harga);
        frameActive(lp_laporan);
        frameNonactive(lp_keluar);

        jLabel16.setText("<html>Mari lihat pencapaian apa saja yang sudah kita buat melalui data</html>");
    }

    private void keluar() {
        state = 10;

        frameNonactive(lp_beranda);
        frameNonactive(lp_peminjaman);
        frameNonactive(lp_pengembalian);
        frameNonactive(lp_profile);
        frameNonactive(lp_vcd);
        frameNonactive(lp_customer);
        frameNonactive(lp_karyawan);
        frameNonactive(lp_laporan);
        frameActive(lp_keluar);

        log_id = null;
        log_name = null;
        log_level = null;

        Login login = new Login();
        login.setVisible(true);
        this.dispose();
    }

    /**
     * Creates new form NewJFrame
     */
    public Admin_dashboard(String id, String nama, String level) {
        initComponents();
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        this.setMaximizedBounds(env.getMaximumWindowBounds());
        this.setExtendedState(this.getExtendedState() | this.MAXIMIZED_BOTH);

        log_id = id;
        log_name = nama;
        log_level = level;

        dashboard();
    }

    public Admin_dashboard() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        left_panel = new javax.swing.JPanel();
        lp_beranda = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        lp_peminjaman = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        lp_pengembalian = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        lp_profile = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        lp_vcd = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        lp_customer = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        lp_karyawan = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        lp_harga = new javax.swing.JPanel();
        jLabel95 = new javax.swing.JLabel();
        jLabel96 = new javax.swing.JLabel();
        lp_laporan = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        lp_keluar = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel94 = new javax.swing.JLabel();
        right_panel = new javax.swing.JLayeredPane();
        dashboard = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        d_pinjam = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        d_kembali = new javax.swing.JPanel();
        jLabel63 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        d_profile = new javax.swing.JPanel();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        d_vcd = new javax.swing.JPanel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        d_customer = new javax.swing.JPanel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        d_staff = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        d_harga = new javax.swing.JPanel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        d_laporan = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        d_keluar = new javax.swing.JPanel();
        jLabel97 = new javax.swing.JLabel();
        jLabel98 = new javax.swing.JLabel();
        jLabel99 = new javax.swing.JLabel();
        peminjaman = new javax.swing.JPanel();
        jLabel119 = new javax.swing.JLabel();
        jLabel120 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tb_pinjam = new javax.swing.JTable();
        pinjam_txt_cari = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_pinjam_det = new javax.swing.JTable();
        pinjam_btn_tambah = new javax.swing.JButton();
        pengembalian = new javax.swing.JPanel();
        jLabel122 = new javax.swing.JLabel();
        jLabel123 = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        tb_kembali = new javax.swing.JTable();
        kembali_txt_cari = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        profile = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        profile_txt_ktp = new javax.swing.JTextField();
        profile_txt_nama = new javax.swing.JTextField();
        profile_txt_level = new javax.swing.JTextField();
        profile_txt_kontak = new javax.swing.JTextField();
        jLabel91 = new javax.swing.JLabel();
        jLabel92 = new javax.swing.JLabel();
        jLabel93 = new javax.swing.JLabel();
        profile_txt_pass_old = new javax.swing.JPasswordField();
        profile_txt_pass_new = new javax.swing.JPasswordField();
        jLabel107 = new javax.swing.JLabel();
        jLabel108 = new javax.swing.JLabel();
        jLabel110 = new javax.swing.JLabel();
        jLabel111 = new javax.swing.JLabel();
        jLabel112 = new javax.swing.JLabel();
        profile_txt_alamat = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        profile_btn_ganti = new javax.swing.JButton();
        vcd = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tb_vcd = new javax.swing.JTable();
        vcd_txt_cari = new javax.swing.JTextField();
        vcd_btn_tambah = new javax.swing.JButton();
        vcd_btn_restock = new javax.swing.JButton();
        vcd_btn_edit = new javax.swing.JButton();
        vcd_btn_hapus = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        customer = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tb_customer = new javax.swing.JTable();
        customer_txt_cari = new javax.swing.JTextField();
        customer_btn_refresh = new javax.swing.JButton();
        customer_btn_tambah = new javax.swing.JButton();
        customer_btn_edit = new javax.swing.JButton();
        customer_btn_hapus = new javax.swing.JButton();
        karyawan = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tb_karyawan = new javax.swing.JTable();
        karyawan_txt_cari = new javax.swing.JTextField();
        karyawan_btn_tambah = new javax.swing.JButton();
        karyawan_btn_edit = new javax.swing.JButton();
        karyawan_btn_hapus = new javax.swing.JButton();
        karyawan_btn_refresh = new javax.swing.JButton();
        harga = new javax.swing.JPanel();
        jLabel100 = new javax.swing.JLabel();
        jLabel101 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tb_harga = new javax.swing.JTable();
        harga_txt_cari = new javax.swing.JTextField();
        harga_btn_refresh = new javax.swing.JButton();
        harga_btn_tambah = new javax.swing.JButton();
        harga_btn_edit = new javax.swing.JButton();
        harga_btn_hapus = new javax.swing.JButton();
        laporan = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        lap_pendapatan = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        jdhjashg = new javax.swing.JLabel();
        lap_vcd = new javax.swing.JPanel();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        lap_restock = new javax.swing.JPanel();
        jLabel69 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        lap_pinjam = new javax.swing.JPanel();
        jLabel72 = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();
        lap_kembali = new javax.swing.JPanel();
        jLabel75 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Home Cinema");
        setBackground(new java.awt.Color(255, 255, 255));

        left_panel.setBackground(new java.awt.Color(244, 244, 244));
        left_panel.setForeground(new java.awt.Color(244, 244, 244));

        lp_beranda.setBackground(new java.awt.Color(244, 244, 244));
        lp_beranda.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(244, 244, 244), 2));
        lp_beranda.setForeground(new java.awt.Color(244, 244, 244));
        lp_beranda.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lp_beranda.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lp_berandaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lp_berandaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lp_berandaMouseExited(evt);
            }
        });

        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/mini-home.png"))); // NOI18N

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(51, 51, 51));
        jLabel20.setText("Beranda");

        javax.swing.GroupLayout lp_berandaLayout = new javax.swing.GroupLayout(lp_beranda);
        lp_beranda.setLayout(lp_berandaLayout);
        lp_berandaLayout.setHorizontalGroup(
            lp_berandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lp_berandaLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel19)
                .addGap(15, 15, 15)
                .addComponent(jLabel20)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        lp_berandaLayout.setVerticalGroup(
            lp_berandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lp_berandaLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(lp_berandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel20)
                    .addComponent(jLabel19))
                .addGap(10, 10, 10))
        );

        lp_peminjaman.setBackground(new java.awt.Color(244, 244, 244));
        lp_peminjaman.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(244, 244, 244), 2));
        lp_peminjaman.setForeground(new java.awt.Color(244, 244, 244));
        lp_peminjaman.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lp_peminjaman.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lp_peminjamanMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lp_peminjamanMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lp_peminjamanMouseExited(evt);
            }
        });

        jLabel37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/mini-pinjam.png"))); // NOI18N

        jLabel38.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(51, 51, 51));
        jLabel38.setText("Transaksi peminjaman");

        javax.swing.GroupLayout lp_peminjamanLayout = new javax.swing.GroupLayout(lp_peminjaman);
        lp_peminjaman.setLayout(lp_peminjamanLayout);
        lp_peminjamanLayout.setHorizontalGroup(
            lp_peminjamanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lp_peminjamanLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel37)
                .addGap(15, 15, 15)
                .addComponent(jLabel38)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        lp_peminjamanLayout.setVerticalGroup(
            lp_peminjamanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lp_peminjamanLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(lp_peminjamanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel38)
                    .addComponent(jLabel37))
                .addGap(10, 10, 10))
        );

        lp_pengembalian.setBackground(new java.awt.Color(244, 244, 244));
        lp_pengembalian.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(244, 244, 244), 2));
        lp_pengembalian.setForeground(new java.awt.Color(244, 244, 244));
        lp_pengembalian.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lp_pengembalian.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lp_pengembalianMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lp_pengembalianMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lp_pengembalianMouseExited(evt);
            }
        });

        jLabel35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/mini-kembali.png"))); // NOI18N

        jLabel36.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(51, 51, 51));
        jLabel36.setText("Transaksi pengembalian");

        javax.swing.GroupLayout lp_pengembalianLayout = new javax.swing.GroupLayout(lp_pengembalian);
        lp_pengembalian.setLayout(lp_pengembalianLayout);
        lp_pengembalianLayout.setHorizontalGroup(
            lp_pengembalianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lp_pengembalianLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel35)
                .addGap(15, 15, 15)
                .addComponent(jLabel36)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        lp_pengembalianLayout.setVerticalGroup(
            lp_pengembalianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lp_pengembalianLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(lp_pengembalianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel36)
                    .addComponent(jLabel35))
                .addGap(10, 10, 10))
        );

        lp_profile.setBackground(new java.awt.Color(244, 244, 244));
        lp_profile.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(244, 244, 244), 2));
        lp_profile.setForeground(new java.awt.Color(244, 244, 244));
        lp_profile.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lp_profile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lp_profileMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lp_profileMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lp_profileMouseExited(evt);
            }
        });

        jLabel33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/mini-profile.png"))); // NOI18N

        jLabel34.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(51, 51, 51));
        jLabel34.setText("Profile anda");

        javax.swing.GroupLayout lp_profileLayout = new javax.swing.GroupLayout(lp_profile);
        lp_profile.setLayout(lp_profileLayout);
        lp_profileLayout.setHorizontalGroup(
            lp_profileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lp_profileLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel33)
                .addGap(15, 15, 15)
                .addComponent(jLabel34)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        lp_profileLayout.setVerticalGroup(
            lp_profileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lp_profileLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(lp_profileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel34)
                    .addComponent(jLabel33))
                .addGap(10, 10, 10))
        );

        lp_vcd.setBackground(new java.awt.Color(244, 244, 244));
        lp_vcd.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(244, 244, 244), 2));
        lp_vcd.setForeground(new java.awt.Color(244, 244, 244));
        lp_vcd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lp_vcd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lp_vcdMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lp_vcdMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lp_vcdMouseExited(evt);
            }
        });

        jLabel31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/mini-vcd.png"))); // NOI18N

        jLabel32.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(51, 51, 51));
        jLabel32.setText("Data VCD");

        javax.swing.GroupLayout lp_vcdLayout = new javax.swing.GroupLayout(lp_vcd);
        lp_vcd.setLayout(lp_vcdLayout);
        lp_vcdLayout.setHorizontalGroup(
            lp_vcdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lp_vcdLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel31)
                .addGap(15, 15, 15)
                .addComponent(jLabel32)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        lp_vcdLayout.setVerticalGroup(
            lp_vcdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lp_vcdLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(lp_vcdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel32)
                    .addComponent(jLabel31))
                .addGap(10, 10, 10))
        );

        lp_customer.setBackground(new java.awt.Color(244, 244, 244));
        lp_customer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(244, 244, 244), 2));
        lp_customer.setForeground(new java.awt.Color(244, 244, 244));
        lp_customer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lp_customer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lp_customerMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lp_customerMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lp_customerMouseExited(evt);
            }
        });

        jLabel29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/mini-member.png"))); // NOI18N

        jLabel30.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(51, 51, 51));
        jLabel30.setText("Data customer");

        javax.swing.GroupLayout lp_customerLayout = new javax.swing.GroupLayout(lp_customer);
        lp_customer.setLayout(lp_customerLayout);
        lp_customerLayout.setHorizontalGroup(
            lp_customerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lp_customerLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel29)
                .addGap(15, 15, 15)
                .addComponent(jLabel30)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        lp_customerLayout.setVerticalGroup(
            lp_customerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lp_customerLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(lp_customerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel30)
                    .addComponent(jLabel29))
                .addGap(10, 10, 10))
        );

        lp_karyawan.setBackground(new java.awt.Color(244, 244, 244));
        lp_karyawan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(244, 244, 244), 2));
        lp_karyawan.setForeground(new java.awt.Color(244, 244, 244));
        lp_karyawan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lp_karyawan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lp_karyawanMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lp_karyawanMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lp_karyawanMouseExited(evt);
            }
        });

        jLabel27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/mini-staff.png"))); // NOI18N

        jLabel28.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(51, 51, 51));
        jLabel28.setText("Data karyawan");

        javax.swing.GroupLayout lp_karyawanLayout = new javax.swing.GroupLayout(lp_karyawan);
        lp_karyawan.setLayout(lp_karyawanLayout);
        lp_karyawanLayout.setHorizontalGroup(
            lp_karyawanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lp_karyawanLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel27)
                .addGap(15, 15, 15)
                .addComponent(jLabel28)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        lp_karyawanLayout.setVerticalGroup(
            lp_karyawanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lp_karyawanLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(lp_karyawanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel28)
                    .addComponent(jLabel27))
                .addGap(10, 10, 10))
        );

        lp_harga.setBackground(new java.awt.Color(244, 244, 244));
        lp_harga.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(244, 244, 244), 2));
        lp_harga.setForeground(new java.awt.Color(244, 244, 244));
        lp_harga.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lp_harga.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lp_hargaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lp_hargaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lp_hargaMouseExited(evt);
            }
        });

        jLabel95.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/mini-money.png"))); // NOI18N

        jLabel96.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel96.setForeground(new java.awt.Color(51, 51, 51));
        jLabel96.setText("Data harga");

        javax.swing.GroupLayout lp_hargaLayout = new javax.swing.GroupLayout(lp_harga);
        lp_harga.setLayout(lp_hargaLayout);
        lp_hargaLayout.setHorizontalGroup(
            lp_hargaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lp_hargaLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel95)
                .addGap(15, 15, 15)
                .addComponent(jLabel96)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        lp_hargaLayout.setVerticalGroup(
            lp_hargaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lp_hargaLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(lp_hargaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel96)
                    .addComponent(jLabel95))
                .addGap(10, 10, 10))
        );

        lp_laporan.setBackground(new java.awt.Color(244, 244, 244));
        lp_laporan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(244, 244, 244), 2));
        lp_laporan.setForeground(new java.awt.Color(244, 244, 244));
        lp_laporan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lp_laporan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lp_laporanMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lp_laporanMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lp_laporanMouseExited(evt);
            }
        });

        jLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/mini-chart.png"))); // NOI18N

        jLabel26.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(51, 51, 51));
        jLabel26.setText("Laporan");

        javax.swing.GroupLayout lp_laporanLayout = new javax.swing.GroupLayout(lp_laporan);
        lp_laporan.setLayout(lp_laporanLayout);
        lp_laporanLayout.setHorizontalGroup(
            lp_laporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lp_laporanLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel25)
                .addGap(15, 15, 15)
                .addComponent(jLabel26)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        lp_laporanLayout.setVerticalGroup(
            lp_laporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lp_laporanLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(lp_laporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel26)
                    .addComponent(jLabel25))
                .addGap(10, 10, 10))
        );

        lp_keluar.setBackground(new java.awt.Color(244, 244, 244));
        lp_keluar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(244, 244, 244), 2));
        lp_keluar.setForeground(new java.awt.Color(244, 244, 244));
        lp_keluar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lp_keluar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lp_keluarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lp_keluarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lp_keluarMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lp_keluarMousePressed(evt);
            }
        });

        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/mini-exit.png"))); // NOI18N

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(51, 51, 51));
        jLabel22.setText("Keluar");

        javax.swing.GroupLayout lp_keluarLayout = new javax.swing.GroupLayout(lp_keluar);
        lp_keluar.setLayout(lp_keluarLayout);
        lp_keluarLayout.setHorizontalGroup(
            lp_keluarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lp_keluarLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel21)
                .addGap(15, 15, 15)
                .addComponent(jLabel22)
                .addContainerGap(224, Short.MAX_VALUE))
        );
        lp_keluarLayout.setVerticalGroup(
            lp_keluarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lp_keluarLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(lp_keluarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel22)
                    .addComponent(jLabel21))
                .addGap(10, 10, 10))
        );

        jLabel94.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logo.png"))); // NOI18N

        javax.swing.GroupLayout left_panelLayout = new javax.swing.GroupLayout(left_panel);
        left_panel.setLayout(left_panelLayout);
        left_panelLayout.setHorizontalGroup(
            left_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lp_beranda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lp_keluar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lp_laporan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lp_karyawan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lp_customer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lp_vcd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lp_profile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lp_pengembalian, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lp_peminjaman, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(left_panelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel94)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(lp_harga, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        left_panelLayout.setVerticalGroup(
            left_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(left_panelLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(jLabel94)
                .addGap(35, 35, 35)
                .addComponent(lp_beranda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lp_peminjaman, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lp_pengembalian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lp_profile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lp_vcd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lp_customer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lp_karyawan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lp_harga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lp_laporan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lp_keluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        right_panel.setBackground(new java.awt.Color(255, 255, 255));
        right_panel.setLayout(new java.awt.CardLayout());

        dashboard.setBackground(new java.awt.Color(255, 255, 255));
        dashboard.setAutoscrolls(true);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setText("Dashboard");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(96, 96, 96));
        jLabel2.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");

        d_pinjam.setBackground(new java.awt.Color(255, 255, 255));
        d_pinjam.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        d_pinjam.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        d_pinjam.setPreferredSize(new java.awt.Dimension(230, 200));
        d_pinjam.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                d_pinjamMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                d_pinjamMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                d_pinjamMouseExited(evt);
            }
        });

        jLabel39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/big-pinjam.png"))); // NOI18N

        jLabel40.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel40.setText("Transaksi peminjaman");

        jLabel41.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(96, 96, 96));
        jLabel41.setText("Lorem ipsum");

        javax.swing.GroupLayout d_pinjamLayout = new javax.swing.GroupLayout(d_pinjam);
        d_pinjam.setLayout(d_pinjamLayout);
        d_pinjamLayout.setHorizontalGroup(
            d_pinjamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(d_pinjamLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(d_pinjamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel40)
                    .addComponent(jLabel39)
                    .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        d_pinjamLayout.setVerticalGroup(
            d_pinjamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(d_pinjamLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel39)
                .addGap(15, 15, 15)
                .addComponent(jLabel40)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel41)
                .addGap(15, 15, 15))
        );

        d_kembali.setBackground(new java.awt.Color(255, 255, 255));
        d_kembali.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        d_kembali.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        d_kembali.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                d_kembaliMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                d_kembaliMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                d_kembaliMouseExited(evt);
            }
        });

        jLabel63.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/big-kembali.png"))); // NOI18N

        jLabel64.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel64.setText("Transaksi pengembalian");

        jLabel65.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel65.setForeground(new java.awt.Color(96, 96, 96));
        jLabel65.setText("Lorem ipsum");

        javax.swing.GroupLayout d_kembaliLayout = new javax.swing.GroupLayout(d_kembali);
        d_kembali.setLayout(d_kembaliLayout);
        d_kembaliLayout.setHorizontalGroup(
            d_kembaliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(d_kembaliLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(d_kembaliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel64, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel63)
                    .addComponent(jLabel65, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        d_kembaliLayout.setVerticalGroup(
            d_kembaliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(d_kembaliLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel63)
                .addGap(15, 15, 15)
                .addComponent(jLabel64)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel65)
                .addGap(15, 15, 15))
        );

        d_profile.setBackground(new java.awt.Color(255, 255, 255));
        d_profile.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        d_profile.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        d_profile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                d_profileMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                d_profileMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                d_profileMouseExited(evt);
            }
        });

        jLabel60.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/big-profile.png"))); // NOI18N

        jLabel61.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel61.setText("Profile anda");

        jLabel62.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(96, 96, 96));
        jLabel62.setText("Lorem ipsum");

        javax.swing.GroupLayout d_profileLayout = new javax.swing.GroupLayout(d_profile);
        d_profile.setLayout(d_profileLayout);
        d_profileLayout.setHorizontalGroup(
            d_profileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(d_profileLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(d_profileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel61)
                    .addComponent(jLabel60)
                    .addComponent(jLabel62, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        d_profileLayout.setVerticalGroup(
            d_profileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(d_profileLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel60)
                .addGap(15, 15, 15)
                .addComponent(jLabel61)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel62)
                .addGap(15, 15, 15))
        );

        d_vcd.setBackground(new java.awt.Color(255, 255, 255));
        d_vcd.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        d_vcd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        d_vcd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                d_vcdMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                d_vcdMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                d_vcdMouseExited(evt);
            }
        });

        jLabel57.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/big-vcd.png"))); // NOI18N

        jLabel58.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel58.setText("Data VCD");

        jLabel59.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(96, 96, 96));
        jLabel59.setText("Lorem ipsum");

        javax.swing.GroupLayout d_vcdLayout = new javax.swing.GroupLayout(d_vcd);
        d_vcd.setLayout(d_vcdLayout);
        d_vcdLayout.setHorizontalGroup(
            d_vcdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(d_vcdLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(d_vcdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel58)
                    .addComponent(jLabel57)
                    .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        d_vcdLayout.setVerticalGroup(
            d_vcdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(d_vcdLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel57)
                .addGap(15, 15, 15)
                .addComponent(jLabel58)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel59)
                .addGap(15, 15, 15))
        );

        d_customer.setBackground(new java.awt.Color(255, 255, 255));
        d_customer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        d_customer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        d_customer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                d_customerMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                d_customerMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                d_customerMouseExited(evt);
            }
        });

        jLabel54.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/big-customer.png"))); // NOI18N

        jLabel55.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel55.setText("Data customer");

        jLabel56.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(96, 96, 96));
        jLabel56.setText("Lorem ipsum");

        javax.swing.GroupLayout d_customerLayout = new javax.swing.GroupLayout(d_customer);
        d_customer.setLayout(d_customerLayout);
        d_customerLayout.setHorizontalGroup(
            d_customerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(d_customerLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(d_customerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel55)
                    .addComponent(jLabel54)
                    .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        d_customerLayout.setVerticalGroup(
            d_customerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(d_customerLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel54)
                .addGap(15, 15, 15)
                .addComponent(jLabel55)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel56)
                .addGap(15, 15, 15))
        );

        d_staff.setBackground(new java.awt.Color(255, 255, 255));
        d_staff.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        d_staff.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        d_staff.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                d_staffMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                d_staffMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                d_staffMouseExited(evt);
            }
        });

        jLabel51.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/big-staff.png"))); // NOI18N

        jLabel52.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel52.setText("Data karyawan");

        jLabel53.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(96, 96, 96));
        jLabel53.setText("Lorem ipsum");

        javax.swing.GroupLayout d_staffLayout = new javax.swing.GroupLayout(d_staff);
        d_staff.setLayout(d_staffLayout);
        d_staffLayout.setHorizontalGroup(
            d_staffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(d_staffLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(d_staffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel52)
                    .addComponent(jLabel51)
                    .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        d_staffLayout.setVerticalGroup(
            d_staffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(d_staffLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel51)
                .addGap(15, 15, 15)
                .addComponent(jLabel52)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel53)
                .addGap(15, 15, 15))
        );

        d_harga.setBackground(new java.awt.Color(255, 255, 255));
        d_harga.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        d_harga.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        d_harga.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                d_hargaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                d_hargaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                d_hargaMouseExited(evt);
            }
        });

        jLabel48.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/big-money.png"))); // NOI18N

        jLabel49.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel49.setText("Data harga");

        jLabel50.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(96, 96, 96));
        jLabel50.setText("Lorem ipsum");

        javax.swing.GroupLayout d_hargaLayout = new javax.swing.GroupLayout(d_harga);
        d_harga.setLayout(d_hargaLayout);
        d_hargaLayout.setHorizontalGroup(
            d_hargaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(d_hargaLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(d_hargaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel49)
                    .addComponent(jLabel48)
                    .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        d_hargaLayout.setVerticalGroup(
            d_hargaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(d_hargaLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel48)
                .addGap(15, 15, 15)
                .addComponent(jLabel49)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel50)
                .addGap(15, 15, 15))
        );

        d_laporan.setBackground(new java.awt.Color(255, 255, 255));
        d_laporan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        d_laporan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        d_laporan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                d_laporanMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                d_laporanMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                d_laporanMouseExited(evt);
            }
        });

        jLabel42.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/big-chart.png"))); // NOI18N

        jLabel43.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel43.setText("Laporan");

        jLabel44.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(96, 96, 96));
        jLabel44.setText("Lorem ipsum");

        javax.swing.GroupLayout d_laporanLayout = new javax.swing.GroupLayout(d_laporan);
        d_laporan.setLayout(d_laporanLayout);
        d_laporanLayout.setHorizontalGroup(
            d_laporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(d_laporanLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(d_laporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel43)
                    .addComponent(jLabel42)
                    .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        d_laporanLayout.setVerticalGroup(
            d_laporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(d_laporanLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel42)
                .addGap(15, 15, 15)
                .addComponent(jLabel43)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel44)
                .addGap(15, 15, 15))
        );

        d_keluar.setBackground(new java.awt.Color(255, 255, 255));
        d_keluar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        d_keluar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        d_keluar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                d_keluarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                d_keluarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                d_keluarMouseExited(evt);
            }
        });

        jLabel97.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/big-exit.png"))); // NOI18N

        jLabel98.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel98.setText("Keluar");

        jLabel99.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel99.setForeground(new java.awt.Color(96, 96, 96));
        jLabel99.setText("Lorem ipsum");

        javax.swing.GroupLayout d_keluarLayout = new javax.swing.GroupLayout(d_keluar);
        d_keluar.setLayout(d_keluarLayout);
        d_keluarLayout.setHorizontalGroup(
            d_keluarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(d_keluarLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(d_keluarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel98)
                    .addComponent(jLabel97)
                    .addComponent(jLabel99, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        d_keluarLayout.setVerticalGroup(
            d_keluarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(d_keluarLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel97)
                .addGap(15, 15, 15)
                .addComponent(jLabel98)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel99)
                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout dashboardLayout = new javax.swing.GroupLayout(dashboard);
        dashboard.setLayout(dashboardLayout);
        dashboardLayout.setHorizontalGroup(
            dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dashboardLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(d_keluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(dashboardLayout.createSequentialGroup()
                        .addGroup(dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(d_customer, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(d_pinjam, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE))
                        .addGap(8, 8, 8)
                        .addGroup(dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(d_staff, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(d_kembali, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(8, 8, 8)
                        .addGroup(dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(d_profile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(d_harga, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(8, 8, 8)
                        .addGroup(dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(d_laporan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(d_vcd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap(310, Short.MAX_VALUE))
        );
        dashboardLayout.setVerticalGroup(
            dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dashboardLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel1)
                .addGap(13, 13, 13)
                .addComponent(jLabel2)
                .addGap(20, 20, 20)
                .addGroup(dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(d_vcd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(d_profile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(d_kembali, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(d_pinjam, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE))
                .addGap(8, 8, 8)
                .addGroup(dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(d_staff, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(d_harga, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(d_laporan, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(d_customer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(d_keluar, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(369, Short.MAX_VALUE))
        );

        right_panel.add(dashboard, "card2");

        peminjaman.setBackground(new java.awt.Color(255, 255, 255));

        jLabel119.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel119.setText("Data transaksi peminjaman");

        jLabel120.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel120.setForeground(new java.awt.Color(96, 96, 96));
        jLabel120.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");

        tb_pinjam.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        tb_pinjam.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tb_pinjam.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_pinjamMouseClicked(evt);
            }
        });
        jScrollPane9.setViewportView(tb_pinjam);

        pinjam_txt_cari.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        pinjam_txt_cari.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(96, 96, 96), 2));

        tb_pinjam_det.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tb_pinjam_det);

        pinjam_btn_tambah.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        pinjam_btn_tambah.setText("Baru");
        pinjam_btn_tambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pinjam_btn_tambahActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout peminjamanLayout = new javax.swing.GroupLayout(peminjaman);
        peminjaman.setLayout(peminjamanLayout);
        peminjamanLayout.setHorizontalGroup(
            peminjamanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(peminjamanLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(peminjamanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(peminjamanLayout.createSequentialGroup()
                        .addGroup(peminjamanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pinjam_txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel119)
                            .addComponent(jLabel120, javax.swing.GroupLayout.DEFAULT_SIZE, 810, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pinjam_btn_tambah))
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        peminjamanLayout.setVerticalGroup(
            peminjamanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(peminjamanLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel119)
                .addGap(15, 15, 15)
                .addComponent(jLabel120)
                .addGap(20, 20, 20)
                .addGroup(peminjamanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pinjam_txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pinjam_btn_tambah))
                .addGap(14, 14, 14)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(381, Short.MAX_VALUE))
        );

        peminjamanLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {pinjam_btn_tambah, pinjam_txt_cari});

        right_panel.add(peminjaman, "card3");

        pengembalian.setBackground(new java.awt.Color(255, 255, 255));

        jLabel122.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel122.setText("Data transaksi pengembalian");

        jLabel123.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel123.setForeground(new java.awt.Color(96, 96, 96));
        jLabel123.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");

        tb_kembali.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        tb_kembali.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kode Booking", "Peminjaman", "Denda", "Status"
            }
        ));
        tb_kembali.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_kembaliMouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(tb_kembali);
        if (tb_kembali.getColumnModel().getColumnCount() > 0) {
            tb_kembali.getColumnModel().getColumn(2).setMinWidth(100);
            tb_kembali.getColumnModel().getColumn(2).setMaxWidth(100);
            tb_kembali.getColumnModel().getColumn(3).setMinWidth(100);
            tb_kembali.getColumnModel().getColumn(3).setMaxWidth(100);
        }

        kembali_txt_cari.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        kembali_txt_cari.setText(" ");
        kembali_txt_cari.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(96, 96, 96), 2));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "VCD", "JUMLAH", "RUSAK", "DENDA", "KARYAWAN"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        jButton4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton4.setText("Proses");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pengembalianLayout = new javax.swing.GroupLayout(pengembalian);
        pengembalian.setLayout(pengembalianLayout);
        pengembalianLayout.setHorizontalGroup(
            pengembalianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pengembalianLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(pengembalianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pengembalianLayout.createSequentialGroup()
                        .addGroup(pengembalianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel123, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 796, Short.MAX_VALUE)
                            .addComponent(kembali_txt_cari, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel122, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(37, 37, 37)
                        .addComponent(jButton4))
                    .addComponent(jScrollPane10)
                    .addComponent(jScrollPane2))
                .addContainerGap(314, Short.MAX_VALUE))
        );
        pengembalianLayout.setVerticalGroup(
            pengembalianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pengembalianLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel122)
                .addGap(15, 15, 15)
                .addComponent(jLabel123)
                .addGap(20, 20, 20)
                .addGroup(pengembalianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(kembali_txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 313, Short.MAX_VALUE)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(80, 80, 80))
        );

        pengembalianLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton4, kembali_txt_cari});

        right_panel.add(pengembalian, "card4");

        profile.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel7.setText("Profile anda");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(96, 96, 96));
        jLabel8.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");

        profile_txt_ktp.setEditable(false);
        profile_txt_ktp.setBackground(new java.awt.Color(244, 244, 244));
        profile_txt_ktp.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        profile_txt_ktp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(96, 96, 96), 2));
        profile_txt_ktp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profile_txt_ktpActionPerformed(evt);
            }
        });

        profile_txt_nama.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        profile_txt_nama.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(96, 96, 96), 2));

        profile_txt_level.setEditable(false);
        profile_txt_level.setBackground(new java.awt.Color(244, 244, 244));
        profile_txt_level.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        profile_txt_level.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(96, 96, 96), 2));

        profile_txt_kontak.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        profile_txt_kontak.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(96, 96, 96), 2));
        profile_txt_kontak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profile_txt_kontakActionPerformed(evt);
            }
        });

        jLabel91.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel91.setText("Ganti password");

        jLabel92.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel92.setForeground(new java.awt.Color(93, 93, 93));
        jLabel92.setText("Password saat ini");

        jLabel93.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel93.setForeground(new java.awt.Color(93, 93, 93));
        jLabel93.setText("Password baru");

        profile_txt_pass_old.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        profile_txt_pass_old.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(96, 96, 96), 2));

        profile_txt_pass_new.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        profile_txt_pass_new.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(96, 96, 96), 2));

        jLabel107.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel107.setForeground(new java.awt.Color(93, 93, 93));
        jLabel107.setText("Nomor KTP");

        jLabel108.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel108.setForeground(new java.awt.Color(93, 93, 93));
        jLabel108.setText("Nama lengkap");

        jLabel110.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel110.setForeground(new java.awt.Color(93, 93, 93));
        jLabel110.setText("Level");

        jLabel111.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel111.setForeground(new java.awt.Color(93, 93, 93));
        jLabel111.setText("Kontak");

        jLabel112.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel112.setForeground(new java.awt.Color(93, 93, 93));
        jLabel112.setText("Alamat");

        profile_txt_alamat.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        profile_txt_alamat.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(96, 96, 96), 2));
        profile_txt_alamat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profile_txt_alamatActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton2.setText("Simpan");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        profile_btn_ganti.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        profile_btn_ganti.setText("Ganti password");
        profile_btn_ganti.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profile_btn_gantiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout profileLayout = new javax.swing.GroupLayout(profile);
        profile.setLayout(profileLayout);
        profileLayout.setHorizontalGroup(
            profileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(profileLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(profileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(profileLayout.createSequentialGroup()
                        .addGroup(profileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(profileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel91, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                                .addComponent(jLabel92, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel93, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel107)
                            .addComponent(jLabel108)
                            .addComponent(jLabel110)
                            .addComponent(jLabel111)
                            .addComponent(jLabel112))
                        .addGap(18, 18, 18)
                        .addGroup(profileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(profile_btn_ganti, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(profile_txt_nama, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                            .addComponent(profile_txt_ktp, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
                            .addComponent(profile_txt_level, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                            .addComponent(profile_txt_kontak, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                            .addComponent(profile_txt_pass_old)
                            .addComponent(profile_txt_pass_new)
                            .addComponent(profile_txt_alamat))))
                .addContainerGap(514, Short.MAX_VALUE))
        );

        profileLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {profile_txt_kontak, profile_txt_ktp, profile_txt_level, profile_txt_nama});

        profileLayout.setVerticalGroup(
            profileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(profileLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel7)
                .addGap(15, 15, 15)
                .addComponent(jLabel8)
                .addGap(20, 20, 20)
                .addGroup(profileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel107)
                    .addComponent(profile_txt_ktp, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(profileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel108)
                    .addComponent(profile_txt_nama, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(profileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel110)
                    .addComponent(profile_txt_level, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(profileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel111)
                    .addComponent(profile_txt_kontak, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(profileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel112)
                    .addComponent(profile_txt_alamat, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addComponent(jButton2)
                .addGap(54, 54, 54)
                .addComponent(jLabel91)
                .addGap(10, 10, 10)
                .addGroup(profileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel92)
                    .addComponent(profile_txt_pass_old, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(profileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel93)
                    .addComponent(profile_txt_pass_new, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addComponent(profile_btn_ganti)
                .addContainerGap(400, Short.MAX_VALUE))
        );

        profileLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton2, profile_btn_ganti, profile_txt_alamat, profile_txt_kontak, profile_txt_ktp, profile_txt_level, profile_txt_nama, profile_txt_pass_new, profile_txt_pass_old});

        right_panel.add(profile, "card5");

        vcd.setBackground(new java.awt.Color(255, 255, 255));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel9.setText("Data VCD");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(96, 96, 96));
        jLabel10.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");

        tb_vcd.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        tb_vcd.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tb_vcd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tb_vcd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_vcdMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tb_vcd);

        vcd_txt_cari.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        vcd_txt_cari.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(96, 96, 96), 2));

        vcd_btn_tambah.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        vcd_btn_tambah.setText("Tambah");
        vcd_btn_tambah.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        vcd_btn_tambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vcd_btn_tambahActionPerformed(evt);
            }
        });

        vcd_btn_restock.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        vcd_btn_restock.setText("Restock");
        vcd_btn_restock.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        vcd_btn_restock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vcd_btn_restockActionPerformed(evt);
            }
        });

        vcd_btn_edit.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        vcd_btn_edit.setText("Edit");
        vcd_btn_edit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        vcd_btn_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vcd_btn_editActionPerformed(evt);
            }
        });

        vcd_btn_hapus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        vcd_btn_hapus.setText("Hapus");
        vcd_btn_hapus.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        vcd_btn_hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vcd_btn_hapusActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton3.setText("Refresh");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout vcdLayout = new javax.swing.GroupLayout(vcd);
        vcd.setLayout(vcdLayout);
        vcdLayout.setHorizontalGroup(
            vcdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vcdLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(vcdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(vcdLayout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(vcdLayout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(vcdLayout.createSequentialGroup()
                        .addGroup(vcdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(vcdLayout.createSequentialGroup()
                                .addComponent(vcd_txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton3)
                                .addGap(10, 10, 10)
                                .addComponent(vcd_btn_tambah)
                                .addGap(10, 10, 10)
                                .addComponent(vcd_btn_restock)
                                .addGap(10, 10, 10)
                                .addComponent(vcd_btn_edit)
                                .addGap(10, 10, 10)
                                .addComponent(vcd_btn_hapus))
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 900, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(45, Short.MAX_VALUE))))
        );
        vcdLayout.setVerticalGroup(
            vcdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vcdLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel9)
                .addGap(15, 15, 15)
                .addComponent(jLabel10)
                .addGap(20, 20, 20)
                .addGroup(vcdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(vcd_btn_hapus)
                    .addComponent(vcd_btn_edit)
                    .addComponent(vcd_btn_restock)
                    .addComponent(vcd_btn_tambah)
                    .addComponent(vcd_txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3))
                .addGap(15, 15, 15)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(372, Short.MAX_VALUE))
        );

        vcdLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton3, vcd_btn_edit, vcd_btn_hapus, vcd_btn_restock, vcd_btn_tambah, vcd_txt_cari});

        right_panel.add(vcd, "card6");

        customer.setBackground(new java.awt.Color(255, 255, 255));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel11.setText("Data customer");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(96, 96, 96));
        jLabel12.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");

        tb_customer.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        tb_customer.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tb_customer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tb_customer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_customerMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tb_customer);

        customer_txt_cari.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        customer_txt_cari.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(96, 96, 96), 2));

        customer_btn_refresh.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        customer_btn_refresh.setText("Refresh");
        customer_btn_refresh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        customer_btn_refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customer_btn_refreshActionPerformed(evt);
            }
        });

        customer_btn_tambah.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        customer_btn_tambah.setText("Tambah");
        customer_btn_tambah.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        customer_btn_tambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customer_btn_tambahActionPerformed(evt);
            }
        });

        customer_btn_edit.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        customer_btn_edit.setText("Edit");
        customer_btn_edit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        customer_btn_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customer_btn_editActionPerformed(evt);
            }
        });

        customer_btn_hapus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        customer_btn_hapus.setText("Hapus");
        customer_btn_hapus.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        customer_btn_hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customer_btn_hapusActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout customerLayout = new javax.swing.GroupLayout(customer);
        customer.setLayout(customerLayout);
        customerLayout.setHorizontalGroup(
            customerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customerLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(customerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addGroup(customerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, customerLayout.createSequentialGroup()
                            .addComponent(customer_txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(customer_btn_refresh)
                            .addGap(10, 10, 10)
                            .addComponent(customer_btn_tambah)
                            .addGap(10, 10, 10)
                            .addComponent(customer_btn_edit)
                            .addGap(10, 10, 10)
                            .addComponent(customer_btn_hapus))
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 900, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(314, Short.MAX_VALUE))
        );
        customerLayout.setVerticalGroup(
            customerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customerLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel11)
                .addGap(15, 15, 15)
                .addComponent(jLabel12)
                .addGap(20, 20, 20)
                .addGroup(customerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(customer_btn_hapus)
                    .addComponent(customer_btn_edit)
                    .addComponent(customer_btn_tambah)
                    .addComponent(customer_btn_refresh)
                    .addComponent(customer_txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50))
        );

        customerLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {customer_btn_edit, customer_btn_hapus, customer_btn_refresh, customer_btn_tambah, customer_txt_cari});

        right_panel.add(customer, "card7");

        karyawan.setBackground(new java.awt.Color(255, 255, 255));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel13.setText("Data karyawan");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(96, 96, 96));
        jLabel14.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");

        tb_karyawan.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        tb_karyawan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tb_karyawan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tb_karyawan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_karyawanMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tb_karyawan);

        karyawan_txt_cari.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        karyawan_txt_cari.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(96, 96, 96), 2));
        karyawan_txt_cari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                karyawan_txt_cariKeyReleased(evt);
            }
        });

        karyawan_btn_tambah.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        karyawan_btn_tambah.setText("Tambah");
        karyawan_btn_tambah.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        karyawan_btn_tambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                karyawan_btn_tambahActionPerformed(evt);
            }
        });

        karyawan_btn_edit.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        karyawan_btn_edit.setText("Edit");
        karyawan_btn_edit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        karyawan_btn_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                karyawan_btn_editActionPerformed(evt);
            }
        });

        karyawan_btn_hapus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        karyawan_btn_hapus.setText("Hapus");
        karyawan_btn_hapus.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        karyawan_btn_hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                karyawan_btn_hapusActionPerformed(evt);
            }
        });

        karyawan_btn_refresh.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        karyawan_btn_refresh.setText("Refresh");
        karyawan_btn_refresh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        karyawan_btn_refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                karyawan_btn_refreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout karyawanLayout = new javax.swing.GroupLayout(karyawan);
        karyawan.setLayout(karyawanLayout);
        karyawanLayout.setHorizontalGroup(
            karyawanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(karyawanLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(karyawanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(karyawanLayout.createSequentialGroup()
                        .addComponent(karyawan_txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 402, Short.MAX_VALUE)
                        .addComponent(karyawan_btn_refresh)
                        .addGap(10, 10, 10)
                        .addComponent(karyawan_btn_tambah)
                        .addGap(10, 10, 10)
                        .addComponent(karyawan_btn_edit)
                        .addGap(10, 10, 10)
                        .addComponent(karyawan_btn_hapus))
                    .addComponent(jLabel13)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4))
                .addContainerGap(314, Short.MAX_VALUE))
        );
        karyawanLayout.setVerticalGroup(
            karyawanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(karyawanLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel13)
                .addGap(15, 15, 15)
                .addComponent(jLabel14)
                .addGap(20, 20, 20)
                .addGroup(karyawanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(karyawan_txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(karyawan_btn_refresh)
                    .addComponent(karyawan_btn_tambah)
                    .addComponent(karyawan_btn_edit)
                    .addComponent(karyawan_btn_hapus))
                .addGap(15, 15, 15)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(372, Short.MAX_VALUE))
        );

        karyawanLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {karyawan_btn_edit, karyawan_btn_hapus, karyawan_btn_refresh, karyawan_btn_tambah, karyawan_txt_cari});

        right_panel.add(karyawan, "card8");

        harga.setBackground(new java.awt.Color(255, 255, 255));

        jLabel100.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel100.setText("Data harga");

        jLabel101.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel101.setForeground(new java.awt.Color(96, 96, 96));
        jLabel101.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");

        tb_harga.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        tb_harga.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tb_harga.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tb_harga.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_hargaMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(tb_harga);

        harga_txt_cari.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        harga_txt_cari.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(96, 96, 96), 2));

        harga_btn_refresh.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        harga_btn_refresh.setText("Refresh");
        harga_btn_refresh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        harga_btn_refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                harga_btn_refreshActionPerformed(evt);
            }
        });

        harga_btn_tambah.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        harga_btn_tambah.setText("Tambah");
        harga_btn_tambah.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        harga_btn_tambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                harga_btn_tambahActionPerformed(evt);
            }
        });

        harga_btn_edit.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        harga_btn_edit.setText("Edit");
        harga_btn_edit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        harga_btn_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                harga_btn_editActionPerformed(evt);
            }
        });

        harga_btn_hapus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        harga_btn_hapus.setText("Hapus");
        harga_btn_hapus.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        harga_btn_hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                harga_btn_hapusActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout hargaLayout = new javax.swing.GroupLayout(harga);
        harga.setLayout(hargaLayout);
        hargaLayout.setHorizontalGroup(
            hargaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hargaLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(hargaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel100)
                    .addComponent(jLabel101, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(hargaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(hargaLayout.createSequentialGroup()
                            .addComponent(harga_txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(harga_btn_refresh)
                            .addGap(10, 10, 10)
                            .addComponent(harga_btn_tambah)
                            .addGap(10, 10, 10)
                            .addComponent(harga_btn_edit)
                            .addGap(10, 10, 10)
                            .addComponent(harga_btn_hapus))
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 900, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        hargaLayout.setVerticalGroup(
            hargaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hargaLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel100)
                .addGap(15, 15, 15)
                .addComponent(jLabel101)
                .addGap(20, 20, 20)
                .addGroup(hargaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(harga_btn_hapus)
                    .addComponent(harga_btn_edit)
                    .addComponent(harga_btn_tambah)
                    .addComponent(harga_btn_refresh)
                    .addComponent(harga_txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(372, Short.MAX_VALUE))
        );

        hargaLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {harga_btn_edit, harga_btn_hapus, harga_btn_refresh, harga_btn_tambah, harga_txt_cari});

        right_panel.add(harga, "card8");

        laporan.setBackground(new java.awt.Color(255, 255, 255));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel15.setText("Data laporan");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(96, 96, 96));
        jLabel16.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");

        lap_pendapatan.setBackground(new java.awt.Color(255, 255, 255));
        lap_pendapatan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        lap_pendapatan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lap_pendapatan.setPreferredSize(new java.awt.Dimension(230, 200));
        lap_pendapatan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lap_pendapatanMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lap_pendapatanMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lap_pendapatanMouseExited(evt);
            }
        });

        jLabel45.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/lap-grap.png"))); // NOI18N

        jdhjashg.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jdhjashg.setText("Grafik transaksi");

        javax.swing.GroupLayout lap_pendapatanLayout = new javax.swing.GroupLayout(lap_pendapatan);
        lap_pendapatan.setLayout(lap_pendapatanLayout);
        lap_pendapatanLayout.setHorizontalGroup(
            lap_pendapatanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lap_pendapatanLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(lap_pendapatanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jdhjashg)
                    .addComponent(jLabel45))
                .addContainerGap(67, Short.MAX_VALUE))
        );
        lap_pendapatanLayout.setVerticalGroup(
            lap_pendapatanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lap_pendapatanLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel45)
                .addGap(15, 15, 15)
                .addComponent(jdhjashg)
                .addGap(15, 15, 15))
        );

        lap_vcd.setBackground(new java.awt.Color(255, 255, 255));
        lap_vcd.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        lap_vcd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lap_vcd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lap_vcdMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lap_vcdMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lap_vcdMouseExited(evt);
            }
        });

        jLabel66.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/lap-vcd.png"))); // NOI18N

        jLabel67.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel67.setText("Laporan data VCD");

        javax.swing.GroupLayout lap_vcdLayout = new javax.swing.GroupLayout(lap_vcd);
        lap_vcd.setLayout(lap_vcdLayout);
        lap_vcdLayout.setHorizontalGroup(
            lap_vcdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lap_vcdLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(lap_vcdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel67)
                    .addComponent(jLabel66))
                .addContainerGap(49, Short.MAX_VALUE))
        );
        lap_vcdLayout.setVerticalGroup(
            lap_vcdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lap_vcdLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel66)
                .addGap(15, 15, 15)
                .addComponent(jLabel67)
                .addGap(15, 15, 15))
        );

        lap_restock.setBackground(new java.awt.Color(255, 255, 255));
        lap_restock.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        lap_restock.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lap_restock.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lap_restockMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lap_restockMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lap_restockMouseExited(evt);
            }
        });

        jLabel69.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/lap-stok.png"))); // NOI18N

        jLabel70.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel70.setText("Laporan restock");

        javax.swing.GroupLayout lap_restockLayout = new javax.swing.GroupLayout(lap_restock);
        lap_restock.setLayout(lap_restockLayout);
        lap_restockLayout.setHorizontalGroup(
            lap_restockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lap_restockLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(lap_restockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel70)
                    .addComponent(jLabel69))
                .addContainerGap(64, Short.MAX_VALUE))
        );
        lap_restockLayout.setVerticalGroup(
            lap_restockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lap_restockLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel69)
                .addGap(15, 15, 15)
                .addComponent(jLabel70)
                .addGap(15, 15, 15))
        );

        lap_pinjam.setBackground(new java.awt.Color(255, 255, 255));
        lap_pinjam.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        lap_pinjam.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lap_pinjam.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lap_pinjamMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lap_pinjamMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lap_pinjamMouseExited(evt);
            }
        });

        jLabel72.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/lap-trans.png"))); // NOI18N

        jLabel73.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel73.setText("Laporan peminjaman");

        javax.swing.GroupLayout lap_pinjamLayout = new javax.swing.GroupLayout(lap_pinjam);
        lap_pinjam.setLayout(lap_pinjamLayout);
        lap_pinjamLayout.setHorizontalGroup(
            lap_pinjamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lap_pinjamLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(lap_pinjamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel73)
                    .addComponent(jLabel72))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        lap_pinjamLayout.setVerticalGroup(
            lap_pinjamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lap_pinjamLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel72)
                .addGap(15, 15, 15)
                .addComponent(jLabel73)
                .addGap(15, 15, 15))
        );

        lap_kembali.setBackground(new java.awt.Color(255, 255, 255));
        lap_kembali.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        lap_kembali.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lap_kembali.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lap_kembaliMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lap_kembaliMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lap_kembaliMouseExited(evt);
            }
        });

        jLabel75.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/lap-trans.png"))); // NOI18N

        jLabel76.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel76.setText("Laporan pengembalian");

        javax.swing.GroupLayout lap_kembaliLayout = new javax.swing.GroupLayout(lap_kembali);
        lap_kembali.setLayout(lap_kembaliLayout);
        lap_kembaliLayout.setHorizontalGroup(
            lap_kembaliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lap_kembaliLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(lap_kembaliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel76)
                    .addComponent(jLabel75))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        lap_kembaliLayout.setVerticalGroup(
            lap_kembaliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lap_kembaliLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel75)
                .addGap(15, 15, 15)
                .addComponent(jLabel76)
                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout laporanLayout = new javax.swing.GroupLayout(laporan);
        laporan.setLayout(laporanLayout);
        laporanLayout.setHorizontalGroup(
            laporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(laporanLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(laporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(laporanLayout.createSequentialGroup()
                        .addGroup(laporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lap_pendapatan, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                            .addComponent(lap_kembali, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(10, 10, 10)
                        .addComponent(lap_vcd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(lap_restock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(lap_pinjam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(246, Short.MAX_VALUE))
        );

        laporanLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {lap_kembali, lap_pendapatan, lap_pinjam, lap_restock, lap_vcd});

        laporanLayout.setVerticalGroup(
            laporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(laporanLayout.createSequentialGroup()
                .addGroup(laporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(laporanLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(jLabel15)
                        .addGap(15, 15, 15)
                        .addComponent(jLabel16)
                        .addGap(20, 20, 20)
                        .addGroup(laporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lap_pinjam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lap_restock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(laporanLayout.createSequentialGroup()
                        .addGap(137, 137, 137)
                        .addGroup(laporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lap_vcd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lap_pendapatan, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(10, 10, 10)
                .addComponent(lap_kembali, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(573, Short.MAX_VALUE))
        );

        right_panel.add(laporan, "card9");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(left_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(right_panel))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(left_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(right_panel)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void d_pinjamMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_d_pinjamMouseEntered
        frameEnter(d_pinjam);
    }//GEN-LAST:event_d_pinjamMouseEntered

    private void d_pinjamMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_d_pinjamMouseExited
        frameExit(d_pinjam);
    }//GEN-LAST:event_d_pinjamMouseExited

    private void d_kembaliMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_d_kembaliMouseEntered
        frameEnter(d_kembali);
    }//GEN-LAST:event_d_kembaliMouseEntered

    private void d_kembaliMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_d_kembaliMouseExited
        frameExit(d_kembali);
    }//GEN-LAST:event_d_kembaliMouseExited

    private void d_profileMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_d_profileMouseEntered
        frameEnter(d_profile);
    }//GEN-LAST:event_d_profileMouseEntered

    private void d_profileMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_d_profileMouseExited
        frameExit(d_profile);
    }//GEN-LAST:event_d_profileMouseExited

    private void d_vcdMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_d_vcdMouseEntered
        frameEnter(d_vcd);
    }//GEN-LAST:event_d_vcdMouseEntered

    private void d_vcdMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_d_vcdMouseExited
        frameExit(d_vcd);
    }//GEN-LAST:event_d_vcdMouseExited

    private void d_customerMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_d_customerMouseEntered
        frameEnter(d_customer);
    }//GEN-LAST:event_d_customerMouseEntered

    private void d_customerMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_d_customerMouseExited
        frameExit(d_customer);
    }//GEN-LAST:event_d_customerMouseExited

    private void d_staffMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_d_staffMouseEntered
        frameEnter(d_staff);
    }//GEN-LAST:event_d_staffMouseEntered

    private void d_staffMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_d_staffMouseExited
        frameExit(d_staff);
    }//GEN-LAST:event_d_staffMouseExited

    private void d_hargaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_d_hargaMouseEntered
        frameEnter(d_harga);
    }//GEN-LAST:event_d_hargaMouseEntered

    private void d_hargaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_d_hargaMouseExited
        frameExit(d_harga);
    }//GEN-LAST:event_d_hargaMouseExited

    private void d_laporanMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_d_laporanMouseEntered
        frameEnter(d_laporan);
    }//GEN-LAST:event_d_laporanMouseEntered

    private void d_laporanMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_d_laporanMouseExited
        frameExit(d_laporan);
    }//GEN-LAST:event_d_laporanMouseExited

    private void lp_berandaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lp_berandaMouseEntered
        panelEnter(lp_beranda);
    }//GEN-LAST:event_lp_berandaMouseEntered

    private void lp_berandaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lp_berandaMouseExited
        if (state == 0) {
            frameActive(lp_beranda);
        } else {
            panelExit(lp_beranda);
        }
    }//GEN-LAST:event_lp_berandaMouseExited

    private void lp_peminjamanMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lp_peminjamanMouseEntered
        panelEnter(lp_peminjaman);
    }//GEN-LAST:event_lp_peminjamanMouseEntered

    private void lp_peminjamanMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lp_peminjamanMouseExited
        if (state == 1) {
            frameActive(lp_peminjaman);
        } else {
            panelExit(lp_peminjaman);
        }
    }//GEN-LAST:event_lp_peminjamanMouseExited

    private void lp_pengembalianMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lp_pengembalianMouseEntered
        panelEnter(lp_pengembalian);
    }//GEN-LAST:event_lp_pengembalianMouseEntered

    private void lp_pengembalianMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lp_pengembalianMouseExited
        if (state == 2) {
            frameActive(lp_pengembalian);
        } else {
            panelExit(lp_pengembalian);
        }
    }//GEN-LAST:event_lp_pengembalianMouseExited

    private void lp_profileMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lp_profileMouseEntered
        panelEnter(lp_profile);
    }//GEN-LAST:event_lp_profileMouseEntered

    private void lp_profileMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lp_profileMouseExited
        if (state == 3) {
            frameActive(lp_profile);
        } else {
            panelExit(lp_profile);
        }
    }//GEN-LAST:event_lp_profileMouseExited

    private void lp_vcdMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lp_vcdMouseEntered
        panelEnter(lp_vcd);
    }//GEN-LAST:event_lp_vcdMouseEntered

    private void lp_vcdMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lp_vcdMouseExited
        if (state == 4) {
            frameActive(lp_vcd);
        } else {
            panelExit(lp_vcd);
        }
    }//GEN-LAST:event_lp_vcdMouseExited

    private void lp_customerMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lp_customerMouseEntered
        panelEnter(lp_customer);
    }//GEN-LAST:event_lp_customerMouseEntered

    private void lp_customerMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lp_customerMouseExited
        if (state == 5) {
            frameActive(lp_customer);
        } else {
            panelExit(lp_customer);
        }
    }//GEN-LAST:event_lp_customerMouseExited

    private void lp_karyawanMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lp_karyawanMouseEntered
        panelEnter(lp_karyawan);
    }//GEN-LAST:event_lp_karyawanMouseEntered

    private void lp_karyawanMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lp_karyawanMouseExited
        if (state == 6) {
            frameActive(lp_karyawan);
        } else {
            panelExit(lp_karyawan);
        }
    }//GEN-LAST:event_lp_karyawanMouseExited

    private void lp_laporanMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lp_laporanMouseEntered
        panelEnter(lp_laporan);
    }//GEN-LAST:event_lp_laporanMouseEntered

    private void lp_laporanMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lp_laporanMouseExited
        if (state == 8) {
            frameActive(lp_laporan);
        } else {
            panelExit(lp_laporan);
        }
    }//GEN-LAST:event_lp_laporanMouseExited

    private void lp_keluarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lp_keluarMouseEntered
        panelEnter(lp_keluar);
    }//GEN-LAST:event_lp_keluarMouseEntered

    private void lp_keluarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lp_keluarMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_lp_keluarMousePressed

    private void lp_keluarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lp_keluarMouseExited
        if (state == 10) {
            frameActive(lp_keluar);
        } else {
            panelExit(lp_keluar);
        }
    }//GEN-LAST:event_lp_keluarMouseExited

    private void lp_berandaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lp_berandaMouseClicked
        dashboard();
    }//GEN-LAST:event_lp_berandaMouseClicked

    private void lp_peminjamanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lp_peminjamanMouseClicked
        peminjaman();
    }//GEN-LAST:event_lp_peminjamanMouseClicked

    private void lp_pengembalianMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lp_pengembalianMouseClicked
        pengembalian();
    }//GEN-LAST:event_lp_pengembalianMouseClicked

    private void lp_profileMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lp_profileMouseClicked
        profile();
    }//GEN-LAST:event_lp_profileMouseClicked

    private void lp_vcdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lp_vcdMouseClicked
        vcd();
    }//GEN-LAST:event_lp_vcdMouseClicked

    private void lp_customerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lp_customerMouseClicked
        customer();
    }//GEN-LAST:event_lp_customerMouseClicked

    private void lp_karyawanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lp_karyawanMouseClicked
        if (log_level.equals("Administrator")) {
            karyawan();
        } else {
            JOptionPane.showMessageDialog(null, "Anda tidak memiliki hak akses untuk menu ini!", "Keamanan", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_lp_karyawanMouseClicked

    private void lp_laporanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lp_laporanMouseClicked
        if (log_level.equals("Administrator")) {
            laporan();
        } else {
            JOptionPane.showMessageDialog(null, "Anda tidak memiliki hak akses untuk menu ini!", "Keamanan", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_lp_laporanMouseClicked

    private void lp_keluarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lp_keluarMouseClicked
        int selection = JOptionPane.showConfirmDialog(null, "Keluar dari dashboard?", "Konfirmasi", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (selection == JOptionPane.OK_OPTION) {
            keluar();
        }
    }//GEN-LAST:event_lp_keluarMouseClicked

    private void d_pinjamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_d_pinjamMouseClicked
        peminjaman();
    }//GEN-LAST:event_d_pinjamMouseClicked

    private void d_kembaliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_d_kembaliMouseClicked
        pengembalian();
    }//GEN-LAST:event_d_kembaliMouseClicked

    private void d_profileMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_d_profileMouseClicked
        profile();
    }//GEN-LAST:event_d_profileMouseClicked

    private void d_vcdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_d_vcdMouseClicked
        vcd();
    }//GEN-LAST:event_d_vcdMouseClicked

    private void d_customerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_d_customerMouseClicked
        customer();
    }//GEN-LAST:event_d_customerMouseClicked

    private void d_staffMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_d_staffMouseClicked
        if (log_level.equals("Administrator")) {
            karyawan();
        } else {
            JOptionPane.showMessageDialog(null, "Anda tidak memiliki hak akses untuk menu ini!", "Keamanan", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_d_staffMouseClicked

    private void d_hargaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_d_hargaMouseClicked
        if (log_level.equals("Administrator")) {
            harga();
        } else {
            JOptionPane.showMessageDialog(null, "Anda tidak memiliki hak akses untuk menu ini!", "Keamanan", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_d_hargaMouseClicked

    private void d_laporanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_d_laporanMouseClicked
        if (log_level.equals("Administrator")) {
            laporan();
        } else {
            JOptionPane.showMessageDialog(null, "Anda tidak memiliki hak akses untuk menu ini!", "Keamanan", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_d_laporanMouseClicked

    private void lp_hargaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lp_hargaMouseClicked
        if (log_level.equals("Administrator")) {
            harga();
        } else {
            JOptionPane.showMessageDialog(null, "Anda tidak memiliki hak akses untuk menu ini!", "Keamanan", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_lp_hargaMouseClicked

    private void lp_hargaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lp_hargaMouseEntered
        panelEnter(lp_harga);
    }//GEN-LAST:event_lp_hargaMouseEntered

    private void lp_hargaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lp_hargaMouseExited
        if (state == 7) {
            frameActive(lp_harga);
        } else {
            panelExit(lp_harga);
        }
    }//GEN-LAST:event_lp_hargaMouseExited

    private void d_keluarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_d_keluarMouseClicked
        int selection = JOptionPane.showConfirmDialog(null, "Keluar dari dashboard?", "Konfirmasi", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (selection == JOptionPane.OK_OPTION) {
            keluar();
        }
    }//GEN-LAST:event_d_keluarMouseClicked

    private void d_keluarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_d_keluarMouseEntered
        frameEnter(d_keluar);
    }//GEN-LAST:event_d_keluarMouseEntered

    private void d_keluarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_d_keluarMouseExited
        frameExit(d_keluar);
    }//GEN-LAST:event_d_keluarMouseExited

    private void profile_txt_ktpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profile_txt_ktpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_profile_txt_ktpActionPerformed

    private void profile_txt_kontakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profile_txt_kontakActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_profile_txt_kontakActionPerformed

    private void profile_txt_alamatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profile_txt_alamatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_profile_txt_alamatActionPerformed

    private void karyawan_btn_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_karyawan_btn_editActionPerformed
        if (d_id_karyawan == null) {
            JOptionPane.showMessageDialog(null, "Mohon pilih dahulu data yang akan diedit pada tabel!", "Oops", JOptionPane.WARNING_MESSAGE);
        } else {
            Karyawan_edit edit = new Karyawan_edit(d_id_karyawan);
            edit.setVisible(true);
            d_id_karyawan = null;
        }
    }//GEN-LAST:event_karyawan_btn_editActionPerformed

    private void karyawan_btn_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_karyawan_btn_tambahActionPerformed
        Karyawan_add tambah = new Karyawan_add();
        tambah.setVisible(true);
    }//GEN-LAST:event_karyawan_btn_tambahActionPerformed

    private void tb_karyawanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_karyawanMouseClicked
        tb_karyawan_klik();
    }//GEN-LAST:event_tb_karyawanMouseClicked

    private void karyawan_btn_refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_karyawan_btn_refreshActionPerformed
        karyawan_tampil();
    }//GEN-LAST:event_karyawan_btn_refreshActionPerformed

    private void karyawan_btn_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_karyawan_btn_hapusActionPerformed
        if (d_id_karyawan == null) {
            JOptionPane.showMessageDialog(null, "Mohon pilih dahulu data yang akan diedit pada tabel!", "Oops", JOptionPane.WARNING_MESSAGE);
        } else {
            int selection = JOptionPane.showConfirmDialog(null, "Hapus data?", "Konfirmasi", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (selection == JOptionPane.OK_OPTION) {
                karyawan_hapus(d_id_karyawan);
                d_id_karyawan = null;
            }
        }
    }//GEN-LAST:event_karyawan_btn_hapusActionPerformed

    private void karyawan_txt_cariKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_karyawan_txt_cariKeyReleased
        String query = karyawan_txt_cari.getText();
        tb_karyawan_cari(query);
    }//GEN-LAST:event_karyawan_txt_cariKeyReleased

    private void customer_btn_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customer_btn_hapusActionPerformed
        if (d_id_customer == null) {
            JOptionPane.showMessageDialog(null, "Mohon pilih dahulu data yang akan diedit pada tabel!", "Oops", JOptionPane.WARNING_MESSAGE);
        } else {
            int selection = JOptionPane.showConfirmDialog(null, "Hapus data?", "Konfirmasi", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (selection == JOptionPane.OK_OPTION) {
                customer_hapus(d_id_customer);
                d_id_customer = null;
            }
        }
    }//GEN-LAST:event_customer_btn_hapusActionPerformed

    private void customer_btn_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customer_btn_editActionPerformed
        if (d_id_customer == null) {
            JOptionPane.showMessageDialog(null, "Mohon pilih dahulu data yang akan diedit pada tabel!", "Oops", JOptionPane.WARNING_MESSAGE);
        } else {
            Customer_edit edit = new Customer_edit(d_id_customer);
            edit.setVisible(true);
            d_id_customer = null;
        }
    }//GEN-LAST:event_customer_btn_editActionPerformed

    private void customer_btn_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customer_btn_tambahActionPerformed
        Customer_add tambah = new Customer_add();
        tambah.setVisible(true);
    }//GEN-LAST:event_customer_btn_tambahActionPerformed

    private void customer_btn_refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customer_btn_refreshActionPerformed
        customer_tampil();
    }//GEN-LAST:event_customer_btn_refreshActionPerformed

    private void tb_customerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_customerMouseClicked
        tb_customer_klik();
    }//GEN-LAST:event_tb_customerMouseClicked

    private void harga_btn_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_harga_btn_editActionPerformed
        if (d_id_harga == null) {
            JOptionPane.showMessageDialog(null, "Mohon pilih dahulu data yang akan diedit pada tabel!", "Oops", JOptionPane.WARNING_MESSAGE);
        } else {
            Harga_edit edit = new Harga_edit(d_id_harga);
            edit.setVisible(true);
            d_id_harga = null;
        }
    }//GEN-LAST:event_harga_btn_editActionPerformed

    private void harga_btn_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_harga_btn_hapusActionPerformed
        if (d_id_harga == null) {
            JOptionPane.showMessageDialog(null, "Mohon pilih dahulu data yang akan diedit pada tabel!", "Oops", JOptionPane.WARNING_MESSAGE);
        } else if (d_id_harga.equals("H0001") || d_id_harga.equals("H0002") || d_id_harga.equals("H0003")) {
            JOptionPane.showMessageDialog(null, "Mohon maaf, data ini tidak boleh dihapus!", "Oops", JOptionPane.WARNING_MESSAGE);
        } else {
            int selection = JOptionPane.showConfirmDialog(null, "Hapus data?", "Konfirmasi", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (selection == JOptionPane.OK_OPTION) {
                harga_hapus(d_id_harga);
                d_id_harga = null;
            }
        }
    }//GEN-LAST:event_harga_btn_hapusActionPerformed

    private void harga_btn_refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_harga_btn_refreshActionPerformed
        harga_tampil();
    }//GEN-LAST:event_harga_btn_refreshActionPerformed

    private void harga_btn_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_harga_btn_tambahActionPerformed
        Harga_add tambah = new Harga_add();
        tambah.setVisible(true);
    }//GEN-LAST:event_harga_btn_tambahActionPerformed

    private void tb_hargaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_hargaMouseClicked
        tb_harga_klik();
    }//GEN-LAST:event_tb_hargaMouseClicked

    private void profile_btn_gantiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profile_btn_gantiActionPerformed
        String old = profile_txt_pass_old.getText(), now = profile_txt_pass_new.getText();

        if (old.isEmpty() || now.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mohon lengkapi data!", "Peringatan", JOptionPane.ERROR_MESSAGE);

            if (old.isEmpty()) {
                profile_txt_pass_old.requestFocus();
            } else if (now.isEmpty()) {
                profile_txt_pass_new.requestFocus();
            }
        } else {
            int selection = JOptionPane.showConfirmDialog(null, "Simpan perubahan?", "Konfirmasi", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (selection == JOptionPane.OK_OPTION) {
                ganti_password(old, now);
            }
        }        // TODO add your
    }//GEN-LAST:event_profile_btn_gantiActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String nama = profile_txt_nama.getText(), kontak = profile_txt_kontak.getText(), alamat = profile_txt_alamat.getText();

        if (nama.isEmpty() || kontak.isEmpty() || alamat.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mohon lengkapi data!", "Peringatan", JOptionPane.ERROR_MESSAGE);

            if (nama.isEmpty()) {
                profile_txt_nama.requestFocus();
            } else if (kontak.isEmpty()) {
                profile_txt_kontak.requestFocus();
            } else if (alamat.isEmpty()) {
                profile_txt_alamat.requestFocus();
            }
        } else {
            int selection = JOptionPane.showConfirmDialog(null, "Simpan perubahan?", "Konfirmasi", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (selection == JOptionPane.OK_OPTION) {
                profil_edit(nama, kontak, alamat);
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void tb_vcdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_vcdMouseClicked
        tb_vcd_klik();
    }//GEN-LAST:event_tb_vcdMouseClicked

    private void vcd_btn_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vcd_btn_tambahActionPerformed
        Vcd_add tambah = new Vcd_add(log_id);
        tambah.setVisible(true);
    }//GEN-LAST:event_vcd_btn_tambahActionPerformed

    private void vcd_btn_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vcd_btn_editActionPerformed
        if (d_id_vcd == null) {
            JOptionPane.showMessageDialog(null, "Mohon pilih dahulu data yang akan diedit pada tabel!", "Oops", JOptionPane.WARNING_MESSAGE);
        } else {
            Vcd_edit edit = new Vcd_edit(d_id_vcd);
            edit.setVisible(true);
            d_id_vcd = null;
        }
    }//GEN-LAST:event_vcd_btn_editActionPerformed

    private void vcd_btn_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vcd_btn_hapusActionPerformed
        if (d_id_vcd == null) {
            JOptionPane.showMessageDialog(null, "Mohon pilih dahulu data yang akan diedit pada tabel!", "Oops", JOptionPane.WARNING_MESSAGE);
        } else {
            int selection = JOptionPane.showConfirmDialog(null, "Hapus data?", "Konfirmasi", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (selection == JOptionPane.OK_OPTION) {
                vcd_hapus(d_id_vcd);
                d_id_vcd = null;
            }
        }
    }//GEN-LAST:event_vcd_btn_hapusActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        vcd_tampil();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void vcd_btn_restockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vcd_btn_restockActionPerformed
        if (d_id_vcd == null) {
            JOptionPane.showMessageDialog(null, "Mohon pilih dahulu data yang akan diedit pada tabel!", "Oops", JOptionPane.WARNING_MESSAGE);
        } else {
            Vcd_restock rest = new Vcd_restock(d_id_vcd, log_id);
            rest.setVisible(true);
            d_id_vcd = null;
        }
    }//GEN-LAST:event_vcd_btn_restockActionPerformed

    private void pinjam_btn_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pinjam_btn_tambahActionPerformed
        Trans_pinjam_cari pinjam = new Trans_pinjam_cari(log_id);
        pinjam.setVisible(true);
    }//GEN-LAST:event_pinjam_btn_tambahActionPerformed

    private void tb_pinjamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_pinjamMouseClicked
        tb_pinjam_klik();
    }//GEN-LAST:event_tb_pinjamMouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if (status.equals("Berjalan")) {
            Trans_kembali kembali = new Trans_kembali(log_id, booking_id);
            kembali.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Transaksi sudah selesai!", "Kesalahan", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void tb_kembaliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_kembaliMouseClicked
        tb_kembali_klik();
    }//GEN-LAST:event_tb_kembaliMouseClicked

    private void lap_pendapatanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lap_pendapatanMouseClicked
        Laporan_grafik2 grafik = new Laporan_grafik2();
        grafik.setVisible(true);

//        Laporan_grafik grafik = new Laporan_grafik();
//        grafik.setVisible(true);
    }//GEN-LAST:event_lap_pendapatanMouseClicked

    private void lap_pendapatanMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lap_pendapatanMouseEntered
        frameEnter(lap_pendapatan);
    }//GEN-LAST:event_lap_pendapatanMouseEntered

    private void lap_pendapatanMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lap_pendapatanMouseExited
        frameExit(lap_pendapatan);
    }//GEN-LAST:event_lap_pendapatanMouseExited

    private void lap_vcdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lap_vcdMouseClicked
        int selection = JOptionPane.showConfirmDialog(null, "Buat file laporan VCD?", "Konfirmasi", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (selection == JOptionPane.OK_OPTION) {
            try {
                dao_laporan dao = new dao_laporan();
                dao.lap_vcd();
            } catch (IOException ex) {
                Logger.getLogger(Admin_dashboard.class.getName()).log(Level.SEVERE, null, ex);
            } catch (WriteException ex) {
                Logger.getLogger(Admin_dashboard.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_lap_vcdMouseClicked

    private void lap_vcdMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lap_vcdMouseEntered
        frameEnter(lap_vcd);
    }//GEN-LAST:event_lap_vcdMouseEntered

    private void lap_vcdMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lap_vcdMouseExited
        frameExit(lap_vcd);
    }//GEN-LAST:event_lap_vcdMouseExited

    private void lap_restockMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lap_restockMouseClicked
        int selection = JOptionPane.showConfirmDialog(null, "Buat file laporan Restock?", "Konfirmasi", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (selection == JOptionPane.OK_OPTION) {
            try {
                dao_laporan dao = new dao_laporan();
                dao.lap_restock();
            } catch (IOException ex) {
                Logger.getLogger(Admin_dashboard.class.getName()).log(Level.SEVERE, null, ex);
            } catch (WriteException ex) {
                Logger.getLogger(Admin_dashboard.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_lap_restockMouseClicked

    private void lap_restockMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lap_restockMouseEntered
        frameEnter(lap_restock);
    }//GEN-LAST:event_lap_restockMouseEntered

    private void lap_restockMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lap_restockMouseExited
        frameExit(lap_restock);
    }//GEN-LAST:event_lap_restockMouseExited

    private void lap_pinjamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lap_pinjamMouseClicked
        int selection = JOptionPane.showConfirmDialog(null, "Buat file laporan Transaksi Peminjaman?", "Konfirmasi", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (selection == JOptionPane.OK_OPTION) {
            try {
                dao_laporan dao = new dao_laporan();
                dao.lap_pinjam();
            } catch (IOException ex) {
                Logger.getLogger(Admin_dashboard.class.getName()).log(Level.SEVERE, null, ex);
            } catch (WriteException ex) {
                Logger.getLogger(Admin_dashboard.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_lap_pinjamMouseClicked

    private void lap_pinjamMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lap_pinjamMouseEntered
        frameEnter(lap_pinjam);
    }//GEN-LAST:event_lap_pinjamMouseEntered

    private void lap_pinjamMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lap_pinjamMouseExited
        frameExit(lap_pinjam);
    }//GEN-LAST:event_lap_pinjamMouseExited

    private void lap_kembaliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lap_kembaliMouseClicked
        int selection = JOptionPane.showConfirmDialog(null, "Buat file laporan Transaksi Pengembalian?", "Konfirmasi", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (selection == JOptionPane.OK_OPTION) {
            try {
                dao_laporan dao = new dao_laporan();
                dao.lap_kembali();
            } catch (IOException ex) {
                Logger.getLogger(Admin_dashboard.class.getName()).log(Level.SEVERE, null, ex);
            } catch (WriteException ex) {
                Logger.getLogger(Admin_dashboard.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_lap_kembaliMouseClicked

    private void lap_kembaliMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lap_kembaliMouseEntered
        frameEnter(lap_kembali);
    }//GEN-LAST:event_lap_kembaliMouseEntered

    private void lap_kembaliMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lap_kembaliMouseExited
        frameExit(lap_kembali);
    }//GEN-LAST:event_lap_kembaliMouseExited

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Admin_dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Admin_dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Admin_dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Admin_dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Admin_dashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel customer;
    private javax.swing.JButton customer_btn_edit;
    private javax.swing.JButton customer_btn_hapus;
    private javax.swing.JButton customer_btn_refresh;
    private javax.swing.JButton customer_btn_tambah;
    private javax.swing.JTextField customer_txt_cari;
    private javax.swing.JPanel d_customer;
    private javax.swing.JPanel d_harga;
    private javax.swing.JPanel d_keluar;
    private javax.swing.JPanel d_kembali;
    private javax.swing.JPanel d_laporan;
    private javax.swing.JPanel d_pinjam;
    private javax.swing.JPanel d_profile;
    private javax.swing.JPanel d_staff;
    private javax.swing.JPanel d_vcd;
    private javax.swing.JPanel dashboard;
    private javax.swing.JPanel harga;
    private javax.swing.JButton harga_btn_edit;
    private javax.swing.JButton harga_btn_hapus;
    private javax.swing.JButton harga_btn_refresh;
    private javax.swing.JButton harga_btn_tambah;
    private javax.swing.JTextField harga_txt_cari;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel107;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel112;
    private javax.swing.JLabel jLabel119;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel120;
    private javax.swing.JLabel jLabel122;
    private javax.swing.JLabel jLabel123;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTable jTable2;
    private javax.swing.JLabel jdhjashg;
    private javax.swing.JPanel karyawan;
    private javax.swing.JButton karyawan_btn_edit;
    private javax.swing.JButton karyawan_btn_hapus;
    private javax.swing.JButton karyawan_btn_refresh;
    private javax.swing.JButton karyawan_btn_tambah;
    private javax.swing.JTextField karyawan_txt_cari;
    private javax.swing.JTextField kembali_txt_cari;
    private javax.swing.JPanel lap_kembali;
    private javax.swing.JPanel lap_pendapatan;
    private javax.swing.JPanel lap_pinjam;
    private javax.swing.JPanel lap_restock;
    private javax.swing.JPanel lap_vcd;
    private javax.swing.JPanel laporan;
    private javax.swing.JPanel left_panel;
    private javax.swing.JPanel lp_beranda;
    private javax.swing.JPanel lp_customer;
    private javax.swing.JPanel lp_harga;
    private javax.swing.JPanel lp_karyawan;
    private javax.swing.JPanel lp_keluar;
    private javax.swing.JPanel lp_laporan;
    private javax.swing.JPanel lp_peminjaman;
    private javax.swing.JPanel lp_pengembalian;
    private javax.swing.JPanel lp_profile;
    private javax.swing.JPanel lp_vcd;
    private javax.swing.JPanel peminjaman;
    private javax.swing.JPanel pengembalian;
    private javax.swing.JButton pinjam_btn_tambah;
    private javax.swing.JTextField pinjam_txt_cari;
    private javax.swing.JPanel profile;
    private javax.swing.JButton profile_btn_ganti;
    private javax.swing.JTextField profile_txt_alamat;
    private javax.swing.JTextField profile_txt_kontak;
    private javax.swing.JTextField profile_txt_ktp;
    private javax.swing.JTextField profile_txt_level;
    private javax.swing.JTextField profile_txt_nama;
    private javax.swing.JPasswordField profile_txt_pass_new;
    private javax.swing.JPasswordField profile_txt_pass_old;
    private javax.swing.JLayeredPane right_panel;
    private javax.swing.JTable tb_customer;
    private javax.swing.JTable tb_harga;
    private javax.swing.JTable tb_karyawan;
    private javax.swing.JTable tb_kembali;
    private javax.swing.JTable tb_pinjam;
    private javax.swing.JTable tb_pinjam_det;
    private javax.swing.JTable tb_vcd;
    private javax.swing.JPanel vcd;
    private javax.swing.JButton vcd_btn_edit;
    private javax.swing.JButton vcd_btn_hapus;
    private javax.swing.JButton vcd_btn_restock;
    private javax.swing.JButton vcd_btn_tambah;
    private javax.swing.JTextField vcd_txt_cari;
    // End of variables declaration//GEN-END:variables
}
