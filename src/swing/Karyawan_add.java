package swing;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import models.m_karyawan;
import dao.dao_karyawan;

public class Karyawan_add extends javax.swing.JFrame {

    /**
     * Creates new form Customer_add
     */
    public Karyawan_add() {
        initComponents();
        jPanel1.setFocusable(true);
    }
    
    void karyawan_tambah(String ktp, String nama, String kontak, String alamat, String level, String user, String pass){
        try {
            dao_karyawan dao = new dao_karyawan();
            m_karyawan data = new m_karyawan();
            String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            
            data.setNoKtp(ktp);
            data.setNama(nama);
            data.setKontak(kontak);
            data.setAlamat(alamat);
            data.setLevel(level);
            data.setUsername(user);
            data.setPassword(pass);
            data.setCreatedAt(date);
            data.setEditedAt(date);
            
            if(dao.insertKaryawan(data)){
                JOptionPane.showMessageDialog (null, "Data berhasil tersimpan!", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                
                regis_txt_ktp.setText("");
                regis_txt_nama.setText("");
                regis_txt_kontak.setText("");
                regis_txt_alamat.setText("");
                regis_cmb_level.setSelectedItem(1); 
                regis_txt_username.setText("");
                regis_txt_password.setText("");
            } else{
                JOptionPane.showMessageDialog(null, "Maaf, data gagal tersimpan!", "Gagal", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menyimpan data!\n" + e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        regis_txt_ktp = new javax.swing.JTextField();
        regis_txt_nama = new javax.swing.JTextField();
        regis_txt_alamat = new javax.swing.JTextField();
        regis_txt_username = new javax.swing.JTextField();
        regis_txt_password = new javax.swing.JPasswordField();
        regis_btn_simpan = new javax.swing.JPanel();
        jLabel73 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        regis_cmb_level = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        regis_txt_kontak = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Registrasi karyawan");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 51));
        jLabel1.setText("Registrasi karyawan");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(96, 96, 96));
        jLabel2.setText("Pastikan nomor KTP karyawan sudah benar");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setText("Data diri karyawan");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(93, 93, 93));
        jLabel4.setText("Nomor KTP");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(93, 93, 93));
        jLabel5.setText("Nama lengkap");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(93, 93, 93));
        jLabel7.setText("Alamat");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 51, 51));
        jLabel8.setText("Akun karyawan");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(93, 93, 93));
        jLabel9.setText("Username");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(93, 93, 93));
        jLabel10.setText("Password");

        regis_txt_ktp.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        regis_txt_ktp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(96, 96, 96), 2));

        regis_txt_nama.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        regis_txt_nama.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(96, 96, 96), 2));
        regis_txt_nama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regis_txt_namaActionPerformed(evt);
            }
        });

        regis_txt_alamat.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        regis_txt_alamat.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(96, 96, 96), 2));

        regis_txt_username.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        regis_txt_username.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(96, 96, 96), 2));

        regis_txt_password.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        regis_txt_password.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(96, 96, 96), 2));

        regis_btn_simpan.setBackground(new java.awt.Color(0, 120, 215));
        regis_btn_simpan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        regis_btn_simpan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                regis_btn_simpanMouseClicked(evt);
            }
        });

        jLabel73.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel73.setForeground(new java.awt.Color(255, 255, 255));
        jLabel73.setText("Simpan data");

        javax.swing.GroupLayout regis_btn_simpanLayout = new javax.swing.GroupLayout(regis_btn_simpan);
        regis_btn_simpan.setLayout(regis_btn_simpanLayout);
        regis_btn_simpanLayout.setHorizontalGroup(
            regis_btn_simpanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(regis_btn_simpanLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel73)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        regis_btn_simpanLayout.setVerticalGroup(
            regis_btn_simpanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel73, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
        );

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(93, 93, 93));
        jLabel11.setText("Level");

        regis_cmb_level.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Kasir", "Administrator" }));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(93, 93, 93));
        jLabel6.setText("Kontak");

        regis_txt_kontak.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        regis_txt_kontak.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(96, 96, 96), 2));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel7)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11)
                            .addComponent(jLabel6))
                        .addGap(63, 63, 63)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(regis_txt_password)
                            .addComponent(regis_txt_username)
                            .addComponent(regis_txt_alamat)
                            .addComponent(regis_txt_nama)
                            .addComponent(regis_txt_ktp, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                            .addComponent(regis_btn_simpan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(regis_cmb_level, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(regis_txt_kontak))))
                .addGap(40, 40, 40))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {regis_cmb_level, regis_txt_alamat, regis_txt_ktp, regis_txt_nama, regis_txt_password, regis_txt_username});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel1)
                .addGap(10, 10, 10)
                .addComponent(jLabel2)
                .addGap(20, 20, 20)
                .addComponent(jLabel3)
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel4)
                    .addComponent(regis_txt_ktp, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel5)
                    .addComponent(regis_txt_nama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel6)
                    .addComponent(regis_txt_kontak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(regis_txt_alamat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(regis_cmb_level, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addComponent(jLabel8)
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel9)
                    .addComponent(regis_txt_username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel10)
                    .addComponent(regis_txt_password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addComponent(regis_btn_simpan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {regis_cmb_level, regis_txt_alamat, regis_txt_kontak, regis_txt_ktp, regis_txt_nama, regis_txt_password, regis_txt_username});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void regis_txt_namaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regis_txt_namaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_regis_txt_namaActionPerformed

    private void regis_btn_simpanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_regis_btn_simpanMouseClicked
        String ktp = regis_txt_ktp.getText(), nama = regis_txt_nama.getText(), kontak = regis_txt_kontak.getText(), alamat = regis_txt_alamat.getText(), level = regis_cmb_level.getSelectedItem().toString(), user = regis_txt_username.getText(), pass = regis_txt_password.getText();

        if (ktp.isEmpty() || nama.isEmpty() || kontak.isEmpty() || alamat.isEmpty() || level.isEmpty() || user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mohon lengkapi data!", "Peringatan", JOptionPane.ERROR_MESSAGE);

            if (ktp.isEmpty()) {
                regis_txt_ktp.requestFocus();
            } else if (nama.isEmpty()) {
                regis_txt_nama.requestFocus();
            } else if (kontak.isEmpty()) {
                regis_txt_kontak.requestFocus();
            } else if (alamat.isEmpty()) {
                regis_txt_alamat.requestFocus();
            } else if (level.isEmpty()) {
                regis_cmb_level.requestFocus();
            } else if (user.isEmpty()) {
                regis_txt_username.requestFocus();
            } else if (pass.isEmpty()) {
                regis_txt_password.requestFocus();
            }
        } else {
            int selection = JOptionPane.showConfirmDialog(null, "Simpan data?", "Konfirmasi", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (selection == JOptionPane.OK_OPTION) {
                karyawan_tambah(ktp, nama, kontak, alamat, level, user, pass);
            }
        }
    }//GEN-LAST:event_regis_btn_simpanMouseClicked

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
            java.util.logging.Logger.getLogger(Karyawan_add.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Karyawan_add.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Karyawan_add.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Karyawan_add.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Karyawan_add().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel regis_btn_simpan;
    private javax.swing.JComboBox<String> regis_cmb_level;
    private javax.swing.JTextField regis_txt_alamat;
    private javax.swing.JTextField regis_txt_kontak;
    private javax.swing.JTextField regis_txt_ktp;
    private javax.swing.JTextField regis_txt_nama;
    private javax.swing.JPasswordField regis_txt_password;
    private javax.swing.JTextField regis_txt_username;
    // End of variables declaration//GEN-END:variables
}
