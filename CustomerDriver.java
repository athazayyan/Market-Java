import java.io.*;
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
        Barang barangToAdd = null;
        for (Barang barang : listBarang) {
            if (barang.id.equals(barangId) && barang.quantity >= quantity) {
                barangToAdd = new Barang(barang.id, barang.name, quantity, barang.price);
                break;
            }
        }
    
        if (barangToAdd != null) {
            cart.add(barangToAdd);
            
            // Menyimpan data barang ke file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("cart.txt", true))) {
                writer.write(barangToAdd.id + "," + barangToAdd.name + "," + barangToAdd.quantity + "," + barangToAdd.price);
                writer.newLine();
            } catch (IOException e) {
                System.out.println("Gagal menyimpan barang ke file: " + e.getMessage());
            }
    
            System.out.println("Barang ditambahkan ke keranjang: " + barangToAdd.name);
        } else {
            System.out.println("Barang tidak ditemukan atau stok barang tidak cukup.");
        }
    }

    // Checkout untuk pelanggan
    public void checkout() {
        Pembayaran pembayaran = null;
        // Membaca barang dari file cart.txt
        try (BufferedReader reader = new BufferedReader(new FileReader("cart.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String id = data[0];
                String name = data[1];
                int quantity = Integer.parseInt(data[2]);
                double price = Double.parseDouble(data[3]);
    
                // Memproses setiap barang dengan metode pembayaran
                Scanner scanner = new Scanner(System.in);
                System.out.println("Pilih metode pembayaran:");
                System.out.println("1. Bank");
                System.out.println("2. COD");
                System.out.println("3. QRIS");
                System.out.print("Pilih opsi: ");
                int paymentChoice = scanner.nextInt();
                scanner.nextLine();
    
                switch (paymentChoice) {
                    case 1:
                        pembayaran = new Bank();
                        pembayaran.setJumlahBayar(name, quantity, (int) price);
                        break;
                    case 2:
                        pembayaran = new COD();
                        pembayaran.setJumlahBayar(name, quantity, (int) price);
                        break;
                    case 3:
                        pembayaran = new QRIS();
                        pembayaran.setJumlahBayar(name, quantity, (int) price);
                        break;
                    default:
                        System.out.println("Pilihan metode pembayaran tidak valid. Checkout dibatalkan.");
                        return;
                }
            }
        } catch (IOException e) {
            System.out.println("Gagal membaca file keranjang: " + e.getMessage());
            return;
        }
    
        // Membuat ID transaksi baru
        String transaksiId = "T" + (listTransaksi.size() + 1);
    
        // Membuat objek transaksi
        Transaksi transaksi = new Transaksi(transaksiId, this.akun, new ArrayList<>(this.cart));
    
        // Membuat invoice berdasarkan transaksi dan pembayaran
        Invoice invoice = new Invoice(transaksi, pembayaran, "2024-11-24");
        invoice.printInvoice(); // Menampilkan invoice
    
        // Menyimpan transaksi ke dalam daftar transaksi
        listTransaksi.add(transaksi);
    
        // Mengosongkan keranjang dan file setelah checkout selesai
        cart.clear();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("cart.txt"))) {
            writer.write(""); // Mengosongkan isi file
        } catch (IOException e) {
            System.out.println("Gagal mengosongkan file keranjang: " + e.getMessage());
        }
    
        System.out.println("Checkout selesai. Terima kasih telah berbelanja!");
    }

    // Menampilkan riwayat transaksi
    public void viewHistory() {
        System.out.println("Melihat Riwayat: " + this.akun.id + " " + this.akun.username);
        for (Transaksi transaksi : listTransaksi) {
            System.out.println("Transaksi ID: " + transaksi.id + ", Customer: " + transaksi.akun.username);
        }
    }

    // Metode untuk menampilkan menu pelanggan
    @Override
    public void Menu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Menu Customer:");
            System.out.println("1. Lihat Barang");
            System.out.println("2. Tambah Barang ke Keranjang");
            System.out.println("3. Checkout");
            System.out.println("4. Lihat Riwayat");
            System.out.println("5. Logout");
            System.out.print("Pilih opsi: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> viewBarang();
                case 2 -> {
                    viewBarang();
                    System.out.print("Masukkan ID Barang untuk ditambahkan ke keranjang: ");
                    String barangId = scanner.nextLine();
                    System.out.print("Masukkan kuantitas: ");
                    int quantity = scanner.nextInt();
                    scanner.nextLine();
                    addBarangToCart(barangId, quantity);
                }
                case 3 -> checkout();
                case 4 -> viewHistory();
                case 5 -> {
                    System.out.println("Keluar...");
                    return;
                }
                default -> System.out.println("Opsi tidak valid. Silakan coba lagi.");
            }
        }
    }
}
