package Kursovaya.FileManegment;

import java.time.LocalDate;
import java.util.List;

public class CompletedSale {
    private String saleId;
    private LocalDate completionDate;
    private String customerId;
    private List<String> wineIds;
    private List<Integer> quantities;
    private String warehouseAddress;
    private String deliveryAddress;
    private double totalPrice;

    public CompletedSale(String saleId, LocalDate completionDate,
                         String customerId, List<String> wineIds,
                         List<Integer> quantities, String warehouseAddress,
                         String deliveryAddress, double totalPrice) {
        this.saleId = saleId;
        this.completionDate = completionDate;
        this.customerId = customerId;
        this.wineIds = wineIds;
        this.quantities = quantities;
        this.warehouseAddress = warehouseAddress;
        this.deliveryAddress = deliveryAddress;
        this.totalPrice = totalPrice;
    }

    // Геттеры
    public String getSaleId() { return saleId; }
    public LocalDate getCompletionDate() { return completionDate; }
    public String getCustomerId() { return customerId; }
    public List<String> getWineIds() { return wineIds; }
    public List<Integer> getQuantities() { return quantities; }
    public String getWarehouseAddress() { return warehouseAddress; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public double getTotalPrice() { return totalPrice; }

    // Сеттеры
    public void setSaleId(String saleId) { this.saleId = saleId; }
    public void setCompletionDate(LocalDate completionDate) { this.completionDate = completionDate; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public void setWineIds(List<String> wineIds) { this.wineIds = wineIds; }
    public void setQuantities(List<Integer> quantities) { this.quantities = quantities; }
    public void setWarehouseAddress(String warehouseAddress) { this.warehouseAddress = warehouseAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    @Override
    public String toString() {
        return String.format(
                "CompletedSale[ID=%s, Дата=%s, Покупатель=%s, Вина=%s, Кол-во=%s, Склад=%s, Доставка=%s, Сумма=%.2f]",
                saleId, completionDate, customerId, wineIds, quantities, warehouseAddress, deliveryAddress, totalPrice
        );
    }
}
