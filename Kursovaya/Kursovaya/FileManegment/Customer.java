package Kursovaya.FileManegment;

public class Customer {
    private String id;
    private String inn;
    private String name;
    private String address;
    private String phone;

    public Customer(String id, String inn, String name, String address, String phone) {
        this.id = id;
        this.inn = inn;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    // Геттеры
    public String getId() { return id; }
    public String getInn() { return inn; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }

    // Сеттеры
    public void setId(String id) { this.id = id; }
    public void setInn(String inn) { this.inn = inn; }
    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }
    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", inn='" + inn + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
