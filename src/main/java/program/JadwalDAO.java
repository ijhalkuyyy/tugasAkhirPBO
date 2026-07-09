package program;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

// public class JadwalDAO {
//     public String tampilSemuaJadwal(){
//         Connection con = Koneksi.getKoneksi();
//         StringBuilder hasil = new StringBuilder();
//         try {
//             String sql = "SELECT * FROM jadwal order by id_jadwal desc";
//             PreparedStatement ps = con.prepareStatement(sql);
//             ResultSet rs = ps.executeQuery();

//             if (!rs.next()) {
//                 return "";
//             }

//             String formatKolom = "%-12s %-20s %-15s %-12s\n";

//             hasil.append(String.format(formatKolom, "ID", "NAMA", "HARGA", "KAPASITAS"));
//             hasil.append("===============================================================\n");

//             do {
//                 hasil.append(String.format(formatKolom,
//                 rs.getString("id_jadwal"),
//                 rs.getString("nama_kelas"),
//                 rs.getString("harga"),
//                 rs.getString("kapasitas")));
//             } while (rs.next());

//         } catch (Exception e) {
//             hasil.append("Error: ").append(e.getMessage());
//         }
//         return hasil.toString();
//     }

//     String geStringenerateIdJadwal() {
//         Connection con = Koneksi.getKoneksi();
//         String id = "";
//         try {
//             String sql = "SELECT MAX(id_jadwal) AS max_id FROM jadwal";
//             PreparedStatement ps = con.prepareStatement(sql);
//             ResultSet rs = ps.executeQuery();

//             if (rs.next()) {
//                 String maxId = rs.getString("max_id");
//                 if (maxId != null) {
//                     int nextId = Integer.parseInt(maxId.substring(1)) + 1;
//                     id = String.format("J%03d", nextId);
//                 } else {
//                     id = "J001";
//                 }
//             }
//         } catch (Exception e) {
//             System.out.println(e.getMessage());
//         }
//         return id;
//     }

//     public String tampilJadwalPerKelas(String idKelas){
//         return null;

//     }

//     // public boolean tambahJadwal(Jadwal jadwal){
//     //     Kelas kelas = jadwal.getKelas();
//     //     Connection con = Koneksi.getKoneksi();
//     //     try {
//     //         String sql = "INSERT INTO jadwal (id_jadwal, nama_kelas, harga, kapasitas) VALUES (?, ?, ?, ?)";
//     //         PreparedStatement ps = con.prepareStatement(sql);
//     //         ps.setString(1, jadwal.getIdJadwal());
//     //         ps.setString(2, kelas.getNamaKelas());
//     //         ps.setDouble(3, kelas.getHarga());
//     //         ps.setInt(4, jadwal.getKapasitas());
//     //         ps.executeUpdate();
//     //         return true;
//     //     } catch (Exception e) {
//     //         System.out.println(e.getMessage());
//     //         return false;
//     //     }

//     // }

//     public String generateIdJadwal(){
//         return null;
//     }
// }






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
                ORDER BY j.id_jadwal DESC
                """;

        PreparedStatement ps = con.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {
            return "";
        }

        String format = "%-12s %-30s %-18s %-18s %-8s\n";

        hasil.append(String.format(format,
                "ID",
                "KELAS",
                "TANGGAL MULAI",
                "TANGGAL SELESAI",
                "SESI"));

        hasil.append("==========================================================================\n");

        do {

            hasil.append(String.format(format,

                    rs.getString("id_jadwal"),

                    rs.getString("id_kelas")
                    + " - "
                    + rs.getString("nama_kelas"),

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
                (id_jadwal,id_kelas,tanggal_mulai,tanggal_selesai,sesi_ke)

                VALUES(?,?,?,?,?)
                """;

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, jadwal.getIdJadwal());

        ps.setString(2, jadwal.getKelas().getIdKelas());

        ps.setString(3, jadwal.getTanggalMulai());

        ps.setString(4, jadwal.getTanggalSelesai());

        ps.setInt(5, jadwal.getSesiKe());

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

}