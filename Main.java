import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class Main {
    HashMap<String, String> userDatabase = new HashMap<>();
    ArrayList<Barang> listBarang = new ArrayList<>();
    ArrayList<Transaksi> listTransaksi = new ArrayList<>();
    ArrayList<Barang> keranjang = new ArrayList<>();  // Menambahkan keranjang untuk menyimpan barang yang dipilih

    // Kelas Barang
    class Barang {
        String id;
        String name;
        int quantity;
        int price;

        Barang(String id, String name, int quantity, int price) {
            this.id = id;
            this.name = name;
            this.quantity = quantity;
            this.price = price;
        }

        @Override
        public String toString() {
            return "ID: " + id + ", Nama: " + name + ", Stok: " + quantity + ", Harga: Rp" + price;
        }

        public int getPrice() {
            return price;
        }
    }

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
            System.out.println("Barang data loaded from allBarang.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Memuat data pengguna dari file
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
            System.out.println("User database loaded from userDatabase.txt");
        } catch (IOException e) {
            System.err.println("Error loading user database: " + e.getMessage());
        }
    }

    // GUI untuk Register
    public void register(JFrame frame) {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JButton registerButton = new JButton("Register");

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(registerButton);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();

        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.equals("Admin")) {
                JOptionPane.showMessageDialog(frame, "Cannot register as Admin.");
                return;
            }

            String id = "C" + (userDatabase.size() + 1);
            userDatabase.put(id, username + ":" + password);
            try (FileWriter writer = new FileWriter("userDatabase.txt", true)) {
                writer.write(id + ":" + username + ":" + password + "\n");
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }

            JOptionPane.showMessageDialog(frame, "Registered successfully with ID: " + id);
            displayLoginScreen(frame);
        });
    }

    // Menampilkan layar login
    public void displayLoginScreen(JFrame frame) {
        JPanel panel = new JPanel(new GridLayout(3, 1));
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        panel.add(loginButton);
        panel.add(registerButton);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();

        loginButton.addActionListener(e -> login(frame));
        registerButton.addActionListener(e -> register(frame));
    }

    // GUI untuk Login
    public void login(JFrame frame) {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField idField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");
    
        panel.add(new JLabel("ID:"));
        panel.add(idField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(loginButton);
    
        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    
        loginButton.addActionListener(e -> {
            String id = idField.getText();
            String password = new String(passwordField.getPassword());
    
            // Mengecek login untuk Admin (ID = A1, Password = 123)
            if (id.equals("A1") && password.equals("123")) {
                JOptionPane.showMessageDialog(frame, "Logged in as Admin");
                displayAdminMenu(frame);  // Menampilkan menu Admin
            } else if (userDatabase.containsKey(id)) {
                // Mengecek login untuk customer
                String[] userDetails = userDatabase.get(id).split(":");
                String correctPassword = userDetails[1];
                if (password.equals(correctPassword)) {
                    JOptionPane.showMessageDialog(frame, "Logged in as Customer: " + userDetails[0]);
                    displayCustomerMenu(frame);
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid Password for Customer");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Login failed: Invalid ID");
            }
        });
    }
    

    // Tampilan menu Admin
    public void displayAdminMenu(JFrame frame) {
        JPanel panel = new JPanel(new GridLayout(3, 1));
        JButton addBarangButton = new JButton("Add Barang");
        JButton viewTransaksiButton = new JButton("View Transaksi");
        JButton logoutButton = new JButton("Logout");

        panel.add(addBarangButton);
        panel.add(viewTransaksiButton);
        panel.add(logoutButton);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();

        addBarangButton.addActionListener(e -> {
            // Implementasi untuk menambahkan barang
        });

        viewTransaksiButton.addActionListener(e -> {
            // Implementasi untuk melihat transaksi
        });

        logoutButton.addActionListener(e -> displayLoginScreen(frame));
    }

    // Tampilan menu Customer
    public void displayCustomerMenu(JFrame frame) {
        JPanel panel = new JPanel(new GridLayout(4, 1));
        JButton viewBarangButton = new JButton("Barang");
        JButton viewKeranjangButton = new JButton("Keranjang");
        JButton addCartButton = new JButton("Pembayaran");
        JButton logoutButton = new JButton("Logout");

        panel.add(viewBarangButton);
        panel.add(viewKeranjangButton);  // Menambahkan tombol Keranjang
        panel.add(addCartButton);
        panel.add(logoutButton);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();

        viewBarangButton.addActionListener(e -> {
            viewBarang(frame);
        });

        viewKeranjangButton.addActionListener(e -> {
            viewKeranjang(frame);  // Menampilkan keranjang
        });

        addCartButton.addActionListener(e -> {
            choosePaymentMethod(frame);  // Menampilkan pilihan metode pembayaran
        });

        logoutButton.addActionListener(e -> displayLoginScreen(frame));
    }

    // Fungsi untuk menampilkan barang di dalam GUI
    public void viewBarang(JFrame frame) {
        JPanel panel = new JPanel();
        JTextArea barangArea = new JTextArea(10, 30);
        barangArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(barangArea);

        // Mengambil data barang dari listBarang dan menampilkannya
        StringBuilder barangList = new StringBuilder();
        for (Barang barang : listBarang) {
            barangList.append(String.format("ID: %s, Name: %s, Quantity: %d, Price: Rp%d\n", 
                barang.id, barang.name, barang.quantity, barang.price));
        }
        barangArea.setText(barangList.toString());

        // Menambahkan tombol untuk memilih barang
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> displayCustomerMenu(frame));

        panel.setLayout(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(backButton, BorderLayout.SOUTH);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    // Fungsi untuk melihat isi keranjang
    public void viewKeranjang(JFrame frame) {
        JPanel panel = new JPanel();
        JTextArea keranjangArea = new JTextArea(10, 30);
        keranjangArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(keranjangArea);

        // Mengambil data barang di keranjang dan menampilkannya
        StringBuilder keranjangList = new StringBuilder();
        if (keranjang.isEmpty()) {
            keranjangList.append("Keranjang Anda kosong.");
        } else {
            for (Barang barang : keranjang) {
                keranjangList.append(String.format("ID: %s, Name: %s, Quantity: %d, Price: Rp%d\n", 
                    barang.id, barang.name, barang.quantity, barang.price));
            }
        }
        keranjangArea.setText(keranjangList.toString());

        // Tombol untuk kembali
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> displayCustomerMenu(frame));

        panel.setLayout(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(backButton, BorderLayout.SOUTH);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    // Fungsi untuk memilih metode pembayaran
    public void choosePaymentMethod(JFrame frame) {
        String[] paymentMethods = {"QRiS", "COD", "Bank"};
        String selectedPaymentMethod = (String) JOptionPane.showInputDialog(frame, 
            "Pilih metode pembayaran:", 
            "Pilih Pembayaran", 
            JOptionPane.PLAIN_MESSAGE, 
            null, 
            paymentMethods, 
            paymentMethods[0]);

        if (selectedPaymentMethod != null) {
            JOptionPane.showMessageDialog(frame, "Metode pembayaran yang dipilih: " + selectedPaymentMethod);
        }
    }

    // Main method
    public static void main(String[] args) {
        Main main = new Main();
        main.loadBarangData();  // Memuat data barang
        main.loadUserDatabase();  // Memuat data pengguna

        JFrame frame = new JFrame("Aplikasi E-Commerce");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        main.displayLoginScreen(frame);  // Menampilkan layar login
        frame.setVisible(true);
    }
}
