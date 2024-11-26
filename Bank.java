public class Bank extends Pembayaran{
    
    @Override
    public void bayar(){
        System.out.println("Pembayaran Bank berhasil");
    }

    @Override
    public void setJumlahBayar(String nama, int jumlahBayar, int Kuantity){
        int jumlahBayarBank = jumlahBayar * Kuantity;
        System.out.println("Jumlah pembayaran Bank berhasil diatur, dengan jumlah yang anda bayarkan adalah Rp. " + jumlahBayarBank + " dengan nama " + nama);
    }
// Sementaraaa ini dlu
}
