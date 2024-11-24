import java.util.*;

public class CustomerDriver extends Driver {
    ArrayList<Barang> cart;
    ArrayList<Barang> listBarang;
    ArrayList<Transaksi> listTransaksi;
    Customer akun;
    ListBarang listBarangObj = new ListBarang();

    // Konstruktor
    CustomerDriver(Customer akun, ArrayList<Barang> cart, ArrayList<Barang> listBarang, ArrayList<Transaksi> listTransaksi) {
        this.akun = akun;
        this.cart = cart;
        this.listBarang = listBarang;
        this.listTransaksi = listTransaksi;
    }

    @Override
    public void viewBarang() {
        // Menampilkan daftar barang yang ada
        listBarangObj.listBarang();
    }

    // Menambahkan barang ke dalam cart
    public void addBarangToCart(String barangId, int quantity) {
        viewBarang();
        Barang barangToAdd = null;
        for (Barang barang : listBarang) {
            if (barang.id.equals(barangId) && barang.quantity >= quantity) {
                barangToAdd = new Barang(barang.id, barang.name, quantity);
                break;
            }
        }
        if (barangToAdd != null) {
            cart.add(barangToAdd);
            System.out.println("Barang added to cart: " + barangToAdd.name);
        } else {
            System.out.println("Barang not available or insufficient quantity.");
        }
    }

    // Checkout untuk pelanggan
    // Checkout untuk pelanggan
public void checkout() {
    // Menampilkan pilihan metode pembayaran terlebih dahulu
    Scanner scanner = new Scanner(System.in);
    System.out.println("Pilih metode pembayaran:");
    System.out.println("1. Bank");
    System.out.println("2. COD");
    System.out.println("3. QRIS");
    System.out.print("Pilih opsi: ");
    int paymentChoice = scanner.nextInt();
    scanner.nextLine();

    Pembayaran pembayaran = null;
    switch (paymentChoice) {
        case 1:
            pembayaran = new Bank();  // Membuat objek pembayaran tipe Bank
            break;
        case 2:
            pembayaran = new COD();  // Membuat objek pembayaran tipe COD
            break;
        case 3:
            pembayaran = new QRIS(); // Membuat objek pembayaran tipe QRIS
            break;
        default:
            System.out.println("Pilihan metode pembayaran tidak valid. Checkout dibatalkan.");
            return; // Keluar dari metode jika pilihan tidak valid
    }

    // Jika metode pembayaran valid, lanjutkan proses checkout
    System.out.println("Checkout dimulai untuk pelanggan: " + akun.username);

    // Membuat ID transaksi baru
    String transaksiId = "T" + (listTransaksi.size() + 1);

    // Membuat objek transaksi
    Transaksi transaksi = new Transaksi(transaksiId, this.akun, new ArrayList<>(this.cart));

    // Membuat invoice berdasarkan transaksi dan pembayaran
    Invoice invoice = new Invoice(transaksi, pembayaran, "2024-11-24");
    invoice.printInvoice();  // Menampilkan invoice

    // Menyimpan transaksi ke dalam daftar transaksi
    listTransaksi.add(transaksi);

    // Mengosongkan keranjang setelah checkout selesai
    cart.clear();
    System.out.println("Checkout selesai. Terima kasih telah berbelanja!");
}


    

    // Menampilkan riwayat transaksi
    public void viewHistory() {
        System.out.println("Viewing history for: " + this.akun.id);
        // Menampilkan riwayat transaksi
        for (Transaksi transaksi : listTransaksi) {
            System.out.println("Transaksi ID: " + transaksi.id + ", Customer: " + transaksi.akun.username);
        }
    }
}
