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
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import models.m_karyawan;
import models.m_karyawan_result;

public class dao_karyawan {

    public String path = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\karyawan.json";

    public List<m_karyawan> login(m_karyawan gs) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<m_karyawan> data = new ArrayList<m_karyawan>();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));
            m_karyawan_result rs = gson.fromJson(br, m_karyawan_result.class);

            String user = gs.getUsername();
            String pass = md5(gs.getPassword());

            if (rs != null) {
                for (m_karyawan k : rs.getKaryawan()) {
                    if (k.getUsername().equals(user) && k.getPassword().equals(pass)) {
                        String id = k.getIdKaryawan();
                        String nama = k.getNama();
                        String ktp = k.getNoKtp();
                        String kontak = k.getKontak();
                        String alamat = k.getAlamat();
                        String level = k.getLevel();
                        String cat = k.getCreatedAt();
                        String eat = k.getEditedAt();

                        data.add(new m_karyawan(id, nama, ktp, kontak, alamat, level, user, pass, cat, eat));
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

    public List<m_karyawan> readKaryawan() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<m_karyawan> data = new ArrayList<m_karyawan>();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));
            m_karyawan_result rs = gson.fromJson(br, m_karyawan_result.class);

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

    public boolean insertKaryawan(m_karyawan gs) throws IOException {
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

    public boolean editKaryawan(m_karyawan gs) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));
            m_karyawan_result rs = gson.fromJson(br, m_karyawan_result.class);
            List<m_karyawan> data = new ArrayList<m_karyawan>();
            int i = 1;

            String param = gs.getIdKaryawan();
            String new_nama = gs.getNama();
            String new_ktp = gs.getNoKtp();
            String new_kontak = gs.getKontak();
            String new_alamat = gs.getAlamat();
            String new_level = gs.getLevel();
            String new_eat = gs.getEditedAt();

            if (rs != null) {
                for (m_karyawan k : rs.getKaryawan()) {
                    if (k.getIdKaryawan().equals(param)) {
                        String id = k.getIdKaryawan();
                        data.add(new m_karyawan(id, new_nama, new_ktp, new_kontak, new_alamat, new_level, k.getUsername(), k.getPassword(), k.getCreatedAt(), new_eat));
                    } else {
                        data.add(new m_karyawan(k.getIdKaryawan(), k.getNama(), k.getNoKtp(), k.getKontak(), k.getAlamat(), k.getLevel(), k.getUsername(), k.getPassword(), k.getCreatedAt(), k.getEditedAt()));
                    }

                    i = (Integer) Integer.parseInt(k.getIdKaryawan().substring(1)) + 1;
                }
            }

            rs = new m_karyawan_result(data);
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
    
    public boolean editProfile(m_karyawan gs) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));
            m_karyawan_result rs = gson.fromJson(br, m_karyawan_result.class);
            List<m_karyawan> data = new ArrayList<m_karyawan>();
            int i = 1;

            String param = gs.getIdKaryawan();
            String new_nama = gs.getNama();
            String new_kontak = gs.getKontak();
            String new_alamat = gs.getAlamat();
            String new_eat = gs.getEditedAt();

            if (rs != null) {
                for (m_karyawan k : rs.getKaryawan()) {
                    if (k.getIdKaryawan().equals(param)) {
                        String id = k.getIdKaryawan();
                        data.add(new m_karyawan(id, new_nama, k.getNoKtp(), new_kontak, new_alamat, k.getLevel(), k.getUsername(), k.getPassword(), k.getCreatedAt(), new_eat));
                    } else {
                        data.add(new m_karyawan(k.getIdKaryawan(), k.getNama(), k.getNoKtp(), k.getKontak(), k.getAlamat(), k.getLevel(), k.getUsername(), k.getPassword(), k.getCreatedAt(), k.getEditedAt()));
                    }

                    i = (Integer) Integer.parseInt(k.getIdKaryawan().substring(1)) + 1;
                }
            }

            rs = new m_karyawan_result(data);
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

    public boolean resetKaryawan(m_karyawan gs) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));
            m_karyawan_result rs = gson.fromJson(br, m_karyawan_result.class);
            List<m_karyawan> data = new ArrayList<m_karyawan>();
            int i = 1;

            String param = gs.getIdKaryawan();
            String new_user = gs.getUsername();
            String new_pass = md5(gs.getUsername());
            String new_eat = gs.getEditedAt();

            if (rs != null) {
                for (m_karyawan k : rs.getKaryawan()) {
                    if (k.getIdKaryawan().equals(param)) {
                        String id = k.getIdKaryawan();
                        data.add(new m_karyawan(id, k.getNama(), k.getNoKtp(), k.getKontak(), k.getAlamat(), k.getLevel(), new_user, new_pass, k.getCreatedAt(), new_eat));
                    } else {
                        data.add(new m_karyawan(k.getIdKaryawan(), k.getNama(), k.getNoKtp(), k.getKontak(), k.getAlamat(), k.getLevel(), k.getUsername(), k.getPassword(), k.getCreatedAt(), k.getEditedAt()));
                    }

                    i = (Integer) Integer.parseInt(k.getIdKaryawan().substring(1)) + 1;
                }
            }

            rs = new m_karyawan_result(data);
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

    public boolean deleteKaryawan(m_karyawan gs) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));
            m_karyawan_result rs = gson.fromJson(br, m_karyawan_result.class);
            List<m_karyawan> data = new ArrayList<m_karyawan>();

            String param = gs.getIdKaryawan();

            if (rs != null) {
                for (m_karyawan k : rs.getKaryawan()) {
                    if (!k.getIdKaryawan().equals(param)) {
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
                    }
                }
            }

            rs = new m_karyawan_result(data);
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

    public Boolean ganti_password(m_karyawan gs, String now) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));
            m_karyawan_result rs = gson.fromJson(br, m_karyawan_result.class);
            List<m_karyawan> data = new ArrayList<m_karyawan>();

            String param = gs.getIdKaryawan();
            String oldPass = md5(gs.getPassword());
            String newPass = md5(now);
            String newDate = gs.getEditedAt();

            if (rs != null) {
                for (m_karyawan k : rs.getKaryawan()) {
                    if (k.getIdKaryawan().equals(param) && k.getPassword().equals(oldPass)) {
                        String id = k.getIdKaryawan();
                        String nama = k.getNama();
                        String ktp = k.getNoKtp();
                        String kontak = k.getKontak();
                        String alamat = k.getAlamat();
                        String level = k.getLevel();
                        String user = k.getUsername();
                        String cat = k.getCreatedAt();

                        data.add(new m_karyawan(id, nama, ktp, kontak, alamat, level, user, newPass, cat, newDate));
                    }
                }
            }

            rs = new m_karyawan_result(data);
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
