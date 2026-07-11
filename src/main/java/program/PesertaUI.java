package program;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class PesertaUI {

    PesertaDAO pesertaDAO = new PesertaDAO();
    Validasi val = new Validasi();

    public void tampilDataPeserta() {
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

    public void inputDataPeserta() {

        String namaLengkap = val.inputNonEmpty("Masukkan nama lengkap");
        String namaPanggilan = val.inputNonEmpty("Masukkan Nama Panggilan");

        String noHp = "";
        do { 
            noHp = val.inputNonEmpty("Masukkan No HP");
            if(noHp == null || noHp.length() < 10 || noHp.length() > 13) {
                JOptionPane.showMessageDialog(null, "No HP harus memiliki panjang antara 10 hingga 13 digit.");
            }
        } while (noHp == null || noHp.length() < 10 || noHp.length() > 13);

        Peserta peserta = new Peserta(pesertaDAO.generateIdPeserta(), namaLengkap, namaPanggilan, noHp);
        pesertaDAO.tambahPeserta(peserta);
    }

    public void cariDataPesertaBerdasarkanID(){
        String dataPeserta = pesertaDAO.tampilSemuaPeserta();

        if(dataPeserta.isEmpty()){
            JOptionPane.showMessageDialog(null, "Belum ada data peserta");
            return;
        }
        
        JTextArea textArea = new JTextArea(dataPeserta);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); 
        textArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 300));

        Object[] tampilanPesan = {
            "Daftar Peserta:",
            scrollPane,
            "\nMasukkan ID Peserta yang ingin dicari: "
        };


        boolean putar = true;
        String idPeserta = "";
        do{
            idPeserta = JOptionPane.showInputDialog(null, tampilanPesan, "Tampilkan Peserta", JOptionPane.PLAIN_MESSAGE);

            if (idPeserta == null) {
                putar = false; 
                break;      
            }

            if (idPeserta.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "ID Peserta tidak boleh kosong!");
                continue;
            }
            
            Peserta peserta = pesertaDAO.cariById(idPeserta);
            if (peserta != null) {
                String teksTampilan = String.format(
                    "=====================================\n" +
                    "            DATA PESERTA             \n" +
                    "=====================================\n" +
                    "%-16s : %s\n" +
                    "%-16s : %s\n" +
                    "%-16s : %s\n" +
                    "%-16s : %s\n" +
                    "=====================================",
                    "ID Peserta", peserta.getIdPeserta(),
                    "Nama Lengkap", peserta.getNamaLengkap(),
                    "Nama Panggilan", peserta.getNamaPanggilan(),
                    "No HP", peserta.getNoHp()
                );
                javax.swing.JTextArea textAreaP = new javax.swing.JTextArea(teksTampilan);
                textAreaP.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 14));
                textAreaP.setEditable(false); 
                textAreaP.setOpaque(false); 
                textAreaP.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

                javax.swing.JOptionPane.showMessageDialog(null, textAreaP, "Informasi Peserta", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                putar = false;
            } else {
                JOptionPane.showMessageDialog(null, "Peserta tidak ditemukan.");
                putar = true;
            }
        }while(putar);
    }
    
    


}
