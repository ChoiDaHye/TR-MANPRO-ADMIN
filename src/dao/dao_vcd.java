package dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
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
import models.m_restock;
import models.m_restock_result;
import models.m_vcd;
import models.m_vcd_result;

public class dao_vcd {

    public String path = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\vcd.json";
    public String rest = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\restock.json";

    public boolean restockVcdNew(int newjumlah, String newvcd, String newkary) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<m_restock> data = new ArrayList<m_restock>();
        BufferedReader br = null;
        m_restock_result rs = null;
        int i = 1;

        try {
            String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

            if (Files.exists(Paths.get(rest))) {
                br = new BufferedReader(new FileReader(rest));
                rs = gson.fromJson(br, m_restock_result.class);

                if (rs != null) {
                    for (m_restock k : rs.getRestock()) {
                        String id = k.getId_vcd();
                        String tanggal = k.getTanggal();
                        int jumlah = k.getJumlah();
                        String vcd = k.getId_vcd();
                        String kary = k.getId_karyawan();

                        data.add(new m_restock(id, tanggal, vcd, kary, jumlah));
                        i = (Integer) Integer.parseInt(id.substring(1)) + 1;
                    }
                }
            } else {
                File files = new File(rest);
                files.createNewFile();
            }

            data.add(new m_restock("R" + String.format("%04d", i), date, newvcd, newkary, newjumlah));
            rs = new m_restock_result(data);

            try (Writer writer = new FileWriter(rest)) {
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

    public boolean insertRestock(m_restock gs) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<m_restock> data = new ArrayList<m_restock>();
        BufferedReader br = null;
        m_restock_result rs = null;
        int i = 1;

        try {
            String newid = gs.getId_vcd();
            String newka = gs.getId_karyawan();
            String newdt = gs.getTanggal();
            int newjm = gs.getJumlah();

            if (Files.exists(Paths.get(rest))) {
                br = new BufferedReader(new FileReader(rest));
                rs = gson.fromJson(br, m_restock_result.class);

                if (rs != null) {
                    for (m_restock k : rs.getRestock()) {
                        String id = k.getId_restock();
                        String iv = k.getId_vcd();
                        String ka = k.getId_karyawan();
                        String dt = k.getTanggal();
                        int jm = k.getJumlah();

                        data.add(new m_restock(id, dt, iv, ka, jm));

                        i = (Integer) Integer.parseInt(id.substring(1)) + 1;
                    }
                }
            } else {
                File files = new File(rest);
                files.createNewFile();
            }

            data.add(new m_restock("R" + String.format("%04d", i), newdt, newid, newka, newjm));
            rs = new m_restock_result(data);

            try (Writer writer = new FileWriter(rest)) {
                gson.toJson(rs, writer);
                
                dao_vcd daov = new dao_vcd();
                m_vcd datav = new m_vcd();
                
                datav.setId_vcd(newid);
                datav.setKondisi_baik(newjm);
                daov.updateRestock(datav);                        
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

    public boolean updateRestock(m_vcd gs) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));
            m_vcd_result rs = gson.fromJson(br, m_vcd_result.class);
            List<m_vcd> data = new ArrayList<m_vcd>();
            int i = 1;

            String param = gs.getId_vcd();
            int newjm = gs.getKondisi_baik();

            if (rs != null) {
                for (m_vcd k : rs.getVcd()) {
                    if (k.getId_vcd().equals(param)) {
                        String id = k.getId_vcd();
                        newjm += k.getKondisi_baik();
                        data.add(new m_vcd(id, k.getJudul(), k.getGenre(), k.getBahasa(), k.getPoster(), k.getId_harga(), k.getRilis(), newjm, k.getKondisi_buruk(), k.getTerpinjam()));
                    } else {
                        data.add(new m_vcd(k.getId_vcd(), k.getJudul(), k.getGenre(), k.getBahasa(), k.getPoster(), k.getId_harga(), k.getRilis(), k.getKondisi_baik(), k.getKondisi_buruk(), k.getTerpinjam()));
                    }

                    i = (Integer) Integer.parseInt(k.getId_vcd().substring(1)) + 1;
                }
            }

            rs = new m_vcd_result(data);
            try (Writer writer = new FileWriter(path);) {
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

    public List<m_vcd> readVcd() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<m_vcd> data = new ArrayList<m_vcd>();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));
            m_vcd_result rs = gson.fromJson(br, m_vcd_result.class);

            if (rs != null) {
                for (m_vcd k : rs.getVcd()) {
                    String id = k.getId_vcd();
                    String judul = k.getJudul();
                    String genre = k.getGenre();
                    String bahasa = k.getBahasa();
                    String poster = k.getPoster();
                    String id_harga = k.getId_harga();
                    String rilis = k.getRilis();
                    int baik = k.getKondisi_baik();
                    int buruk = k.getKondisi_buruk();
                    int pinjam = k.getTerpinjam();

                    data.add(new m_vcd(id, judul, genre, bahasa, poster, id_harga, rilis, baik, buruk, pinjam));
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

    public boolean insertVcd(m_vcd gs, String kary) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<m_vcd> data = new ArrayList<m_vcd>();
        BufferedReader br = null;
        m_vcd_result rs = null;
        int i = 1;

        try {
            String newid = gs.getId_vcd();
            String newjudul = gs.getJudul();
            String newgenre = gs.getGenre();
            String newbahasa = gs.getBahasa();
            String newposter = gs.getPoster();
            String newid_harga = gs.getId_harga();
            String newrilis = gs.getRilis();
            int newbaik = gs.getKondisi_baik();
            int newburuk = gs.getKondisi_buruk();
            int newpinjam = 0;

            if (Files.exists(Paths.get(path))) {
                br = new BufferedReader(new FileReader(path));
                rs = gson.fromJson(br, m_vcd_result.class);

                if (rs != null) {
                    for (m_vcd k : rs.getVcd()) {
                        String id = k.getId_vcd();
                        String judul = k.getJudul();
                        String genre = k.getGenre();
                        String bahasa = k.getBahasa();
                        String poster = k.getPoster();
                        String id_harga = k.getId_harga();
                        String rilis = k.getRilis();
                        int baik = k.getKondisi_baik();
                        int buruk = k.getKondisi_buruk();
                        int pinjam = k.getTerpinjam();

                        data.add(new m_vcd(id, judul, genre, bahasa, poster, id_harga, rilis, baik, buruk, pinjam));

                        i = (Integer) Integer.parseInt(id.substring(1)) + 1;
                    }
                }
            } else {
                File files = new File(path);
                files.createNewFile();
            }

            data.add(new m_vcd("V" + String.format("%04d", i), newjudul, newgenre, newbahasa, newposter, newid_harga, newrilis, newbaik, newburuk, newpinjam));
            rs = new m_vcd_result(data);

            try (Writer writer = new FileWriter(path)) {
                gson.toJson(rs, writer);
                restockVcdNew(newbaik, "V" + String.format("%04d", i), kary);
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

    public boolean editVcd(m_vcd gs) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));
            m_vcd_result rs = gson.fromJson(br, m_vcd_result.class);
            List<m_vcd> data = new ArrayList<m_vcd>();
            int i = 1;

            String param = gs.getId_vcd();
            String newjudul = gs.getJudul();
            String newgenre = gs.getGenre();
            String newbahasa = gs.getBahasa();
            String newposter = gs.getPoster();
            String newid_harga = gs.getId_harga();
            String newrilis = gs.getRilis();

            if (rs != null) {
                for (m_vcd k : rs.getVcd()) {
                    if (k.getId_vcd().equals(param)) {
                        String id = k.getId_vcd();
                        data.add(new m_vcd(id, newjudul, newgenre, newbahasa, newposter, newid_harga, newrilis, k.getKondisi_baik(), k.getKondisi_buruk(), k.getTerpinjam()));
                    } else {
                        data.add(new m_vcd(k.getId_vcd(), k.getJudul(), k.getGenre(), k.getBahasa(), k.getPoster(), k.getId_harga(), k.getRilis(), k.getKondisi_baik(), k.getKondisi_buruk(), k.getTerpinjam()));
                    }

                    i = (Integer) Integer.parseInt(k.getId_vcd().substring(1)) + 1;
                }
            }

            rs = new m_vcd_result(data);
            try (Writer writer = new FileWriter(path);) {
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

    public boolean deleteVcd(m_vcd gs) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));
            m_vcd_result rs = gson.fromJson(br, m_vcd_result.class);
            List<m_vcd> data = new ArrayList<m_vcd>();

            String param = gs.getId_vcd();

            if (rs != null) {
                for (m_vcd k : rs.getVcd()) {
                    if (!k.getId_vcd().equals(param)) {
                        String id = k.getId_vcd();
                        String judul = k.getJudul();
                        String genre = k.getGenre();
                        String bahasa = k.getBahasa();
                        String poster = k.getPoster();
                        String id_harga = k.getId_harga();
                        String rilis = k.getRilis();
                        int baik = k.getKondisi_baik();
                        int buruk = k.getKondisi_buruk();
                        int pinjam = k.getTerpinjam();

                        data.add(new m_vcd(id, judul, genre, bahasa, poster, id_harga, rilis, baik, buruk, pinjam));
                    }
                }
            }

            rs = new m_vcd_result(data);
            try (Writer writer = new FileWriter(path);) {
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
}
