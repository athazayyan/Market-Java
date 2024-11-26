public class COD extends Pembayaran{
    
    @Override
    public void bayar(){
        System.out.println("Pembayaran COD berhasil");
        
    }

    @Override
    public void setJumlahBayar(int index, String nama, int jumlahBayar, int Kuantity){
        int jumlahBayarCOD = jumlahBayar * Kuantity;
        System.out.println("Jumlah pembayaran COD berhasil diatur, dengan jumlah yang anda bayarkan adalah Rp. " + jumlahBayarCOD + " dengan nama " + nama);
    }
    
}
