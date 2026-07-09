package program;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class PesertaDAO {

    // ==========================
    // TAMPIL SEMUA PESERTA
    // ==========================
    public String tampilSemuaPeserta() {

        Connection con = Koneksi.getKoneksi();
        StringBuilder hasil = new StringBuilder();

        try {

            String sql = "SELECT * FROM peserta ORDER BY id_peserta DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return "";
            }

            String format = "%-10s %-20s %-20s %-15s\n";

            hasil.append(String.format(format,
                    "ID",
                    "NAMA",
                    "PANGGILAN",
                    "NO HP"));

            hasil.append("====================================================================\n");

            do {

                hasil.append(String.format(format,
                        rs.getString("id_peserta"),
                        rs.getString("nama_lengkap"),
                        rs.getString("nama_panggilan"),
                        rs.getString("no_hp")));

            } while (rs.next());

        } catch (Exception e) {

            return "Error : " + e.getMessage();

        }

        return hasil.toString();

    }

    // ==========================
    // TAMBAH PESERTA
    // ==========================
    public boolean tambahPeserta(Peserta peserta) {

        Connection con = Koneksi.getKoneksi();

        if (con == null) {
            JOptionPane.showMessageDialog(null, "Gagal terhubung ke database! Pastikan MySQL sudah menyala.", "Koneksi Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {

            String sql = """
                         INSERT INTO peserta
                         (id_peserta,nama_lengkap,nama_panggilan,no_hp)
                         VALUES (?,?,?,?)
                         """;

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, peserta.getIdPeserta());
            ps.setString(2, peserta.getNamaLengkap());
            ps.setString(3, peserta.getNamaPanggilan());
            ps.setString(4, peserta.getNoHp());

            ps.executeUpdate();

            return true;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal menyimpan data peserta!", "Insert Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getMessage());
            return false;
        }

    }

    public List<Peserta> ambilSemuaPesertaObjek() {
        List<Peserta> daftarPeserta = new ArrayList<>();
        Connection con = Koneksi.getKoneksi();

        try {
            String sql = "SELECT id_peserta, nama_lengkap, nama_panggilan, no_hp FROM peserta ORDER BY id_peserta";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                daftarPeserta.add(new Peserta(
                        rs.getString("id_peserta"),
                        rs.getString("nama_lengkap"),
                        rs.getString("nama_panggilan"),
                        rs.getString("no_hp")
                ));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil daftar peserta!", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getMessage());
        }

        return daftarPeserta;
    }

    // ==========================
    // CARI BERDASARKAN ID
    // ==========================
    public Peserta cariById(String idPeserta) {

        Connection con = Koneksi.getKoneksi();

        Peserta peserta = null;

        try {

            String sql = "SELECT * FROM peserta WHERE id_peserta = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, idPeserta);

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

    // ==========================
    // CARI BERDASARKAN NO HP
    // ==========================
    public Peserta cariByNoHp(String noHp) {

        Connection con = Koneksi.getKoneksi();

        Peserta peserta = null;

        try {

            String sql = "SELECT * FROM peserta WHERE no_hp = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, noHp);

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

    // ==========================
    // GENERATE ID PESERTA
    // ==========================
    public String generateIdPeserta() {

        Connection con = Koneksi.getKoneksi();
        String id = "P001";
        try {

            String sql = "SELECT MAX(id_peserta) AS max_id FROM peserta";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String maxId = rs.getString("max_id");

                if (maxId != null) {

                    int angka = Integer.parseInt(maxId.substring(1));
                    angka++;
                    id = "P" + String.format("%03d", angka);

                }

            }

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }

        return id;

    }

}