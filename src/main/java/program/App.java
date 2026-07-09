package program;
import javax.swing.JOptionPane;


public class App {



    public static void main(String[] args) {
        PesertaUI pesertaUI = new PesertaUI();
        KelasUI kelasUI = new KelasUI();
        JadwalUI jadwalUI = new JadwalUI();
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
                        "3. Cari Data Peserta ID\n\n" +
                        "========Kelas========\n" +
                        "4. Input Kelas\n" +
                        "5. Tampil Data Kelas\n" +
                        "6. Ubah Harga Kelas\n\n" +
                        "========JADWAL========\n" +
                        "7. Input tambah Jadwal\n" +
                        "8. Tampil Data Jadwal\n" +
                        "9. Ubah Jadwal\n" +
                        "10. Keluar\n\n" +
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
                case 1: pesertaUI.inputDataPeserta();break;
                case 2: pesertaUI.tampilDataPeserta(); break;
                case 3: pesertaUI.cariDataPesertaBerdasarkanID(); break;
                case 4: kelasUI.tambahKelas(); break;
                case 5: kelasUI.tampilDataKelas(); break;
                case 6: kelasUI.ubahHargaKelas(); break;
                case 7: jadwalUI.inputDataJadwal(); break;
                case 8: jadwalUI.tampilDataJadwal(); break;
                case 9: jadwalUI.ubahJadwal(); break;
                case 10: running = false; break;
            }
        } while(running);
    }

        
}

