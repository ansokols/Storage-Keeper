package Model;

public class Area {
    private Integer areaId;
    private String name;

    public Area(Integer id, String name) {
        this.areaId = id;
        this.name = name;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
