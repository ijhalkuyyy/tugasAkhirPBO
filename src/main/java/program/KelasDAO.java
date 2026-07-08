package program;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class KelasDAO {
    public String tampilSemuaKelas(){
        Connection conn = Koneksi.getKoneksi();

        StringBuilder hasil = new StringBuilder();

        try {
            String sql = "SELECT * FROM kelas order by id_kelas desc";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return "";
            }

            String formatKolom = "%-12s %-20s %-15s %-12s\n";

            hasil.append(String.format(formatKolom, "ID", "NAMA", "HARGA", "KAPASITAS"));
            hasil.append("===============================================================\n");

            do {
                hasil.append(String.format(formatKolom,
                rs.getString("id_kelas"),
                rs.getString("nama_kelas"),
                rs.getString("harga"),
                rs.getString("kapasitas")));
            } while (rs.next());

        } catch (Exception e) {
            hasil.append("Error: ").append(e.getMessage());
        }
        return hasil.toString();
    }

    public List<String> ambilSemuaNamaKelas(){
        List<String> namaKelas = new ArrayList<>();
        Connection conn = Koneksi.getKoneksi();

        try {
            String sql = "SELECT nama_kelas FROM kelas ORDER BY nama_kelas";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                namaKelas.add(rs.getString("nama_kelas"));
            }

            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return namaKelas;
    }

    public Kelas cariById(String idKelas){
        return null;

    }

    public boolean ubahHarga(String idKelas, int hargaBaru){
        return false;
    }

    String generateId(){
        Connection conn = Koneksi.getKoneksi();
        String id = "";
        try {
            String sql = "SELECT MAX(id_kelas) as max_id FROM kelas";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getString("max_id");
                if (id == null) {
                    id = "K001";
                } else {
                    int nomorUrut = Integer.parseInt(id.substring(1)) + 1;
                    id = String.format("K%03d", nomorUrut);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

    /**
     * Generate ID dengan format "BC-BOL-01".
     * - "BC" adalah prefix tetap.
     * - "BOL" adalah 3 karakter awal dari `namaKelas` (spasi dihapus, uppercase).
     * - "01" adalah nomor urut dua digit yang selalu bertambah untuk kombinasi BOL.
     */
    public String generateId(String namaKelas){
        Connection conn = Koneksi.getKoneksi();
        String id = "";
        try {
            String clean = namaKelas == null ? "" : namaKelas.trim().replaceAll("\\s+", "").toUpperCase();
            String bol = clean.length() >= 3 ? clean.substring(0,3) : clean;
            if (bol.isEmpty()) bol = "XXX";
            String prefix = "BC-" + bol + "-";

            String sql = "SELECT MAX(id_kelas) as max_id FROM kelas WHERE id_kelas LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, prefix + "%");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String max = rs.getString("max_id");
                if (max == null) {
                    id = prefix + String.format("%02d", 1);
                } else {
                    int lastDash = max.lastIndexOf('-');
                    String numStr = lastDash >= 0 ? max.substring(lastDash + 1) : "0";
                    int next = Integer.parseInt(numStr) + 1;
                    id = prefix + String.format("%02d", next);
                }
            } else {
                id = prefix + String.format("%02d", 1);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

    public void tambahKelas(Kelas kelas){
        Connection conn = Koneksi.getKoneksi();
        try {
            String sql = "INSERT INTO kelas ( id_kelas, nama_kelas, harga, kapasitas) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, kelas.getIdKelas());
            ps.setString(2, kelas.getNamaKelas());
            ps.setDouble(3, kelas.getHarga());
            ps.setInt(4, kelas.getKapasitas());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
