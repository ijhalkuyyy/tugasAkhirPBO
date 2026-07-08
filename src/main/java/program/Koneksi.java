package program;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Koneksi {
    public static Connection getKoneksi() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            return DriverManager.getConnection(
                    "jdbc:mysql://localhost/tugas_akhir_pbo",
                    "root",
                    "");
        } catch (Exception e) {
            System.out.println("Koneksi gagal : "
                    + e.getMessage());
            return null;
        }
    }
    
    public static void main(String[] args) {
        try {
            Connection tes = Koneksi.getKoneksi();
            if (tes != null) {
                System.out.println("Mantap ngab, Koneksi Database Berhasil!");
            }
        } catch (Exception e) {
            System.out.println("Koneksi gagal, cek lagi XAMPP-nya: " + e.getMessage());
        }
    }
}
