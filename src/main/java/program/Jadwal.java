package program;
public class Jadwal {

    private String idJadwal;
    private Kelas kelas;
    private String tanggalMulai;
    private String tanggalSelesai;
    private int sesi;

    public Jadwal(String idJadwal, Kelas kelas, String tanggalMulai, String tanggalSelesai, int sesi) {
        this.idJadwal = idJadwal;
        this.kelas = kelas;
        this.tanggalMulai = tanggalMulai;
        this.tanggalSelesai = tanggalSelesai;
        this.sesi = sesi;
    }

    public void setIdJadwal(String idJadwal) {
        this.idJadwal = idJadwal;
    }

    public void setKelas(Kelas kelas) {
        this.kelas = kelas;
    }

    public void setTanggalMulai(String tanggalMulai) {
        this.tanggalMulai = tanggalMulai;
    }

    public void setTanggalSelesai(String tanggalSelesai) {
        this.tanggalSelesai = tanggalSelesai;
    }

    public void setSesi(int sesi) {
        this.sesi = sesi;
    }

    public String getIdJadwal() {
        return idJadwal;
    }

    public Kelas getKelas() {
        return kelas;
    }

    public String getTanggalMulai() {
        return tanggalMulai;
    }

    public String getTanggalSelesai() {
        return tanggalSelesai;
    }

    public int getSesiKe() {
        return sesi;
    }

}
