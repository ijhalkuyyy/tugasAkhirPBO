package program;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class Main {

    static PesertaDAO pesertaDAO = new PesertaDAO();
    static KelasDAO kelasDAO = new KelasDAO();
    // static JadwalDAO jadwalDAO = new JadwalDAO();
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
                        "1. Input Data Peserta\n" +
                        "2. Tampil Data Peserta\n" +
                        "3. Cari Data Peserta ID\n" +
                        "4. Tampil Data Kelas\n" +
                        "5. Tambah Kelas\n" +
                        "6. Laporan Peserta\n" +
                        "7. Keluar\n\n" +
                        "Pilihan : "
                    );

                    if (inputMenu == null) {
                        System.exit(0); 
                    }

                    pilih = Integer.parseInt(inputMenu);
                    if (pilih >= 1 && pilih <= 7) {
                        putar = false;
                    } else {
                        JOptionPane.showMessageDialog(null, "Masukkan pilihan 1-7.");
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Masukkan angka yang valid!");
                }
            } while (putar);

            switch(pilih) {
                case 1: inputDataPeserta(); break;
                case 2: tampilDataPeserta(); break;
                case 3: cariDataPesertaBerdasarkanID(); break;
                case 4: tampilDataKelas(); break;
                case 5: tambahKelas(); break;
                //case 5: inputPeserta(); break;
                //case 6: lapPeserta(); break;
                case 7: running = false; break;
            }
        } while(running);
    }
    // public static void Main(String[] args) {
        

    // }




    // MENU PESERTA
    static void tampilDataPeserta() {
        String dataPeserta = pesertaDAO.tampilSemuaPeserta();
        if(dataPeserta.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tidak ada data peserta.");
        }else {
            JTextArea textArea = new JTextArea(dataPeserta);
            textArea.setEditable(false);
            JOptionPane.showMessageDialog(null, textArea, "Data Peserta", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    static void inputDataPeserta() {
        String namaLengkap = JOptionPane.showInputDialog("Masukkan Nama Lengkap:");
        String namaPanggilan = JOptionPane.showInputDialog("Masukkan Nama Panggilan:");

        String noHp = "";
        do { 
            noHp = JOptionPane.showInputDialog("Masukkan No HP:");
            if(noHp == null || noHp.length() < 10 || noHp.length() > 13) {
                JOptionPane.showMessageDialog(null, "No HP harus memiliki panjang antara 10 hingga 13 digit.");
            }
        } while (noHp == null || noHp.length() < 10 || noHp.length() > 13);

        Peserta peserta = new Peserta(pesertaDAO.generateId(), namaLengkap, namaPanggilan, noHp);
        pesertaDAO.tambahPeserta(peserta);
    }

    static void cariDataPesertaBerdasarkanID(){
        String id = JOptionPane.showInputDialog("Masukkan ID Peserta:");
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
            JTextArea textArea = new JTextArea(dataKelas);
            textArea.setEditable(false);
            JOptionPane.showMessageDialog(null, textArea, "Data Kelas", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    static void ubahHargaKelas() {
        String idKelas = JOptionPane.showInputDialog("Masukkan ID Kelas:");
        String hargaBaruStr = JOptionPane.showInputDialog("Masukkan Harga Baru:");
        int hargaBaru = Integer.parseInt(hargaBaruStr);
        boolean berhasil = kelasDAO.ubahHarga(idKelas, hargaBaru);
        if (berhasil) {
            JOptionPane.showMessageDialog(null, "Harga kelas berhasil diubah.");
        } else {
            JOptionPane.showMessageDialog(null, "Gagal mengubah harga kelas.");
        }
    }
    

    static void tambahKelas() {
        String namaKelas = JOptionPane.showInputDialog("Masukkan Nama Kelas:");
        String hargaStr = JOptionPane.showInputDialog("Masukkan Harga Kelas:");
        double harga = Double.parseDouble(hargaStr);
        String kapasitasStr = JOptionPane.showInputDialog("Masukkan Kapasitas Kelas:");
        int kapasitas = Integer.parseInt(kapasitasStr);

        Kelas kelas = new Kelas(kelasDAO.generateId(), namaKelas, harga, kapasitas);
        kelasDAO.tambahKelas(kelas);
    }










}

