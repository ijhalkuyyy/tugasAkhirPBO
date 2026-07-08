package program;

public class Kelas {

    private String idKelas;
    private String namaKelas;
    private double harga;
    private int kapasitas;

    public Kelas(String idKelas, String namaKelas, double harga, int kapasitas) {
        this.idKelas = idKelas;
        this.namaKelas = namaKelas;
        this.harga = harga;
        this.kapasitas = kapasitas;
    }

    public void setIdKelas(String idKelas) {
        this.idKelas = idKelas;
    }

    public void setNamaKelas(String namaKelas) {
        this.namaKelas = namaKelas;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }   

    public void setKapasitas(int kapasitas) {
        this.kapasitas = kapasitas;
    }

    public String getIdKelas() {
        return idKelas;
    }

    public String getNamaKelas() {
        return namaKelas;
    }

    public double getHarga() {
        return harga;
    }

    public int getKapasitas() {
        return kapasitas;
    }

}
