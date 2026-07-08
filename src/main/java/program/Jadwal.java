package program;
public class Jadwal {

    private String idJadwal;
    private Kelas kelas;
    private String tanggalMulai;

    public Jadwal(String idJadwal, Kelas kelas, String tanggalMulai) {
        this.idJadwal = idJadwal;
        this.kelas = kelas;
        
        this.tanggalMulai = tanggalMulai;
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

    public String getIdJadwal() {
        return idJadwal;
    }

    public Kelas getKelas() {
        return kelas;
    }

    public String getTanggalMulai() {
        return tanggalMulai;
    }

}
