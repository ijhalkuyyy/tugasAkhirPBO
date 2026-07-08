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
}
