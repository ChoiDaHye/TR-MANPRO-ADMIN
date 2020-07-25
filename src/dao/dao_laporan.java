package dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import models.m_customer;
import models.m_customer_result;
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
import models.m_restock;
import models.m_restock_result;
import models.m_vcd;
import models.m_vcd_result;

public class dao_laporan {

    public void lap_vcd() throws IOException, WriteException {
        //SET FILE AND DESTINATION        
        String date = new SimpleDateFormat("ddMMyyyy").format(new Date());
        String date2 = new SimpleDateFormat("dd-MMMM-yyyy").format(new Date());
        String random = UUID.randomUUID().toString();
        File f = new File(System.getProperty("user.home") + "\\Documents\\vcd-data\\laporan\\vcd\\" + date + " - VCD - " + random + ".xls");
        WritableWorkbook myexcel = Workbook.createWorkbook(f);
        WritableSheet mysheet = myexcel.createSheet(date2, 0);

        //SET HEADER
        Label h1 = new Label(0, 0, "ID VCD");
        Label h2 = new Label(1, 0, "JUDUL");
        Label h3 = new Label(2, 0, "GENRE");
        Label h4 = new Label(3, 0, "BAHASA");
        Label h5 = new Label(4, 0, "RILIS");
        Label h6 = new Label(5, 0, "JUMLAH");
        Label h7 = new Label(6, 0, "KONDISI BAIK");
        Label h8 = new Label(7, 0, "KONDISI BURUK");
        mysheet.addCell(h1);
        mysheet.addCell(h2);
        mysheet.addCell(h3);
        mysheet.addCell(h4);
        mysheet.addCell(h5);
        mysheet.addCell(h6);
        mysheet.addCell(h7);
        mysheet.addCell(h8);

        //SET DATA
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader br = null;
        try {
            String path = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\vcd.json";
            br = new BufferedReader(new FileReader(path));
            m_vcd_result rs = gson.fromJson(br, m_vcd_result.class);
            int i = 1;

            if (rs != null) {
                for (m_vcd k : rs.getVcd()) {
                    Label d1 = new Label(0, i, k.getId_vcd());
                    Label d2 = new Label(1, i, k.getJudul());
                    Label d3 = new Label(2, i, k.getGenre());
                    Label d4 = new Label(3, i, k.getBahasa());
                    Label d5 = new Label(4, i, k.getRilis());
                    Label d6 = new Label(5, i, Integer.toString(k.getKondisi_baik() + k.getKondisi_buruk()));
                    Label d7 = new Label(6, i, Integer.toString(k.getKondisi_baik()));
                    Label d8 = new Label(7, i, Integer.toString(k.getKondisi_buruk()));
                    mysheet.addCell(d1);
                    mysheet.addCell(d2);
                    mysheet.addCell(d3);
                    mysheet.addCell(d4);
                    mysheet.addCell(d5);
                    mysheet.addCell(d6);
                    mysheet.addCell(d7);
                    mysheet.addCell(d8);
                    i++;
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

        //EXECUTE FILE       
        myexcel.write();
        myexcel.close();
    }

    public void lap_restock() throws IOException, WriteException {
        //SET FILE AND DESTINATION        
        String date = new SimpleDateFormat("ddMMyyyy").format(new Date());
        String date2 = new SimpleDateFormat("dd-MMMM-yyyy").format(new Date());
        String random = UUID.randomUUID().toString();
        File f = new File(System.getProperty("user.home") + "\\Documents\\vcd-data\\laporan\\restock\\" + date + " - RESTOCK - " + random + ".xls");
        WritableWorkbook myexcel = Workbook.createWorkbook(f);
        WritableSheet mysheet = myexcel.createSheet(date2, 0);

        //SET HEADER
        Label h1 = new Label(0, 0, "ID RESTOCK");
        Label h2 = new Label(1, 0, "TANGGAL");
        Label h3 = new Label(2, 0, "ID VCD");
        Label h4 = new Label(3, 0, "JUDUL");
        Label h5 = new Label(4, 0, "ID KARYAWAN");
        Label h6 = new Label(5, 0, "KARYAWAN");
        Label h7 = new Label(6, 0, "JUMLAH STOCK");
        mysheet.addCell(h1);
        mysheet.addCell(h2);
        mysheet.addCell(h3);
        mysheet.addCell(h4);
        mysheet.addCell(h5);
        mysheet.addCell(h6);
        mysheet.addCell(h7);

        //SET DATA
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader br = null;
        try {
            String path = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\restock.json";
            br = new BufferedReader(new FileReader(path));
            m_restock_result rs = gson.fromJson(br, m_restock_result.class);
            int i = 1;

            if (rs != null) {
                for (m_restock k : rs.getRestock()) {
                    Label d1 = new Label(0, i, k.getId_restock());
                    Label d2 = new Label(1, i, k.getTanggal());
                    Label d3 = new Label(2, i, k.getId_vcd());
                    Label d4 = new Label(3, i, getJudul(k.getId_vcd()));
                    Label d5 = new Label(4, i, k.getId_karyawan());
                    Label d6 = new Label(5, i, getKar(k.getId_karyawan()));
                    Label d7 = new Label(6, i, Integer.toString(k.getJumlah()));
                    mysheet.addCell(d1);
                    mysheet.addCell(d2);
                    mysheet.addCell(d3);
                    mysheet.addCell(d4);
                    mysheet.addCell(d5);
                    mysheet.addCell(d6);
                    mysheet.addCell(d7);
                    i++;
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

        //EXECUTE FILE       
        myexcel.write();
        myexcel.close();
    }

    public void lap_pinjam() throws IOException, WriteException {
        //SET FILE AND DESTINATION        
        String date = new SimpleDateFormat("ddMMyyyy").format(new Date());
        String date2 = new SimpleDateFormat("dd-MMMM-yyyy").format(new Date());
        String random = UUID.randomUUID().toString();
        File f = new File(System.getProperty("user.home") + "\\Documents\\vcd-data\\laporan\\peminjaman\\" + date + " - PEMINJAMAN - " + random + ".xls");
        WritableWorkbook myexcel = Workbook.createWorkbook(f);
        WritableSheet mysheet = myexcel.createSheet(date2, 0);

        //SET HEADER
        Label h1 = new Label(0, 0, "ID PINJAM");
        Label h6 = new Label(1, 0, "TANGGAL");
        Label h2 = new Label(2, 0, "ID CUSTOMER");
        Label h3 = new Label(3, 0, "CUSTOMER");
        Label h4 = new Label(4, 0, "ID KARYAWAN");
        Label h5 = new Label(5, 0, "KARYAWAN");
        Label h7 = new Label(6, 0, "HARGA TOTAL");
        Label h8 = new Label(8, 0, "ID VCD");
        Label h9 = new Label(9, 0, "JUDUL");
        Label h10 = new Label(10, 0, "JUMLAH");
        Label h11 = new Label(11, 0, "SUB HARGA");
        mysheet.addCell(h1);
        mysheet.addCell(h2);
        mysheet.addCell(h3);
        mysheet.addCell(h4);
        mysheet.addCell(h5);
        mysheet.addCell(h6);
        mysheet.addCell(h7);
        mysheet.addCell(h8);
        mysheet.addCell(h9);
        mysheet.addCell(h10);
        mysheet.addCell(h11);

        //SET DATA
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader br = null;

        try {
            String path = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\pinjam.json";
            String path2 = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\pinjam_det.json";
            br = new BufferedReader(new FileReader(path));
            m_pinjam_result rs = gson.fromJson(br, m_pinjam_result.class);

            br = new BufferedReader(new FileReader(path2));
            m_pinjam_det_result rs2 = gson.fromJson(br, m_pinjam_det_result.class);
            int i = 1;

            if (rs != null) {
                for (m_pinjam k : rs.getPinjam()) {
                    Label d1 = new Label(0, i, k.getId_pinjam());
                    Label d2 = new Label(1, i, k.getTgl_pinjam());
                    Label d3 = new Label(2, i, k.getId_customer());
                    Label d4 = new Label(3, i, getCus(k.getId_customer()));
                    Label d5 = new Label(4, i, k.getId_karyawan());
                    Label d6 = new Label(5, i, getKar(k.getId_karyawan()));
                    Label d7 = new Label(6, i, Float.toString(k.getHarga_total()));
                    mysheet.addCell(d1);
                    mysheet.addCell(d2);
                    mysheet.addCell(d3);
                    mysheet.addCell(d4);
                    mysheet.addCell(d5);
                    mysheet.addCell(d6);
                    mysheet.addCell(d7);

                    if (rs2 != null) {
                        for (m_pinjam_det l : rs2.getPinjam_det()) {
                            if (k.getId_pinjam().equals(l.getId_pinjam())) {
                                Label d8 = new Label(8, i, l.getId_vcd());
                                Label d9 = new Label(9, i, getJudul(l.getId_vcd()));
                                Label d10 = new Label(10, i, Integer.toString(l.getJumlah()));
                                Label d11 = new Label(11, i, Float.toString(l.getSubtotal()));
                                mysheet.addCell(d8);
                                mysheet.addCell(d9);
                                mysheet.addCell(d10);
                                mysheet.addCell(d11);
                                i++;
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

        //EXECUTE FILE       
        myexcel.write();
        myexcel.close();
    }

    public void lap_kembali() throws IOException, WriteException {
        //SET FILE AND DESTINATION        
        String date = new SimpleDateFormat("ddMMyyyy").format(new Date());
        String date2 = new SimpleDateFormat("dd-MMMM-yyyy").format(new Date());
        String random = UUID.randomUUID().toString();
        File f = new File(System.getProperty("user.home") + "\\Documents\\vcd-data\\laporan\\pengembalian\\" + date + " - PENGEMBALIAN - " + random + ".xls");
        WritableWorkbook myexcel = Workbook.createWorkbook(f);
        WritableSheet mysheet = myexcel.createSheet(date2, 0);

        //SET HEADER
        Label h1 = new Label(0, 0, "ID KEMBALI");
        Label h2 = new Label(1, 0, "ID PINJAM");
        Label h3 = new Label(2, 0, "TANGGAL");
        Label h4 = new Label(3, 0, "ID KARYAWAN");
        Label h5 = new Label(4, 0, "KARYAWAN");
        Label h6 = new Label(5, 0, "DENDA TOTAL");
        Label h7 = new Label(7, 0, "ID VCD");
        Label h8 = new Label(8, 0, "JUDUL");
        Label h9 = new Label(9, 0, "JUMLAH");
        Label h10 = new Label(10, 0, "KONDISI RUSAK");
        Label h11 = new Label(11, 0, "SUB DENDA");
        mysheet.addCell(h1);
        mysheet.addCell(h2);
        mysheet.addCell(h3);
        mysheet.addCell(h4);
        mysheet.addCell(h5);
        mysheet.addCell(h6);
        mysheet.addCell(h7);
        mysheet.addCell(h8);
        mysheet.addCell(h9);
        mysheet.addCell(h10);
        mysheet.addCell(h11);

        //SET DATA
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader br = null;

        try {
            String path = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\kembali.json";
            String path2 = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\kembali_det.json";
            br = new BufferedReader(new FileReader(path));
            m_kembali_result rs = gson.fromJson(br, m_kembali_result.class);

            br = new BufferedReader(new FileReader(path2));
            m_kembali_det_result rs2 = gson.fromJson(br, m_kembali_det_result.class);
            int i = 1;

            if (rs != null) {
                for (m_kembali k : rs.getKembali()) {
                    Label d1 = new Label(0, i, k.getId_kembali());
                    Label d2 = new Label(1, i, k.getId_pinjam());
                    Label d3 = new Label(2, i, k.getTgl_kembali());
                    Label d4 = new Label(3, i, k.getId_karyawan());
                    Label d5 = new Label(4, i, getKar(k.getId_karyawan()));
                    Label d6 = new Label(5, i, Float.toString(k.getDenda_total()));
                    mysheet.addCell(d1);
                    mysheet.addCell(d2);
                    mysheet.addCell(d3);
                    mysheet.addCell(d4);
                    mysheet.addCell(d5);
                    mysheet.addCell(d6);

                    if (rs2 != null) {
                        for (m_kembali_det l : rs2.getKembali_det()) {
                            if (k.getId_kembali().equals(l.getId_kembali())) {
                                Label d7 = new Label(7, i, l.getId_vcd());
                                Label d8 = new Label(8, i, getJudul(l.getId_vcd()));
                                Label d9 = new Label(9, i, Integer.toString(l.getJumlah()));
                                Label d10 = new Label(10, i, Integer.toString(l.getKondisi_rusak()));
                                Label d11 = new Label(11, i, Float.toString(l.getDenda()));
                                mysheet.addCell(d7);
                                mysheet.addCell(d8);
                                mysheet.addCell(d9);
                                mysheet.addCell(d10);
                                mysheet.addCell(d11);
                                i++;
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

        //EXECUTE FILE       
        myexcel.write();
        myexcel.close();
    }

    public String[][] lap_trans(String bulan, String tahun) {
        //AMBIL SEMUA HARI SENIN
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate start = LocalDate.parse("01/" + bulan + "/" + tahun, formatter);
        LocalDate end = LocalDate.parse("01/" + bulan + "/" + tahun, formatter).with(TemporalAdjusters.lastDayOfMonth());

        List<LocalDate> senin = new ArrayList<>();
        LocalDate Senin = start.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));

        while (Senin.isBefore(end)) {
            senin.add(Senin);
            Senin = Senin.plusWeeks(1);
        }

        //AMBIL BANYAK MINGGU
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2020);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, 6);
        int minggu = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);

        String[][] total = new String[minggu][2];
        for (int i = 0; i < minggu; i++) {
            total[i][0] = "Minggu ke-" + (i + 1);
            total[i][1] = "0";
        }

        //GET DATA
        String path = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\pinjam.json";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));
            m_pinjam_result rs = gson.fromJson(br, m_pinjam_result.class);

            if (rs != null) {
                for (m_pinjam k : rs.getPinjam()) {
                    LocalDate ini = LocalDate.parse(k.getTgl_pinjam(), formatter);

                    if (ini.equals(start) || (ini.isAfter(start) && ini.isBefore(senin.get(0)))) {
                        int get = Integer.parseInt(total[0][1]);
                        get += 1;
                        total[0][1] = Integer.toString(get);
                    } else if (ini.equals(end)) {
                        int get = Integer.parseInt(total[minggu - 1][1]);
                        get += 1;
                        total[minggu - 1][1] = Integer.toString(get);
                    } else {
                        int i = 1;
                        for (LocalDate l : senin) {
                            if ((ini.isEqual(l) || ini.isAfter(l)) && ini.isBefore(l.plusWeeks(1))) {
                                int get = Integer.parseInt(total[i][1]);
                                get += 1;
                                total[i][1] = Integer.toString(get);
                            }
                            i++;
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

        return total;
    }

    public String getCus(String param) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader br = null;
        String nama = "";

        try {
            String cust = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\customer.json";
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
        String kary = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\karyawan.json";
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
        String vcd = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\vcd.json";
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

    public List<String> getTahun() {
        List<String> data = new ArrayList<>();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<String> selesai = new ArrayList<>();
        List<String> thn = new ArrayList<>();
        BufferedReader br = null, br2 = null;

        try {
            String path = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\pinjam.json";
            br = new BufferedReader(new FileReader(path));
            m_pinjam_result rs = gson.fromJson(br, m_pinjam_result.class);

            if (rs != null) {
                for (m_pinjam k : rs.getPinjam()) {
                    if (k.getStatus().equals("Selesai")) {
                        selesai.add(k.getId_pinjam());
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

        try {
            String path = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\kembali.json";
            br2 = new BufferedReader(new FileReader(path));
            m_kembali_result rs = gson.fromJson(br2, m_kembali_result.class);

            if (rs != null) {
                for (m_kembali k : rs.getKembali()) {
                    String tahun = k.getTgl_kembali().substring(6);

                    for (String l : selesai) {
                        if (k.getId_pinjam().equals(l)) {
                            thn.add(k.getTgl_kembali().substring(6));
                        }
                    }
                }
            }

            for (String k : thn) {
                if (data != null) {
                    boolean cp = false;
                    for (String l : data) {
                        if (l.equals(k)) {
                            cp = true;
                        }
                    }

                    if (!cp) {
                        data.add(k);
                    }
                } else {
                    data.add(k);
                }
            }
        } catch (FileNotFoundException e) {
        } finally {
            if (br2 != null) {
                try {
                    br2.close();
                } catch (IOException e) {
                }
            }
        }

        return data;
    }

    public void lap_pendapatan(String bulan, String tahun) throws IOException, WriteException {
        //SET FILE AND DESTINATION        
        String date = new SimpleDateFormat("ddMMyyyy").format(new Date());
        String date2 = new SimpleDateFormat("dd-MMMM-yyyy").format(new Date());
        String random = UUID.randomUUID().toString();
        File f = new File(System.getProperty("user.home") + "\\Documents\\vcd-data\\laporan\\pendapatan\\" + date + " - PENDAPATAN - " + random + ".xls");
        WritableWorkbook myexcel = Workbook.createWorkbook(f);
        WritableSheet mysheet = myexcel.createSheet(date2, 0);

        //SET HEADER
        Label h1 = new Label(0, 0, "ID TRANSAKSI");
        Label h2 = new Label(1, 0, "TANGGAL TRANSAKSI");
        Label h3 = new Label(2, 0, "HARGA");
        Label h4 = new Label(3, 0, "DENDA");
        Label h5 = new Label(4, 0, "TOTAL");
        mysheet.addCell(h1);
        mysheet.addCell(h2);
        mysheet.addCell(h3);
        mysheet.addCell(h4);
        mysheet.addCell(h5);

        //SET DATA
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader br = null;

        try {
            String path = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\pinjam.json";
            String path2 = System.getProperty("user.home") + "\\Documents\\vcd-data\\data\\kembali.json";
            br = new BufferedReader(new FileReader(path));
            m_pinjam_result rs = gson.fromJson(br, m_pinjam_result.class);

            br = new BufferedReader(new FileReader(path2));
            m_kembali_result rs2 = gson.fromJson(br, m_kembali_result.class);
            int i = 1;

            if (rs != null) {
                for (m_pinjam k : rs.getPinjam()) {
                    String tgl = bulan + "/" + tahun;
                    String k_tgl = k.getTgl_pinjam().substring(3);

                    if (k_tgl.equals(tgl)) {
                        float t_denda = 0;

                        if (rs2 != null) {
                            for (m_kembali l : rs2.getKembali()) {
                                if (l.getId_pinjam().equals(k.getId_pinjam())) {
                                    t_denda += l.getDenda_total();
                                }
                            }
                        }

                        Label d1 = new Label(0, i, k.getId_pinjam());
                        Label d2 = new Label(1, i, k.getTgl_pinjam());
                        Label d3 = new Label(2, i, Float.toString(k.getHarga_total()));
                        Label d4 = new Label(3, i, Float.toString(t_denda));
                        Label d5 = new Label(4, i, Float.toString(k.getHarga_total() + t_denda));
                        mysheet.addCell(d1);
                        mysheet.addCell(d2);
                        mysheet.addCell(d3);
                        mysheet.addCell(d4);
                        mysheet.addCell(d5);
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

        //EXECUTE FILE       
        myexcel.write();
        myexcel.close();
    }
}
