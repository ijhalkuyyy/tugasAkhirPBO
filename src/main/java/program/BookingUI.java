package program;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class BookingUI {

    private final BookingDAO bookingDAO = new BookingDAO();
    private final PesertaDAO pesertaDAO = new PesertaDAO();
    private final PesertaUI pesertaUI = new PesertaUI();
    private final KelasDAO kelasDAO = new KelasDAO();
    private final JadwalDAO jadwalDAO = new JadwalDAO();

    void tambahBooking() {
        String[] opsiStatusPeserta = {"Sudah pernah daftar", "Belum pernah daftar"};
        JComboBox<String> comboStatusPeserta = new JComboBox<>(opsiStatusPeserta);
        int hasilStatusPeserta = JOptionPane.showConfirmDialog(
                null,
                comboStatusPeserta,
                "Status Peserta",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (hasilStatusPeserta != JOptionPane.OK_OPTION) {
            return;
        }

        if ("Belum pernah daftar".equals(comboStatusPeserta.getSelectedItem())) {
            pesertaUI.inputDataPeserta();
        }

        List<Peserta> daftarPeserta = pesertaDAO.ambilSemuaPesertaObjek();
        if (daftarPeserta.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Belum ada data peserta. Tambah peserta terlebih dahulu.");
            return;
        }

        List<Jadwal> daftarJadwal = jadwalDAO.ambilJadwalAktifObjek();

        if (daftarJadwal.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Belum ada jadwal aktif. Tambah atau aktifkan jadwal terlebih dahulu.");
            return;
        }

        JPanel panel = new JPanel(new GridLayout(0, 1, 8, 8));

        JLabel lblPeserta = new JLabel("Pilih nama peserta:");
        JComboBox<ComboItem> comboPeserta = new JComboBox<>();
        for (Peserta peserta : daftarPeserta) {
            comboPeserta.addItem(new ComboItem(
                    peserta.getIdPeserta(),
                    peserta.getNamaLengkap() + " (" + peserta.getIdPeserta() + ")"
            ));
        }

        JLabel lblJadwal = new JLabel("Pilih jadwal:");
        JComboBox<ComboItem> comboJadwal = new JComboBox<>();
        for (Jadwal jadwal : daftarJadwal) {
            comboJadwal.addItem(new ComboItem(
                    jadwal.getIdJadwal(),
                    jadwal.getKelas().getNamaKelas() + " | Mulai: " + jadwal.getTanggalMulai() + " | Sesi: " + jadwal.getSesiKe()
            ));
        }

        JLabel lblStatusPembayaran = new JLabel("Status pembayaran:");
        JComboBox<String> comboStatusPembayaran = new JComboBox<>(new String[]{"Lunas", "DP 50%"});

        JLabel lblMetodePembayaran = new JLabel("Metode pembayaran:");
        JComboBox<String> comboMetodePembayaran = new JComboBox<>(new String[]{"Cash", "Transfer"});

        panel.add(lblPeserta);
        panel.add(comboPeserta);
        panel.add(lblJadwal);
        panel.add(comboJadwal);
        panel.add(lblStatusPembayaran);
        panel.add(comboStatusPembayaran);
        panel.add(lblMetodePembayaran);
        panel.add(comboMetodePembayaran);

        int hasil = JOptionPane.showConfirmDialog(null, panel, "Tambah Booking", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (hasil != JOptionPane.OK_OPTION) {
            return;
        }

        ComboItem pesertaTerpilih = (ComboItem) comboPeserta.getSelectedItem();
        ComboItem jadwalTerpilih = (ComboItem) comboJadwal.getSelectedItem();
        Jadwal jadwalDipilih = jadwalDAO.cariById(jadwalTerpilih.getId());
        double hargaKelas = jadwalDipilih.getKelas().getHarga();

        if (pesertaTerpilih == null || jadwalTerpilih == null || jadwalTerpilih.getId().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Pilih peserta dan jadwal terlebih dahulu.");
            return;
        }

        boolean sudahAda = bookingDAO.cekBookingDuplikat(pesertaTerpilih.getId(), jadwalTerpilih.getId());
        if (sudahAda) {
            JOptionPane.showMessageDialog(null, "Data tidak ditambah karena peserta sudah pernah mendaftar kelas dan jadwal yang sama.");
            return;
        }

        double hargaBayar;
        if(comboStatusPembayaran.getSelectedItem().equals("Lunas")){
            hargaBayar = hargaKelas;
        }else{
            hargaBayar = hargaKelas * 0.5;
        }

        String idBooking = bookingDAO.generateIdBooking();
        Booking booking = new Booking(
                idBooking,
                pesertaTerpilih.getId(),
                jadwalTerpilih.getId(),
                hargaBayar,
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
        String[] opsiStatus = {"Semua", "Lunas", "DP 50%"};
        JComboBox<String> comboFilter = new JComboBox<>(opsiStatus);
        int hasil = JOptionPane.showConfirmDialog(
                null,
                comboFilter,
                "Filter Data Booking",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (hasil != JOptionPane.OK_OPTION) {
            return;
        }

        String statusDipilih = (String) comboFilter.getSelectedItem();
        String statusFilter = "Semua".equals(statusDipilih) ? null : statusDipilih;
        String dataTransaksi = bookingDAO.tampilSemuaTransaksi(statusFilter);

        if (dataTransaksi.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tidak ada data transaksi untuk filter tersebut.");
        } else {
            double totalHargaMasuk = 0;
            List<Booking> daftarBooking = bookingDAO.ambilSemuaBookingObjek();
            for (Booking booking : daftarBooking) {
                boolean cocokFilter = statusFilter == null || statusFilter.equalsIgnoreCase(booking.getStatusPembayaran());
                if (cocokFilter) {
                    totalHargaMasuk += booking.getHarga();
                }
            }

            String ringkasanTransaksi = dataTransaksi
                    + "\n\nTotal Harga Masuk: Rp " + String.format("%,.0f", totalHargaMasuk);
            tampilkanScroll(ringkasanTransaksi, "Data Transaksi");
        }
    }

    void tampilkanScroll(String teks, String judul) {
        JTextArea textArea = new JTextArea(teks);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(1100, 300));
        JOptionPane.showMessageDialog(null, scrollPane, judul, JOptionPane.INFORMATION_MESSAGE);
    }

    public void ubahStatusPembayaran() {
        String dataBooking = bookingDAO.tampilSemuaTransaksi("DP 50%");
        if (dataBooking.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Belum ada booking dengan status DP 50%.");
            return;
        }

        List<Booking> daftarBooking = bookingDAO.ambilSemuaBookingObjek();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JTextArea textArea = new JTextArea(dataBooking);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        textArea.setMargin(new Insets(8, 8, 8, 8));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(700, 220));

        panel.add(new JLabel("Data Booking:"));
        panel.add(Box.createVerticalStrut(6));
        panel.add(scrollPane);
        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("Pilih ID booking yang ingin diubah status pembayarannya:"));

        JComboBox<ComboItem> comboBooking = new JComboBox<>();
        for (Booking booking : daftarBooking) {
            if (!"DP 50%".equalsIgnoreCase(booking.getStatusPembayaran())) {
                continue;
            }
            comboBooking.addItem(new ComboItem(
                    booking.getIdBooking(),
                    booking.getIdBooking() + " - " + booking.getStatusPembayaran()
            ));
        }

        if (comboBooking.getItemCount() == 0) {
            JOptionPane.showMessageDialog(null, "Tidak ada booking dengan status DP 50%.");
            return;
        }

        panel.add(comboBooking);

        int hasil = JOptionPane.showConfirmDialog(null, panel, "Ubah Status Pembayaran", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (hasil != JOptionPane.OK_OPTION) {
            return;
        }

        ComboItem bookingTerpilih = (ComboItem) comboBooking.getSelectedItem();
        if (bookingTerpilih == null) {
            JOptionPane.showMessageDialog(null, "Pilih booking terlebih dahulu.");
            return;
        }

        boolean berhasil = bookingDAO.ubahStatusPembayaran(bookingTerpilih.getId());
        if (berhasil) {
            JOptionPane.showMessageDialog(null, "Status pembayaran berhasil diubah menjadi Lunas.");
        } else {
            JOptionPane.showMessageDialog(null, "Gagal mengubah status pembayaran.");
        }
    }
}

