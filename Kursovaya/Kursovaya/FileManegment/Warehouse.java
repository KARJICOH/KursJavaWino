package Kursovaya.FileManegment;

public class Warehouse {
    private String address;
    private int capacity;
    private int currentStock;

    public Warehouse(String address, int capacity, int currentStock) {
        this.address = address;
        this.capacity = capacity;
        this.currentStock = currentStock;
    }

    // Геттеры
    public String getAddress() { return address; }
    public int getCapacity() { return capacity; }
    public int getCurrentStock() { return currentStock; }

    // Сеттеры
    public void setAddress(String address) { this.address = address; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }

    @Override
    public String toString() {
        return String.format("Склад [Адрес: %s, Вместимость: %d, Текущий запас: %d]",
                address, capacity, currentStock);
    }
}
