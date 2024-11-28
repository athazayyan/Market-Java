abstract public class Pembayaran {
    String idPembayaran;
    String tanggal;
    Invoice invoice;

    abstract public void bayar();
    abstract public void setJumlahBayar(int index, String nama, int jumlahBayar, int Kuantity);
    
}
