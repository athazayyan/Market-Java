import javax.swing.*;
import java.awt.*;

public class QRIS extends Pembayaran{
    
    @Override
    public void bayar(){
        System.out.println("Pembayaran QRIS berhasil");
        tampilkanGambar();
    }

    @Override
    public void setJumlahBayar(int index, String nama, int jumlahBayar, int Kuantity){
        int jumlahBayarQRIS = jumlahBayar * Kuantity;
        System.out.println("Jumlah pembayaran QRIS berhasil diatur, jumlah yang anda Bayarkan adalah" + jumlahBayarQRIS+ "dengan nama" + nama);
    }

    private void tampilkanGambar() {
        JFrame frame = new JFrame("Pembayaran QRIS");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 500); 
        ImageIcon imageIcon = new ImageIcon("foto/QRS.png"); 
        JLabel label = new JLabel(imageIcon);
        frame.add(label); 
        frame.setVisible(true);
    }
    
}


