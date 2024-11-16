class Akun {
    String id;

    Akun(String id) {
        this.id = id;
    }
}

abstract class Driver {
    Akun akun;
}

class Admin extends Akun {
    Admin(String id) {
        super(id);
    }
}

class Customer extends Akun {
    Customer(String id) {
        super(id);
    }
}
