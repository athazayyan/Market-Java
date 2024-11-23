public class Customer extends Akun {
    String username;
    String password;

    Customer(String id, String username, String password) {
        super(id);
        this.username = username;
        this.password = password;
    }
}
