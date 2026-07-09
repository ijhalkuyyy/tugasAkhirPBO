package program;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class JadwalUI {

    JadwalDAO jadwaldao = new JadwalDAO();
    KelasUI kelasUI = new KelasUI();
    Validasi val =  new Validasi();

    public void tampilDataJadwal() {
        String dataJadwal = jadwaldao.tampilSemuaJadwal();
        if(dataJadwal.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tidak ada data jadwal.");
        }else {
            JTextArea textArea = new JTextArea(dataJadwal);
            textArea.setEditable(false);
            JOptionPane.showMessageDialog(null, textArea, "Data Jadwal", JOptionPane.INFORMATION_MESSAGE);
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

        String tanggalMulai = val.inputNonEmpty("Masukkan Tanggal Mulai (YYYY-MM-DD):");
        String tanggalSelesai = val.inputNonEmpty("Masukkan Tanggal Selesai (YYYY-MM-DD):");
        int sesi = jadwaldao.generateSesi(kelas.getIdKelas());

        Jadwal jadwal = new Jadwal(jadwaldao.generateIdJadwal(), kelas, tanggalMulai, tanggalSelesai, sesi);
        jadwaldao.tambahJadwal(jadwal);
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
        String tanggalBaru = val.inputNonEmpty("Masukkan tanggal baru (YYYY-MM-DD):");

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

    

}
