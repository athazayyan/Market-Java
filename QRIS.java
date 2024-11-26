public class QRIS extends Pembayaran{
    
    @Override
    public void bayar(){
        System.out.println("Pembayaran QRIS berhasil");
    }

    @Override
    public void setJumlahBayar(int index, String nama, int jumlahBayar, int Kuantity){
        int jumlahBayarQRIS = jumlahBayar * Kuantity;
        System.out.println("Jumlah pembayaran QRIS berhasil diatur, jumlah yang anda Bayarkan adalah" + jumlahBayarQRIS+ "dengan nama" + nama);
    }
    
}
