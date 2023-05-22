package DTO;

public class CellType {
    private Integer cellTypeId;
    private Integer capacity;
    private Integer cellId;
    private Integer typeId;

    public CellType(Integer cellTypeId, Integer capacity, Integer cellId, Integer typeId) {
        this.cellTypeId = cellTypeId;
        this.capacity = capacity;
        this.cellId = cellId;
        this.typeId = typeId;
    }

    public Integer getCellTypeId() {
        return cellTypeId;
    }

    public void setCellTypeId(Integer cellTypeId) {
        this.cellTypeId = cellTypeId;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getCellId() {
        return cellId;
    }

    public void setCellId(Integer cellId) {
        this.cellId = cellId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }
}
