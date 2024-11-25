public class Invoice {
    private Transaksi transaksi;
    private Pembayaran pembayaran;
    private String tanggal;

    public Invoice(Transaksi transaksi, Pembayaran pembayaran, String tanggal) {
        this.transaksi = transaksi;
        this.pembayaran = pembayaran;
        this.tanggal = tanggal;
    }

    public void printInvoice() {
        System.out.println("Invoice ID: " + transaksi.id);
        System.out.println("Customer: " + transaksi.akun.username);
        System.out.println("Tanggal: " + tanggal);
        System.out.println("Daftar Barang:");
        for (Barang barang : transaksi.barang) {
            System.out.println(" - " + barang.name + " (x" + barang.quantity + ")");
        }
        pembayaran.bayar();
    }
}
