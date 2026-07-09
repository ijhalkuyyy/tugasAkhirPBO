package program;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class App {

    static PesertaDAO pesertaDAO = new PesertaDAO();
    static KelasDAO kelasDAO = new KelasDAO();
    static JadwalDAO jadwaldao = new JadwalDAO();
    // static BookingDAO bookingDAO = new BookingDAO();


    public static void main(String[] args) {
        boolean running = true;
        do {
            int pilih = 0;
            boolean putar = true;
            do {
                try {
                    String inputMenu = JOptionPane.showInputDialog(
                        "MENU SISTEM\n\n" +
                        "========Peserta========\n" +
                        "1. Input Data Peserta\n" +
                        "2. Tampil Data Peserta\n" +
                        "3. Cari Data Peserta ID\n" +
                        "========Kelas========\n" +
                        "4. Input Kelas\n" +
                        "5. Tampil Data Kelas\n" +
                        "6. Ubah Harga Kelas\n" +
                        "7. Keluar\n\n" +
                        "========JADWAL========\n" +
                        "8. Input tambah Jadwal\n" +
                        "9. Tampil Data Jadwal\n" +
                        "10. Ubah Jadwal\n" +
                        "11. Keluar\n\n" +
                        "Pilihan : "
                    );

                    if (inputMenu == null) {
                        System.exit(0); 
                    }

                    pilih = Integer.parseInt(inputMenu);
                    if (pilih >= 1 && pilih <= 11) {
                        putar = false;
                    } else {
                        JOptionPane.showMessageDialog(null, "Masukkan pilihan 1-11.");
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Masukkan angka yang valid!");
                }
            } while (putar);

            switch(pilih) {
                case 1: inputDataPeserta(); break;
                case 2: tampilDataPeserta(); break;
                case 3: cariDataPesertaBerdasarkanID(); break;
                case 4: tambahKelas(); break;
                case 5: tampilDataKelas(); break;
                case 6: ubahHargaKelas(); break;
                case 7: running = false; break;
                case 8: inputDataJadwal(); break;
                case 9: tampilDataJadwal(); break;
                case 10: ubahJadwal(); break;
                case 11: running = false; break;
            }
        } while(running);
    }

    // MENU PESERTA
    static void tampilDataPeserta() {
        String dataPeserta = pesertaDAO.tampilSemuaPeserta();
        if(dataPeserta.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tidak ada data peserta.");
        }else {
            JTextArea textArea = new JTextArea(dataPeserta);
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); 
            textArea.setMargin(new Insets(10, 10, 10, 10));
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(600, 300));
            JOptionPane.showMessageDialog(null, scrollPane, "Data Kelas", JOptionPane.INFORMATION_MESSAGE);
            //tampilkanScroll(dataPeserta, "Data Peserta");
        }
    }

    static void inputDataPeserta() {
        String namaLengkap = inputNonEmpty("Masukkan Nama Lengkap");
        String namaPanggilan = inputNonEmpty("Masukkan Nama Panggilan");

        String noHp = "";
        do { 
            noHp =inputNonEmpty("Masukkan No HP");
            if(noHp == null || noHp.length() < 10 || noHp.length() > 13) {
                JOptionPane.showMessageDialog(null, "No HP harus memiliki panjang antara 10 hingga 13 digit.");
            }
        } while (noHp == null || noHp.length() < 10 || noHp.length() > 13);

        Peserta peserta = new Peserta(pesertaDAO.generateIdPeserta(), namaLengkap, namaPanggilan, noHp);
        pesertaDAO.tambahPeserta(peserta);
    }

    static void cariDataPesertaBerdasarkanID(){
        String id = inputNonEmpty("Masukkan ID Peserta");
        Peserta peserta = pesertaDAO.cariById(id);
        if (peserta != null) {
            JOptionPane.showMessageDialog(null, "Data Peserta:\n" +
                "ID: " + peserta.getIdPeserta() + "\n" +
                "Nama Lengkap: " + peserta.getNamaLengkap() + "\n" +
                "Nama Panggilan: " + peserta.getNamaPanggilan() + "\n" +
                "No HP: " + peserta.getNoHp());
        } else {
            JOptionPane.showMessageDialog(null, "Peserta tidak ditemukan.");
        }
    }

    // MENU KELAS
    static void tampilDataKelas() {
        String dataKelas = kelasDAO.tampilSemuaKelas();
        if(dataKelas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tidak ada data kelas.");
        }else {
           tampilkanScroll(dataKelas, "Data Kelas");
            // JTextArea textArea = new JTextArea(dataKelas);
            // textArea.setEditable(false);
        }
    }

    static void ubahHargaKelas() {
        String dataKelas = kelasDAO.tampilSemuaKelas();
        
        if (dataKelas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Belum ada data kelas yang bisa diubah harganya.");
            return;
        }

        JTextArea textArea = new JTextArea(dataKelas);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); 
        textArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 300));

        Object[] tampilanPesan = {
            "Daftar Kelas Tersedia:", 
            scrollPane, 
            "\nMasukkan ID Kelas yang harganya ingin diubah:"
        };

        String idKelas = JOptionPane.showInputDialog(null, tampilanPesan, "Ubah Harga Kelas", JOptionPane.PLAIN_MESSAGE);
        
        if (idKelas == null || idKelas.trim().isEmpty()) {
            return;
        }

        String hargaBaruStr = JOptionPane.showInputDialog("Masukkan Harga Baru untuk Kelas " + idKelas + ":");
        if (hargaBaruStr == null || hargaBaruStr.trim().isEmpty()) {
            return; 
        }

        try {
            int hargaBaru = Integer.parseInt(hargaBaruStr);
            boolean berhasil = kelasDAO.ubahHarga(idKelas, hargaBaru);
            
            if (berhasil) {
                JOptionPane.showMessageDialog(null, "Harga kelas " + idKelas + " berhasil diubah menjadi " + hargaBaru + ".");
            } else {
                JOptionPane.showMessageDialog(null, "Gagal mengubah harga! Pastikan ID Kelas (" + idKelas + ") benar dan ada di daftar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: Harga baru harus berupa angka!", "Input Tidak Valid", JOptionPane.ERROR_MESSAGE);
        }
    }

    static void tambahKelas() {
        String namaKelas = inputNonEmpty("Masukkan Nama Kelas");
        double harga =  inputDouble("Masukkan Harga Kelas");
        int kapasitas = inputInt("Masukkan Kapasitas Kelas");
        // JOptionPane.showInputDialog("Masukkan Harga Kelas:");
        // double harga = Double.parseDouble(hargaStr);
        // String kapasitasStr = JOptionPane.showInputDialog("Masukkan Kapasitas Kelas:");
        Kelas kelas = new Kelas(kelasDAO.generateId(namaKelas), namaKelas, harga, kapasitas);
        kelasDAO.tambahKelas(kelas);
    }

    // MENU JADWAL
    static void tampilDataJadwal() {
        String dataJadwal = jadwaldao.tampilSemuaJadwal();
        if(dataJadwal.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tidak ada data jadwal.");
        }else {
            JTextArea textArea = new JTextArea(dataJadwal);
            textArea.setEditable(false);
            JOptionPane.showMessageDialog(null, textArea, "Data Jadwal", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    static void inputDataJadwal() {
        List<Kelas> daftarKelas = kelasDAO.ambilSemuaKelasObjek();
        if (daftarKelas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Belum ada data kelas yang tersedia.");
            return;
        }

        List<String> opsiKelas = new ArrayList<>();
        for (Kelas kelas : daftarKelas) {
            opsiKelas.add(kelas.getIdKelas() + " - " + kelas.getNamaKelas());
        }

        JComboBox<String> comboBoxKelas = new JComboBox<>(opsiKelas.toArray(String[]::new));
        int pilih = JOptionPane.showConfirmDialog(
            null,
            comboBoxKelas,
            "Pilih Kelas",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (pilih != JOptionPane.OK_OPTION) {
            return;
        }

        String selectedValue = (String) comboBoxKelas.getSelectedItem();
        String idKelas = selectedValue != null ? selectedValue.split(" - ")[0] : "";
        Kelas kelas = kelasDAO.cariById(idKelas);
        if (kelas == null) {
            JOptionPane.showMessageDialog(null, "Kelas dengan ID tersebut tidak ditemukan.");
            return;
        }

        String tanggalMulai = inputNonEmpty("Masukkan Tanggal Mulai (YYYY-MM-DD):");
        String tanggalSelesai = inputNonEmpty("Masukkan Tanggal Selesai (YYYY-MM-DD):");
        int sesi = jadwaldao.generateSesi(kelas.getIdKelas());

        Jadwal jadwal = new Jadwal(jadwaldao.generateIdJadwal(), kelas, tanggalMulai, tanggalSelesai, sesi);
        jadwaldao.tambahJadwal(jadwal);
    }

    static void ubahJadwal() {
        List<String> daftarJadwal = jadwaldao.ambilSemuaJadwalDropdown();
        if (daftarJadwal.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Belum ada data jadwal yang bisa diubah.");
            return;
        }

        JComboBox<String> comboBoxJadwal = new JComboBox<>(daftarJadwal.toArray(String[]::new));
        int pilihJadwal = JOptionPane.showConfirmDialog(
            null,
            comboBoxJadwal,
            "Pilih Jadwal",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (pilihJadwal != JOptionPane.OK_OPTION) {
            return;
        }

        String selectedJadwal = (String) comboBoxJadwal.getSelectedItem();
        String idJadwal = selectedJadwal != null ? selectedJadwal.split(" - ")[0] : "";

        String[] opsiField = {"Tanggal Mulai", "Tanggal Selesai"};
        JComboBox<String> comboBoxField = new JComboBox<>(opsiField);
        int pilihField = JOptionPane.showConfirmDialog(
            null,
            comboBoxField,
            "Pilih Data yang ingin diubah",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (pilihField != JOptionPane.OK_OPTION) {
            return;
        }

        Object selectedField = comboBoxField.getSelectedItem();
        String fieldDb = selectedField != null && selectedField.equals("Tanggal Mulai")
                ? "tanggal_mulai"
                : "tanggal_selesai";
        String tanggalBaru = inputNonEmpty("Masukkan tanggal baru (YYYY-MM-DD):");

        if (tanggalBaru == null || tanggalBaru.trim().isEmpty()) {
            return;
        }

        boolean berhasil = jadwaldao.ubahJadwal(idJadwal, fieldDb, tanggalBaru);
        if (berhasil) {
            JOptionPane.showMessageDialog(null, "Jadwal berhasil diubah.");
        } else {
            JOptionPane.showMessageDialog(null, "Gagal mengubah jadwal.");
        }
    }

    public static void tampilkanScroll(String teks, String judul) {
        JTextArea textArea = new JTextArea(teks);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); 
        textArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        JOptionPane.showMessageDialog(null, scrollPane, judul, JOptionPane.INFORMATION_MESSAGE);
        // JTextArea area = new JTextArea(teks, 20, 80);
        // area.setFont(new java.awt.Font(java.awt.Font.MONOSPACED, java.awt.Font.PLAIN, 12));
        // area.setEditable(false);
        // area.setCaretPosition(0);
        // JScrollPane scroll = new JScrollPane(area);
        // scroll.setPreferredSize(new java.awt.Dimension(800, 300));
        // JOptionPane.showMessageDialog(null, scroll, judul, JOptionPane.INFORMATION_MESSAGE);
    }

    public static String inputNonEmpty(String message) {
        String input;
        do {
            input = JOptionPane.showInputDialog(message);
            if (input == null) System.exit(0);
        } while (input.trim().isEmpty());
        return input.trim();
    }

    public static double inputDouble(String message) {
        String input;
        double value = 0;
        boolean valid = false;
        do {
            input = inputNonEmpty(message);
            try {
                value = Double.parseDouble(input);
                valid = true;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Input harus angka. Silakan coba lagi.");
            }
        } while (!valid);
        return value;
    }

    public static int inputInt(String message) {
        String input;
        int value = 0;
        boolean valid = false;
        do {
            input = inputNonEmpty(message);
            try {
                value = Integer.parseInt(input);
                valid = true;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Input harus angka bulat. Silakan coba lagi.");
            }
        } while (!valid);
        return value;
    }
    







}

