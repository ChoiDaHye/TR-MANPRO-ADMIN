package swing;

import dao.dao_kembali;
import dao.dao_pinjam;
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
import javax.swing.table.DefaultTableModel;
import models.m_kembali;
import models.m_kembali_det;
import models.m_keranjang_2;
import models.m_pinjam_det;

public class Trans_kembali extends javax.swing.JFrame {

    /**
     * Creates new form Trans_kembali
     */
    private String log_id, booking_id, id_vcd;
    private int cek;
    private float d_total, d_rusak, d_telat = 0;
    private List<m_keranjang_2> keranjang = new ArrayList<>();

    public Trans_kembali(String kary, String id) {
        initComponents();
        
        log_id = kary;
        booking_id = id;
        tampil();
    }
    
    public Trans_kembali() {
        initComponents();
    }

    void tampil(){
        try {
            dao_kembali dao = new dao_kembali();
            String data = dao.getCode();
            String date = LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            d_telat = dao.getDtelat(booking_id);
            d_rusak = dao.getDrusak();
            kembali_txt_booking.setText(booking_id);
            kembali_txt_code.setText(data);
            
            NumberFormat nf = NumberFormat.getInstance(new Locale("da", "DK"));
            kembali_txt_denda.setText(nf.format(d_telat));
            
            jLabel6.setText("<html>Jangan lupa minta kode booking dari pelanggan dan periksa kondisi VCD yang dipinjam ya<html>");

            tb_detail_pesanan();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal mendapatkan data\n" + e, "Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }

    void tb_detail_pesanan() {
        String id_pinjam = booking_id;

        Object[] baris = {"ID VCD", "JUDUL", "HARUS DIKEMBALIKAN"};
        DefaultTableModel dt = new DefaultTableModel(null, baris);
        tb_kembali.setModel(dt);
        dt.setRowCount(0);

        try {
            dao_kembali dao = new dao_kembali();
            List<m_pinjam_det> data = dao.readDetail(id_pinjam);

            for (m_pinjam_det d : data) {
                int balik = d.getJumlah() - d.getKembali();
                if(balik > 0){
                    String[] isi = {d.getId_vcd(), dao.getJudul(d.getId_vcd()), Integer.toString(balik)};
                    dt.addRow(isi);
                }                
            }
        } catch (Exception e) {
        }
    }

    void tb_klik() {
        int row = tb_kembali.getSelectedRow();
        id_vcd = tb_kembali.getModel().getValueAt(row, 0).toString();
        int baik = Integer.parseInt(tb_kembali.getModel().getValueAt(row, 2).toString());
        int buruk = 0;
        
        cek = baik;
        kembali_txt_baik.setText(Integer.toString(baik));        
        kembali_txt_rusak.setText(Integer.toString(buruk));        
    }
    
    void tb_vcd_to_basket(String id, int bk, int rk) {
        try {
            dao_kembali dao = new dao_kembali();
            float sub = rk * d_rusak;
            keranjang.add(new m_keranjang_2(id, bk, rk, sub));
            
            tampil_keranjang();
            hit_total();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal mendapatkan data\n" + e, "Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    void tampil_keranjang() {
        try {
            DefaultTableModel model = (DefaultTableModel) tb_kembali_keranjang.getModel();
            model.setRowCount(0);
            NumberFormat nf = NumberFormat.getInstance(new Locale("da", "DK"));
            for (m_keranjang_2 k : keranjang) {
                String[] isi = {k.getId(), Integer.toString(k.getBaik()), Integer.toString(k.getBuruk()), nf.format(k.getSubdenda())};
                model.addRow(isi);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal mendapatkan data\n" + e, "Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    void hit_total(){
        d_total = 0;
        try {
            d_total += d_telat;
            for(m_keranjang_2 k : keranjang){
                d_total += k.getSubdenda();
            }
            
            NumberFormat nf = NumberFormat.getInstance(new Locale("da", "DK"));
            kembali_txt_denda.setText(nf.format(d_total));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal mendapatkan data\n" + e, "Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    void simpan_trans(String code, Float bayar){
        try {
            dao_kembali dao = new dao_kembali();
            m_kembali data1 = new m_kembali();
            String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            Boolean c1 = false, c2 = false;
            
            data1.setId_kembali(code);
            data1.setTgl_kembali(date);
            data1.setDenda_total(d_total);
            data1.setId_pinjam(booking_id);
            data1.setId_karyawan(log_id);
            
            if(dao.simpanTrans(data1)){
                c1 = true;
                for (m_keranjang_2 k : keranjang){
                    m_kembali_det data2 = new m_kembali_det();
                    
                    data2.setId_kembali(code);
                    data2.setId_vcd(k.getId());
                    data2.setJumlah(k.getBaik() + k.getBuruk());
                    data2.setKondisi_rusak(k.getBuruk());
                    data2.setDenda(k.getSubdenda());
                    
                    if (dao.simpanTransDet(data2, booking_id)) {
                        c2 = true;
                    }
                }
            }
            
            if (c1 && c2) {
                JOptionPane.showMessageDialog(null, "Transaksi berhasil!", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                JOptionPane.showMessageDialog(null, "Kembalian \nRp. " + (bayar - d_total), "Info", JOptionPane.INFORMATION_MESSAGE);
//                dao.struk(kb, by);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Maaf, transaksi gagal!", "Gagal", JOptionPane.ERROR_MESSAGE);
                System.out.println(c1);
                System.out.println(c2);
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

        pengembalian = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        kembali_txt_code = new javax.swing.JTextField();
        kembali_txt_baik = new javax.swing.JTextField();
        kembali_txt_rusak = new javax.swing.JTextField();
        kembali_txt_denda = new javax.swing.JTextField();
        kembali_txt_bayar = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        kembali_btn_proses1 = new javax.swing.JPanel();
        jLabel119 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        kembali_txt_booking = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tb_kembali = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_kembali_keranjang = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Transaksi pengembalian");
        setPreferredSize(new java.awt.Dimension(999, 686));
        setResizable(false);

        pengembalian.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel5.setText("Transaksi pengembalian");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(96, 96, 96));
        jLabel6.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        kembali_txt_code.setEditable(false);
        kembali_txt_code.setBackground(new java.awt.Color(244, 244, 244));
        kembali_txt_code.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        kembali_txt_code.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(96, 96, 96), 2));

        kembali_txt_baik.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        kembali_txt_baik.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(96, 96, 96), 2));
        kembali_txt_baik.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kembali_txt_baikKeyPressed(evt);
            }
        });

        kembali_txt_rusak.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        kembali_txt_rusak.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(96, 96, 96), 2));
        kembali_txt_rusak.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kembali_txt_rusakKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                kembali_txt_rusakKeyReleased(evt);
            }
        });

        kembali_txt_denda.setEditable(false);
        kembali_txt_denda.setBackground(new java.awt.Color(244, 244, 244));
        kembali_txt_denda.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        kembali_txt_denda.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        kembali_txt_denda.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(96, 96, 96), 2));

        kembali_txt_bayar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        kembali_txt_bayar.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        kembali_txt_bayar.setText("0");
        kembali_txt_bayar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(96, 96, 96), 2));
        kembali_txt_bayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kembali_txt_bayarKeyPressed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton2.setText("Proses");
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        kembali_btn_proses1.setBackground(new java.awt.Color(0, 120, 215));
        kembali_btn_proses1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        kembali_btn_proses1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                kembali_btn_proses1MouseClicked(evt);
            }
        });

        jLabel119.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel119.setForeground(new java.awt.Color(255, 255, 255));
        jLabel119.setText("Simpan transaksi");

        javax.swing.GroupLayout kembali_btn_proses1Layout = new javax.swing.GroupLayout(kembali_btn_proses1);
        kembali_btn_proses1.setLayout(kembali_btn_proses1Layout);
        kembali_btn_proses1Layout.setHorizontalGroup(
            kembali_btn_proses1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kembali_btn_proses1Layout.createSequentialGroup()
                .addContainerGap(64, Short.MAX_VALUE)
                .addComponent(jLabel119)
                .addContainerGap(65, Short.MAX_VALUE))
        );
        kembali_btn_proses1Layout.setVerticalGroup(
            kembali_btn_proses1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel119, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(93, 93, 93));
        jLabel1.setText("No. Transaksi");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(93, 93, 93));
        jLabel2.setText("Kondisi baik");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(93, 93, 93));
        jLabel4.setText("Kondisi buruk");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(93, 93, 93));
        jLabel7.setText("Total denda");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(93, 93, 93));
        jLabel8.setText("Harga bayar");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(51, 51, 51));
        jLabel9.setText("Data transaksi");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(51, 51, 51));
        jLabel10.setText("Keranjang dan pembayaran");

        kembali_txt_booking.setEditable(false);
        kembali_txt_booking.setBackground(new java.awt.Color(244, 244, 244));
        kembali_txt_booking.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        kembali_txt_booking.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(96, 96, 96), 2));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(93, 93, 93));
        jLabel3.setText("Kode pinjam");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(jLabel8)
                            .addComponent(jLabel7)
                            .addComponent(jLabel3))
                        .addGap(40, 40, 40)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(kembali_txt_bayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(kembali_txt_denda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2)
                            .addComponent(kembali_txt_rusak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(kembali_txt_baik, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(kembali_txt_code, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(kembali_btn_proses1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(kembali_txt_booking, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton2, kembali_btn_proses1, kembali_txt_baik, kembali_txt_bayar, kembali_txt_code, kembali_txt_denda, kembali_txt_rusak});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel9)
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1)
                    .addComponent(kembali_txt_code, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel3)
                    .addComponent(kembali_txt_booking, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(kembali_txt_baik, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel4)
                    .addComponent(kembali_txt_rusak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addComponent(jButton2)
                .addGap(35, 35, 35)
                .addComponent(jLabel10)
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel7)
                    .addComponent(kembali_txt_denda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel8)
                    .addComponent(kembali_txt_bayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addComponent(kembali_btn_proses1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton2, kembali_txt_baik, kembali_txt_bayar, kembali_txt_code, kembali_txt_denda, kembali_txt_rusak});

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        tb_kembali.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tb_kembali.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_kembaliMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(tb_kembali);

        tb_kembali_keranjang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID VCD", "BAIK", "RUSAK", "DENDA"
            }
        ));
        tb_kembali_keranjang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_kembali_keranjangMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tb_kembali_keranjang);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(110, 110, 110)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout pengembalianLayout = new javax.swing.GroupLayout(pengembalian);
        pengembalian.setLayout(pengembalianLayout);
        pengembalianLayout.setHorizontalGroup(
            pengembalianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pengembalianLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(pengembalianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pengembalianLayout.createSequentialGroup()
                        .addGroup(pengembalianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pengembalianLayout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45))))
        );
        pengembalianLayout.setVerticalGroup(
            pengembalianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pengembalianLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel5)
                .addGap(15, 15, 15)
                .addComponent(jLabel6)
                .addGap(20, 20, 20)
                .addGroup(pengembalianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1374, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addComponent(pengembalian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, 0)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1354, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addComponent(pengembalian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, 0)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tb_kembaliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_kembaliMouseClicked
        tb_klik();
    }//GEN-LAST:event_tb_kembaliMouseClicked

    private void kembali_txt_baikKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kembali_txt_baikKeyPressed
        acceptNUMBER(kembali_txt_baik);
    }//GEN-LAST:event_kembali_txt_baikKeyPressed

    private void kembali_txt_rusakKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kembali_txt_rusakKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_kembali_txt_rusakKeyReleased

    private void kembali_txt_rusakKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kembali_txt_rusakKeyPressed
        acceptNUMBER(kembali_txt_rusak);
    }//GEN-LAST:event_kembali_txt_rusakKeyPressed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if(!numberOrNot(kembali_txt_baik.getText())){
            kembali_txt_baik.requestFocus();
        } else if(!numberOrNot(kembali_txt_rusak.getText())){
            kembali_txt_rusak.requestFocus();
        } else{
            int baik = Integer.parseInt(kembali_txt_baik.getText());
            int buruk= Integer.parseInt(kembali_txt_rusak.getText());
            
            if((baik + buruk) > cek || (baik + buruk) <= 0){
                JOptionPane.showMessageDialog(null, "Jumlah baik dan buruk tidak diterima!", "Kesalahan", JOptionPane.ERROR_MESSAGE);
                kembali_txt_baik.setText(Integer.toString(cek));
                kembali_txt_rusak.setText("0");
                kembali_txt_baik.requestFocus();
            } else{
                tb_vcd_to_basket(id_vcd, baik, buruk);
                kembali_txt_rusak.setText("");
                kembali_txt_baik.setText("");
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void tb_kembali_keranjangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_kembali_keranjangMouseClicked
//        int row = tb_kembali_keranjang.getSelectedRow();
//        String vcd = tb_kembali_keranjang.getModel().getValueAt(row, 0).toString();
    }//GEN-LAST:event_tb_kembali_keranjangMouseClicked

    private void kembali_txt_bayarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kembali_txt_bayarKeyPressed
        acceptNUMBER(kembali_txt_bayar);
    }//GEN-LAST:event_kembali_txt_bayarKeyPressed

    private void kembali_btn_proses1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kembali_btn_proses1MouseClicked
        if(kembali_txt_bayar.getText().equals("") || !numberOrNot(kembali_txt_bayar.getText())){
            kembali_txt_bayar.requestFocus();
        } else if(Float.parseFloat(kembali_txt_bayar.getText()) < d_total){
            JOptionPane.showMessageDialog(null, "Nominal bayar tidak diterima!", "Kesalahan", JOptionPane.ERROR_MESSAGE);
            kembali_txt_bayar.setText("");
            kembali_txt_bayar.requestFocus();
        } else{
            simpan_trans(kembali_txt_code.getText(), Float.parseFloat(kembali_txt_bayar.getText()));
            
        }
    }//GEN-LAST:event_kembali_btn_proses1MouseClicked

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
            java.util.logging.Logger.getLogger(Trans_kembali.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Trans_kembali.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Trans_kembali.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Trans_kembali.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Trans_kembali().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel119;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JPanel kembali_btn_proses1;
    private javax.swing.JTextField kembali_txt_baik;
    private javax.swing.JTextField kembali_txt_bayar;
    private javax.swing.JTextField kembali_txt_booking;
    private javax.swing.JTextField kembali_txt_code;
    private javax.swing.JTextField kembali_txt_denda;
    private javax.swing.JTextField kembali_txt_rusak;
    private javax.swing.JPanel pengembalian;
    private javax.swing.JTable tb_kembali;
    private javax.swing.JTable tb_kembali_keranjang;
    // End of variables declaration//GEN-END:variables
}
