package swing;

import dao.dao_pinjam;
import dao.dao_vcd;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import models.m_keranjang;
import models.m_pinjam;
import models.m_pinjam_det;
import models.m_vcd;

public class Trans_pinjam extends javax.swing.JFrame {

    private String p_karyawan, p_customer, log_vcd = "", log_jd = "";
    private float p_total;
    private int log_sup = 0;
    private List<m_keranjang> keranjang = new ArrayList<m_keranjang>();

    /**
     * Creates new form Trans_pinjam
     *
     * @param karyawan
     * @param customer
     */
    public Trans_pinjam(String karyawan, String customer) {
        initComponents();
        p_karyawan = karyawan;
        p_customer = customer;
        tampil();
    }

    public Trans_pinjam() {
        initComponents();
    }

    void tampil() {
        try {
            dao_pinjam dao = new dao_pinjam();
            String data = dao.getCode();
            String date = LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            pinjam_txt_deadline.setText(date);
            pinjam_txt_booking.setText(data);
            pinjam_txt_customer.setText(p_customer);
            jLabel4.setText("<html>Periksa semua VCD yang akan disewa ada di dalam keranjang<html>");

            vcd_data();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal mendapatkan data\n" + e, "Gagal", JOptionPane.ERROR_MESSAGE);
        }

    }

    void vcd_data() {
        Object[] baris = {"ID", "JUDUL", "TERSEDIA"};
        DefaultTableModel dt = new DefaultTableModel(null, baris);
        tb_pinjam_data.setModel(dt);

        try {
            dao_vcd dao = new dao_vcd();
            List<m_vcd> data = dao.readVcd();

            for (m_vcd d : data) {
                String sedia = Integer.toString(d.getKondisi_baik() - d.getTerpinjam());
                String[] isi = {d.getId_vcd(), d.getJudul(), sedia};
                dt.addRow(isi);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal mendapatkan data\n" + e, "Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }

    void tb_vcd_cari(String query) {
        try {
            DefaultTableModel model = (DefaultTableModel) tb_pinjam_data.getModel();
            TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(model);
            tb_pinjam_data.setRowSorter(tr);
            tr.setRowFilter(RowFilter.regexFilter(query));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void tb_vcd_click() {
        try {
            int row = tb_pinjam_data.getSelectedRow();
            log_vcd = tb_pinjam_data.getModel().getValueAt(row, 0).toString();
            log_jd = tb_pinjam_data.getModel().getValueAt(row, 1).toString();
            log_sup = (Integer) Integer.parseInt(tb_pinjam_data.getModel().getValueAt(row, 2).toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal mendapatkan data\n" + e, "Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }

    void tb_vcd_to_basket(String id, String jd, int jm) {
        try {
            dao_pinjam dao = new dao_pinjam();

            float vcd_st = dao.getHarga(id);
            float sub = jm * vcd_st;

            if (keranjang.isEmpty()) {
                keranjang.add(new m_keranjang(id, jd, jm, sub));
            } else {
                int i = 0;
                boolean cek = false;

                for (m_keranjang k : keranjang) {
                    if (k.getId().equals(id)) {
                        cek = true;
                        int newjm = k.getJumlah() + jm;
                        float newsub = k.getSubtotal() + sub;
                        keranjang.set(i, new m_keranjang(id, jd, newjm, newsub));
                    }
                    i++;
                }

                if (!cek) {
                    keranjang.add(new m_keranjang(id, jd, jm, sub));
                }
            }

            tampil_keranjang();
            hit_total();

            log_jd = "";
            log_vcd = "";
            log_sup = 0;
            pinjam_txt_qty.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal mendapatkan data\n" + e, "Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }

    void tampil_keranjang() {
        try {
            DefaultTableModel model = (DefaultTableModel) tb_pinjam_keranjang.getModel();
            model.setRowCount(0);
            NumberFormat nf = NumberFormat.getInstance(new Locale("da", "DK"));
            for (m_keranjang k : keranjang) {
                String[] isi = {k.getId(), k.getJudul(), Integer.toString(k.getJumlah()), nf.format(k.getSubtotal())};
                model.addRow(isi);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal mendapatkan data\n" + e, "Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }

    void edit_keranjang() {
        try {
            int row = tb_pinjam_keranjang.getSelectedRow();
            String id = tb_pinjam_keranjang.getModel().getValueAt(row, 0).toString();
            int jm = (Integer) Integer.parseInt(tb_pinjam_keranjang.getModel().getValueAt(row, 2).toString());

            JFrame frame = new JFrame("New QTY");
            String qty = JOptionPane.showInputDialog(frame, "Masukkan jumlah baru", "Ubah jumlah pinjam", JOptionPane.QUESTION_MESSAGE);

            if (qty.isEmpty() || !numberOrNot(qty)) {
                JOptionPane.showMessageDialog(null, "Input salah!", "Kesalahan", JOptionPane.ERROR_MESSAGE);
            } else {
                int newqty = (Integer) Integer.parseInt(qty);

                if (newqty >= jm) {
                    JOptionPane.showMessageDialog(null, "Jumlah tidak bisa ditambah melalui jalur ini! \nSilahkan tambah jumlah dengan cara menginput data baru.", "Oops", JOptionPane.WARNING_MESSAGE);
                } else if (newqty == 0) {
                    int i = 0, c = 0;
                    for (m_keranjang k : keranjang) {
                        if (k.getId().equals(id)) {
                            c = i;
                        }
                        i++;
                    }
                    keranjang.remove(c);
                } else {
                    int i = 0;
                    for (m_keranjang k : keranjang) {
                        if (k.getId().equals(id)) {
                            float harga = k.getSubtotal() / k.getJumlah();
                            float newsub = newqty * harga;
                            keranjang.set(i, new m_keranjang(k.getId(), k.getJudul(), newqty, newsub));
                        }
                        i++;
                    }
                }
            }

            tampil_keranjang();
            hit_total();
        } catch (Exception e) {
        }
    }

    void hit_total() {
        p_total = 0;
        try {
            for (m_keranjang k : keranjang) {
                p_total += k.getSubtotal();
            }

            NumberFormat nf = NumberFormat.getInstance(new Locale("da", "DK"));
            pinjam_txt_harga.setText(nf.format(p_total));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal mendapatkan data\n" + e, "Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }

    void simpan_trans(String kb, String dl, float by) {
        try {
            dao_pinjam dao = new dao_pinjam();
            m_pinjam data1 = new m_pinjam();
            String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            Boolean c1 = false, c2 = false;

            data1.setId_pinjam(kb);
            data1.setTgl_pinjam(date);
            data1.setJatuh_tempo(dl);
            data1.setHarga_total(p_total);
            data1.setStatus("Berjalan");
            data1.setId_customer(p_customer);
            data1.setId_karyawan(p_karyawan);

            if (dao.simpanTrans(data1)) {
                c1 = true;
                for (m_keranjang k : keranjang) {
                    m_pinjam_det data2 = new m_pinjam_det();

                    data2.setId_pinjam(kb);
                    data2.setId_vcd(k.getId());
                    data2.setJumlah(k.getJumlah());
                    data2.setSubtotal(k.getSubtotal());
                    data2.setKembali(0);

                    if (dao.simpanTransDet(data2)) {
                        c2 = true;
                    }
                }
            }

            if (c1 && c2) {
                JOptionPane.showMessageDialog(null, "Transaksi berhasil!", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                JOptionPane.showMessageDialog(null, "Kembalian \nRp. " + (by - p_total), "Info", JOptionPane.INFORMATION_MESSAGE);
                dao.struk(kb, by);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Maaf, transaksi gagal!", "Gagal", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menyimpan data.\n" + e, "Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }

    void acceptNUMBER(JTextField tf) {
        tf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                char ch = ke.getKeyChar();
                if (Character.isDigit(ch) || ke.getExtendedKeyCode() == KeyEvent.VK_SPACE || ke.getExtendedKeyCode() == KeyEvent.VK_BACK_SPACE || ke.getExtendedKeyCode() == KeyEvent.VK_DELETE) {
                    tf.setEditable(true);
                } else {
                    tf.setEditable(false);
                }
            }
        });
    }

    static boolean numberOrNot(String input) {
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        peminjaman = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel66 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        pinjam_txt_booking = new javax.swing.JTextField();
        pinjam_txt_deadline = new javax.swing.JTextField();
        pinjam_txt_customer = new javax.swing.JTextField();
        jLabel69 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        pinjam_txt_harga = new javax.swing.JTextField();
        pinjam_txt_bayar = new javax.swing.JTextField();
        pinjam_btn_proses = new javax.swing.JPanel();
        jLabel73 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        pinjam_txt_cari = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        pinjam_txt_qty = new javax.swing.JTextField();
        pinjam_btn_tambah = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_pinjam_data = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tb_pinjam_keranjang = new javax.swing.JTable();
        jLabel68 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Transaksi peminjaman");
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);

        peminjaman.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel3.setText("Transaksi peminjaman");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(96, 96, 96));
        jLabel4.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel66.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel66.setForeground(new java.awt.Color(51, 51, 51));
        jLabel66.setText("Data transaksi");

        jLabel72.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel72.setForeground(new java.awt.Color(93, 93, 93));
        jLabel72.setText("Jatuh tempo");

        jLabel71.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel71.setForeground(new java.awt.Color(93, 93, 93));
        jLabel71.setText("Customer");

        jLabel70.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel70.setForeground(new java.awt.Color(93, 93, 93));
        jLabel70.setText("No. Transaksi");

        pinjam_txt_booking.setEditable(false);
        pinjam_txt_booking.setBackground(new java.awt.Color(244, 244, 244));
        pinjam_txt_booking.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        pinjam_txt_booking.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(96, 96, 96), 2));
        pinjam_txt_booking.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pinjam_txt_bookingActionPerformed(evt);
            }
        });

        pinjam_txt_deadline.setEditable(false);
        pinjam_txt_deadline.setBackground(new java.awt.Color(244, 244, 244));
        pinjam_txt_deadline.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        pinjam_txt_deadline.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(96, 96, 96), 2));

        pinjam_txt_customer.setEditable(false);
        pinjam_txt_customer.setBackground(new java.awt.Color(244, 244, 244));
        pinjam_txt_customer.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        pinjam_txt_customer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(96, 96, 96), 2));
        pinjam_txt_customer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pinjam_txt_customerActionPerformed(evt);
            }
        });

        jLabel69.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel69.setForeground(new java.awt.Color(51, 51, 51));
        jLabel69.setText("Keranjang dan pembayaran");

        jLabel74.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel74.setForeground(new java.awt.Color(93, 93, 93));
        jLabel74.setText("Total harga");

        jLabel75.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel75.setForeground(new java.awt.Color(93, 93, 93));
        jLabel75.setText("Harga bayar");

        pinjam_txt_harga.setEditable(false);
        pinjam_txt_harga.setBackground(new java.awt.Color(244, 244, 244));
        pinjam_txt_harga.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        pinjam_txt_harga.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        pinjam_txt_harga.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(96, 96, 96), 2));

        pinjam_txt_bayar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        pinjam_txt_bayar.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        pinjam_txt_bayar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(96, 96, 96), 2));
        pinjam_txt_bayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pinjam_txt_bayarActionPerformed(evt);
            }
        });
        pinjam_txt_bayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pinjam_txt_bayarKeyReleased(evt);
            }
        });

        pinjam_btn_proses.setBackground(new java.awt.Color(0, 120, 215));
        pinjam_btn_proses.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pinjam_btn_proses.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pinjam_btn_prosesMouseClicked(evt);
            }
        });

        jLabel73.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel73.setForeground(new java.awt.Color(255, 255, 255));
        jLabel73.setText("Simpan transaksi");

        javax.swing.GroupLayout pinjam_btn_prosesLayout = new javax.swing.GroupLayout(pinjam_btn_proses);
        pinjam_btn_proses.setLayout(pinjam_btn_prosesLayout);
        pinjam_btn_prosesLayout.setHorizontalGroup(
            pinjam_btn_prosesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pinjam_btn_prosesLayout.createSequentialGroup()
                .addContainerGap(64, Short.MAX_VALUE)
                .addComponent(jLabel73)
                .addContainerGap(65, Short.MAX_VALUE))
        );
        pinjam_btn_prosesLayout.setVerticalGroup(
            pinjam_btn_prosesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel73, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel75)
                            .addComponent(jLabel74))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pinjam_txt_bayar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pinjam_txt_harga, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(pinjam_btn_proses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel69, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel71)
                                    .addComponent(jLabel72)
                                    .addComponent(jLabel70)
                                    .addComponent(jLabel66))
                                .addGap(26, 26, 26)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(pinjam_txt_booking, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(pinjam_txt_deadline, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(pinjam_txt_customer, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(0, 0, 0))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel66)
                .addGap(15, 15, 15)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel70)
                    .addComponent(pinjam_txt_booking, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel72)
                    .addComponent(pinjam_txt_deadline, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel71)
                    .addComponent(pinjam_txt_customer, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(130, 130, 130)
                .addComponent(jLabel69)
                .addGap(15, 15, 15)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel74)
                    .addComponent(pinjam_txt_harga, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel75)
                    .addComponent(pinjam_txt_bayar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addComponent(pinjam_btn_proses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {pinjam_txt_booking, pinjam_txt_customer, pinjam_txt_deadline});

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {pinjam_txt_bayar, pinjam_txt_harga});

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        pinjam_txt_cari.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        pinjam_txt_cari.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(96, 96, 96), 2));
        pinjam_txt_cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pinjam_txt_cariActionPerformed(evt);
            }
        });
        pinjam_txt_cari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pinjam_txt_cariKeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI Light", 0, 14)); // NOI18N
        jLabel2.setText("Qty");

        pinjam_txt_qty.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        pinjam_txt_qty.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        pinjam_txt_qty.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(93, 93, 93), 2));
        pinjam_txt_qty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pinjam_txt_qtyKeyReleased(evt);
            }
        });

        pinjam_btn_tambah.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        pinjam_btn_tambah.setText("Tambah");
        pinjam_btn_tambah.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pinjam_btn_tambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pinjam_btn_tambahActionPerformed(evt);
            }
        });

        tb_pinjam_data.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tb_pinjam_data.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tb_pinjam_data.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_pinjam_dataMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tb_pinjam_data);

        tb_pinjam_keranjang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "JUDUL", "JUMLAH", "SUB TOTAL"
            }
        ));
        tb_pinjam_keranjang.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tb_pinjam_keranjang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_pinjam_keranjangMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tb_pinjam_keranjang);

        jLabel68.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel68.setForeground(new java.awt.Color(96, 96, 96));
        jLabel68.setText("Klik data pada tabel untuk menghapus dari keranjang");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(pinjam_txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addGap(10, 10, 10)
                        .addComponent(pinjam_txt_qty, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(pinjam_btn_tambah))))
            .addComponent(jLabel68)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(pinjam_txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(pinjam_txt_qty, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pinjam_btn_tambah))
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel68)
                .addGap(0, 0, 0))
        );

        jPanel4Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {pinjam_btn_tambah, pinjam_txt_qty});

        javax.swing.GroupLayout peminjamanLayout = new javax.swing.GroupLayout(peminjaman);
        peminjaman.setLayout(peminjamanLayout);
        peminjamanLayout.setHorizontalGroup(
            peminjamanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(peminjamanLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(peminjamanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(peminjamanLayout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(peminjamanLayout.createSequentialGroup()
                        .addGroup(peminjamanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(peminjamanLayout.createSequentialGroup()
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(50, 50, 50)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(45, 45, 45))))
        );
        peminjamanLayout.setVerticalGroup(
            peminjamanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(peminjamanLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel3)
                .addGap(15, 15, 15)
                .addComponent(jLabel4)
                .addGap(20, 20, 20)
                .addGroup(peminjamanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(peminjaman, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(peminjaman, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void pinjam_txt_bayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pinjam_txt_bayarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pinjam_txt_bayarActionPerformed

    private void pinjam_txt_customerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pinjam_txt_customerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pinjam_txt_customerActionPerformed

    private void pinjam_txt_cariKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pinjam_txt_cariKeyReleased
        String query = pinjam_txt_cari.getText();
        tb_vcd_cari(query);
    }//GEN-LAST:event_pinjam_txt_cariKeyReleased

    private void pinjam_txt_qtyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pinjam_txt_qtyKeyReleased
        acceptNUMBER(pinjam_txt_qty);
    }//GEN-LAST:event_pinjam_txt_qtyKeyReleased

    private void pinjam_txt_bookingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pinjam_txt_bookingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pinjam_txt_bookingActionPerformed

    private void pinjam_txt_cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pinjam_txt_cariActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pinjam_txt_cariActionPerformed

    private void pinjam_txt_bayarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pinjam_txt_bayarKeyReleased
        acceptNUMBER(pinjam_txt_bayar);
    }//GEN-LAST:event_pinjam_txt_bayarKeyReleased

    private void pinjam_btn_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pinjam_btn_tambahActionPerformed
        String qty = pinjam_txt_qty.getText();

        if (!log_vcd.isEmpty() && !log_jd.isEmpty() && !qty.isEmpty() && log_sup != 0 && numberOrNot(qty)) {
            if (Integer.parseInt(qty) > log_sup) {
                JOptionPane.showMessageDialog(null, "Jumlah pinjam yang anda masukkan \nmelebihi ketersediaan toko!", "Peringatan", JOptionPane.ERROR_MESSAGE);
                pinjam_txt_qty.setText("");
                pinjam_txt_qty.requestFocus();
            } else {
                tb_vcd_to_basket(log_vcd, log_jd, Integer.parseInt(qty));
            }
        } else if (qty.isEmpty() || !numberOrNot(qty)) {
            JOptionPane.showMessageDialog(null, "Jumlah pinjam belum dimasukkan atau salah!", "Oops", JOptionPane.WARNING_MESSAGE);
            pinjam_txt_qty.setText("");
            pinjam_txt_qty.requestFocus();
        } else if (log_vcd.isEmpty() || log_jd.isEmpty() || log_sup == 0) {
            JOptionPane.showMessageDialog(null, "Pilih dulu data VCD-nya!", "Oops", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_pinjam_btn_tambahActionPerformed

    private void tb_pinjam_dataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_pinjam_dataMouseClicked
        tb_vcd_click();
    }//GEN-LAST:event_tb_pinjam_dataMouseClicked

    private void tb_pinjam_keranjangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_pinjam_keranjangMouseClicked
        edit_keranjang();
    }//GEN-LAST:event_tb_pinjam_keranjangMouseClicked

    private void pinjam_btn_prosesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pinjam_btn_prosesMouseClicked
        if(pinjam_txt_bayar.getText().equals("") || !numberOrNot(pinjam_txt_bayar.getText())){
            pinjam_txt_bayar.requestFocus();
        } else if(Float.parseFloat(pinjam_txt_bayar.getText()) < p_total){
            JOptionPane.showMessageDialog(null, "Nominal bayar tidak diterima!", "Kesalahan", JOptionPane.ERROR_MESSAGE);
            pinjam_txt_bayar.setText("");
            pinjam_txt_bayar.requestFocus();
        } else{
            simpan_trans(pinjam_txt_booking.getText(), pinjam_txt_deadline.getText(), Float.parseFloat(pinjam_txt_bayar.getText()));
        }
    }//GEN-LAST:event_pinjam_btn_prosesMouseClicked

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
            java.util.logging.Logger.getLogger(Trans_pinjam.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Trans_pinjam.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Trans_pinjam.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Trans_pinjam.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Trans_pinjam().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel peminjaman;
    private javax.swing.JPanel pinjam_btn_proses;
    private javax.swing.JButton pinjam_btn_tambah;
    private javax.swing.JTextField pinjam_txt_bayar;
    private javax.swing.JTextField pinjam_txt_booking;
    private javax.swing.JTextField pinjam_txt_cari;
    private javax.swing.JTextField pinjam_txt_customer;
    private javax.swing.JTextField pinjam_txt_deadline;
    private javax.swing.JTextField pinjam_txt_harga;
    private javax.swing.JTextField pinjam_txt_qty;
    private javax.swing.JTable tb_pinjam_data;
    private javax.swing.JTable tb_pinjam_keranjang;
    // End of variables declaration//GEN-END:variables
}
