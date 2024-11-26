import java.io.*; // Untuk operasi file
import java.util.*; // Untuk struktur data dan utilitas

public class Main {
    HashMap<String, String> userDatabase = new HashMap<>(); // Database pengguna
    ArrayList<Barang> listBarang = new ArrayList<>(); // Daftar barang
    ArrayList<Transaksi> listTransaksi = new ArrayList<>(); // Daftar transaksi

    // Memuat data barang dari file
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

    // Memuat database pengguna dari file
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

    // Metode untuk login pengguna
    public void login() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Masukkan ID: ");
        String id = scanner.nextLine();

        // Login sebagai admin
        if (id.equals("A1")) {
            System.out.print("Masukkan Password Admin: ");
            String password = scanner.nextLine();

            if (password.equals("#Admin123")) {
                Admin akun = new Admin(id);
                AdminDriver adminDriver = new AdminDriver(akun, listBarang, listTransaksi);
                System.out.println("Masuk sebagai Admin");
                adminDriver.Menu();
                return;
            } else {
                System.out.println("Password Admin tidak valid.");
            }
        } 
        // Login sebagai customer
        else if (userDatabase.containsKey(id)) {
            System.out.print("Masukkan password: ");
            String password = scanner.nextLine();

            String[] userDetails = userDatabase.get(id).split(":");
            String username = userDetails[0];
            String correctPassword = userDetails[1];

            if (password.equals(correctPassword)) {
                Customer akun = new Customer(id, username, password);
                CustomerDriver customerDriver = new CustomerDriver(akun, new ArrayList<>(), listBarang, listTransaksi);
                System.out.println("Masuk sebagai Customer: " + username);
                customerDriver.Menu(); // Memanggil menu customer dari CustomerDriver
            } else {
                System.out.println("Password tidak valid.");
            }
        } else {
            System.out.println("ID tidak valid.");
        }
    }

    // Metode untuk mendaftar pengguna baru
    public void register() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Masukkan username: ");
        String username = scanner.nextLine();

        if (userDatabase.values().stream().anyMatch(data -> data.split(":")[0].equals(username))) {
            System.out.println("Username sudah terdaftar. Silakan pilih username lain.");
            return;
        }

        System.out.print("Masukkan password: ");
        String password = scanner.nextLine();

        if (username.equalsIgnoreCase("Admin")) {
            System.out.println("Tidak dapat mendaftar sebagai Admin. Akun Admin sudah ada.");
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
    }

    // Metode utama untuk menjalankan program
    public static void main(String[] args) {
        Main main = new Main();

        // Memuat data dari file
        main.loadBarangData();
        main.loadUserDatabase();

        Scanner scanner = new Scanner(System.in);

        // Menu utama
        while (true) {
            System.out.println("=== Sistem Manajemen Toko ===");
            System.out.println("1. Daftar");
            System.out.println("2. Masuk");
            System.out.println("3. Keluar");
            System.out.print("Pilih opsi: ");
            if (!scanner.hasNextLine()) {
                break; // Menghindari NoSuchElementException jika tidak ada input
            }
            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Membersihkan newline dari buffer
            } catch (InputMismatchException e) {
                System.out.println("Input tidak valid. Masukkan angka.");
                scanner.nextLine(); // Membersihkan input yang salah
                continue;
            }
        

            switch (choice) {
                case 1 -> main.register();
                case 2 -> main.login();
                case 3 -> {
                    System.out.println("Keluar dari program...");
                    return;
                }
                default -> System.out.println("Opsi tidak valid. Silakan coba lagi.");
            }
        }
    }
}
