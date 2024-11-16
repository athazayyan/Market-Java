import java.io.*;
import java.util.*;

class Main {
    HashMap<String, String> userDatabase = new HashMap<>();
    ArrayList<Barang> listBarang = new ArrayList<>();
    ArrayList<Transaksi> listTransaksi = new ArrayList<>();

    public void loadBarangData() {
        try (BufferedReader reader = new BufferedReader(new FileReader("allBarang.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String id = data[0];
                String name = data[1];
                int quantity = Integer.parseInt(data[2]);
                listBarang.add(new Barang(id, name, quantity));
            }
            System.out.println("Barang data loaded from allBarang.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void login() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter ID: ");
        String id = scanner.nextLine();

        if (id.startsWith("A")) {
            Akun akun = new Admin(id);
            AdminDriver driverAkun = new AdminDriver(akun, listBarang, listTransaksi);
            saveLoginDetails(id);
            System.out.println("Logged in as Admin");
            this.adminMenu(driverAkun);
        } else {
            Akun akun = new Customer(id);
            CustomerDriver driverAkun = new CustomerDriver((Customer) akun, new ArrayList<>(), listBarang, listTransaksi);
            saveLoginDetails(id);
            System.out.println("Logged in as Customer");
            customerMenu(driverAkun);
        }
    }

    public void register() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (username.equals("Admin")) {
            System.out.println("Cannot register as Admin. Only one Admin allowed.");
            return;
        }

        String id = "C" + (userDatabase.size() + 1);
        userDatabase.put(id, username + ":" + password);

        try (FileWriter writer = new FileWriter("userDatabase.txt", true)) {
            writer.write(id + ":" + username + ":" + password + "\n");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        System.out.println("Registered successfully with ID: " + id);
    }

    public void adminMenu(AdminDriver adminDriver) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Admin Menu:");
            System.out.println("1. Add Barang");
            System.out.println("2. Edit Barang");
            System.out.println("3. Delete Barang");
            System.out.println("4. View Transaksi");
            System.out.println("5. Accept Transaksi");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter Barang ID: ");
                    String id = scanner.nextLine();
                    System.out.print("Enter Barang name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Barang quantity: ");
                    int quantity = scanner.nextInt();
                    scanner.nextLine();
                    adminDriver.addBarang(new Barang(id, name, quantity));
                    break;
                case 2:
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
                    adminDriver.editBarang(editIndex, new Barang(newId, newName, newQuantity));
                    break;
                case 3:
                    System.out.print("Enter index to delete: ");
                    int deleteIndex = scanner.nextInt();
                    scanner.nextLine();
                    adminDriver.deleteBarang(deleteIndex);
                    break;
                case 4:
                    adminDriver.viewTransaksi();
                    break;
                case 5:
                    System.out.print("Enter index of transaksi to accept: ");
                    int transIndex = scanner.nextInt();
                    scanner.nextLine();
                    adminDriver.acceptTransaksi(transIndex);
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option");
                    break;
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
                case 1:
                    customerDriver.viewBarang();
                    break;
                case 2:
                    System.out.print("Enter Barang ID to add to cart: ");
                    String barangId = scanner.nextLine();
                    System.out.print("Enter quantity: ");
                    int quantity = scanner.nextInt();
                    scanner.nextLine();
                    customerDriver.addBarangToCart(barangId, quantity);
                    break;
                case 3:
                    customerDriver.checkout();
                    break;
                case 4:
                    customerDriver.viewHistory();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private void saveLoginDetails(String id) {
        // Save login details if necessary
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.loadBarangData();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    main.register();
                    break;
                case 2:
                    main.login();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }
}
