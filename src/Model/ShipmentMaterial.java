package Model;

public class ShipmentMaterial {
    private Integer shipmentMaterialId;
    private Integer amount;
    private Integer loadedAmount;
    private Double unitPrice;
    private Integer shipmentId;
    private Integer materialId;

    public ShipmentMaterial(Integer shipmentMaterialId, Integer amount, Integer loadedAmount, Double unitPrice, Integer shipmentId, Integer materialId) {
        this.shipmentMaterialId = shipmentMaterialId;
        this.amount = amount;
        this.loadedAmount = loadedAmount;
        this.unitPrice = unitPrice;
        this.shipmentId = shipmentId;
        this.materialId = materialId;
    }

    public Integer getShipmentMaterialId() {
        return shipmentMaterialId;
    }

    public Integer getAmount() {
        return amount;
    }
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getLoadedAmount() { return loadedAmount; }
    public void setLoadedAmount(Integer loadedAmount) { this.loadedAmount = loadedAmount; }

    public Double getUnitPrice() {
        return unitPrice;
    }
    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getShipmentId() {
        return shipmentId;
    }
    public void setShipmentId(Integer shipmentId) {
        this.shipmentId = shipmentId;
    }

    public Integer getMaterialId() {
        return materialId;
    }
    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }
}
