package models;

public class Customer {
    private String nic;
    private String name;
    private String phone;
    private String email;

    public Customer(String nic, String name, String phone, String email) {
        this.nic = nic;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public String getNic() { return nic; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
}
