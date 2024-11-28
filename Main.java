import java.io.*;
import java.util.*;

public class Main {
    private HashMap<String, String> userDatabase = new HashMap<>();
    private ArrayList<Barang> listBarang = new ArrayList<>();
    private ArrayList<Transaksi> listTransaksi = new ArrayList<>();

    public void loadBarangData() {
        try (BufferedReader reader = new BufferedReader(new FileReader("allBarang.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String id = data[0];
                String name = data[1];
                int quantity = Integer.parseInt(data[2]);
                int price = Integer.parseInt(data[3]);
                listBarang.add(new Barang(id, name, quantity, price));
            }
            System.out.println("Data barang berhasil dimuat dari allBarang.txt");
        } catch (IOException e) {
            System.err.println("Kesalahan membaca file: " + e.getMessage());
        }
    }

    public void loadUserDatabase() {
        try (BufferedReader reader = new BufferedReader(new FileReader("userDatabase.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(":");
                if (userDetails.length == 3) {
                    String id = userDetails[0];
                    String username = userDetails[1];
                    String password = userDetails[2];
                    userDatabase.put(id, username + ":" + password);
                }
            }
            System.out.println("Database pengguna berhasil dimuat dari userDatabase.txt");
        } catch (IOException e) {
            System.err.println("Kesalahan memuat database pengguna: " + e.getMessage());
        }
    }

    public void login() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Masukkan ID: ");
        String id = scanner.nextLine();

        if (id.equals("A1")) {
            System.out.print("Masukkan Password Admin: ");
            String password = scanner.nextLine();

            if (password.equals("#Admin123")) {
                Admin akun = new Admin(id);
                AdminDriver adminDriver = new AdminDriver(akun, listBarang, listTransaksi);
                System.out.println("Masuk sebagai Admin");
                adminDriver.Menu();
            } else {
                System.out.println("Password Admin tidak valid.");
                waitForUser(scanner);
            }
        } else if (userDatabase.containsKey(id)) {
            System.out.print("Masukkan password: ");
            String password = scanner.nextLine();

            String[] userDetails = userDatabase.get(id).split(":");
            String username = userDetails[0];
            String correctPassword = userDetails[1];

            if (password.equals(correctPassword)) {
                Customer akun = new Customer(id, username, password);
                CustomerDriver customerDriver = new CustomerDriver(akun, new ArrayList<>(), listBarang, listTransaksi);
                System.out.println("Masuk sebagai Customer: " + username);
                customerDriver.Menu();
            } else {
                System.out.println("Password tidak valid.");
                waitForUser(scanner);
            }
        } else {
            System.out.println("ID tidak valid.");
            waitForUser(scanner);
        }
    }

    public void register() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Masukkan username: ");
        String username = scanner.nextLine();

        if (userDatabase.values().stream().anyMatch(data -> data.split(":")[0].equals(username))) {
            System.out.println("Username sudah terdaftar. Silakan pilih username lain.");
            waitForUser(scanner);
            return;
        }

        System.out.print("Masukkan password: ");
        String password = scanner.nextLine();

        if (username.equalsIgnoreCase("Admin")) {
            System.out.println("Tidak dapat mendaftar sebagai Admin. Akun Admin sudah ada.");
            waitForUser(scanner);
            return;
        }

        String id = "C" + (userDatabase.size() + 1);
        userDatabase.put(id, username + ":" + password);

        try (FileWriter writer = new FileWriter("userDatabase.txt", true)) {
            writer.write(id + ":" + username + ":" + password + "\n");
        } catch (IOException e) {
            System.err.println("Kesalahan menyimpan data pengguna: " + e.getMessage());
        }

        System.out.println("Berhasil mendaftar dengan ID: " + id);
        waitForUser(scanner);
    }

    public void halamanMenuUtama() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            clearScreen(); // Bersihkan layar sebelum menampilkan menu utama
            System.out.println("=== Sistem Manajemen Toko ===");
            System.out.println("1. Daftar");
            System.out.println("2. Masuk");
            System.out.println("3. Keluar");
            System.out.print("Pilih opsi: ");
            if (!scanner.hasNextLine()) {
                break;
            }
            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Konsumsi baris berikutnya
            } catch (InputMismatchException e) {
                System.out.println("Input tidak valid. Masukkan angka.");
                scanner.nextLine(); // Konsumsi input yang salah
                waitForUser(scanner); // Tunggu sebelum membersihkan layar
                continue;
            }

            switch (choice) {
                case 1:
                    register();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    System.out.println("Keluar dari program...");
                    System.exit(0);
                default:
                    System.out.println("Opsi tidak valid. Silakan coba lagi.");
                    waitForUser(scanner);
                    break;
            }
        }
    }

    public static void waitForUser(Scanner scanner) {
        System.out.println("\nTekan Enter untuk melanjutkan...");
        scanner.nextLine(); // Tunggu pengguna menekan Enter
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

    public static void main(String[] args) {
        Main main = new Main();
        main.loadBarangData();
        main.loadUserDatabase();
        main.halamanMenuUtama();
    }
}
