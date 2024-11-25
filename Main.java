import java.io.*; // Mengimpor kelas untuk operasi input/output
import java.util.*; // Mengimpor kelas utilitas seperti ArrayList dan HashMap


public class Main {
    HashMap<String, String> userDatabase = new HashMap<>(); // Database pengguna untuk menyimpan ID dan detail login
    ArrayList<Barang> listBarang = new ArrayList<>(); // Daftar barang yang tersedia
    ArrayList<Transaksi> listTransaksi = new ArrayList<>(); // Daftar transaksi yang telah dilakukan

    // Metode untuk memuat data barang dari file
    public void loadBarangData() {
        try (BufferedReader reader = new BufferedReader(new FileReader("allBarang.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(","); // Memisahkan data berdasarkan koma
                String id = data[0]; // ID barang
                String name = data[1]; // Nama barang
                int quantity = Integer.parseInt(data[2]); // Kuantitas barang
                int price = data.length > 3 ? Integer.parseInt(data[3]) : 0; // Harga barang
                listBarang.add(new Barang(id, name, quantity, price)); // Menambahkan barang ke daftar
            }
            System.out.println("Barang data loaded from allBarang.txt");
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage()); // Menampilkan pesan kesalahan jika terjadi kesalahan saat membaca file
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
                    userDatabase.put(id, username + ":" + password); // Menyimpan data ke dalam userDatabase
                }
            }
            System.out.println("User database loaded from userDatabase.txt");
        } catch (IOException e) {
            System.err.println("Error loading user database: " + e.getMessage());
        }
    }

    // Metode untuk login pengguna
    public void login() {
        try (Scanner scanner = new Scanner(System.in)) {
        System.out.print("Enter ID: ");
        String id = scanner.nextLine();
    
        // Memeriksa apakah ID untuk Admin (A1)
        if (id.equals("A1")) {
            System.out.print("Enter Admin Password: ");
            String password = scanner.nextLine();
    
            // Memeriksa password untuk Admin
            if (password.equals("#Admin123")) {
                Akun akun = new Admin(id); // Membuat objek Admin
                AdminDriver driverAkun = new AdminDriver(akun, listBarang, listTransaksi); // Membuat driver untuk admin
                // saveLoginDetails(id); // Menyimpan detail login (jika diperlukan)
                System.out.println("Logged in as Admin");
                adminMenu(driverAkun);
            } // Menampilkan menu admin
             else {
                    System.out.println("Invalid Admin Password");
                }
            }
         else if (userDatabase.containsKey(id)) {
                System.out.print("Enter password: ");
                String password = scanner.nextLine();
        
                // Memeriksa password sesuai dengan yang tersimpan dalam userDatabase
                String[] userDetails = userDatabase.get(id).split(":");
                String correctUsername = userDetails[0];
                String correctPassword = userDetails[1];
        
                if (password.equals(correctPassword)) {
                    // Membuat objek Customer dengan username dan password yang sesuai
                    Akun akun = new Customer(id, correctUsername, correctPassword);
                    CustomerDriver driverAkun = new CustomerDriver((Customer) akun, new ArrayList<>(), listBarang, listTransaksi);
                    System.out.println("Logged in as Customer: " + correctUsername);
                // saveLoginDetails(id); // Menyimpan detail login (jika diperlukan)

                    customerMenu(driverAkun); // Menampilkan menu customer
                } else {
                    System.out.println("Invalid Password for Customer");
                }
            } else {
                System.out.println("Login failed: Invalid ID");
                login();
            }
        } catch (Exception e) {
            System.err.println("Error during login: " + e.getMessage());
        }
    }
        

    public void register() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
    
        // Validasi username
        if (userDatabase.values().stream().anyMatch(data -> data.split(":")[0].equals(username))) {
            System.out.println("Username already exists. Please choose a different one.");
            return;
        }
    
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
    
        if (username.equalsIgnoreCase("Admin")) {
            System.out.println("Cannot register as Admin. Only one Admin allowed.");
            return;
        }
    
        String id = "C" + (userDatabase.size() + 1);
        userDatabase.put(id, username + ":" + password);
    
        // Simpan ke file
        try (FileWriter writer = new FileWriter("userDatabase.txt", true)) {
            writer.write(id + ":" + username + ":" + password + "\n");
        } catch (IOException e) {
            System.err.println("Error saving user: " + e.getMessage());
        }
    
        System.out.println("Registered successfully with ID: " + id);
    }
    
    
    // Metode untuk menampilkan menu admin
    public void adminMenu(AdminDriver adminDriver) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("Admin Menu:");
                System.out.println("1. Add Barang");
                System.out.println("2. Edit Barang");
                System.out.println("3. Delete Barang");
                System.out.println("4. View Transaksi");
                System.out.println("5. Accept Transaksi");
                System.out.println("6. Lihat Ketersediaan Stok");
                System.out.println("7. Logout");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Barang ID: ");
                    String id = scanner.nextLine();
                    System.out.print("Enter Barang name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Barang quantity: ");
                    int quantity = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Masukkan harga barang: ");
                    int price = scanner.nextInt();
                    scanner.nextLine();
                    // Menambahkan barang baru menggunakan metode dari AdminDriver
                    adminDriver.addBarang(new Barang(id, name, quantity, price));
                }
                case 2 -> {
                    System.out.print("Enter index to edit: ");
                    int editIndex = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter new Barang ID: ");
                    String newId = scanner.nextLine();
                    System.out.print("Enter new Barang name: ");
                    String newName = scanner.nextLine();
                    System.out.print("Enter new Barang quantity: ");
                    int newQuantity = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Masukkan harga barang Baru: ");
                    int newPrice = scanner.nextInt();
                    scanner.nextLine();
                    // Mengedit barang yang ada menggunakan metode dari AdminDriver
                    adminDriver.editBarang(editIndex, new Barang(newId, newName, newQuantity, newPrice));
                }
                case 3 -> {
                    System.out.print("Enter index to delete: ");
                    int deleteIndex = scanner.nextInt();
                    scanner.nextLine();
                    // Menghapus barang menggunakan metode dari AdminDriver
                    adminDriver.deleteBarang(deleteIndex);
                }
                case 4 -> adminDriver.viewTransaksi(); // Menampilkan daftar transaksi menggunakan metode dari AdminDriver
                case 5 -> {
                    System.out.print("Enter index of transaksi to accept: ");
                    int transIndex = scanner.nextInt();
                    scanner.nextLine();
                    // Menerima transaksi menggunakan metode dari AdminDriver
                    adminDriver.acceptTransaksi(transIndex);
                }
                case 6 -> adminDriver.viewBarang();
                case 7 -> {
                    login();                }
                default -> {

                System.out.println("Invalid option");
                login();} // Menampilkan pesan jika opsi tidak valid
            }
        }
    }
}


public void customerMenu(CustomerDriver customerDriver) {
    Scanner scanner = new Scanner(System.in);
    while (true) {
        System.out.println("Customer Menu:");
        System.out.println("1. View Barang");
        System.out.println("2. Add Barang to Cart");
        System.out.println("3. Checkout");
        System.out.println("4. View History");
        System.out.println("5. Logout");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> customerDriver.viewBarang();
            case 2 -> {
                customerDriver.viewBarang();
                System.out.print("Enter Barang ID to add to cart: ");
                String barangId = scanner.nextLine();
                System.out.print("Enter quantity: ");
                int quantity = scanner.nextInt();
                scanner.nextLine();
                customerDriver.addBarangToCart(barangId, quantity);
            }
            case 3 -> customerDriver.checkout();
            case 4 -> customerDriver.viewHistory();
            case 5 -> {
                System.out.println("Logging out...");
                login();
            }
            default -> System.out.println("Invalid option. Please try again.");
        }
    }
}

    
    

    // Metode utama untuk menjalankan aplikasi
    public static void main(String[] args) {
        Main main = new Main(); // Membuat objek Main
        main.loadBarangData(); // Memuat data barang dari file
        main.loadUserDatabase(); // Memuat data pengguna dari file
        Scanner scanner = new Scanner(System.in);
    
        while (true) {
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
    
            switch (choice) {
                case 1 -> main.register(); // Memanggil metode pendaftaran
                case 2 -> main.login();    // Memanggil metode login
                case 3 -> {
                    System.out.println("Exiting program...");
                    return; // Menghentikan program
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    
}
