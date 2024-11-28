import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QRIS extends Pembayaran {

    @Override
    public void bayar() {
        System.out.println("\n\nMetode Pembayaran: QRIS");
        System.out.println("Pembayaran QRIS Sedang Diproses...");
        // Tampilkan pop-up dengan pesan bahwa pembayaran berhasil
        tampilkanPopup("Pembayaran QRIS berhasil");
    }

    @Override
    public void setJumlahBayar(int index, String nama, int kuantity, int jumlahBayar) {
        int jumlahBayarQRIS = jumlahBayar * kuantity;
        String detailPembayaran = "<html><body><h2>Pembayaran QRIS Berhasil</h2>"
                + "<p>Nama Barang: " + nama + "</p>"
                + "<p>Harga Barang: Rp. " + jumlahBayar + "</p>"
                + "<p>Jumlah: " + kuantity + "</p>"
                + "<p><strong>Total: Rp. " + jumlahBayarQRIS + "</strong></p></body></html>";

        System.out.println("Jumlah pembayaran QRIS berhasil diatur: " + jumlahBayarQRIS);

        // Tampilkan pop-up dengan detail pembayaran dan QR code
        tampilkanPopup(detailPembayaran);
    }

    // Metode untuk menampilkan dialog pop-up dengan desain yang menarik dan menampilkan QR code
    private void tampilkanPopup(String pesan) {
        // Membuat JDialog sebagai pop-up
        JDialog dialog = new JDialog();
        dialog.setTitle("Konfirmasi Pembayaran QRIS");
        dialog.setSize(500, 600);
        dialog.setLayout(new BorderLayout());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(null); // Agar pop-up muncul di tengah layar

        // Panel utama dengan background warna putih lembut
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Label untuk menampilkan pesan detail pembayaran
        JLabel label = new JLabel(pesan);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.TOP);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(new Color(0, 102, 51)); // Warna teks hijau gelap
        panel.add(label, BorderLayout.NORTH);

        // Menampilkan gambar kode QR dari file
        ImageIcon imageIcon = new ImageIcon("foto/QRS.png");
        JLabel qrCodeLabel = new JLabel(imageIcon);
        qrCodeLabel.setHorizontalAlignment(JLabel.CENTER);
        qrCodeLabel.setVerticalAlignment(JLabel.CENTER);
        panel.add(qrCodeLabel, BorderLayout.CENTER);

        // Tombol OK untuk menutup pop-up
        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Arial", Font.PLAIN, 14));
        okButton.setBackground(new Color(60, 179, 113)); // Warna hijau
        okButton.setForeground(Color.WHITE); // Warna teks putih
        okButton.setFocusPainted(false);
        okButton.setPreferredSize(new Dimension(100, 40));

        // Listener untuk menutup dialog saat tombol OK diklik
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose(); // Menutup dialog
            }
        });

        // Panel bawah untuk tombol OK
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(okButton);

        // Menambahkan komponen ke dialog
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        // Menampilkan dialog
        dialog.setVisible(true);
    }
}


