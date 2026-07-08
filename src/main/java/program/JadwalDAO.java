package program;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class JadwalDAO {
    public String tampilSemuaJadwal(){
        Connection con = Koneksi.getKoneksi();
        StringBuilder hasil = new StringBuilder();
        try {
            String sql = "SELECT * FROM jadwal order by id_jadwal desc";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return "";
            }

            String formatKolom = "%-12s %-20s %-15s %-12s\n";

            hasil.append(String.format(formatKolom, "ID", "NAMA", "HARGA", "KAPASITAS"));
            hasil.append("===============================================================\n");

            do {
                hasil.append(String.format(formatKolom,
                rs.getString("id_jadwal"),
                rs.getString("nama_kelas"),
                rs.getString("harga"),
                rs.getString("kapasitas")));
            } while (rs.next());

        } catch (Exception e) {
            hasil.append("Error: ").append(e.getMessage());
        }
        return hasil.toString();
    }

    String geStringenerateIdJadwal() {
        Connection con = Koneksi.getKoneksi();
        String id = "";
        try {
            String sql = "SELECT MAX(id_jadwal) AS max_id FROM jadwal";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String maxId = rs.getString("max_id");
                if (maxId != null) {
                    int nextId = Integer.parseInt(maxId.substring(1)) + 1;
                    id = String.format("J%03d", nextId);
                } else {
                    id = "J001";
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

    public String tampilJadwalPerKelas(String idKelas){
        return null;

    }

    // public boolean tambahJadwal(Jadwal jadwal){
    //     Kelas kelas = jadwal.getKelas();
    //     Connection con = Koneksi.getKoneksi();
    //     try {
    //         String sql = "INSERT INTO jadwal (id_jadwal, nama_kelas, harga, kapasitas) VALUES (?, ?, ?, ?)";
    //         PreparedStatement ps = con.prepareStatement(sql);
    //         ps.setString(1, jadwal.getIdJadwal());
    //         ps.setString(2, kelas.getNamaKelas());
    //         ps.setDouble(3, kelas.getHarga());
    //         ps.setInt(4, jadwal.getKapasitas());
    //         ps.executeUpdate();
    //         return true;
    //     } catch (Exception e) {
    //         System.out.println(e.getMessage());
    //         return false;
    //     }

    // }

    public String generateIdJadwal(){
        return null;
    }
}
