import java.util.ArrayList;

public class Keranjang {
    private ArrayList<Barang> barang;

    public Keranjang() {
        this.barang = new ArrayList<>();
    }

    public void addBarang(Barang barang) {
        this.barang.add(barang);
    }

    public void clear() {
        this.barang.clear();
    }

    public ArrayList<Barang> getBarang() {
        return new ArrayList<>(this.barang);
    }

    public boolean isEmpty() {
        return this.barang.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Barang b : barang) {
            sb.append(b.name).append(" (x").append(b.quantity).append("), ");
        }
        return sb.toString();
    }
}
