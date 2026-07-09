package program;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class JadwalUI {

    JadwalDAO jadwaldao = new JadwalDAO();
    KelasUI kelasUI = new KelasUI();
    Validasi val =  new Validasi();

    public void tampilDataJadwal() {
        String dataJadwal = jadwaldao.tampilSemuaJadwal();
        if(dataJadwal.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tidak ada data jadwal.");
        } else {
            JTextArea textArea = new JTextArea(dataJadwal);
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); 
            textArea.setMargin(new Insets(10, 10, 10, 10));
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(800, 300));
            JOptionPane.showMessageDialog(null, scrollPane, "Data Kelas", JOptionPane.INFORMATION_MESSAGE);
            // JTextArea textArea = new JTextArea(dataJadwal);
            // textArea.setEditable(false);
            // JOptionPane.showMessageDialog(null, textArea, "Data Jadwal", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void inputDataJadwal() {
        List<Kelas> daftarKelas = kelasUI.kelasDAO.ambilSemuaKelasObjek();
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
        Kelas kelas = kelasUI.kelasDAO.cariById(idKelas);
        if (kelas == null) {
            JOptionPane.showMessageDialog(null, "Kelas dengan ID tersebut tidak ditemukan.");
            return;
        }

        // --- VALIDASI TANGGAL MULAI ---
        LocalDate dateMulai = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate hariIni = LocalDate.now();

        while (true) {
            String inputMulai = val.inputNonEmpty("Masukkan Tanggal Mulai (YYYY-MM-DD):");
            if (inputMulai == null) return;
            
            try {
                dateMulai = LocalDate.parse(inputMulai, formatter);
                if (dateMulai.isBefore(hariIni)) {
                    JOptionPane.showMessageDialog(null, "Tanggal Mulai tidak boleh kurang dari hari ini!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                } else {
                    break;
                }
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(null, "Format salah! Gunakan format YYYY-MM-DD\nContoh: 2023-10-25", "Peringatan", JOptionPane.WARNING_MESSAGE);
            }
        }

        LocalDate dateSelesai = null;
        while (true) {
            String inputSelesai = val.inputNonEmpty("Masukkan Tanggal Selesai (YYYY-MM-DD):");
            if (inputSelesai == null) return;
            
            try {
                dateSelesai = LocalDate.parse(inputSelesai, formatter);
                
                // Cek apakah tanggal selesai lebih dulu dari tanggal mulai
                if (dateSelesai.isBefore(dateMulai)) {
                    JOptionPane.showMessageDialog(null, "Tanggal Selesai tidak boleh kurang dari Tanggal Mulai!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                } else {
                    break;
                }
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(null, "Format salah! Gunakan format YYYY-MM-DD\nContoh: 2023-10-25", "Peringatan", JOptionPane.WARNING_MESSAGE);
            }
        }

        String tanggalMulai = dateMulai.toString();
        String tanggalSelesai = dateSelesai.toString();

        int sesi = jadwaldao.generateSesi(kelas.getIdKelas());
        jadwaldao.nonaktifkanJadwalLama(kelas.getIdKelas());

        String statusBaru = "A";
        Jadwal jadwal = new Jadwal(jadwaldao.generateIdJadwal(), kelas, tanggalMulai, tanggalSelesai, sesi);
        jadwal.setStatus(statusBaru);
        jadwaldao.tambahJadwal(jadwal);
        JOptionPane.showMessageDialog(null, "Jadwal berhasil ditambahkan!");
    }

    public void ubahStatusJadwalMenjadiNonaktif() {
        List<Jadwal> daftarJadwalAktif = jadwaldao.ambilJadwalAktifObjek();
        if (daftarJadwalAktif.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tidak ada jadwal aktif yang bisa dinonaktifkan.");
            return;
        }

        JComboBox<String> comboBoxJadwal = new JComboBox<>();
        for (Jadwal jadwal : daftarJadwalAktif) {
            comboBoxJadwal.addItem(
                    jadwal.getIdJadwal() + " - " + jadwal.getKelas().getNamaKelas() + " - Sesi " + jadwal.getSesiKe()
            );
        }

        int pilihJadwal = JOptionPane.showConfirmDialog(
                null,
                comboBoxJadwal,
                "Pilih Jadwal Aktif",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (pilihJadwal != JOptionPane.OK_OPTION) {
            return;
        }

        String selectedJadwal = (String) comboBoxJadwal.getSelectedItem();
        String idJadwal = selectedJadwal != null ? selectedJadwal.split(" - ")[0] : "";

        boolean berhasil = jadwaldao.ubahStatusJadwalMenjadiNonaktif(idJadwal);
        if (berhasil) {
            JOptionPane.showMessageDialog(null, "Jadwal berhasil dinonaktifkan.");
        } else {
            JOptionPane.showMessageDialog(null, "Gagal menonaktifkan jadwal.");
        }
    }

    public void ubahJadwal() {
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
                
        // --- VALIDASI FORMAT UNTUK UBAH JADWAL ---
        String tanggalBaru = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        while (true) {
            tanggalBaru = val.inputNonEmpty("Masukkan tanggal baru (YYYY-MM-DD):");
            if (tanggalBaru == null || tanggalBaru.trim().isEmpty()) {
                return; // Batal jika user klik cancel
            }
            
            try {
                LocalDate.parse(tanggalBaru, formatter); // Cek format saja
                // Catatan: Untuk cek Tgl Mulai vs Tgl Selesai di menu Update, 
                // kamu harus query data tanggal yang satunya lagi dari Database lewat jadwaldao.
                break; 
            } catch (DateTimeParseException e) {
                 JOptionPane.showMessageDialog(null, "Format salah! Gunakan format YYYY-MM-DD\nContoh: 2023-10-25", "Peringatan", JOptionPane.WARNING_MESSAGE);
            }
        }

        boolean berhasil = jadwaldao.ubahJadwal(idJadwal, fieldDb, tanggalBaru);
        if (berhasil) {
            JOptionPane.showMessageDialog(null, "Jadwal berhasil diubah.");
        } else {
            JOptionPane.showMessageDialog(null, "Gagal mengubah jadwal.");
        }
    }
}