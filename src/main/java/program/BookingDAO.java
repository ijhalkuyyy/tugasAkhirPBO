package program;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BookingDAO {
    public String tampilSemuaTransaksi(){
        Connection con = Koneksi.getKoneksi();
        StringBuilder hasil = new StringBuilder();
        try {
            String sql = """
                SELECT
                    b.id_booking,
                    p.id_peserta,
                    p.nama_lengkap,
                    k.id_kelas,
                    k.nama_kelas,
                    j.id_jadwal,
                    j.tanggal_mulai,
                    b.harga,
                    b.metode_pembayaran,
                    b.status_pembayaran
                FROM booking b
                JOIN peserta p
                    ON b.id_peserta = p.id_peserta
                JOIN jadwal j
                    ON b.id_jadwal = j.id_jadwal
                JOIN kelas k
                    ON j.id_kelas = k.id_kelas
                ORDER BY b.id_booking DESC
                """;

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return "";
            }
            String format = "%-4s %-8s %-20s %-8s %-20s %-10s %-15s %-12s %-12s %-10s\n";
            hasil.append(String.format(format,
                    "NO",
                    "ID PST",
                    "NAMA PESERTA",
                    "ID KLS",
                    "NAMA KELAS",
                    "ID JWL",
                    "TGL MULAI",
                    "HARGA",
                    "METODE",
                    "STATUS"));

            hasil.append("=========================================================================================================================================\n");
            int no = 1;
            do {
                hasil.append(String.format(format,
                        no++,
                        rs.getString("id_peserta"),
                        rs.getString("nama_lengkap"),
                        rs.getString("id_kelas"),
                        rs.getString("nama_kelas"),
                        rs.getString("id_jadwal"),
                        rs.getString("tanggal_mulai"),
                        rs.getDouble("harga"),
                        rs.getString("metode_pembayaran"),
                        rs.getString("status_pembayaran")
                ));
            } while (rs.next());

        } catch (Exception e) {
            return "Error : " + e.getMessage();
        }
        return hasil.toString();

    }

    public boolean tambahBooking(Booking booking){
        Connection con = Koneksi.getKoneksi();
        try {

            String sql = """
                INSERT INTO booking
                (id_booking,
                id_peserta,
                id_jadwal,
                harga,
                metode_pembayaran,
                status_pembayaran)

                VALUES (?,?,?,?,?,?)
                """;

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, booking.getIdBooking());
            ps.setString(2, booking.getIdPeserta());
            ps.setString(3, booking.getIdJadwal());
            ps.setDouble(4, booking.getHarga());
            ps.setString(5, booking.getMetodePembayaran());
            ps.setString(6, booking.getStatusPembayaran());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println(e.getMessage());

            return false;

        }
    }

    // public Booking cariById(String idBooking){
        
    // }

    public boolean ubahStatusPembayaran(String idBooking){
        Connection con = Koneksi.getKoneksi();
        try {
            String sql = """
                UPDATE booking
                SET status_pembayaran='Lunas'
                WHERE id_booking=?
                """;
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, idBooking);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public String generateIdBooking(){
        Connection con = Koneksi.getKoneksi();
        String id = "BK001";
        try {
            String sql = "SELECT MAX(id_booking) AS max_id FROM booking";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String maxId = rs.getString("max_id");
                if (maxId != null) {
                    int angka = Integer.parseInt(maxId.substring(2));
                    angka++;
                    id = "BK" + String.format("%03d", angka);
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

}
