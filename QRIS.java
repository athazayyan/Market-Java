public class QRIS extends Pembayaran{
    
    @Override
    public void bayar(){
        System.out.println("Pembayaran QRIS berhasil");
    }

    @Override
    public void setJumlahBayar(){
        System.out.println("Jumlah pembayaran QRIS berhasil diatur");
    }
    
}
