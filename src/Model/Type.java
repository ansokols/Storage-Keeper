package Model;

public class Type {
    private Integer typeId;
    private String name;

    public Type(Integer typeId, String name) {
        this.typeId = typeId;
        this.name = name;
    }

    public Integer getTypeId() { return typeId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
