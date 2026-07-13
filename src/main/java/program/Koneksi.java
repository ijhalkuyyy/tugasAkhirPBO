package program;

import java.sql.Connection;
import java.sql.DriverManager;

public class Koneksi {
    public static Connection getKoneksi() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            return DriverManager.getConnection(
                    "jdbc:mysql://localhost/onty_cake",
                    "root",
                    "");
        } catch (Exception e) {
            System.out.println("Koneksi gagal : "
                    + e.getMessage());
            return null;
        }
    }
}
