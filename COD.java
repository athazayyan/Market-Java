public class COD extends Pembayaran{
    
    @Override
    public void bayar(){
        System.out.println("Pembayaran COD berhasil");
    }

    @Override
    public void setJumlahBayar(){
        System.out.println("Jumlah pembayaran COD berhasil diatur");
    }
    
}
