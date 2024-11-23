public class Bank extends Pembayaran{
    
    @Override
    public void bayar(){
        System.out.println("Pembayaran Bank berhasil");
    }

    @Override
    public void setJumlahBayar(){
        System.out.println("Jumlah pembayaran Bank berhasil diatur");
    }
// Sementaraaa ini dlu
}
