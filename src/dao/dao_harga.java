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
import java.util.ArrayList;
import java.util.List;
import models.m_harga;
import models.m_harga_result;

public class dao_harga {
    
    public String path = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\harga.json";

    public List<m_harga> readHarga() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<m_harga> data = new ArrayList<m_harga>();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));
            m_harga_result rs = gson.fromJson(br, m_harga_result.class);

            if (rs != null) {
                for (m_harga k : rs.getHarga()) {
                    String id = k.getIdHarga();
                    String nama = k.getNama();
                    float harga = k.getHarga();

                    data.add(new m_harga(id, nama, harga));
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

    public boolean insertHarga(m_harga gs) throws IOException {
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

    public boolean editHarga(m_harga gs) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));
            m_harga_result rs = gson.fromJson(br, m_harga_result.class);
            List<m_harga> data = new ArrayList<m_harga>();
            int i = 1;

            String param = gs.getIdHarga();
            String new_nama = gs.getNama();
            float new_harga = gs.getHarga();

            if (rs != null) {
                for (m_harga k : rs.getHarga()) {
                    if (k.getIdHarga().equals(param)) {
                        String id = k.getIdHarga();
                        data.add(new m_harga(id, new_nama, new_harga));
                    } else {
                        data.add(new m_harga(k.getIdHarga(), k.getNama(), k.getHarga()));
                    }

                    i = (Integer) Integer.parseInt(k.getIdHarga().substring(1)) + 1;
                }
            }

            rs = new m_harga_result(data);
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

    public boolean deleteHarga(m_harga gs) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));
            m_harga_result rs = gson.fromJson(br, m_harga_result.class);
            List<m_harga> data = new ArrayList<m_harga>();

            String param = gs.getIdHarga();
            
            if (rs != null) {
                for (m_harga k : rs.getHarga()) {
                    if (!k.getIdHarga().equals(param)) {
                        String id = k.getIdHarga();
                        String nama = k.getNama();
                        float harga = k.getHarga();

                        data.add(new m_harga(id, nama, harga));
                    }
                }
            }

            rs = new m_harga_result(data);
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