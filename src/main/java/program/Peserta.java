package program;
public class Peserta {

    private String idPeserta;
    private String namaLengkap;
    private String namaPanggilan;
    private String noHp;
    
    
    public Peserta(String idPeserta, String namaLengkap, String namaPanggilan, String noHp) {
        this.idPeserta = idPeserta;
        this.namaLengkap = namaLengkap;
        this.namaPanggilan = namaPanggilan;
        this.noHp = noHp;
    }

    public void setIdPeserta(String idPeserta) {
        this.idPeserta = idPeserta;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public void setNamaPanggilan(String namaPanggilan) {
        this.namaPanggilan = namaPanggilan;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getIdPeserta() {
        return idPeserta;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public String getNamaPanggilan() {
        return namaPanggilan;
    }

    public String getNoHp() {
        return noHp;
    }
    
}
