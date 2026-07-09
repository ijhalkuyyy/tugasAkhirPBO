package program;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.List;

public class BookingUI {

    private final BookingDAO bookingDAO = new BookingDAO();
    private final PesertaDAO pesertaDAO = new PesertaDAO();
    private final KelasDAO kelasDAO = new KelasDAO();
    private final JadwalDAO jadwalDAO = new JadwalDAO();

    void tambahBooking() {
        List<Peserta> daftarPeserta = pesertaDAO.ambilSemuaPesertaObjek();
        List<Kelas> daftarKelas = kelasDAO.ambilSemuaKelasObjek();
        List<Jadwal> daftarJadwal = jadwalDAO.ambilSemuaJadwalObjek();

        if (daftarPeserta.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Belum ada data peserta. Tambah peserta terlebih dahulu.");
            return;
        }

        if (daftarKelas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Belum ada data kelas. Tambah kelas terlebih dahulu.");
            return;
        }

        JPanel panel = new JPanel(new GridLayout(0, 1, 8, 8));

        JLabel lblPeserta = new JLabel("Pilih peserta:");
        JComboBox<ComboItem> comboPeserta = new JComboBox<>();
        for (Peserta peserta : daftarPeserta) {
            comboPeserta.addItem(new ComboItem(peserta.getIdPeserta(),
                    peserta.getNamaLengkap() + " (" + peserta.getIdPeserta() + ")"));
        }

        JLabel lblKelas = new JLabel("Pilih kelas:");
        JComboBox<ComboItem> comboKelas = new JComboBox<>();
        for (Kelas kelas : daftarKelas) {
            comboKelas.addItem(new ComboItem(kelas.getIdKelas(),
                    kelas.getNamaKelas() + " (" + kelas.getIdKelas() + ")"));
        }

        JLabel lblJadwal = new JLabel("Pilih jadwal:");
        JComboBox<ComboItem> comboJadwal = new JComboBox<>();
        for (Jadwal jadwal : daftarJadwal) {
            comboJadwal.addItem(new ComboItem(jadwal.getIdJadwal(),
                    jadwal.getTanggalMulai() + " (" + jadwal.getIdJadwal() + ")"));
        }

        JLabel lblStatusPembayaran = new JLabel("Status pembayaran:");
        JComboBox<String> comboStatusPembayaran = new JComboBox<>(new String[]{"Lunas", "DP 50%"});

        JLabel lblMetodePembayaran = new JLabel("Metode pembayaran:");
        JComboBox<String> comboMetodePembayaran = new JComboBox<>(new String[]{"Cash", "Transfer"});

        comboKelas.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                ComboItem itemKelas = (ComboItem) comboKelas.getSelectedItem();
                if (itemKelas != null) {
                    isiJadwalCombo(comboJadwal, itemKelas.getId());
                }
            }
        });

        panel.add(lblPeserta);
        panel.add(comboPeserta);
        panel.add(lblKelas);
        panel.add(comboKelas);
        panel.add(lblJadwal);
        panel.add(comboJadwal);
        panel.add(lblStatusPembayaran);
        panel.add(comboStatusPembayaran);
        panel.add(lblMetodePembayaran);
        panel.add(comboMetodePembayaran);

        if (comboKelas.getItemCount() > 0) {
            comboKelas.setSelectedIndex(0);
        }

        int hasil = JOptionPane.showConfirmDialog(null, panel, "Tambah Booking", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (hasil != JOptionPane.OK_OPTION) {
            return;
        }

        ComboItem pesertaTerpilih = (ComboItem) comboPeserta.getSelectedItem();
        ComboItem kelasTerpilih = (ComboItem) comboKelas.getSelectedItem();
        ComboItem jadwalTerpilih = (ComboItem) comboJadwal.getSelectedItem();

        if (pesertaTerpilih == null || kelasTerpilih == null || jadwalTerpilih == null || jadwalTerpilih.getId().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Pilih peserta, kelas, dan jadwal terlebih dahulu.");
            return;
        }

        Kelas kelasDipilih = kelasDAO.cariById(kelasTerpilih.getId());
        if (kelasDipilih == null) {
            JOptionPane.showMessageDialog(null, "Kelas yang dipilih tidak ditemukan.");
            return;
        }

        String idBooking = bookingDAO.generateIdBooking();
        Booking booking = new Booking(
                idBooking,
                pesertaTerpilih.getId(),
                jadwalTerpilih.getId(),
                (String) comboMetodePembayaran.getSelectedItem(),
                (String) comboStatusPembayaran.getSelectedItem()
        );

        boolean berhasil = bookingDAO.tambahBooking(booking);

        if (berhasil) {
            JOptionPane.showMessageDialog(null, "Booking berhasil ditambahkan dengan ID " + idBooking + ".");
        } else {
            JOptionPane.showMessageDialog(null, "Gagal menambahkan booking.");
        }
    }

    private void isiJadwalCombo(JComboBox<ComboItem> comboJadwal, String idKelas) {
        comboJadwal.removeAllItems();
        List<Jadwal> daftarJadwal = jadwalDAO.ambilJadwalByKelas(idKelas);

        if (daftarJadwal.isEmpty()) {
            comboJadwal.addItem(new ComboItem("", "Tidak ada jadwal"));
            return;
        }

        for (Jadwal jadwal : daftarJadwal) {
            comboJadwal.addItem(new ComboItem(
                    jadwal.getIdJadwal(),
                    "Jadwal " + jadwal.getIdJadwal() + " - " + jadwal.getTanggalMulai()
            ));
        }
    }

    private static class ComboItem {
        private final String id;
        private final String label;

        private ComboItem(String id, String label) {
            this.id = id;
            this.label = label;
        }

        public String getId() {
            return id;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    void tampilDataTransaksi() {
        String dataTransaksi = bookingDAO.tampilSemuaTransaksi();
        if (dataTransaksi.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tidak ada data transaksi.");
        } else {
            tampilkanScroll(dataTransaksi, "Data Transaksi");
        }
    }

    void tampilkanScroll(String teks, String judul) {
        JTextArea textArea = new JTextArea(teks);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        JOptionPane.showMessageDialog(null, scrollPane, judul, JOptionPane.INFORMATION_MESSAGE);
    }

    void ubahStatusPembayaran(String idBooking) {
        boolean berhasil = bookingDAO.ubahStatusPembayaran(idBooking);
        if (berhasil) {
            JOptionPane.showMessageDialog(null, "Status pembayaran berhasil diubah.");
        } else {
            JOptionPane.showMessageDialog(null, "Gagal mengubah status pembayaran. ID Booking tidak ditemukan.");
        }
    }
}

