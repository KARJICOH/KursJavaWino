package Kursovaya.FileManegment;

import java.time.LocalDate;

public class Wine {
    private String id;
    private String brand;
    private String type;
    private String variety;
    private String origin;
    private LocalDate bottlingDate;
    private double alcoholContent;
    private String supplierId;
    private double purchasePrice;
    private double salePrice;

    public Wine(String id, String brand, String type, String variety, String origin,
                LocalDate bottlingDate, double alcoholContent, String supplierId,
                double purchasePrice, double salePrice) {
        this.id = id;
        this.brand = brand;
        this.type = type;
        this.variety = variety;
        this.origin = origin;
        this.bottlingDate = bottlingDate;
        this.alcoholContent = alcoholContent;
        this.supplierId = supplierId;
        this.purchasePrice = purchasePrice;
        this.salePrice = salePrice;
    }

    // Геттеры
    public String getId() { return id; }
    public String getBrand() { return brand; }
    public String getType() { return type; }
    public String getVariety() { return variety; }
    public String getOrigin() { return origin; }
    public LocalDate getBottlingDate() { return bottlingDate; }
    public double getAlcoholContent() { return alcoholContent; }
    public String getSupplierId() { return supplierId; }
    public double getPurchasePrice() { return purchasePrice; }
    public double getSalePrice() { return salePrice; }

    // Сеттеры
    public void setId(String id) { this.id = id; }
    public void setBrand(String brand) { this.brand = brand; }
    public void setType(String type) { this.type = type; }
    public void setVariety(String variety) { this.variety = variety; }
    public void setOrigin(String origin) { this.origin = origin; }
    public void setBottlingDate(LocalDate bottlingDate) { this.bottlingDate = bottlingDate; }
    public void setAlcoholContent(double alcoholContent) { this.alcoholContent = alcoholContent; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }
    public void setPurchasePrice(double purchasePrice) { this.purchasePrice = purchasePrice; }
    public void setSalePrice(double salePrice) { this.salePrice = salePrice; }

    @Override
    public String toString() {
        return String.format(
                "Wine[ID=%s, Бренд=%s, Тип=%s, Сорт=%s, Происхождение=%s, Дата розлива=%s, Алкоголь=%.1f%%, Поставщик=%s, Цена закупки=%.2f, Цена продажи=%.2f]",
                id, brand, type, variety, origin, bottlingDate, alcoholContent, supplierId, purchasePrice, salePrice
        );
    }
}
