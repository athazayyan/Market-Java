import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Bank extends Pembayaran {

    @Override
    public void bayar() {
        System.out.println("Pembayaran Bank berhasil");
        // Panggil metode untuk menampilkan pop-up dengan desain
        tampilkanPopup("Pembayaran Bank berhasil");
    }

    @Override
    public void setJumlahBayar(int index, String nama, int jumlahBayar, int kuantity) {
        int jumlahBayarBank = jumlahBayar * kuantity;
        String detailPembayaran = "<html><body><h2>Pembayaran Berhasil</h2>"
                + "<p>Nama Barang: " + nama + "</p>"
                + "<p>Harga Barang: Rp. " + jumlahBayar + "</p>"
                + "<p>Jumlah: " + kuantity + "</p>"
                + "<p><strong>Total: Rp. " + jumlahBayarBank + "</strong></p></body></html>";
        
        System.out.println("Jumlah pembayaran Bank berhasil diatur: " + jumlahBayarBank);
        
        // Tampilkan popup dengan detail pembayaran
        tampilkanPopup(detailPembayaran);
    }

    // Metode untuk menampilkan dialog pop-up dengan desain yang menarik
    private void tampilkanPopup(String pesan) {
        // Membuat JDialog sebagai pop-up
        JDialog dialog = new JDialog();
        dialog.setTitle("Konfirmasi Pembayaran Bank");
        dialog.setSize(400, 300);
        dialog.setLayout(new BorderLayout());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(null); // Agar pop-up muncul di tengah layar

        // Panel utama dengan background warna biru lembut
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(173, 216, 230)); // Warna biru lembut
        
        // Label untuk menampilkan pesan
        JLabel label = new JLabel(pesan);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(new Color(0, 51, 102)); // Warna teks biru gelap
        panel.add(label, BorderLayout.CENTER);

        // Tombol OK dengan warna hijau dan font besar
        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Arial", Font.PLAIN, 14));
        okButton.setBackground(new Color(60, 179, 113)); // Warna hijau
        okButton.setForeground(Color.WHITE); // Warna teks putih
        okButton.setFocusPainted(false); // Menghilangkan border saat tombol dipilih
        okButton.setPreferredSize(new Dimension(100, 40));
        
        // Listener untuk menutup dialog ketika tombol OK diklik
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose(); // Menutup dialog
            }
        });

        // Panel bawah untuk meletakkan tombol OK
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(173, 216, 230)); // Warna yang sama dengan panel utama
        bottomPanel.add(okButton);
        
        // Menambahkan panel dan tombol ke dialog
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        // Menampilkan dialog
        dialog.setVisible(true);
    }
}
