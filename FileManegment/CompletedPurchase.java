package Kursovaya.FileManegment;

import java.time.LocalDate;
import java.util.List;

public class CompletedPurchase {
    private String purchaseId;
    private LocalDate completionDate;
    private String supplierId;
    private List<String> wineIds;
    private List<Integer> quantities;
    private String deliveryAddress;
    private double totalPrice;

    public CompletedPurchase(String purchaseId, LocalDate completionDate,
                             String supplierId, List<String> wineIds,
                             List<Integer> quantities, String deliveryAddress,
                             double totalPrice) {
        this.purchaseId = purchaseId;
        this.completionDate = completionDate;
        this.supplierId = supplierId;
        this.wineIds = wineIds;
        this.quantities = quantities;
        this.deliveryAddress = deliveryAddress;
        this.totalPrice = totalPrice;
    }

    // Геттеры
    public String getPurchaseId() { return purchaseId; }
    public LocalDate getCompletionDate() { return completionDate; }
    public String getSupplierId() { return supplierId; }
    public List<String> getWineIds() { return wineIds; }
    public List<Integer> getQuantities() { return quantities; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public double getTotalPrice() { return totalPrice; }

    // Сеттеры
    public void setPurchaseId(String purchaseId) { this.purchaseId = purchaseId; }
    public void setCompletionDate(LocalDate completionDate) { this.completionDate = completionDate; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }
    public void setWineIds(List<String> wineIds) { this.wineIds = wineIds; }
    public void setQuantities(List<Integer> quantities) { this.quantities = quantities; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    @Override
    public String toString() {
        return String.format(
                "CompletedPurchase[ID=%s, Дата=%s, Поставщик=%s, Вина=%s, Кол-во=%s, Адрес=%s, Сумма=%.2f]",
                purchaseId, completionDate, supplierId, wineIds, quantities, deliveryAddress, totalPrice
        );
    }
}