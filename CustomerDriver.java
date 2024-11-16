import java.util.*;

class CustomerDriver extends Driver {
    ArrayList<Barang> cart;
    ArrayList<Barang> listBarang;
    ArrayList<Transaksi> listTransaksi;

    CustomerDriver(Customer akun, ArrayList<Barang> cart, ArrayList<Barang> listBarang, ArrayList<Transaksi> listTransaksi) {
        this.akun = akun;
        this.cart = cart;
        this.listBarang = listBarang;
        this.listTransaksi = listTransaksi;
    }

    public void viewBarang() {
        for (Barang barang : listBarang) {
            System.out.println("ID: " + barang.id + ", Name: " + barang.name + ", Quantity: " + barang.quantity);
        }
    }

    public void addBarangToCart(String barangId, int quantity) {
        Barang barangToAdd = null;
        for (Barang barang : listBarang) {
            if (barang.id.equals(barangId) && barang.quantity >= quantity) {
                barangToAdd = new Barang(barang.id, barang.name, quantity);
                break;
            }
        }
        if (barangToAdd != null) {
            cart.add(barangToAdd);
            System.out.println("Barang added to cart: " + barangToAdd.name);
        } else {
            System.out.println("Barang not available or insufficient quantity.");
        }
    }

    public void checkout() {
        String transaksiId = "T" + (listTransaksi.size() + 1);
        Transaksi transaksi = new Transaksi(transaksiId, (Customer) this.akun, new ArrayList<>(this.cart));
        listTransaksi.add(transaksi); // Add to the pending transactions list
        System.out.println("Checkout completed. Transaksi ID: " + transaksiId);
        cart.clear(); // Clear the cart after checkout
    }

    public void viewHistory() {
        System.out.println("Viewing history for: " + this.akun.id);
    }
}
