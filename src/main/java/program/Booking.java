package program;

public class Booking {
    private String idBooking;
    private Peserta peserta;
    private Jadwal jadwal;
    private String metodePembayaran;
    private String statusPembayaran;



    public Booking(String idBooking, Peserta peserta, Jadwal jadwal, String metodePembayaran, String statusPembayaran) {
        this.idBooking = idBooking;
        this.peserta = peserta;
        this.jadwal = jadwal;
        this.metodePembayaran = metodePembayaran;
        this.statusPembayaran = statusPembayaran;
    }
}
