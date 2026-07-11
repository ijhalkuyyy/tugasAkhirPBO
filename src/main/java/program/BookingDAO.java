package program;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {
    public String tampilSemuaTransaksi(){
        return tampilSemuaTransaksi(null);
    }

    public String tampilSemuaTransaksi(String statusPembayaran){
        Connection con = Koneksi.getKoneksi();
        StringBuilder hasil = new StringBuilder();
        try {
            StringBuilder sql = new StringBuilder("""
                SELECT
                    b.id_booking,
                    p.id_peserta,
                    p.nama_lengkap,
                    k.id_kelas,
                    k.nama_kelas,
                    j.id_jadwal,
                    j.tanggal_mulai,
                    j.sesi_ke,
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
                    WHERE j.status = 'a'
                """);

            if (statusPembayaran != null && !statusPembayaran.isBlank()) {
                sql.append(" AND b.status_pembayaran = ?");
            }
            sql.append(" ORDER BY b.id_booking DESC");

            PreparedStatement ps = con.prepareStatement(sql.toString());
            if (statusPembayaran != null && !statusPembayaran.isBlank()) {
                ps.setString(1, statusPembayaran);
            }
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return "";
            }
            String format = "%-4s %-12s %-20s %-28s %-12s %-12s %-12s %-10s\n";
            hasil.append(String.format(format,
                    "NO",
                    "ID BOOKING",
                    //"ID PST",
                    "NAMA PESERTA",
                    //"ID KLS",
                    "NAMA KELAS/SESI",
                    "TGL MULAI",
                    "Total Bayar",
                    "METODE",
                    "STATUS"));

            hasil.append("================================================================================================================\n");
            int no = 1;
            do {
                hasil.append(String.format(format,
                        no++,
                        rs.getString("id_booking"),
                        //rs.getString("id_peserta"),2
                        rs.getString("nama_lengkap"),
                        //rs.getString("id_kelas"),
                        rs.getString("nama_kelas") + " (Sesi " + rs.getInt("sesi_ke") + ")",
                        //rs.getString("id_jadwal"),
                        rs.getString("tanggal_mulai"),
                        String.format("%.0f", rs.getDouble("harga")),
                        rs.getString("metode_pembayaran"),
                        rs.getString("status_pembayaran")
                ));
            } while (rs.next());
        } catch (Exception e) {
            return "Error : " + e.getMessage();
        }
        return hasil.toString();

    }

    public String tampilSemuaRekapTransaksi() { 
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
                    j.sesi_ke,
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
            
            String format = "%-4s %-12s %-20s %-28s %-12s %-12s %-12s %-10s\n";
            hasil.append(String.format(format,
                    "NO",
                    "ID BOOKING",
                    "NAMA PESERTA",
                    "NAMA KELAS/SESI",
                    "TGL MULAI",
                    "Total Bayar",
                    "METODE",
                    "STATUS"));

            hasil.append("================================================================================================================\n");
            int no = 1;
            do {
                hasil.append(String.format(format,
                        no++,
                        rs.getString("id_booking"),
                        rs.getString("nama_lengkap"),
                        rs.getString("nama_kelas") + " (Sesi " + rs.getInt("sesi_ke") + ")",
                        rs.getString("tanggal_mulai"),
                        String.format("%.0f", rs.getDouble("harga")),
                        rs.getString("metode_pembayaran"),
                        rs.getString("status_pembayaran")
                ));
            } while (rs.next());

        } catch (Exception e) {
            return "Error : " + e.getMessage();
        }
        return hasil.toString();
    }

    public String tampilTransaksiBerdasarkanJadwal(String idJadwal) {
        Connection con = Koneksi.getKoneksi();
        StringBuilder hasil = new StringBuilder();
        try {
            String sql = """
                SELECT 
                    b.id_booking, 
                    p.nama_lengkap, 
                    k.nama_kelas, 
                    j.tanggal_mulai, 
                    j.sesi_ke, 
                    b.harga, 
                    b.metode_pembayaran, 
                    b.status_pembayaran 
                FROM booking b 
                JOIN peserta p ON b.id_peserta = p.id_peserta 
                JOIN jadwal j ON b.id_jadwal = j.id_jadwal 
                JOIN kelas k ON j.id_kelas = k.id_kelas 
                WHERE j.id_jadwal = ? 
                ORDER BY b.id_booking DESC
                """;

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, idJadwal);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return "";
            }

            String format = "%-4s %-12s %-20s %-28s %-12s %-12s %-12s %-10s\n";
            hasil.append(String.format(format,
                    "NO", "ID BOOKING", "NAMA PESERTA", "NAMA KELAS/SESI",
                    "TGL MULAI", "Total Bayar", "METODE", "STATUS"));

            hasil.append("================================================================================================================\n");
            
            int no = 1;
            do {
                hasil.append(String.format(format,
                        no++,
                        rs.getString("id_booking"),
                        rs.getString("nama_lengkap"),
                        rs.getString("nama_kelas") + " (Sesi " + rs.getInt("sesi_ke") + ")",
                        rs.getString("tanggal_mulai"),
                        String.format("%.0f", rs.getDouble("harga")),
                        rs.getString("metode_pembayaran"),
                        rs.getString("status_pembayaran")
                ));
            } while (rs.next());

        } catch (Exception e) {
            return "Error : " + e.getMessage();
        }
        return hasil.toString();
    }

    public boolean cekBookingDuplikat(String idPeserta, String idJadwal) {
        Connection con = Koneksi.getKoneksi();
        try {
            String sql = "SELECT COUNT(*) AS total FROM booking WHERE id_peserta = ? AND id_jadwal = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, idPeserta);
            ps.setString(2, idJadwal);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total") > 0;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
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

    public List<Booking> ambilSemuaBookingObjek() {
        List<Booking> daftarBooking = new ArrayList<>();
        Connection con = Koneksi.getKoneksi();
        try {
            String sql = """
                SELECT id_booking, id_peserta, id_jadwal, harga, metode_pembayaran, status_pembayaran
                FROM booking
                ORDER BY id_booking DESC
                """;
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                daftarBooking.add(new Booking(
                        rs.getString("id_booking"),
                        rs.getString("id_peserta"),
                        rs.getString("id_jadwal"),
                        rs.getDouble("harga"),
                        rs.getString("metode_pembayaran"),
                        rs.getString("status_pembayaran")
                ));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return daftarBooking;
    }

    public boolean ubahStatusPembayaran(String idBooking){
        Connection con = Koneksi.getKoneksi();
        try {
            String sql = """
                UPDATE booking
                SET status_pembayaran = 'Lunas',
                    harga = harga * 2
                WHERE id_booking = ?
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

    public List<String[]> ambilDataKelasUntukCombo() {
        List<String[]> list = new ArrayList<>();
        Connection con = Koneksi.getKoneksi();
        try {
            String sql = "SELECT id_kelas, nama_kelas FROM kelas ORDER BY nama_kelas ASC";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new String[]{rs.getString("id_kelas"), rs.getString("nama_kelas")});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String[]> ambilDataJadwalUntukCombo(String idKelas, String statusJadwal) {
        List<String[]> list = new ArrayList<>();
        Connection con = Koneksi.getKoneksi();
        try {
            String sql = "SELECT id_jadwal, tanggal_mulai, sesi_ke FROM jadwal WHERE id_kelas = ? AND status = ? ORDER BY tanggal_mulai ASC";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, idKelas);
            ps.setString(2, statusJadwal);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                String idJadwal = rs.getString("id_jadwal");
                String label = rs.getString("tanggal_mulai") + " (Sesi " + rs.getInt("sesi_ke") + ")";
                list.add(new String[]{idJadwal, label});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
