import javax.swing.*;
import java.awt.*;

public class COD extends Pembayaran {

    @Override
    public void bayar() {
        System.out.println("Pembayaran COD berhasil");

        tampilkanGambar();
    }

    @Override
    public void setJumlahBayar(int index, String nama, int jumlahBayar, int Kuantity) {
        int jumlahBayarCOD = jumlahBayar * Kuantity;
        System.out.println("Jumlah pembayaran COD berhasil diatur, dengan jumlah yang anda bayarkan adalah Rp. " + jumlahBayarCOD + " dengan nama " + nama);
    }

    private void tampilkanGambar() {
        JFrame frame = new JFrame("Pembayaran COD");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 500); 
        ImageIcon imageIcon = new ImageIcon("foto/cod.jpg"); 
        JLabel label = new JLabel(imageIcon);
        frame.add(label); 
        frame.setVisible(true);
    }
}
