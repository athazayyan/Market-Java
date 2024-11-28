import javax.swing.*;
import java.awt.*;

public class COD extends Pembayaran {

    @Override
    public void bayar() {
        System.out.println("Pembayaran COD berhasil");
           
    }

   
@Override
public void setJumlahBayar(int index, String nama, int Kuantity, int jumlahBayar) {
    int jumlahBayarCOD = jumlahBayar * Kuantity;
    System.out.println("Jumlah pembayaran COD berhasil diatur, dengan jumlah yang anda bayarkan adalah Rp. " + jumlahBayarCOD + " dengan nama " + nama);
    tampilkanGambar(nama, Kuantity, jumlahBayarCOD);
}

public void tampilkanGambar(String nama, int Kuantity, int jumlahBayarCOD) {
    JFrame frame = new JFrame("Pembayaran COD");
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setSize(500, 500);
    
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    
    ImageIcon imageIcon = new ImageIcon("foto/cod.jpg");
    JLabel labelGambar = new JLabel(imageIcon);
    panel.add(labelGambar);
    
    JLabel labelInfo = new JLabel("Nama Barang: " + nama + ", Kuantity: " + Kuantity + ", Jumlah Bayar: Rp. " + jumlahBayarCOD);
    panel.add(labelInfo);
    
    JButton okButton = new JButton("OK");
    okButton.addActionListener(e -> frame.dispose());
    panel.add(okButton);
    
    frame.add(panel);
    frame.setVisible(true);
}
}