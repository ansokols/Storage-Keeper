package DTO;

public class Unit {
    private Integer unitId;
    private String name;

    public Unit(Integer unitId, String name) {
        this.unitId = unitId;
        this.name = name;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString()  {
        return getName();
    }
}
