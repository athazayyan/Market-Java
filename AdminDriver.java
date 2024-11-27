import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AdminDriver extends Driver {
    ArrayList<Barang> listBarang;
    ArrayList<Transaksi> listTransaksi;
    ListBarang listBarangObj = new ListBarang();

    AdminDriver(Akun akun, ArrayList<Barang> listBarang, ArrayList<Transaksi> listTransaksi) {
        this.akun = akun;
        this.listBarang = listBarang;
        this.listTransaksi = listTransaksi;
    }

    // Tambah Barang
    public void addBarang(Barang barang) {
        listBarang.add(barang);
        saveAllBarangToFile();
        System.out.println("Barang ditambah: " + barang.name);
    }

    // Edit Barang
    public void editBarang(int index, Barang barang) {
        if (isIndexValid(index)) {
            listBarang.set(index, barang);
            saveAllBarangToFile();
            System.out.println("Barang diedit: " + barang.name);
        } else {
            System.out.println("Indeks tidak valid.");
        }
    }

    // Hapus Barang
    public void deleteBarang(int index) {
        if (isIndexValid(index)) {
            Barang removed = listBarang.remove(index);
            saveAllBarangToFile();
            System.out.println("Barang dihapus: " + removed.name);
        } else {
            System.out.println("Indeks tidak valid.");
        }
    }

    // Lihat Transaksi
    public void viewTransaksi() {
        try {
            Path dir = Paths.get(".");
            DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "CheckoutC*.txt");
            List<Path> files = new ArrayList<>();

            System.out.println("Daftar file Checkout:");
            int index = 0;
            for (Path file : stream) {
                files.add(file);
                String fileNameWithoutExtension = file.getFileName().toString().replace(".txt", "");
                System.out.println(index + ". " + fileNameWithoutExtension);
                index++;
            }

            if (!files.isEmpty()) {
                try (Scanner scanner = new Scanner(System.in)) {
                    while (true) {
                        System.out.print("Masukkan indeks file untuk dibuka (atau 'q' untuk kembali): ");
                        String input = scanner.nextLine();

                        if (input.equalsIgnoreCase("q")) {
                            System.out.println("Kembali ke menu.");
                            Menu();
                        }

                        try {
                            int fileIndex = Integer.parseInt(input);
                            if (fileIndex >= 0 && fileIndex < files.size()) {
                                Path selectedFile = files.get(fileIndex);
                                System.out.println("Isi file " + selectedFile.getFileName() + ":");
                                List<String> lines = Files.readAllLines(selectedFile);
                                for (String line : lines) {
                                    System.out.println(line);
                                }
                            } else {
                                System.out.println("Indeks file tidak valid.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Input tidak valid. Masukkan angka atau 'q' untuk keluar.");
                        }
                    }
                }
            } else {
                System.out.println("Tidak ada file Checkout yang ditemukan.");
            }
        } catch (IOException e) {
            System.out.println("Terjadi kesalahan saat membaca file: " + e.getMessage());
        }
    }

  // Terima Transaksi
public void acceptTransaksi() {
    try {
        Path dir = Paths.get(".");
        DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "CheckoutC*.txt");
        List<Path> files = new ArrayList<>();

        System.out.println("Daftar file Checkout:");
        int index = 0;
        for (Path file : stream) {
            files.add(file);
            System.out.println(index + ". " + file.getFileName());
            index++;
        }

        if (!files.isEmpty()) {
            try (Scanner scanner = new Scanner(System.in)) {
                System.out.print("Masukkan indeks file untuk diproses (atau -1 untuk kembali): ");
                int fileIndex = Integer.parseInt(scanner.nextLine());

                if (fileIndex >= 0 && fileIndex < files.size()) {
                    Path selectedFile = files.get(fileIndex);
                    List<String> lines = Files.readAllLines(selectedFile);

                    // Menampilkan isi file transaksi
                    System.out.println("Isi file " + selectedFile.getFileName() + ":");
                    for (int i = 0; i < lines.size(); i++) {
                        System.out.println(i + ". " + lines.get(i));
                    }

                    // Memilih transaksi untuk diterima
                    System.out.print("Masukkan indeks transaksi untuk diterima: ");
                    int transIndex = Integer.parseInt(scanner.nextLine());

                    // Validasi indeks transaksi
                    if (transIndex >= 0 && transIndex < lines.size()) {
                        String acceptedTransaction = lines.get(transIndex);
                        System.out.println("Transaksi diterima: " + acceptedTransaction);

                        // Memproses transaksi dan memperbarui stok
                        Transaksi transaksi = parseTransaction(acceptedTransaction);
                        updateStockAfterTransaksiAcceptance(transaksi);

                        // Menghapus transaksi yang diterima
                        lines.remove(transIndex);

                        // Menyimpan perubahan pada file
                        Files.write(selectedFile, lines);

                        // Menghapus file jika kosong
                        if (lines.isEmpty()) {
                            Files.delete(selectedFile);
                            System.out.println("File " + selectedFile.getFileName() + " dihapus karena kosong.");
                        }
                    } else {
                        System.out.println("Indeks transaksi tidak valid.");
                    }
                } else if (fileIndex == -1) {
                    System.out.println("Kembali ke menu.");
                } else {
                    System.out.println("Indeks file tidak valid.");
                }
            }
        } else {
            System.out.println("Tidak ada file Checkout yang ditemukan.");
        }
    } catch (IOException e) {
        System.out.println("Terjadi kesalahan saat membaca atau menulis file: " + e.getMessage());
    } catch (NumberFormatException e) {
        System.out.println("Input tidak valid. Harap masukkan angka yang benar.");
    }
}

    private void updateStockAfterTransaksiAcceptance(Transaksi transaksi) {
        for (Barang transBarang : transaksi.barang) {
            for (Barang barang : listBarang) {
                if (barang.id.equals(transBarang.id)) {
                    barang.quantity -= transBarang.quantity;
                    break;
                }
            }
        }
        saveAllBarangToFile();
    }

    // private Transaksi parseTransaction(String transactionString) {
    //     String[] parts = transactionString.split(",");
    //     List<Barang> barangList = new ArrayList<>();
    //     for (int i = 1; i < parts.length; i += 4) {
    //         String id = parts[i];
    //         String name = parts[i + 1];
    //         int quantity = Integer.parseInt(parts[i + 2]);
    //         int price = Integer.parseInt(parts[i + 3]);
    //         barangList.add(new Barang(id, name, quantity, price));
    //     }
    //     Customer customer = new Customer(parts[0], "defaultName", "defaultEmail");
    //     return new Transaksi(parts[0], customer, new ArrayList<>(barangList));
    // }
    private Transaksi parseTransaction(String transactionString) {
        try {
            // Memisahkan string transaksi berdasarkan koma
            String[] parts = transactionString.split(",");
            if (parts.length < 4) {
                throw new IllegalArgumentException("Format transaksi tidak valid.");
            }
    
            // Mengambil data barang
            String id = parts[0];
            String name = parts[1];
            int quantity = Integer.parseInt(parts[2]);
            double price = Double.parseDouble(parts[3]); // Parsing harga sebagai double
    
            // Metode pembayaran (opsional)
            String paymentMethod = parts.length > 4 ? parts[4].trim() : "Unknown";
    
            // Membuat barang dan transaksi
            Barang barang = new Barang(id, name, quantity, (int) price);
            Customer customer = new Customer("defaultId", "defaultName", "defaultEmail");
            ArrayList<Barang> barangList = new ArrayList<>();
            barangList.add(barang);
    
            System.out.println("Transaksi berhasil diparse: " + id + ", " + name + ", x" + quantity + ", " + price + ", Metode: " + paymentMethod);
    
            barangList.add(barang);
            return new Transaksi("defaultTransaksiId", customer, barangList);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Kesalahan dalam parsing angka: " + e.getMessage());
        }
    }
    

    @Override
    public void viewBarang() {
        listBarangObj.listBarang();
    }

    private void saveAllBarangToFile() {
        try (FileWriter writer = new FileWriter("allBarang.txt")) {
            for (Barang barang : listBarang) {
                writer.write(barang.id + "," + barang.name + "," + barang.quantity + "," + barang.price + "\n");
            }
            System.out.println("Semua barang disimpan ke allBarang.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void Menu() {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean stayInMenu = true;

            while (stayInMenu) {
                System.out.println("\nMenu Admin:");
                System.out.println("1. Tambah Barang");
                System.out.println("2. Edit Barang");
                System.out.println("3. Hapus Barang");
                System.out.println("4. Lihat Transaksi");
                System.out.println("5. Terima Transaksi");
                System.out.println("6. Lihat Ketersediaan Stok");
                System.out.println("7. Logout");
                System.out.print("Pilih opsi: ");

                int choice = getValidatedIntInput(scanner, "Pilih opsi: ");

                switch (choice) {
                    case 1:
                        String id = getStringInput(scanner, "Masukkan ID Barang: ");
                        String name = getStringInput(scanner, "Masukkan nama Barang: ");
                        int quantity = getValidatedIntInput(scanner, "Masukkan kuantitas Barang: ");
                        int price = getValidatedIntInput(scanner, "Masukkan harga Barang: ");
                        addBarang(new Barang(id, name, quantity, price));
                        break;
                    case 2:
                        int editIndex = getValidatedIntInput(scanner, "Masukkan indeks untuk diedit: ");
                        if (isIndexValid(editIndex)) {
                            String newId = getStringInput(scanner, "Masukkan ID Barang baru: ");
                            String newName = getStringInput(scanner, "Masukkan nama Barang baru: ");
                            int newQuantity = getValidatedIntInput(scanner, "Masukkan kuantitas Barang baru: ");
                            int newPrice = getValidatedIntInput(scanner, "Masukkan harga Barang baru: ");
                            editBarang(editIndex, new Barang(newId, newName, newQuantity, newPrice));
                        } else {
                            System.out.println("Indeks tidak valid.");
                        }
                        break;
                    case 3:
                        int deleteIndex = getValidatedIntInput(scanner, "Masukkan indeks untuk dihapus: ");
                        if (isIndexValid(deleteIndex)) {
                            deleteBarang(deleteIndex);
                        } else {
                            System.out.println("Indeks tidak valid.");
                        }
                        break;
                    case 4:
                        viewTransaksi();
                        Menu();
                    case 5:
                        acceptTransaksi();
                        break;
                    case 6:
                        viewBarang();
                        break;
                    case 7:
                        System.out.println("Keluar dari akun Admin...");
                        stayInMenu = false;
                        break;
                    default:
                        System.out.println("Opsi tidak valid. Silakan coba lagi.");
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan: " + e.getMessage());
        }
    }

    private boolean isIndexValid(int index) {
        return index >= 0 && index < listBarang.size();
    }

    private int getValidatedIntInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid. Masukkan angka.");
            }
        }
    }

    private String getStringInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}
