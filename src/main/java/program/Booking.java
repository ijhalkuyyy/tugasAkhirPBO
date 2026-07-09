package program;

public class Booking {

    private String idBooking;
    private String idPeserta;
    private String idJadwal;
    private double harga;
    private String metodePembayaran;
    private String statusPembayaran;

    public Booking(String idBooking,
                   String idPeserta,
                   String idJadwal,
                   double  harga,
                   String metodePembayaran,
                   String statusPembayaran) {

        this.idBooking = idBooking;
        this.idPeserta = idPeserta;
        this.idJadwal = idJadwal;
        this.harga = harga;
        this.metodePembayaran = metodePembayaran;
        this.statusPembayaran = statusPembayaran;
    }

    public double getHarga() {
        return harga;
    }

    public String getIdBooking() {
        return idBooking;
    }

    public String getIdPeserta() {
        return idPeserta;
    }

    public String getIdJadwal() {
        return idJadwal;
    }

    public String getMetodePembayaran() {
        return metodePembayaran;
    }

    public String getStatusPembayaran() {
        return statusPembayaran;
    }

    public void setIdBooking(String idBooking) {
        this.idBooking = idBooking;
    }

    public void setIdPeserta(String idPeserta) {
        this.idPeserta = idPeserta;
    }

    public void setIdJadwal(String idJadwal) {
        this.idJadwal = idJadwal;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public void setMetodePembayaran(String metodePembayaran) {
        this.metodePembayaran = metodePembayaran;
    }

    public void setStatusPembayaran(String statusPembayaran) {
        this.statusPembayaran = statusPembayaran;
    }

}