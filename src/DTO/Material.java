package DTO;

public class Material {
    private Integer materialId;
    private String name;
    private String manufacturer;
    private Double unitPrice;
    private Integer amount;
    private Integer typeId;
    private Integer unitId;

    public Material(Integer materialId, String name, String manufacturer, Double unitPrice, Integer amount, Integer typeId, Integer unitId) {
        this.materialId = materialId;
        this.name = name;
        this.manufacturer = manufacturer;
        this.unitPrice = unitPrice;
        this.amount = amount;
        this.typeId = typeId;
        this.unitId = unitId;
    }

    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }
}
