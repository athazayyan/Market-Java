import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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

    public void addBarang(Barang barang) {
        listBarang.add(barang);
        saveAllBarangToFile();
        System.out.println("Barang ditambah: " + barang.name);
    }

    public void editBarang(int index, Barang barang) {
        if (index >= 0 && index < listBarang.size()) {
            listBarang.set(index, barang);
            saveAllBarangToFile();
            System.out.println("Barang diedit: " + barang.name);
        } else {
            System.out.println("Invalid index");
        }
    }

    public void deleteBarang(int index) {
        if (index >= 0 && index < listBarang.size()) {
            Barang removed = listBarang.remove(index);
            saveAllBarangToFile();
            System.out.println("Barang dihapus: " + removed.name);
        } else {
            System.out.println("Invalid index");
        }
    }

    public void viewTransaksi() {
        for (int i = 0; i < listTransaksi.size(); i++) {
            Transaksi transaksi = listTransaksi.get(i);
            System.out.println("Index: " + i + ", Transaksi ID: " + transaksi.id + ", Customer ID: " + transaksi.akun.id);
        }
    }

    public void acceptTransaksi(int index) {
        if (index >= 0 && index < listTransaksi.size()) {
            Transaksi transaksi = listTransaksi.get(index);
            updateStockAfterTransaksiAcceptance(transaksi);
            listTransaksi.remove(index); 
            System.out.println("Transaksi diterima dengan id: " + transaksi.akun.id);
        } else {
            System.out.println("Invalid index");
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

    @Override
    public void viewBarang() {
        listBarangObj.listBarang();
    }

    private void saveAllBarangToFile() {
        try (FileWriter writer = new FileWriter("allBarang.txt")) {
            for (Barang barang : listBarang) {
                writer.write(barang.id + "," + barang.name + "," + barang.quantity + "\n");
            }
            System.out.println("Semua barang disimpan ke allBarang.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // **Metode Admin Menu**
    @Override
    public void Menu() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("Menu Admin:");
                System.out.println("1. Tambah Barang");
                System.out.println("2. Edit Barang");
                System.out.println("3. Hapus Barang");
                System.out.println("4. Lihat Transaksi");
                System.out.println("5. Terima Transaksi");
                System.out.println("6. Lihat Ketersediaan Stok");
                System.out.println("7. Logout");
                System.out.print("Pilih opsi: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> {
                        System.out.print("Masukkan ID Barang: ");
                        String id = scanner.nextLine();
                        System.out.print("Masukkan nama Barang: ");
                        String name = scanner.nextLine();
                        System.out.print("Masukkan kuantitas Barang: ");
                        int quantity = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Masukkan harga Barang: ");
                        int price = scanner.nextInt();
                        scanner.nextLine();
                        addBarang(new Barang(id, name, quantity, price)); // Tambah Barang
                    }
                    case 2 -> {
                        System.out.print("Masukkan indeks untuk diedit: ");
                        int editIndex = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Masukkan ID Barang baru: ");
                        String newId = scanner.nextLine();
                        System.out.print("Masukkan nama Barang baru: ");
                        String newName = scanner.nextLine();
                        System.out.print("Masukkan kuantitas Barang baru: ");
                        int newQuantity = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Masukkan harga Barang baru: ");
                        int newPrice = scanner.nextInt();
                        scanner.nextLine();
                        editBarang(editIndex, new Barang(newId, newName, newQuantity, newPrice)); // Edit Barang
                    }
                    case 3 -> {
                        System.out.print("Masukkan indeks untuk dihapus: ");
                        int deleteIndex = scanner.nextInt();
                        scanner.nextLine();
                        deleteBarang(deleteIndex); // Hapus Barang
                    }
                    case 4 -> viewTransaksi(); // Lihat Transaksi
                    case 5 -> {
                        System.out.print("Masukkan indeks transaksi untuk diterima: ");
                        int transIndex = scanner.nextInt();
                        scanner.nextLine();
                        acceptTransaksi(transIndex); // Terima Transaksi
                    }
                    case 6 -> viewBarang(); // Lihat Barang
                    case 7 -> {
                        System.out.println("Keluar dari akun Admin...");
                        return;
                    }
                    default -> System.out.println("Opsi tidak valid");
                }
            }
        }
    }
}
