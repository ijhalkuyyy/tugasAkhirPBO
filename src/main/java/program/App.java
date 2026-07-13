package program;
import javax.swing.JOptionPane;

public class App {
    public static void main(String[] args) {
        PesertaUI pesertaUI = new PesertaUI();
        KelasUI kelasUI = new KelasUI();
        JadwalUI jadwalUI = new JadwalUI();
        BookingUI bookingUI = new BookingUI();
        boolean running = true;
        do {
            int pilih = 0;
            boolean putar = true;
            do {
                try {
                    String inputMenu = JOptionPane.showInputDialog(
                        "MENU SISTEM\n\n" +
                        "======== PESERTA ========\n" +
                        "1. Tampil Data Peserta\n" +
                        "2. Cari Data Peserta ID\n\n" +
                        "========= KELAS =========\n" +
                        "3. Input Kelas\n" +
                        "4. Tampil Data Kelas\n" +
                        "5. Ubah Harga Kelas\n\n" +
                        "========= JADWAL ========\n" +
                        "6. Input tambah Jadwal\n" +
                        "7. Tampil Data Jadwal\n" +
                        "8. Ubah Jadwal\n" +
                        "9. Nonaktifkan Jadwal Aktif\n\n" +
                        "========= BOOKING =========\n" +
                        "10. Tambah Booking\n" +
                        "11. Ubah Status Pembayaran\n" +
                        "12. Tampil Data Transaksi Aktif\n" +
                        "13. Tampil Data Transkasi per Jadwal\n" +
                        "14. Keluar\n\n" +
                        "Pilihan : "
                    );

                    if (inputMenu == null) {
                        System.exit(0); 
                    }

                    pilih = Integer.parseInt(inputMenu);
                    if (pilih >= 1 && pilih <= 14) {
                        putar = false;
                    } else {
                        JOptionPane.showMessageDialog(null, "Masukkan pilihan 1-14.");
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Masukkan angka yang valid!");
                }
            } while (putar);

            switch(pilih) {
                case 1: pesertaUI.tampilDataPeserta(); break;
                case 2: pesertaUI.cariDataPesertaBerdasarkanID(); break;
                case 3: kelasUI.tambahKelas(); break;
                case 4: kelasUI.tampilDataKelas(); break;
                case 5: kelasUI.ubahHargaKelas(); break;
                case 6: jadwalUI.inputDataJadwal(); break;
                case 7: jadwalUI.tampilDataJadwal(); break;
                case 8: jadwalUI.ubahJadwal(); break;
                case 9: jadwalUI.ubahStatusJadwalMenjadiNonaktif(); break;
                case 10: bookingUI.tambahBooking(); break;
                case 11: bookingUI.ubahStatusPembayaran(); break;
                case 12: bookingUI.tampilDataTransaksi(); break;
                case 13: bookingUI.tampilTransaksiPerKelasDanStatus(); break;
                case 14: 
                    running = false; 
                    JOptionPane.showMessageDialog(null, "Happiness In Every Moment", "Thank You!", JOptionPane.INFORMATION_MESSAGE);
                    break;
            }
        } while(running);
    }      
}