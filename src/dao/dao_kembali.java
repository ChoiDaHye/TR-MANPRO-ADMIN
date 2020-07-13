package dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import models.m_customer;
import models.m_customer_result;
import models.m_karyawan;
import models.m_karyawan_result;
import models.m_pinjam;
import models.m_pinjam_det;
import models.m_pinjam_det_result;
import models.m_pinjam_result;
import models.m_vcd;
import models.m_vcd_result;

public class dao_kembali {

    private String pinjam = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\pinjam.json";
    private String detl = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\pinjam_det.json";
    private String kary = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\karyawan.json";
    private String cust = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\customer.json";
    private String vcd = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\vcd.json";

    public List<m_pinjam> readPinjam() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<m_pinjam> data = new ArrayList<m_pinjam>();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(pinjam));
            m_pinjam_result rs = gson.fromJson(br, m_pinjam_result.class);

            if (rs != null) {
                for (m_pinjam k : rs.getPinjam()) {
                    if (k.getStatus().equals("Berjalan")) {
                        data.add(new m_pinjam(k.getId_pinjam(), k.getTgl_pinjam(), k.getJatuh_tempo(), k.getHarga_total(), k.getStatus(), k.getId_customer(), k.getId_karyawan()));
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

    public List<m_pinjam_det> readDetail(String param) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<m_pinjam_det> data = new ArrayList<m_pinjam_det>();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(detl));
            m_pinjam_det_result rs = gson.fromJson(br, m_pinjam_det_result.class);

            if (rs != null) {
                for (m_pinjam_det k : rs.getPinjam_det()) {
                    if (k.getId_pinjam().equals(param)) {
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
    
    public float getDtelat(){
        float denda = 0;
        
        return denda;
    }
    
    public float getDrusak(){
        float denda = 0;
        
        return denda;
    }    
}
