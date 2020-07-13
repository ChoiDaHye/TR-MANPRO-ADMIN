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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import models.m_customer;
import models.m_customer_result;
import models.m_harga;
import models.m_harga_result;
import models.m_karyawan;
import models.m_karyawan_result;
import models.m_pinjam;
import models.m_pinjam_det;
import models.m_pinjam_det_result;
import models.m_pinjam_result;
import models.m_vcd;
import models.m_vcd_result;

public class dao_pinjam {

    private String path = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\pinjam.json";
    private String detl = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\pinjam_det.json";
    private String kary = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\karyawan.json";
    private String cust = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\customer.json";
    private String vcd = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\vcd.json";
    private String harga = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\harga.json";
    private String spjm = System.getProperty("user.home") + "\\Documents\\vcd-data\\struk\\peminjaman\\";

    public boolean simpanTrans(m_pinjam gs) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<m_pinjam> data = new ArrayList<m_pinjam>();
        BufferedReader br = null;
        m_pinjam_result rs = null;

        try {
            String newid_pinjam = gs.getId_pinjam();
            String newstatus = gs.getStatus();
            String newid_customer = gs.getId_customer();
            String newid_karyawan = gs.getId_karyawan();
            String newtgl_pinjam = gs.getTgl_pinjam();
            String newjatuh_tempo = gs.getJatuh_tempo();
            float newharga_total = gs.getHarga_total();

            if (Files.exists(Paths.get(path))) {
                br = new BufferedReader(new FileReader(path));
                rs = gson.fromJson(br, m_pinjam_result.class);

                if (rs != null) {
                    for (m_pinjam k : rs.getPinjam()) {
                        data.add(new m_pinjam(k.getId_pinjam(), k.getTgl_pinjam(), k.getJatuh_tempo(), k.getHarga_total(), k.getStatus(), k.getId_customer(), k.getId_karyawan()));
                    }
                }
            } else {
                File files = new File(path);
                files.createNewFile();
            }

            data.add(new m_pinjam(newid_pinjam, newtgl_pinjam, newjatuh_tempo, newharga_total, newstatus, newid_customer, newid_karyawan));
            rs = new m_pinjam_result(data);

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

    public boolean simpanTransDet(m_pinjam_det gs) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<m_pinjam_det> data = new ArrayList<m_pinjam_det>();
        BufferedReader br = null;
        m_pinjam_det_result rs = null;

        try {
            String newdid_pinjam = gs.getId_pinjam();
            String newdid_vcd = gs.getId_vcd();
            float newdsubtotal = gs.getSubtotal();
            int newdjumlah = gs.getJumlah();
            int newdkembali = gs.getKembali();

            if (Files.exists(Paths.get(detl))) {
                br = new BufferedReader(new FileReader(detl));
                rs = gson.fromJson(br, m_pinjam_det_result.class);

                if (rs != null) {
                    for (m_pinjam_det k : rs.getPinjam_det()) {
                        data.add(new m_pinjam_det(k.getId_pinjam(), k.getId_vcd(), k.getJumlah(), k.getSubtotal(), k.getKembali()));
                    }
                }
            } else {
                File files = new File(detl);
                files.createNewFile();
            }

            data.add(new m_pinjam_det(newdid_pinjam, newdid_vcd, newdjumlah, newdsubtotal, newdkembali));
            rs = new m_pinjam_det_result(data);

            try (Writer writer = new FileWriter(detl)) {
                gson.toJson(rs, writer);
                updateVCD(newdid_vcd, newdjumlah);
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

    public boolean updateVCD(String id, int jm) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(vcd));
            m_vcd_result rs = gson.fromJson(br, m_vcd_result.class);
            List<m_vcd> data = new ArrayList<m_vcd>();
            int i = 1;

            if (rs != null) {
                for (m_vcd k : rs.getVcd()) {
                    if (k.getId_vcd().equals(id)) {
                        int newpj = k.getTerpinjam() + jm;
                        data.add(new m_vcd(id, k.getJudul(), k.getGenre(), k.getBahasa(), k.getPoster(), k.getId_harga(), k.getRilis(), k.getKondisi_baik(), k.getKondisi_buruk(), newpj));
                    } else {
                        data.add(new m_vcd(k.getId_vcd(), k.getJudul(), k.getGenre(), k.getBahasa(), k.getPoster(), k.getId_harga(), k.getRilis(), k.getKondisi_baik(), k.getKondisi_buruk(), k.getTerpinjam()));
                    }

                    i = (Integer) Integer.parseInt(k.getId_vcd().substring(1)) + 1;
                }
            }

            rs = new m_vcd_result(data);
            try (Writer writer = new FileWriter(vcd);) {
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

    public void struk(String param, float nom) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader br1 = null, br2 = null;
        float hrg = 0;

        try {
            br1 = new BufferedReader(new FileReader(path));
            br2 = new BufferedReader(new FileReader(detl));
            m_pinjam_result rs1 = gson.fromJson(br1, m_pinjam_result.class);
            m_pinjam_det_result rs2 = gson.fromJson(br2, m_pinjam_det_result.class);

            if (rs1 != null) {
                for (m_pinjam k : rs1.getPinjam()) {
                    if (k.getId_pinjam().equals(param)) {
                        BufferedWriter bw = new BufferedWriter(new FileWriter(spjm + param + ".txt"));

                        bw.write("###############################################");bw.newLine();
                        bw.write("                  Home Cinema                  ");bw.newLine();bw.newLine();
                        bw.write("Kode Booking : " + k.getId_pinjam());bw.newLine();
                        bw.write("Penyewa      : " + getCus(k.getId_customer()));bw.newLine();
                        bw.write("Karyawan     : " + getKar(k.getId_karyawan()));bw.newLine();
                        bw.write("Tanggal      : " + k.getTgl_pinjam().replace('/', '-'));bw.newLine();
                        bw.write("Jatuh Tempo  : " + k.getJatuh_tempo().replace('/', '-'));bw.newLine();
                        bw.write("===============================================");bw.newLine();
                        bw.write("ID VCD   QTY   SUBTOTAL   JUDUL");bw.newLine();
                        bw.write("===============================================");bw.newLine();
                        if (rs2 != null) {
                            for (m_pinjam_det l : rs2.getPinjam_det()) {
                                if (l.getId_pinjam().equals(k.getId_pinjam())) {
                                    bw.write(l.getId_vcd() + "     " + l.getJumlah() + "    " + l.getSubtotal() + "    " + getJudul(l.getId_vcd()));
                                    bw.newLine();
                                }
                            }
                        }
                        bw.write("===============================================");bw.newLine();
                        bw.write("TOTAL          " + k.getHarga_total());bw.newLine();
                        bw.write("BAYAR          " + nom);bw.newLine();
                        bw.write("KEMBALI        " + (nom - k.getHarga_total()));bw.newLine();bw.newLine();
                        bw.write("###############################################");
                        bw.close();                        
                    }
                }
            }
        } catch (FileNotFoundException e) {
        } finally {
            if (br1 != null || br2 != null) {
                try {
                    br1.close();
                    br2.close();
                } catch (IOException e) {
                }
            }

            if (br2 != null) {
                try {
                    br2.close();
                } catch (IOException e) {
                }
            }
        }
    }
    
    public String getCode() {
        String date1 = new SimpleDateFormat("ddMMyyyy").format(new Date());
        String date2 = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader br = null;
        int i = 1;

        try {
            br = new BufferedReader(new FileReader(path));
            m_pinjam_result rs = gson.fromJson(br, m_pinjam_result.class);

            if (rs != null) {
                for (m_pinjam k : rs.getPinjam()) {
                    if (k.getTgl_pinjam().equals(date2)) {
                        String id = k.getId_pinjam();
                        i = (Integer) Integer.parseInt(id.substring(10)) + 1;
                    }
                }
            }
        } catch (FileNotFoundException e) {
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }

        return "P" + date1 + "-" + String.format("%04d", i);
    }
    
    public String getCus(String param){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader br = null;
        String nama = "";

        try {
            br = new BufferedReader(new FileReader(cust));
            m_customer_result rs = gson.fromJson(br, m_customer_result.class);

            if (rs != null) {
                for (m_customer k : rs.getCustomer()) {
                    if (k.getIdCustomer().equals(param)) {
                        nama = k.getNama();
                    }
                }
            }
        } catch (FileNotFoundException e) {
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }

        return nama;
    }
    
    public String getKar(String param){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader br = null;
        String nama = "";

        try {
            br = new BufferedReader(new FileReader(kary));
            m_karyawan_result rs = gson.fromJson(br, m_karyawan_result.class);

            if (rs != null) {
                for (m_karyawan k : rs.getKaryawan()) {
                    if (k.getIdKaryawan().equals(param)) {
                        nama = k.getNama();
                    }
                }
            }
        } catch (FileNotFoundException e) {
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }

        return nama;
    }
    
    public String getJudul(String param){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader br = null;
        String nama = "";

        try {
            br = new BufferedReader(new FileReader(vcd));
            m_vcd_result rs = gson.fromJson(br, m_vcd_result.class);

            if (rs != null) {
                for (m_vcd k : rs.getVcd()) {
                    if (k.getId_vcd().equals(param)) {
                        nama = k.getJudul();
                    }
                }
            }
        } catch (FileNotFoundException e) {
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }

        return nama;
    }

    public float getHarga(String id) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader br1 = null, br2 = null;
        float hrg = 0;

        try {
            br1 = new BufferedReader(new FileReader(vcd));
            br2 = new BufferedReader(new FileReader(harga));
            m_vcd_result rs1 = gson.fromJson(br1, m_vcd_result.class);
            m_harga_result rs2 = gson.fromJson(br2, m_harga_result.class);

            if (rs1 != null) {
                for (m_vcd k : rs1.getVcd()) {
                    if (k.getId_vcd().equals(id)) {
                        if (rs2 != null) {
                            for (m_harga l : rs2.getHarga()) {
                                if (l.getIdHarga().equals(k.getId_harga())) {
                                    hrg = l.getHarga();
                                }
                            }
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
        } finally {
            if (br1 != null) {
                try {
                    br1.close();
                } catch (IOException e) {
                }
            }

            if (br2 != null) {
                try {
                    br2.close();
                } catch (IOException e) {
                }
            }
        }

        return hrg;
    }
    
    public List<m_pinjam> readPinjam() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<m_pinjam> data = new ArrayList<m_pinjam>();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));
            m_pinjam_result rs = gson.fromJson(br, m_pinjam_result.class);

            if (rs != null) {
                for (m_pinjam k : rs.getPinjam()) {
                    data.add(new m_pinjam(k.getId_pinjam(), k.getTgl_pinjam(), k.getJatuh_tempo(), k.getHarga_total(), k.getStatus(), k.getId_customer(), k.getId_karyawan()));
                }
            }
        } catch (FileNotFoundException e) {
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }

        return data;
    }
    
    public List<m_pinjam_det> readDetail(String param) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<m_pinjam_det> data = new ArrayList<m_pinjam_det>();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(detl));
            m_pinjam_det_result rs = gson.fromJson(br, m_pinjam_det_result.class);

            if (rs != null) {
                for (m_pinjam_det k : rs.getPinjam_det()) {
                    if(k.getId_pinjam().equals(param)){
                        data.add(new m_pinjam_det(k.getId_pinjam(), k.getId_vcd(), k.getJumlah(), k.getSubtotal(), k.getKembali()));
                    }
                }
            }
        } catch (FileNotFoundException e) {
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }

        return data;
    }

}
