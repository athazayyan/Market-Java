import java.util.*;

class Transaksi {
    String id;
    Customer akun;
    ArrayList<Barang> barang;

    Transaksi(String id, Customer akun, ArrayList<Barang> barang) {
        this.id = id;
        this.akun = akun;
        this.barang = barang;
    }
}
