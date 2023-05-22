package DTO;

public class Cell {
    private Integer cellId;
    private String name;
    private Integer occupancy;
    private Integer areaId;
    private Integer materialId;

    public Cell(Integer cellId, String name, Integer occupancy, Integer areaId, Integer materialId) {
        this.cellId = cellId;
        this.name = name;
        this.occupancy = occupancy;
        this.areaId = areaId;
        this.materialId = materialId;
    }

    public Integer getCellId() {
        return cellId;
    }

    public void setCellId(Integer cellId) {
        this.cellId = cellId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }
}
