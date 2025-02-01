package Kursovaya.FileManegment;

import java.time.LocalDate;
import java.util.List;

public class SalesOrder {
    private String orderId;
    private String customerId;
    private List<String> wineIds;
    private List<Integer> quantities;
    private String warehouseAddress;
    private String deliveryAddress;
    private double totalPrice;
    private LocalDate orderDate;

    public SalesOrder(String orderId, String customerId, List<String> wineIds,
                      List<Integer> quantities, String warehouseAddress,
                      String deliveryAddress, double totalPrice, LocalDate orderDate) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.wineIds = wineIds;
        this.quantities = quantities;
        this.warehouseAddress = warehouseAddress;
        this.deliveryAddress = deliveryAddress;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
    }

    // Геттеры
    public String getOrderId() { return orderId; }
    public String getCustomerId() { return customerId; }
    public List<String> getWineIds() { return wineIds; }
    public List<Integer> getQuantities() { return quantities; }
    public String getWarehouseAddress() { return warehouseAddress; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public double getTotalPrice() { return totalPrice; }
    public LocalDate getOrderDate() { return orderDate; }

    // Сеттеры
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public void setWineIds(List<String> wineIds) { this.wineIds = wineIds; }
    public void setQuantities(List<Integer> quantities) { this.quantities = quantities; }
    public void setWarehouseAddress(String warehouseAddress) { this.warehouseAddress = warehouseAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }

    @Override
    public String toString() {
        return String.format(
                "SalesOrder[ID=%s, Покупатель=%s, Вина=%s, Количество=%s, Склад=%s, Доставка=%s, Сумма=%.2f, Дата=%s]",
                orderId, customerId, wineIds, quantities, warehouseAddress, deliveryAddress, totalPrice, orderDate
        );
    }
}
