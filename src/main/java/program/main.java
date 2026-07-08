package program;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
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
                        "========Peserta========\n" +
                        "1. Input Data Peserta\n" +
                        "2. Tampil Data Peserta\n" +
                        "3. Cari Data Peserta ID\n" +
                        "========Kelas========\n" +
                        "4. Input Kelas\n" +
                        "5. Tampil Data Kelas\n" +
                        "6. Ubah Harga Kelas\n" +
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
                case 4: tambahKelas(); break;
                case 5: tampilDataKelas(); break;
                case 6: ubahHargaKelas(); break;
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

        Peserta peserta = new Peserta(pesertaDAO.generateIdPeserta(), namaLengkap, namaPanggilan, noHp);
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
        String namaKelas = JOptionPane.showInputDialog("Masukkan Nama Kelas:");
        String hargaStr = JOptionPane.showInputDialog("Masukkan Harga Kelas:");
        double harga = Double.parseDouble(hargaStr);
        String kapasitasStr = JOptionPane.showInputDialog("Masukkan Kapasitas Kelas:");
        int kapasitas = Integer.parseInt(kapasitasStr);

        Kelas kelas = new Kelas(kelasDAO.generateId(namaKelas), namaKelas, harga, kapasitas);
        kelasDAO.tambahKelas(kelas);
    }










}

