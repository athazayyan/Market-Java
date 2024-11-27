import java.io.*;
import java.util.*;
import java.time.LocalDate;

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
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("cart" + akun.id + ".txt", true))) {
                // Menggunakan cart.size() untuk memastikan indeks dimulai dari 1
                int index = cart.size(); // cart.size() sudah mulai dari 1 ketika barang pertama dimasukkan
                writer.write(index + "," + barangToAdd.id + "," + barangToAdd.name + "," + barangToAdd.quantity + "," + barangToAdd.price);
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
        String tanggalHariIni = LocalDate.now().toString();
        Pembayaran pembayaran = null;
    
        // Menampilkan barang-barang dalam keranjang
        System.out.println("Barang-barang dalam keranjang:");
        List<String> cartLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("cart" + akun.id + ".txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                cartLines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Gagal membaca file keranjang: " + e.getMessage());
            return;
        }
    
        // Meminta pengguna memilih indeks barang yang akan di-checkout
        Scanner scanner = new Scanner(System.in);
        System.out.print("Masukkan indeks barang yang akan di-checkout: ");
        int barangIndex = scanner.nextInt();
        scanner.nextLine(); // Mengonsumsi newline
    
        Barang barangToCheckout = null;
        String lineToRemove = null;
    
        // Mencari barang dengan indeks yang sesuai di dalam cartLines
        for (String line : cartLines) {
            String[] data = line.split(",");
            int index = Integer.parseInt(data[0]);
            if (index == barangIndex) {
                String id = data[1];
                String name = data[2];
                int quantity = Integer.parseInt(data[3]);
                int price = Integer.parseInt(data[4]);
                barangToCheckout = new Barang(id, name, quantity, price);
                lineToRemove = line;
                break;
            }
        }
    
        if (barangToCheckout == null) {
            System.out.println("Barang dengan indeks tersebut tidak ditemukan.");
            return;
        }
    
        // Meminta pengguna memilih metode pembayaran
        System.out.println("Pilih metode pembayaran:");
        System.out.println("1. Bank");
        System.out.println("2. COD");
        System.out.println("3. QRIS");
        System.out.print("Pilih opsi: ");
        int paymentChoice = scanner.nextInt();
        scanner.nextLine(); // Mengonsumsi newline
    
        switch (paymentChoice) {
            case 1:
                pembayaran = new Bank();
                pembayaran.setJumlahBayar(barangIndex, barangToCheckout.name, barangToCheckout.quantity, barangToCheckout.price);
                break;
            case 2:
                pembayaran = new COD();
                pembayaran.setJumlahBayar(barangIndex, barangToCheckout.name, barangToCheckout.quantity, barangToCheckout.price);
                break;
            case 3:
                pembayaran = new QRIS();
                pembayaran.setJumlahBayar(barangIndex, barangToCheckout.name, barangToCheckout.quantity, barangToCheckout.price);
                break;
            default:
                System.out.println("Pilihan metode pembayaran tidak valid. Checkout dibatalkan.");
                return;
        }
    
        // Membuat ID transaksi baru
        String transaksiId = "T" + (listTransaksi.size() + 1);
    
        // Membuat objek transaksi
        ArrayList<Barang> selectedBarang = new ArrayList<>();
        selectedBarang.add(barangToCheckout);
        Transaksi transaksi = new Transaksi(transaksiId, this.akun, selectedBarang);
    
        // Membuat invoice berdasarkan transaksi dan pembayaran
        Invoice invoice = new Invoice(transaksi, pembayaran, tanggalHariIni);
        invoice.printInvoice(); // Menampilkan invoice
    
        // Menyimpan transaksi ke dalam daftar transaksi
        listTransaksi.add(transaksi);
    
        // Menghapus barang yang telah di-checkout dari daftar cart dan menulis ulang file
        cart.remove(barangToCheckout);
        cartLines.remove(lineToRemove);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("cart" + akun.id + ".txt"))) {
            for (String line : cartLines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Gagal menghapus barang dari file keranjang: " + e.getMessage());
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
            if (!scanner.hasNextLine()) {
                break; // Menghindari NoSuchElementException jika tidak ada input
            }

            switch (choice) {
                case 1:
                    viewBarang();
                    break;
                case 2:
                    viewBarang();
                    scanner.nextLine();
                    System.out.print("Masukkan ID Barang untuk ditambahkan ke keranjang: ");
                    String barangId = scanner.nextLine();
                    System.out.print("Masukkan kuantitas: ");
                    int quantity = scanner.nextInt();
                    scanner.nextLine();
                    addBarangToCart(barangId, quantity);
                    break;
                case 3:
                    checkout();
                    break;
                case 4:
                    viewHistory();
                    break;
                case 5:
                    System.out.println("Keluar...");
                    return;
                default:
                    System.out.println("Opsi tidak valid. Silakan coba lagi.");
                    break;
            }
        }
    }
}
