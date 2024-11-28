import java.io.*;
import java.util.*;

public class CustomerDriver extends Driver {
    ArrayList<Barang> cart;
    ArrayList<Barang> listBarang;
    ArrayList<Transaksi> listTransaksi;
    Customer akun;
    ListBarang listBarangObj = new ListBarang();

    CustomerDriver(Customer akun, ArrayList<Barang> cart, ArrayList<Barang> listBarang, ArrayList<Transaksi> listTransaksi) {
        this.akun = akun;
        this.cart = cart;
        this.listBarang = listBarang;
        this.listTransaksi = listTransaksi;
    }

    @Override
    public void viewBarang() {
        listBarangObj.listBarang();
    }

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
        List<String> cartItems = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("cart" + akun.id + ".txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                cartItems.add(line);
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

        String checkoutFileName = "Checkout" + akun.id + ".txt";
        try (BufferedWriter checkoutWriter = new BufferedWriter(new FileWriter(checkoutFileName, true))) {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("Masukkan nomor barang yang ingin dicekout, atau ketik 'selesai' untuk checkout:");
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("selesai")) {
                    break;
                }

                try {
                    int itemIndex = Integer.parseInt(input);
                    boolean isValidIndex = cartItems.stream().anyMatch(item -> item.startsWith(String.valueOf(itemIndex)));

                    if (!isValidIndex) {
                        System.out.println("Indeks tidak valid. Silakan coba lagi.");
                        continue;
                    }

                    String selectedItem = null;
                    for (String item : cartItems) {
                        if (item.startsWith(String.valueOf(itemIndex))) {
                            selectedItem = item;
                            break;
                        }
                    }

                    if (selectedItem != null) {
                        selectedItems.add(selectedItem);

                        System.out.println("\nPilih metode pembayaran untuk " + selectedItem + ":");
                        System.out.println("1. Bank");
                        System.out.println("2. COD");
                        System.out.println("3. QRIS");
                        System.out.print("Pilih opsi: ");
                        int paymentChoice = scanner.nextInt();
                        scanner.nextLine();

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

                        String[] data = selectedItem.split(",");
                        String id = data[1];
                        String name = data[2];
                        int quantity = Integer.parseInt(data[3]);
                        double price = Double.parseDouble(data[4]);

                        pembayaran.setJumlahBayar(itemIndex, name, quantity, (int) price);

                        System.out.println("Barang " + name + " (x" + quantity + ") telah diproses untuk pembayaran.");

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

        cartItems.removeAll(selectedItems);

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

    public void viewHistory() {
        System.out.println("Melihat Riwayat Transaksi... Anda " + akun.id + " " + akun.username);
        System.out.print("Mau lihat yang lagi di proses atau yang sudah selesai? (1 lagi diproses/2 yang sudah selesai): ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine();
        if (choice == 1) {
            try (BufferedReader reader = new BufferedReader(new FileReader("Checkout" + akun.id + ".txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                System.out.println("Gagal membaca file checkout: atau belum ada transaksi nih" + e.getMessage());
            }
        } else if (choice == 2) {
            try (BufferedReader reader = new BufferedReader(new FileReader("acceptedTransactions.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data[0].equals(akun.id)) {
                        System.out.println(line);
                    }
                }
            } catch (IOException e) {
                System.out.println("Gagal membaca file acceptedTransactions: " + e.getMessage());
            }
        }
    }

    @Override
    public void Menu() {
        Scanner scanner = new Scanner(System.in);
        clearScreen();
        while (true) {
            System.out.println("\n\n=================Menu Customer:==================");
            System.out.println("Selamat datang, " + akun.username + "!");

            System.out.println("1. Lihat Barang");
            System.out.println("2. Tambah Barang ke Keranjang");
            System.out.println("3. Checkout");
            System.out.println("4. Lihat Riwayat");
            System.out.println("5. Logout");
            System.out.print("Pilih opsi: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewBarang();
                    break;
                case 2:
                    viewBarang();
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

    public static void clearScreen() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("linux") || os.contains("mac")) {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            } else if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                for (int i = 0; i < 50; i++) {
                    System.out.println();
                }
            }
        } catch (Exception e) {
            System.err.println("Gagal membersihkan terminal: " + e.getMessage());
        }
    }
}
