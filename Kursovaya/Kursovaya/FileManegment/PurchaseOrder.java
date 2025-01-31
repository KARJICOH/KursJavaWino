package Kursovaya.FileManegment;

import java.time.LocalDate;
import java.util.List;

public class PurchaseOrder {
    private String orderId;
    private String supplierId;
    private List<String> wineIds;
    private List<Integer> quantities;
    private String deliveryAddress;
    private double totalPrice;
    private LocalDate orderDate;

    public PurchaseOrder(String orderId, String supplierId, List<String> wineIds,
                         List<Integer> quantities, String deliveryAddress,
                         double totalPrice, LocalDate orderDate) {
        this.orderId = orderId;
        this.supplierId = supplierId;
        this.wineIds = wineIds;
        this.quantities = quantities;
        this.deliveryAddress = deliveryAddress;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
    }

    // Геттеры
    public String getOrderId() { return orderId; }
    public String getSupplierId() { return supplierId; }
    public List<String> getWineIds() { return wineIds; }
    public List<Integer> getQuantities() { return quantities; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public double getTotalPrice() { return totalPrice; }
    public LocalDate getOrderDate() { return orderDate; }

    // Сеттеры
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }
    public void setWineIds(List<String> wineIds) { this.wineIds = wineIds; }
    public void setQuantities(List<Integer> quantities) { this.quantities = quantities; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }

    @Override
    public String toString() {
        return String.format(
                "PurchaseOrder[ID=%s, Поставщик=%s, Вина=%s, Количество=%s, Адрес=%s, Сумма=%.2f, Дата=%s]",
                orderId, supplierId, wineIds, quantities, deliveryAddress, totalPrice, orderDate
        );
    }
}
