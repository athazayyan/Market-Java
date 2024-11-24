import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ListBarang {

    // Metode untuk menampilkan daftar barang
    public void listBarang() {
        String filePath = "allBarang.txt";
        System.out.println("+--------------------------------------------------------+");
        System.out.println("| No | ID Barang | Nama Barang       | Stok             |");
        System.out.println("+--------------------------------------------------------+");
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int index = 0; 
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    String id = data[0].trim();
                    String name = data[1].trim();
                    String stock = data[2].trim();
                    System.out.printf("| %-2d | %-9s | %-16s | %-15s |\n", index++, id, name, stock);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }

        System.out.println("+--------------------------------------------------------+");
    }
}
