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
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import models.m_customer;
import models.m_customer_result;

public class dao_customer {
    
    public String path = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\customer.json";
    
    public List<m_customer> readCustomer() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<m_customer> data = new ArrayList<m_customer>();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));
            m_customer_result rs = gson.fromJson(br, m_customer_result.class);

            if (rs != null) {
                for (m_customer k : rs.getCustomer()) {
                    String id = k.getIdCustomer();
                    String nama = k.getNama();
                    String ktp = k.getNoKtp();
                    String kontak = k.getKontak();
                    String alamat = k.getAlamat();
                    String user = k.getUsername();
                    String pass = k.getPassword();
                    String cat = k.getCreatedAt();
                    String eat = k.getEditedAt();

                    data.add(new m_customer(id, nama, ktp, kontak, alamat, user, pass, cat, eat));
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

    public boolean insertCustomer(m_customer gs) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<m_customer> data = new ArrayList<m_customer>();
        BufferedReader br = null;
        m_customer_result rs = null;
        int i = 1;

        try {
            String new_nama = gs.getNama();
            String new_ktp = gs.getNoKtp();
            String new_kontak = gs.getKontak();
            String new_alamat = gs.getAlamat();
            String new_user = gs.getUsername();
            String new_pass = md5(gs.getPassword());
            String new_cat = gs.getCreatedAt();
            String new_eat = gs.getEditedAt();

            if (Files.exists(Paths.get(path))) {
                br = new BufferedReader(new FileReader(path));
                rs = gson.fromJson(br, m_customer_result.class);

                if (rs != null) {
                    for (m_customer k : rs.getCustomer()) {
                        String id = k.getIdCustomer();
                        String nama = k.getNama();
                        String ktp = k.getNoKtp();
                        String kontak = k.getKontak();
                        String alamat = k.getAlamat();
                        String user = k.getUsername();
                        String pass = k.getPassword();
                        String cat = k.getCreatedAt();
                        String eat = k.getEditedAt();

                        data.add(new m_customer(id, nama, ktp, kontak, alamat, user, pass, cat, eat));

                        i = (Integer) Integer.parseInt(id.substring(1)) + 1;
                    }
                }
            } else {
                File files = new File(path);
                files.createNewFile();
            }

            data.add(new m_customer("C" + String.format("%04d", i), new_nama, new_ktp, new_kontak, new_alamat, new_user, new_pass, new_cat, new_eat));
            rs = new m_customer_result(data);

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

    public boolean editCustomer(m_customer gs) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));
            m_customer_result rs = gson.fromJson(br, m_customer_result.class);
            List<m_customer> data = new ArrayList<m_customer>();
            int i = 1;

            String param = gs.getIdCustomer();
            String new_nama = gs.getNama();
            String new_ktp = gs.getNoKtp();
            String new_kontak = gs.getKontak();
            String new_alamat = gs.getAlamat();
            String new_eat = gs.getEditedAt();

            if (rs != null) {
                for (m_customer k : rs.getCustomer()) {
                    if (k.getIdCustomer().equals(param)) {
                        String id = k.getIdCustomer();
                        data.add(new m_customer(id, new_nama, new_ktp, new_kontak, new_alamat, k.getUsername(), k.getPassword(), k.getCreatedAt(), new_eat));
                    } else {
                        data.add(new m_customer(k.getIdCustomer(), k.getNama(), k.getNoKtp(), k.getKontak(), k.getAlamat(), k.getUsername(), k.getPassword(), k.getCreatedAt(), k.getEditedAt()));
                    }

                    i = (Integer) Integer.parseInt(k.getIdCustomer().substring(1)) + 1;
                }
            }

            rs = new m_customer_result(data);
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
    
    public boolean resetKaryawan(m_customer gs) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));
            m_customer_result rs = gson.fromJson(br, m_customer_result.class);
            List<m_customer> data = new ArrayList<m_customer>();
            int i = 1;

            String param = gs.getIdCustomer();
            String new_user = gs.getUsername();
            String new_pass = md5(gs.getUsername());
            String new_eat = gs.getEditedAt();

            if (rs != null) {
                for (m_customer k : rs.getCustomer()) {
                    if (k.getIdCustomer().equals(param)) {
                        String id = k.getIdCustomer();
                        data.add(new m_customer(id, k.getNama(), k.getNoKtp(), k.getKontak(), k.getAlamat(), new_user, new_pass, k.getCreatedAt(), new_eat));
                    } else {
                        data.add(new m_customer(k.getIdCustomer(), k.getNama(), k.getNoKtp(), k.getKontak(), k.getAlamat(), k.getUsername(), k.getPassword(), k.getCreatedAt(), k.getEditedAt()));
                    }

                    i = (Integer) Integer.parseInt(k.getIdCustomer().substring(1)) + 1;
                }
            }

            rs = new m_customer_result(data);
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
    
    public boolean deleteKaryawan(m_customer gs) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));
            m_customer_result rs = gson.fromJson(br, m_customer_result.class);
            List<m_customer> data = new ArrayList<m_customer>();

            String param = gs.getIdCustomer();
            
            if (rs != null) {
                for (m_customer k : rs.getCustomer()) {
                    if (!k.getIdCustomer().equals(param)) {
                        String id = k.getIdCustomer();
                        String nama = k.getNama();
                        String ktp = k.getNoKtp();
                        String kontak = k.getKontak();
                        String alamat = k.getAlamat();
                        String user = k.getUsername();
                        String pass = k.getPassword();
                        String cat = k.getCreatedAt();
                        String eat = k.getEditedAt();

                        data.add(new m_customer(id, nama, ktp, kontak, alamat, user, pass, cat, eat));
                    }
                }
            }

            rs = new m_customer_result(data);
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
}