package program;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class JadwalDAO{

    public String tampilSemuaJadwal() {

        Connection con = Koneksi.getKoneksi();

        StringBuilder hasil = new StringBuilder();

        try {

            String sql = """
                    SELECT
                        j.id_jadwal,
                        k.id_kelas,
                        k.nama_kelas,
                        j.tanggal_mulai,
                        j.tanggal_selesai,
                        j.sesi_ke
                    FROM jadwal j
                    JOIN kelas k
                    ON j.id_kelas = k.id_kelas
                    WHERE j.status = 'A'
                    ORDER BY j.id_jadwal DESC
                    """;

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return "";
            }

            String format = "%-12s %-18s %-18s %-18s %-8s\n";

            hasil.append(String.format(format,
                    "ID",
                    "KELAS",
                    "TANGGAL MULAI",
                    "TANGGAL SELESAI",
                    "SESI"));

            hasil.append("=====================================================================================\n");

            do {

                hasil.append(String.format(format,

                        rs.getString("id_jadwal"),
                        rs.getString("nama_kelas"),
                        rs.getString("tanggal_mulai"),
                        rs.getString("tanggal_selesai"),
                        rs.getInt("sesi_ke")

                ));

            } while (rs.next());

        } catch (Exception e) {

            return e.getMessage();

        }
        return hasil.toString();
    }

    

    public boolean tambahJadwal(Jadwal jadwal){

        Connection con = Koneksi.getKoneksi();
        try{
            String sql = """
                    INSERT INTO jadwal
                    (id_jadwal,id_kelas,tanggal_mulai,tanggal_selesai,sesi_ke, status)

                    VALUES(?,?,?,?,?,?)
                    """;
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, jadwal.getIdJadwal());
            ps.setString(2, jadwal.getKelas().getIdKelas());
            ps.setString(3, jadwal.getTanggalMulai());
            ps.setString(4, jadwal.getTanggalSelesai());
            ps.setInt(5, jadwal.getSesiKe());
            ps.setString(6, jadwal.getStatus());

            int hasil = ps.executeUpdate();

            return hasil>0;

        }catch(Exception e){

            System.out.println(e.getMessage());

            return false;

        }

    }

    public List<String> ambilSemuaJadwalDropdown() {
        List<String> daftarJadwal = new ArrayList<>();
        Connection con = Koneksi.getKoneksi();

        try {
            String sql = """
                    SELECT
                        j.id_jadwal,
                        k.nama_kelas,
                        j.sesi_ke
                    FROM jadwal j
                    JOIN kelas k
                    ON j.id_kelas = k.id_kelas
                    ORDER BY j.id_jadwal DESC
                    """;

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                daftarJadwal.add(
                    rs.getString("id_jadwal") + " - " + rs.getString("nama_kelas") + " - Sesi " + rs.getInt("sesi_ke")
                );
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return daftarJadwal;
    }

    public List<Jadwal> ambilJadwalByKelas(String idKelas) {
        List<Jadwal> daftarJadwal = new ArrayList<>();
        Connection con = Koneksi.getKoneksi();

        try {
            String sql = """
                    SELECT
                        j.id_jadwal,
                        j.tanggal_mulai,
                        j.tanggal_selesai,
                        j.sesi_ke,
                        k.id_kelas,
                        k.nama_kelas,
                        k.harga,
                        k.kapasitas
                    FROM jadwal j
                    JOIN kelas k
                    ON j.id_kelas = k.id_kelas
                    WHERE j.id_kelas = ?
                    ORDER BY j.tanggal_mulai ASC
                    """;

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, idKelas);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Kelas kelas = new Kelas(
                        rs.getString("id_kelas"),
                        rs.getString("nama_kelas"),
                        rs.getDouble("harga"),
                        rs.getInt("kapasitas")
                );

                daftarJadwal.add(new Jadwal(
                        rs.getString("id_jadwal"),
                        kelas,
                        rs.getString("tanggal_mulai"),
                        rs.getString("tanggal_selesai"),
                        rs.getInt("sesi_ke")
                ));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return daftarJadwal;
    }

    public boolean ubahJadwal(String idJadwal, String kolom, String nilaiBaru) {
        Connection con = Koneksi.getKoneksi();

        try {
            String sql = "UPDATE jadwal SET " + kolom + " = ? WHERE id_jadwal = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nilaiBaru);
            ps.setString(2, idJadwal);
            int hasil = ps.executeUpdate();
            return hasil > 0;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public String generateIdJadwal() {

        Connection con = Koneksi.getKoneksi();

        String id = "JWL-01";

        try {
            String sql = "SELECT MAX(id_jadwal) AS max_id FROM jadwal";
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String maxId = rs.getString("max_id");

                if (maxId != null) {

                    int angka = Integer.parseInt(maxId.substring(4));

                    angka++;

                    id = "JWL-" + String.format("%02d", angka);

                }

            }

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }

            return id;

    }

    public int generateSesi(String idKelas) {

        Connection con = Koneksi.getKoneksi();

        int sesi = 1;

        try {

            String sql = """
                        SELECT MAX(sesi_ke) AS sesi
                        FROM jadwal
                        WHERE id_kelas = ?
                        """;

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, idKelas);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                int sesiTerakhir = rs.getInt("sesi");

                if (!rs.wasNull()) {

                    sesi = sesiTerakhir + 1;

                }

            }

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }

        return sesi;

    }


    public List<Jadwal> ambilSemuaJadwalObjek() {
        List<Jadwal> daftarJadwal = new ArrayList<>();
        Connection con = Koneksi.getKoneksi();

        try {
            String sql = """
                    SELECT
                        j.id_jadwal,
                        j.tanggal_mulai,
                        j.tanggal_selesai,
                        j.sesi_ke,
                        k.id_kelas,
                        k.nama_kelas,
                        k.harga,
                        k.kapasitas
                    FROM jadwal j
                    JOIN kelas k
                    ON j.id_kelas = k.id_kelas
                    ORDER BY j.id_jadwal DESC
                    """;

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Kelas kelas = new Kelas(
                        rs.getString("id_kelas"),
                        rs.getString("nama_kelas"),
                        rs.getDouble("harga"),
                        rs.getInt("kapasitas")
                );

                daftarJadwal.add(new Jadwal(
                        rs.getString("id_jadwal"),
                        kelas,
                        rs.getString("tanggal_mulai"),
                        rs.getString("tanggal_selesai"),
                        rs.getInt("sesi_ke")
                ));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return daftarJadwal;
    }

    public List<Jadwal> ambilJadwalAktifObjek() {
        List<Jadwal> daftarJadwal = new ArrayList<>();
        String sql = """
                SELECT
                    j.id_jadwal,
                    j.tanggal_mulai,
                    j.tanggal_selesai,
                    j.sesi_ke,
                    j.status,
                    k.id_kelas,
                    k.nama_kelas,
                    k.harga,
                    k.kapasitas
                FROM jadwal j
                JOIN kelas k ON j.id_kelas = k.id_kelas
                WHERE j.status = 'A'
                ORDER BY j.tanggal_mulai ASC
                """;

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Kelas kelas = new Kelas(
                        rs.getString("id_kelas"),
                        rs.getString("nama_kelas"),
                        rs.getDouble("harga"),
                        rs.getInt("kapasitas")
                );

                Jadwal jadwal = new Jadwal(
                        rs.getString("id_jadwal"),
                        kelas,
                        rs.getString("tanggal_mulai"),
                        rs.getString("tanggal_selesai"),
                        rs.getInt("sesi_ke")
                );
                jadwal.setStatus(rs.getString("status"));
                daftarJadwal.add(jadwal);
            }
        } catch (SQLException e) {
            System.out.println("Gagal mengambil jadwal aktif: " + e.getMessage());
        }

        return daftarJadwal;
    }

    public boolean ubahStatusJadwalMenjadiNonaktif(String idJadwal) {
        String sql = "UPDATE jadwal SET status = 'T' WHERE id_jadwal = ?";
        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idJadwal);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Gagal mengubah status jadwal: " + e.getMessage());
            return false;
        }
    }

    public Jadwal cariById(String idJadwal) {
        Connection con = Koneksi.getKoneksi();

        Jadwal jadwal = null;

        try {

            String sql = """
                SELECT
                    j.id_jadwal,
                    j.tanggal_mulai,
                    j.sesi_ke,
                    j.status,

                    k.id_kelas,
                    k.nama_kelas,
                    k.harga,
                    k.kapasitas

                FROM jadwal j

                JOIN kelas k
                    ON j.id_kelas = k.id_kelas

                WHERE j.id_jadwal = ?
                """;

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, idJadwal);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Kelas kelas = new Kelas(
                        rs.getString("id_kelas"),
                        rs.getString("nama_kelas"),
                        rs.getDouble("harga"),
                        rs.getInt("kapasitas")
                );

                jadwal = new Jadwal(
                        rs.getString("id_jadwal"),
                        kelas,
                        rs.getString("tanggal_mulai"),
                        rs.getString("status"),
                        rs.getInt("sesi_ke")
                );

            }

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }

        return jadwal;
    }

}