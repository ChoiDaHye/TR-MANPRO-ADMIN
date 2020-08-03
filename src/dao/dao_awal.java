package dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import models.m_karyawan;
import models.m_karyawan_result;
import models.m_harga;
import models.m_harga_result;

public class dao_awal {

    public boolean cek_induk() {
        File induk = new File(System.getProperty("user.home") + "\\Documents\\vcd-data\\");
        boolean ada_induk = induk.exists();
        boolean cek = true;

        if (!ada_induk) {
            induk.mkdirs();
            cek = false;
        }

        cek_data();
        cek_laporan();
        cek_struk();

        return cek;
    }

    public void cek_data() {
        File data = new File(System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\");
        boolean ada_data = data.exists();

        if (!ada_data) {
            data.mkdirs();
        }
    }

    public void cek_laporan() {
        File laporan = new File(System.getProperty("user.home") + "\\Documents\\vcd-data\\laporan\\");
        boolean ada_laporan = laporan.exists();

        if (!ada_laporan) {
            laporan.mkdirs();
        }

        cek_laporan_peminjaman();
        cek_laporan_pengembalian();
        cek_laporan_restock();
        cek_laporan_vcd();
    }

    public void cek_laporan_peminjaman() {
        File laporan_peminjaman = new File(System.getProperty("user.home") + "\\Documents\\vcd-data\\laporan\\peminjaman\\");
        boolean ada_laporan_peminjaman = laporan_peminjaman.exists();

        if (!ada_laporan_peminjaman) {
            laporan_peminjaman.mkdirs();
        }
    }

    public void cek_laporan_pengembalian() {
        File laporan_pengembalian = new File(System.getProperty("user.home") + "\\Documents\\vcd-data\\laporan\\pengembalian\\");
        boolean ada_laporan_pengembalian = laporan_pengembalian.exists();

        if (!ada_laporan_pengembalian) {
            laporan_pengembalian.mkdirs();
        }
    }

    public void cek_laporan_restock() {
        File laporan_restock = new File(System.getProperty("user.home") + "\\Documents\\vcd-data\\laporan\\restock\\");
        boolean ada_laporan_restock = laporan_restock.exists();

        if (!ada_laporan_restock) {
            laporan_restock.mkdirs();
        }
    }

    public void cek_laporan_vcd() {
        File laporan_vcd = new File(System.getProperty("user.home") + "\\Documents\\vcd-data\\laporan\\vcd\\");
        boolean ada_laporan_vcd = laporan_vcd.exists();

        if (!ada_laporan_vcd) {
            laporan_vcd.mkdirs();
        }
    }

    public void cek_struk() {
        File struk = new File(System.getProperty("user.home") + "\\Documents\\vcd-data\\struk\\");
        boolean ada_struk = struk.exists();

        if (!ada_struk) {
            struk.mkdirs();
        }

        cek_struk_peminjaman();
        cek_struk_pengembalian();
        cek_struk_customer();
        cek_struk_karyawan();
    }

    public void cek_struk_peminjaman() {
        File struk_peminjaman = new File(System.getProperty("user.home") + "\\Documents\\vcd-data\\struk\\peminjaman");
        boolean ada_struk_peminjaman = struk_peminjaman.exists();

        if (!ada_struk_peminjaman) {
            struk_peminjaman.mkdirs();
        }
    }

    public void cek_struk_pengembalian() {
        File struk_pengembalian = new File(System.getProperty("user.home") + "\\Documents\\vcd-data\\struk\\pengembalian\\");
        boolean ada_struk_pengembalian = struk_pengembalian.exists();

        if (!ada_struk_pengembalian) {
            struk_pengembalian.mkdirs();
        }
    }

    public void cek_struk_customer() {
        File struk_customer = new File(System.getProperty("user.home") + "\\Documents\\vcd-data\\struk\\customer\\");
        boolean ada_struk_customer = struk_customer.exists();

        if (!ada_struk_customer) {
            struk_customer.mkdirs();
        }

        cek_struk_customer_account();
        cek_struk_customer_reset();
    }

    public void cek_struk_customer_account() {
        File struk_customer_account = new File(System.getProperty("user.home") + "\\Documents\\vcd-data\\struk\\customer\\account\\");
        boolean ada_struk_customer_account = struk_customer_account.exists();

        if (!ada_struk_customer_account) {
            struk_customer_account.mkdirs();
        }
    }

    public void cek_struk_customer_reset() {
        File struk_customer_reset = new File(System.getProperty("user.home") + "\\Documents\\vcd-data\\struk\\customer\\reset\\");
        boolean ada_struk_customer_reset = struk_customer_reset.exists();

        if (!ada_struk_customer_reset) {
            struk_customer_reset.mkdirs();
        }
    }

    public void cek_struk_karyawan() {
        File struk_karyawan = new File(System.getProperty("user.home") + "\\Documents\\vcd-data\\struk\\karyawan\\");
        boolean ada_struk_karyawan = struk_karyawan.exists();

        if (!ada_struk_karyawan) {
            struk_karyawan.mkdirs();
        }

        cek_struk_karyawan_account();
        cek_struk_karyawan_reset();
    }

    public void cek_struk_karyawan_account() {
        File struk_karyawan_account = new File(System.getProperty("user.home") + "\\Documents\\vcd-data\\struk\\karyawan\\account\\");
        boolean ada_struk_karyawan_account = struk_karyawan_account.exists();

        if (!ada_struk_karyawan_account) {
            struk_karyawan_account.mkdirs();
        }
    }

    public void cek_struk_karyawan_reset() {
        File struk_karyawan_reset = new File(System.getProperty("user.home") + "\\Documents\\vcd-data\\struk\\karyawan\\reset\\");
        boolean ada_struk_karyawan_reset = struk_karyawan_reset.exists();

        if (!ada_struk_karyawan_reset) {
            struk_karyawan_reset.mkdirs();
        }
    }

    public boolean buat_akun_awal(m_karyawan gs) throws IOException {
        String path = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\karyawan.json";    
        String sact = System.getProperty("user.home") + "\\Documents\\vcd-data\\struk\\karyawan\\account\\";
    
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<m_karyawan> data = new ArrayList<m_karyawan>();
        BufferedReader br = null;
        m_karyawan_result rs = null;
        int i = 1;

        try {
            String new_nama = gs.getNama();
            String new_ktp = gs.getNoKtp();
            String new_kontak = gs.getKontak();
            String new_alamat = gs.getAlamat();
            String new_level = gs.getLevel();
            String new_user = gs.getUsername();
            String new_pass = md5(gs.getPassword());
            String new_pass2 = gs.getPassword();
            String new_cat = gs.getCreatedAt();
            String new_eat = gs.getEditedAt();

            if (Files.exists(Paths.get(path))) {
                br = new BufferedReader(new FileReader(path));
                rs = gson.fromJson(br, m_karyawan_result.class);

                if (rs != null) {
                    for (m_karyawan k : rs.getKaryawan()) {
                        String id = k.getIdKaryawan();
                        String nama = k.getNama();
                        String ktp = k.getNoKtp();
                        String kontak = k.getKontak();
                        String alamat = k.getAlamat();
                        String level = k.getLevel();
                        String user = k.getUsername();
                        String pass = k.getPassword();
                        String cat = k.getCreatedAt();
                        String eat = k.getEditedAt();

                        data.add(new m_karyawan(id, nama, ktp, kontak, alamat, level, user, pass, cat, eat));

                        i = (Integer) Integer.parseInt(id.substring(1)) + 1;
                    }
                }
            } else {
                File files = new File(path);
                files.createNewFile();
            }

            data.add(new m_karyawan("K" + String.format("%04d", i), new_nama, new_ktp, new_kontak, new_alamat, new_level, new_user, new_pass, new_cat, new_eat));
            rs = new m_karyawan_result(data);

            try (Writer writer = new FileWriter(path)) {
                gson.toJson(rs, writer);
                struk(sact, new_user, new_pass2, new_cat, "K" + String.format("%04d", i));
            } catch (Exception e) {
            }
            
            return true;
        } catch (FileNotFoundException e) {
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }
        
        return false;
    }

    public boolean buat_harga_awal(m_harga gs) throws IOException {
        String path = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\harga.json";
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<m_harga> data = new ArrayList<m_harga>();
        BufferedReader br = null;
        m_harga_result rs = null;
        int i = 1;

        try {
            String new_nama = gs.getNama();
            float new_harga = gs.getHarga();

            if (Files.exists(Paths.get(path))) {
                br = new BufferedReader(new FileReader(path));
                rs = gson.fromJson(br, m_harga_result.class);

                if (rs != null) {
                    for (m_harga k : rs.getHarga()) {
                        String id = k.getIdHarga();
                        String nama = k.getNama();
                        float harga = k.getHarga();

                        data.add(new m_harga(id, nama, harga));

                        i = (Integer) Integer.parseInt(id.substring(1)) + 1;
                    }
                }
            } else {
                File files = new File(path);
                files.createNewFile();
            }

            data.add(new m_harga("H" + String.format("%04d", i), new_nama, new_harga));
            rs = new m_harga_result(data);

            try (Writer writer = new FileWriter(path)) {
                gson.toJson(rs, writer);
            } catch (Exception e) {
            }

            return true;
        } catch (FileNotFoundException e) {
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }

        return false;
    }
    
    public String md5(String teks) {
        String hashtext = "";

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(teks.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);

            hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
        } catch (NoSuchAlgorithmException e) {
        }

        return hashtext;
    }

    void struk(String pt, String u, String p, String d, String i){
        d = d.replace('/','-');
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(pt + i + " " + d + ".txt"));
            bw.write("==========================="); bw.newLine();
            bw.write("    HOME CINEMA ACCOUNT    "); bw.newLine();
            bw.write("==========================="); bw.newLine();
            bw.write("Username: " + u); bw.newLine();
            bw.write("Password: " + p); bw.newLine();
            bw.write("===========================");
            bw.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
