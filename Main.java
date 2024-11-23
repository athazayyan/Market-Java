import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

@SuppressWarnings("unused")
public class Main {
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

            if (id.equals("A1")) {
                if (password.equals("#Admin123")) {
                    JOptionPane.showMessageDialog(frame, "Logged in as Admin");
                    displayAdminMenu(frame);
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid Admin Password");
                }
            } else if (userDatabase.containsKey(id)) {
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
        JPanel panel = new JPanel(new GridLayout(3, 1));
        JButton viewBarangButton = new JButton("View Barang");
        JButton addCartButton = new JButton("Add Barang to Cart");
        JButton logoutButton = new JButton("Logout");

        panel.add(viewBarangButton);
        panel.add(addCartButton);
        panel.add(logoutButton);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();

        viewBarangButton.addActionListener(e -> {
            // Implementasi untuk melihat barang
        });

        addCartButton.addActionListener(e -> {
            // Implementasi untuk menambahkan barang ke keranjang
        });

        logoutButton.addActionListener(e -> displayLoginScreen(frame));
    }

    // Tampilan Login
    public void displayLoginScreen(JFrame frame) {
        JPanel panel = new JPanel(new GridLayout(3, 1));
        JButton registerButton = new JButton("Register");
        JButton loginButton = new JButton("Login");

        panel.add(registerButton);
        panel.add(loginButton);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();

        registerButton.addActionListener(e -> register(frame));
        loginButton.addActionListener(e -> login(frame));
    }

    // Main method
    public static void main(String[] args) {
        Main main = new Main();
        main.loadBarangData();
        main.loadUserDatabase();

        JFrame frame = new JFrame("E-Commerce Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        main.displayLoginScreen(frame); // Menampilkan layar login
        frame.setVisible(true);
    }
}
