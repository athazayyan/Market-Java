abstract public class Pembayaran {
    String idPembayaran;
    String tanggal;

    abstract public void bayar();
    abstract public void setJumlahBayar(String nama, int jumlahBayar, int Kuantity);
    
}
