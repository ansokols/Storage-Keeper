package Model;

public class Cell {
    private Integer cellId;
    private String name;
    private Integer capacity;
    private Integer occupancy;
    private Integer areaId;
    private Integer typeId;
    private Integer materialId;

    public Cell(Integer cellId, String name, Integer capacity, Integer occupancy, Integer areaId, Integer typeId, Integer materialId) {
        this.cellId = cellId;
        this.name = name;
        this.capacity = capacity;
        this.occupancy = occupancy;
        this.areaId = areaId;
        this.typeId = typeId;
        this.materialId = materialId;
    }

    public Integer getCellId() {
        return cellId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Integer getCapacity() {
        return capacity;
    }
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getOccupancy() {
        return occupancy;
    }
    public void setOccupancy(Integer occupancy) {
        this.occupancy = occupancy;
    }

    public Integer getAreaId() {
        return areaId;
    }
    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public Integer getTypeId() {
        return typeId;
    }
    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getMaterialId() {
        return materialId;
    }
    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }
}
