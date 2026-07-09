package program;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.*;

public class KelasUI {

    KelasDAO kelasDAO = new KelasDAO();
    Validasi val = new Validasi();

    public void tampilDataKelas() {
        String dataKelas = kelasDAO.tampilSemuaKelas();
        if(dataKelas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tidak ada data kelas.");
        }else {
           tampilkanScroll(dataKelas, "Data Kelas");
            // JTextArea textArea = new JTextArea(dataKelas);
            // textArea.setEditable(false);
        }
    }

    public void ubahHargaKelas() {
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

        String idKelas = "";        
        while (true) {
            do{
                idKelas = JOptionPane.showInputDialog(null, tampilanPesan, "Ubah Harga Kelas", JOptionPane.PLAIN_MESSAGE);
                if(idKelas==null) System.exit(0);
            }while(idKelas.trim().isEmpty());
            // idKelas = JOptionPane.showInputDialog(null, tampilanPesan, "Ubah Harga Kelas", JOptionPane.PLAIN_MESSAGE);
            
            if (idKelas == null || idKelas.trim().isEmpty()) {
                return;
            }
            boolean kelasDitemukan = kelasDAO.cekKelasAda(idKelas);

            if (kelasDitemukan) {
                break; 
            } else {
                JOptionPane.showMessageDialog(null, 
                    "ID Kelas (" + idKelas + ") tidak Ditemukan!\n Silakan masukkan ID yang benar.", 
                    "ID Tidak Valid", 
                    JOptionPane.WARNING_MESSAGE);
            }
        }

        Double hargaBaru = 0.0;
        do {
            hargaBaru = val.inputDouble("Masukkan Harga Baru untuk Kelas");
        } while (hargaBaru<=0);

        try {
            boolean berhasil = kelasDAO.ubahHarga(idKelas, hargaBaru);
            Kelas dataCelass = kelasDAO.cariById(idKelas);
            String namaKelas = dataCelass.getNamaKelas();
            
            if (berhasil) {
                JOptionPane.showMessageDialog(null, "Harga kelas " + namaKelas + " berhasil diubah menjadi " + hargaBaru + ".");
            } else {
                JOptionPane.showMessageDialog(null, "Gagal mengubah harga! Pastikan ID Kelas (" + idKelas + ") benar dan ada di daftar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: Harga baru harus berupa angka!", "Input Tidak Valid", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void tambahKelas() {
        String namaKelas = val.inputNonEmpty("Masukkan Nama Kelas");
        double harga = 0;
        do {
            harga =  val.inputDouble("Masukkan Harga Kelas");
        } while (harga<=0);

        int kapasitas = 0;
        do {
            kapasitas = val.inputInt("Masukkan Kapasitas Kelas");
        } while (kapasitas<=0); 
        // JOptionPane.showInputDialog("Masukkan Harga Kelas:");
        // double harga = Double.parseDouble(hargaStr);
        // String kapasitasStr = JOptionPane.showInputDialog("Masukkan Kapasitas Kelas:");
        Kelas kelas = new Kelas(kelasDAO.generateId(namaKelas), namaKelas, harga, kapasitas);
        kelasDAO.tambahKelas(kelas);
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

    
}
