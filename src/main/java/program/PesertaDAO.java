package program;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PesertaDAO {
    public String tampilSemuaPeserta(){
        Connection con = Koneksi.getKoneksi();

        StringBuilder hasil = new StringBuilder();

        try {
            String sql = "SELECT * FROM peserta order by id_peserta desc";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return "";
            }

            String formatKolom = "%-12s %-20s %-15s %-12s\n";

            hasil.append(String.format(formatKolom, "ID", "NAMA", "PANGGILAN", "NO HP"));
            hasil.append("===============================================================\n");

            do {
                hasil.append(String.format(formatKolom,
                rs.getString("id_peserta"),
                rs.getString("nama_lengkap"),
                rs.getString("nama_panggilan"),
                rs.getString("no_hp")));
            } while (rs.next());

        } catch (Exception e) {
            hasil.append("Error: ").append(e.getMessage());
        }
        return hasil.toString();
    }

    public void tambahPeserta(Peserta peserta){
        
        Connection con = Koneksi.getKoneksi();
        try {
            String sql = "INSERT INTO peserta (id_peserta, nama_lengkap, nama_panggilan, no_hp) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, peserta.getIdPeserta());
            ps.setString(2, peserta.getNamaLengkap());
            ps.setString(3, peserta.getNamaPanggilan());
            ps.setString(4, peserta.getNoHp());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    public String generateId(){
        Connection con = Koneksi.getKoneksi();
        String id = "";
        try {
            String sql = "SELECT MAX(id_peserta) as max_id FROM peserta";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getString("max_id");
                if (id == null) {
                    id = "P001";
                } else {
                    int nextId = Integer.parseInt(id.substring(2)) + 1;
                    id = "PS" + String.format("%03d", nextId);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

    public Peserta cariById(String id){
        Connection con = Koneksi.getKoneksi();
        Peserta peserta = null;
        try {
            String sql = "SELECT * FROM peserta WHERE id_peserta = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                peserta = new Peserta(
                    rs.getString("id_peserta"),
                    rs.getString("nama_lengkap"),
                    rs.getString("nama_panggilan"),
                    rs.getString("no_hp")
                );
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return peserta;
    }

    // public Peserta cariByNoHp(String noHp){

    // }

    ///////////////////////////////

    // public Peserta getAllPeserta() {
    //     Peserta peserta = null;
    //     try {
    //         String sql = "SELECT * FROM peserta";
    //         PreparedStatement ps = con.prepareStatement(sql);
    //         ResultSet rs = ps.executeQuery();

    //         if (rs.next()) {
    //             String idPeserta = rs.getString("ID_Peserta");
    //             String namaLengkap = rs.getString("Nama_Lengkap");
    //             String namaPanggilan = rs.getString("Nama_Panggilan");
    //             String noHp = rs.getString("No_HP");

    //             peserta = new Peserta(idPeserta, namaLengkap, namaPanggilan, noHp);
    //         }
    //     } catch (Exception e) {
    //         System.out.println(e.getMessage());
    //     }
    //     return peserta;
    // }


    // public void simpan(Peserta p) {
    //     try {
    //         String sql = "INSERT INTO peserta VALUES (?,?,?,?)";
    //         PreparedStatement ps = con.prepareStatement(sql);
    //         ps.setString(1, p.getIdPeserta());
    //         ps.setString(2, p.getNamaLengkap());
    //         ps.setString(3, p.getNamaPanggilan());
    //         ps.setString(4, p.getNoHp());
    //         ps.executeUpdate();
    //     } catch (Exception e) {
    //         System.out.println(e.getMessage());
    //     }
    // }

    // public String tampilData() {
    //     StringBuilder hasil = new StringBuilder();

    //     try {
    //         String sql = "SELECT * FROM `peserta`";
    //         PreparedStatement ps = con.prepareStatement(sql);
    //         ResultSet rs = ps.executeQuery();

    //         String formatKolom = "%-12s %-20s %-15s %-12s\n";

    //         hasil.append(String.format(formatKolom, "ID", "NAMA", "PANGGILAN", "NO HP"));
    //         hasil.append("===============================================================\n");

    //         while (rs.next()) {
    //             hasil.append(String.format(formatKolom,
    //                     rs.getString("id_peserta"),
    //                     rs.getString("nama_lengkap"),
    //                     rs.getString("nama_panggilan"),
    //                     rs.getString("no_hp")
    //             ));
    //         }

    //     } catch (Exception e) {
    //         hasil.append("Error: ").append(e.getMessage());
    //     }

    //     return hasil.toString();
    // }
    // public String tampilSemuaPeserta(){
        
    // }

    // public Peserta cariByNoHp(String noHp){
        
    // }

    // public Peserta cariById(String idPeserta){
        
    // }

    // public boolean tambahPeserta(Peserta peserta){

    // }

    // public String generateIdPeserta(){

    // }
}
