import java.awt.image.IndexColorModel;
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
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("cart" + akun.id + ".txt", true))) {
                
                writer.write(cart.size() + "," + barangToAdd.id + "," + barangToAdd.name + "," + barangToAdd.quantity + "," + barangToAdd.price);
                writer.newLine();
            } catch (IOException e) {
                System.out.println("Gagal menyimpan barang ke file: " + e.getMessage());
            }
    
            System.out.println("Barang ditambahkan ke keranjang: " + barangToAdd.name);
        } else {
            System.out.println("Barang tidak ditemukan atau stok barang tidak cukup.");
        }
    }

    public void checkout() {
        Pembayaran pembayaran = null;
        List<String> selectedItems = new ArrayList<>();
        List<String> cartItems = new ArrayList<>(); // Menyimpan semua barang dari keranjang
    
        // Membaca barang dari file cart.txt
        try (BufferedReader reader = new BufferedReader(new FileReader("cart" + akun.id + ".txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                cartItems.add(line); // Menyimpan setiap baris ke dalam daftar
                String[] data = line.split(",");
                String index = data[0];
                String name = data[2];
                int quantity = Integer.parseInt(data[3]);
                double price = Double.parseDouble(data[4]);
    
                System.out.println(index + ". " + name + " (x" + quantity + "), Harga: " + price);
            }
        } catch (IOException e) {
            System.out.println("Gagal membaca file keranjang: " + e.getMessage());
            return;
        }
    
        // File untuk mencatat barang yang di-checkout
        String checkoutFileName = "Checkout" + akun.id + ".txt";
        try (BufferedWriter checkoutWriter = new BufferedWriter(new FileWriter(checkoutFileName, true))) {
            // Memilih barang untuk checkout
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("Masukkan nomor barang yang ingin dicekout, atau ketik 'selesai' untuk checkout:");
                String input = scanner.nextLine();
    
                if (input.equalsIgnoreCase("selesai")) {
                    break;
                }
    
                try {
                    // Validasi apakah input berupa nomor indeks yang valid
                    int itemIndex = Integer.parseInt(input);
                    boolean isValidIndex = cartItems.stream().anyMatch(item -> item.startsWith(String.valueOf(itemIndex)));
    
                    if (!isValidIndex) {
                        System.out.println("Indeks tidak valid. Silakan coba lagi.");
                        continue;
                    }
    
                    // Ambil barang berdasarkan indeks
                    String selectedItem = null;
                    for (String item : cartItems) {
                        if (item.startsWith(String.valueOf(itemIndex))) {
                            selectedItem = item;
                            break;
                        }
                    }
    
                    if (selectedItem != null) {
                        selectedItems.add(selectedItem); // Menyimpan barang yang dipilih
    
                        // Pilih metode pembayaran
                        System.out.println("Pilih metode pembayaran untuk " + selectedItem + ":");
                        System.out.println("1. Bank");
                        System.out.println("2. COD");
                        System.out.println("3. QRIS");
                        System.out.print("Pilih opsi: ");
                        int paymentChoice = scanner.nextInt();
                        scanner.nextLine(); // Bersihkan buffer
    
                        switch (paymentChoice) {
                            case 1:
                                pembayaran = new Bank();
                                pembayaran.bayar();
                                break;
                            case 2:
                                pembayaran = new COD();
                                pembayaran.bayar();
                                break;
                            case 3:
                                pembayaran = new QRIS();
                                pembayaran.bayar();
                                break;
                            default:
                                System.out.println("Pilihan metode pembayaran tidak valid. Checkout dibatalkan.");
                                return;
                        }
    
                        // Proses pembayaran
                        String[] data = selectedItem.split(",");
                        String id = data[1];        // ID barang
                        String name = data[2];      // Nama barang
                        int quantity = Integer.parseInt(data[3]); // Jumlah barang
                        double price = Double.parseDouble(data[4]); // Harga barang
    
                        pembayaran.setJumlahBayar(itemIndex, name, quantity, (int) price);
    
                        System.out.println("Barang " + name + " (x" + quantity + ") telah diproses untuk pembayaran.");
    
                        // Menyimpan informasi transaksi tanpa indeks, hanya ID barang, nama barang, jumlah barang, harga barang, dan metode pembayaran
                        checkoutWriter.write(id + "," + name + "," + quantity + "," + price + ", Metode: " + pembayaran.getClass().getSimpleName());
                        checkoutWriter.newLine();
                        System.out.println("Barang ditambahkan ke file checkout: " + id + "," + name + "," + quantity + "," + price + ", Metode: " + pembayaran.getClass().getSimpleName());
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Input tidak valid. Silakan masukkan nomor barang.");
                }
            }
        } catch (IOException e) {
            System.out.println("Gagal menulis ke file checkout: " + e.getMessage());
        }
    
        // Hapus barang yang sudah dibeli dari keranjang
        cartItems.removeAll(selectedItems);
    
        // Tulis ulang file cart.txt dengan barang yang tersisa
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("cart" + akun.id + ".txt"))) {
            for (String remainingItem : cartItems) {
                writer.write(remainingItem);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Gagal mengupdate file keranjang: " + e.getMessage());
        }
    
        System.out.println("Checkout selesai. Barang yang dibeli telah dicatat di " + checkoutFileName + ".");
    }
        
    
    
    

    // Menampilkan riwayat transaksi new ArrayList<>(this.cart)
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
                case 1 -> viewBarang();
                case 2 -> {
                    viewBarang();
                    scanner.nextLine();
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
