package program;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class KelasDAO {

   // ===================================================
    // TAMPIL SEMUA KELAS
    // ===================================================
    public String tampilSemuaKelas() {

        Connection con = Koneksi.getKoneksi();
        StringBuilder hasil = new StringBuilder();

        try {

            String sql = "SELECT * FROM kelas ORDER BY id_kelas DESC";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return "";
            }

            String format = "%-10s %-25s %-15s %-12s\n";

            hasil.append(String.format(format,
                    "ID",
                    "NAMA KELAS",
                    "HARGA",
                    "KAPASITAS"));

            hasil.append("====================================================================\n");

            do {

                hasil.append(String.format(format,
                        rs.getString("id_kelas"),
                        rs.getString("nama_kelas"),
                        rs.getInt("harga"),
                        rs.getInt("kapasitas")));

            } while (rs.next());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal menampilkan data kelas!", "Laporan Error", JOptionPane.ERROR_MESSAGE);
            return "Error : " + e.getMessage();
        }

        return hasil.toString();

    }

    // ===================================================
    // TAMBAH KELAS
    // ===================================================
    public boolean tambahKelas(Kelas kelas) {
        Connection con = Koneksi.getKoneksi();

        try {

            String sql = """
                         INSERT INTO kelas
                         (id_kelas,nama_kelas,harga,kapasitas)
                         VALUES (?,?,?,?)
                         """;

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, kelas.getIdKelas());
            ps.setString(2, kelas.getNamaKelas());
            ps.setDouble(3, kelas.getHarga());
            ps.setInt(4, kelas.getKapasitas());
            int hasil = ps.executeUpdate();

            return hasil > 0;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal menyimpan data kelas!", "Insert Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getMessage());
            return false;

        }

    }

    public List<Kelas> ambilSemuaKelasObjek() {
        List<Kelas> daftarKelas = new ArrayList<>();
        Connection con = Koneksi.getKoneksi();

        try {
            String sql = "SELECT id_kelas, nama_kelas, harga, kapasitas FROM kelas ORDER BY id_kelas";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                daftarKelas.add(new Kelas(
                    rs.getString("id_kelas"),
                    rs.getString("nama_kelas"),
                    rs.getInt("harga"),
                    rs.getInt("kapasitas")
                ));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil daftar kelas!", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getMessage());
        }

        return daftarKelas;
    }
    // ===================================================
    // CARI BERDASARKAN ID
    // ===================================================
    public Kelas cariById(String idKelas) {

        Connection con = Koneksi.getKoneksi();

        Kelas kelas = null;

        try {

            String sql = "SELECT * FROM kelas WHERE id_kelas = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, idKelas);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                kelas = new Kelas(

                        rs.getString("id_kelas"),
                        rs.getString("nama_kelas"),
                        rs.getInt("harga"),
                        rs.getInt("kapasitas")
                );

            }

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }

        return kelas;

    }

    // ===================================================
    // UBAH HARGA
    // ===================================================
    public boolean ubahHarga(String idKelas, int hargaBaru) {
        Connection con = Koneksi.getKoneksi();
        try {

            String sql = """
                         UPDATE kelas
                         SET harga = ?
                         WHERE id_kelas = ?
                         """;

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, hargaBaru);
            ps.setString(2, idKelas);
            int hasil = ps.executeUpdate();
            return hasil > 0;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal mengubah harga kelas!", "Update Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getMessage());

            return false;

        }

    }

    // ===================================================
    // GENERATE ID KELAS
    // ===================================================
    public String generateId(String namaKelas){
        Connection conn = Koneksi.getKoneksi();
        String id = "";
        try {
            String clean = namaKelas == null ? "" : namaKelas.trim().replaceAll("\s+", "").toUpperCase();
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

}