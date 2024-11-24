import java.io.*;
import java.util.*;


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
        System.out.println("Barang added: " + barang.name);
    }
    public void editBarang(int index, Barang barang) {
        if (index >= 0 && index < listBarang.size()) {
            listBarang.set(index, barang);
            saveAllBarangToFile();
            System.out.println("Barang edited: " + barang.name);
        } else {
            System.out.println("Invalid index");
        }
    }

    public void deleteBarang(int index) {
        if (index >= 0 && index < listBarang.size()) {
            Barang removed = listBarang.remove(index);
            saveAllBarangToFile();
            System.out.println("Barang deleted: " + removed.name);
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
            System.out.println("Transaksi accepted for: " + transaksi.akun.id);
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
            System.out.println("All barang saved to allBarang.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
    
import java.io.*;
import java.util.*;


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
        System.out.println("Barang added: " + barang.name);
    }
    public void editBarang(int index, Barang barang) {
        if (index >= 0 && index < listBarang.size()) {
            listBarang.set(index, barang);
            saveAllBarangToFile();
            System.out.println("Barang edited: " + barang.name);
        } else {
            System.out.println("Invalid index");
        }
    }

    public void deleteBarang(int index) {
        if (index >= 0 && index < listBarang.size()) {
            Barang removed = listBarang.remove(index);
            saveAllBarangToFile();
            System.out.println("Barang deleted: " + removed.name);
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
            System.out.println("Transaksi accepted for: " + transaksi.akun.id);
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
            System.out.println("All barang saved to allBarang.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
    
import java.io.*;
import java.util.*;


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
        System.out.println("Barang added: " + barang.name);
    }
    public void editBarang(int index, Barang barang) {
        if (index >= 0 && index < listBarang.size()) {
            listBarang.set(index, barang);
            saveAllBarangToFile();
            System.out.println("Barang edited: " + barang.name);
        } else {
            System.out.println("Invalid index");
        }
    }

    public void deleteBarang(int index) {
        if (index >= 0 && index < listBarang.size()) {
            Barang removed = listBarang.remove(index);
            saveAllBarangToFile();
            System.out.println("Barang deleted: " + removed.name);
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
            System.out.println("Transaksi accepted for: " + transaksi.akun.id);
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
            System.out.println("All barang saved to allBarang.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
    
