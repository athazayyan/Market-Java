import javax.swing.*;
import java.awt.*;

public class Bank extends Pembayaran{
    
    @Override
    public void bayar(){
        System.out.println("Pembayaran Bank berhasil");
        tampilkanGambar();
    }

    @Override
    public void setJumlahBayar(int index, String nama, int jumlahBayar, int Kuantity){
        int jumlahBayarBank = jumlahBayar * Kuantity;
        System.out.println("Jumlah pembayaran Bank berhasil diatur, dengan jumlah yang anda bayarkan adalah Rp. " + jumlahBayarBank + " dengan nama " + nama);
    }
    private void tampilkanGambar() {
        JFrame frame = new JFrame("Pembayaran BANK");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 500); 
        ImageIcon imageIcon = new ImageIcon("foto/bank.jpg"); 
        JLabel label = new JLabel(imageIcon);
        frame.add(label); 
        frame.setVisible(true);
    }
// Sementaraaa ini dlu
}



