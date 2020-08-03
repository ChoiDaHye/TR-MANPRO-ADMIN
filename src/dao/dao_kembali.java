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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import models.m_customer;
import models.m_customer_result;
import models.m_harga;
import models.m_harga_result;
import models.m_karyawan;
import models.m_karyawan_result;
import models.m_kembali;
import models.m_kembali_det;
import models.m_kembali_det_result;
import models.m_kembali_result;
import models.m_pinjam;
import models.m_pinjam_det;
import models.m_pinjam_det_result;
import models.m_pinjam_result;
import models.m_vcd;
import models.m_vcd_result;

public class dao_kembali {

    private String path = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\kembali.json";
    private String detl2 = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\kembali_det.json";
    private String detl = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\pinjam_det.json";
    private String pinjam = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\pinjam.json";
    private String kary = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\karyawan.json";
    private String cust = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\customer.json";
    private String vcd = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\vcd.json";
    private String hrg = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\harga.json";
    private String skbl = System.getProperty("user.home") + "\\Documents\\vcd-data\\struk\\pengembalian\\";

    public boolean simpanTrans(m_kembali gs) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<m_kembali> data = new ArrayList<m_kembali>();
        BufferedReader br = null;
        m_kembali_result rs = null;

        try {
            String newid_kembali = gs.getId_kembali();
            String newtgl_kembali = gs.getTgl_kembali();
            float newdenda_total = gs.getDenda_total();
            String newid_pinjam = gs.getId_pinjam();
            String newid_karyawan = gs.getId_karyawan();

            if (Files.exists(Paths.get(path))) {
                br = new BufferedReader(new FileReader(path));
                rs = gson.fromJson(br, m_kembali_result.class);

                if (rs != null) {
                    for (m_kembali k : rs.getKembali()) {
                        data.add(new m_kembali(k.getId_kembali(), k.getId_pinjam(), k.getId_karyawan(), k.getTgl_kembali(), k.getDenda_total()));
                    }
                }
            } else {
                File files = new File(path);
                files.createNewFile();
            }

            data.add(new m_kembali(newid_kembali, newid_pinjam, newid_karyawan, newtgl_kembali, newdenda_total));
            rs = new m_kembali_result(data);

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

    public boolean simpanTransDet(m_kembali_det gs, String id) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<m_kembali_det> data = new ArrayList<m_kembali_det>();
        BufferedReader br = null;
        m_kembali_det_result rs = null;

        try {
            String newid_kembali = gs.getId_kembali();
            String newid_vcd = gs.getId_vcd();
            int newjumlah = gs.getJumlah();
            int newrusak = gs.getKondisi_rusak();
            float newdenda = gs.getDenda();

            if (Files.exists(Paths.get(detl2))) {
                br = new BufferedReader(new FileReader(detl2));
                rs = gson.fromJson(br, m_kembali_det_result.class);

                if (rs != null) {
                    for (m_kembali_det k : rs.getKembali_det()) {
                        data.add(new m_kembali_det(k.getId_kembali(), k.getId_vcd(), k.getJumlah(), k.getKondisi_rusak(), k.getDenda()));
                    }
                }
            } else {
                File files = new File(detl2);
                files.createNewFile();
            }

            data.add(new m_kembali_det(newid_kembali, newid_vcd, newjumlah, newrusak, newdenda));
            rs = new m_kembali_det_result(data);

            try (Writer writer = new FileWriter(detl2)) {
                gson.toJson(rs, writer);
                updateVCD(newid_vcd, newjumlah, newrusak);
                updatePinjamDet(id, newid_vcd, newjumlah);
                updatePinjam(id);
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

    public boolean updateVCD(String id, int jm, int rk) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(vcd));
            m_vcd_result rs = gson.fromJson(br, m_vcd_result.class);
            List<m_vcd> data = new ArrayList<m_vcd>();

            if (rs != null) {
                for (m_vcd k : rs.getVcd()) {
                    if (k.getId_vcd().equals(id)) {
                        int newpj = k.getTerpinjam() - jm;
                        int newrk = k.getKondisi_buruk() + rk;
                        int newbg = k.getKondisi_baik() - rk;
                        data.add(new m_vcd(id, k.getJudul(), k.getGenre(), k.getBahasa(), k.getPoster(), k.getId_harga(), k.getRilis(), newbg, newrk, newpj));
                    } else {
                        data.add(new m_vcd(k.getId_vcd(), k.getJudul(), k.getGenre(), k.getBahasa(), k.getPoster(), k.getId_harga(), k.getRilis(), k.getKondisi_baik(), k.getKondisi_buruk(), k.getTerpinjam()));
                    }
                }
            }

            rs = new m_vcd_result(data);
            try (Writer writer = new FileWriter(vcd)) {
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

    public boolean updatePinjamDet(String id, String vcdku, int jumlah) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(detl));
            m_pinjam_det_result rs = gson.fromJson(br, m_pinjam_det_result.class);
            List<m_pinjam_det> data = new ArrayList<m_pinjam_det>();

            if (rs != null) {
                for (m_pinjam_det k : rs.getPinjam_det()) {
                    if (k.getId_pinjam().equals(id) && k.getId_vcd().equals(vcdku)) {
                        int kembali = k.getKembali() + jumlah;
                        data.add(new m_pinjam_det(k.getId_pinjam(), k.getId_vcd(), k.getJumlah(), k.getSubtotal(), kembali));
                    } else {
                        data.add(new m_pinjam_det(k.getId_pinjam(), k.getId_vcd(), k.getJumlah(), k.getSubtotal(), k.getKembali()));
                    }
                }
            }

            rs = new m_pinjam_det_result(data);
            try (Writer writer = new FileWriter(detl)) {
                gson.toJson(rs, writer);
            } catch (Exception e) {
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

        return false;
    }

    public boolean updatePinjam(String id) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader br = null;
        int j = 0, b = 0;

        try {
            br = new BufferedReader(new FileReader(detl));
            m_pinjam_det_result rs = gson.fromJson(br, m_pinjam_det_result.class);

            if (rs != null) {
                for (m_pinjam_det k : rs.getPinjam_det()) {
                    if (k.getId_pinjam().equals(id)) {
                        j++;
                        if (k.getJumlah() == k.getKembali()) {
                            b++;
                        }
                    }
                }
            }
            if (j == b && j != 0) {
                updateStatus(id);
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

    public boolean updateStatus(String id) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(pinjam));
            m_pinjam_result rs = gson.fromJson(br, m_pinjam_result.class);
            List<m_pinjam> data = new ArrayList<m_pinjam>();

            if (rs != null) {
                for (m_pinjam k : rs.getPinjam()) {
                    if (k.getId_pinjam().equals(id)) {
                        data.add(new m_pinjam(k.getId_pinjam(), k.getTgl_pinjam(), k.getJatuh_tempo(), k.getHarga_total(), "Selesai", k.getId_customer(), k.getId_karyawan()));
                    } else {
                        data.add(new m_pinjam(k.getId_pinjam(), k.getTgl_pinjam(), k.getJatuh_tempo(), k.getHarga_total(), k.getStatus(), k.getId_customer(), k.getId_karyawan()));
                    }
                }
            }
            rs = new m_pinjam_result(data);
            try (Writer writer = new FileWriter(pinjam)) {
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

    public List<m_pinjam> readPinjam() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<m_pinjam> data = new ArrayList<m_pinjam>();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(pinjam));
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

    public String getCode() {
        String date1 = new SimpleDateFormat("ddMMyyyy").format(new Date());
        String date2 = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader br = null;
        int i = 1;

        try {
            br = new BufferedReader(new FileReader(path));
            m_kembali_result rs = gson.fromJson(br, m_kembali_result.class);

            if (rs != null) {
                for (m_kembali k : rs.getKembali()) {
                    if (k.getTgl_kembali().equals(date2)) {
                        String id = k.getId_kembali();
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

        return "B" + date1 + "-" + String.format("%04d", i);
    }

    public String getCus(String param) {
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

    public String getKar(String param) {
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

    public String getJudul(String param) {
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

    public float getDtelat(String param) throws ParseException {
        float denda = 0;
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String tgl = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader br = null, br2 = null;

        try {
            br = new BufferedReader(new FileReader(pinjam));
            m_pinjam_result rs = gson.fromJson(br, m_pinjam_result.class);

            if (rs != null) {
                for (m_pinjam k : rs.getPinjam()) {
                    if (k.getId_pinjam().equals(param)) {
                        LocalDate date = LocalDate.parse(tgl, format);
                        LocalDate jtp = LocalDate.parse(k.getJatuh_tempo(), format);
                        long n = ChronoUnit.DAYS.between(date, jtp);
                        
                        if (n < 0) {
                            br2 = new BufferedReader(new FileReader(hrg));
                            m_harga_result rs2 = gson.fromJson(br2, m_harga_result.class);

                            if (rs2 != null) {
                                for (m_harga l : rs2.getHarga()) {
                                    if (l.getIdHarga().equals("H0002")) {
                                        denda = l.getHarga() * ((n * -1));                                        
                                    }
                                }
                            }
                        }
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

        return denda;
    }

    public float getDrusak() {
        float denda = 0;

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(hrg));
            m_harga_result rs = gson.fromJson(br, m_harga_result.class);

            if (rs != null) {
                for (m_harga l : rs.getHarga()) {
                    if (l.getIdHarga().equals("H0003")) {
                        denda = l.getHarga();
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

        return denda;
    }

    public void struk(String param, float nom) throws IOException, ParseException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader br1 = null, br2 = null;
        float hrg = 0;

        try {
            br1 = new BufferedReader(new FileReader(path));
            br2 = new BufferedReader(new FileReader(detl2));
            m_kembali_result rs1 = gson.fromJson(br1, m_kembali_result.class);
            m_kembali_det_result rs2 = gson.fromJson(br2, m_kembali_det_result.class);

            if (rs1 != null) {
                for (m_kembali k : rs1.getKembali()) {
                    if (k.getId_kembali().equals(param)) {
                        BufferedWriter bw = new BufferedWriter(new FileWriter(skbl + param + ".txt"));

                        bw.write("###############################################");
                        bw.newLine();
                        bw.write("                  Home Cinema                  ");
                        bw.newLine();
                        bw.newLine();
                        bw.write("No. Transaksi: " + k.getId_kembali());
                        bw.newLine();
                        bw.write("Kode Booking : " + k.getId_pinjam());
                        bw.newLine();
                        bw.write("Karyawan     : " + getKar(k.getId_karyawan()));
                        bw.newLine();
                        bw.write("Tanggal      : " + k.getTgl_kembali().replace('/', '-'));
                        bw.newLine();
                        bw.write("===============================================");
                        bw.newLine();
                        bw.write("ID VCD   QTY   RUSAK   SUBTOTAL   JUDUL");
                        bw.newLine();
                        bw.write("===============================================");
                        bw.newLine();
                        if (rs2 != null) {
                            for (m_kembali_det l : rs2.getKembali_det()) {
                                if (l.getId_kembali().equals(k.getId_kembali())) {
                                    bw.write(l.getId_vcd() + "     " + l.getJumlah() + "     " + l.getKondisi_rusak() + "     " + l.getDenda() + "    " + getJudul(l.getId_vcd()));
                                    bw.newLine();
                                }
                            }
                        }
                        bw.write("===============================================");
                        bw.newLine();
                        bw.write("TERLAMBAT             " + getDtelat(k.getId_pinjam()));
                        bw.newLine();
                        bw.write("===============================================");
                        bw.newLine();
                        bw.write("TOTAL                 " + k.getDenda_total());
                        bw.newLine();
                        bw.write("BAYAR                 " + nom);
                        bw.newLine();
                        bw.write("KEMBALI               " + (nom - k.getDenda_total()));
                        bw.newLine();
                        bw.newLine();
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
}
