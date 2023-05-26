package DTO;

public class Post {
    private Integer postId;
    private String name;
    private boolean employeeAccess;
    private boolean employeeEdit;
    private boolean materialAccess;
    private boolean materialEdit;
    private boolean postAccess;
    private boolean postEdit;
    private boolean shipmentAccess;
    private boolean shipmentEdit;
    private boolean shipperAccess;
    private boolean shipperEdit;
    private boolean storageMapAccess;
    private boolean storageMapEdit;
    private boolean storageAccess;
    private boolean storageEdit;
    private boolean typeAccess;
    private boolean typeEdit;

    public Post(Integer postId, String name,
                boolean employeeAccess, boolean employeeEdit, boolean materialAccess, boolean materialEdit,
                boolean postAccess, boolean postEdit, boolean shipmentAccess, boolean shipmentEdit,
                boolean shipperAccess, boolean shipperEdit, boolean storageMapAccess, boolean storageMapEdit,
                boolean storageAccess, boolean storageEdit, boolean postAccess1, boolean postEdit1) {
        this.postId = postId;
        this.name = name;
        this.employeeAccess = employeeAccess;
        this.employeeEdit = employeeEdit;
        this.materialAccess = materialAccess;
        this.materialEdit = materialEdit;
        this.postAccess = postAccess;
        this.postEdit = postEdit;
        this.shipmentAccess = shipmentAccess;
        this.shipmentEdit = shipmentEdit;
        this.shipperAccess = shipperAccess;
        this.shipperEdit = shipperEdit;
        this.storageMapAccess = storageMapAccess;
        this.storageMapEdit = storageMapEdit;
        this.storageAccess = storageAccess;
        this.storageEdit = storageEdit;
        this.typeAccess = postAccess1;
        this.typeEdit = postEdit1;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEmployeeAccess() {
        return employeeAccess;
    }

    public void setEmployeeAccess(boolean employeeAccess) {
        this.employeeAccess = employeeAccess;
    }

    public boolean isEmployeeEdit() {
        return employeeEdit;
    }

    public void setEmployeeEdit(boolean employeeEdit) {
        this.employeeEdit = employeeEdit;
    }

    public boolean isMaterialAccess() {
        return materialAccess;
    }

    public void setMaterialAccess(boolean materialAccess) {
        this.materialAccess = materialAccess;
    }

    public boolean isMaterialEdit() {
        return materialEdit;
    }

    public void setMaterialEdit(boolean materialEdit) {
        this.materialEdit = materialEdit;
    }

    public boolean isPostAccess() {
        return postAccess;
    }

    public void setPostAccess(boolean postAccess) {
        this.postAccess = postAccess;
    }

    public boolean isPostEdit() {
        return postEdit;
    }

    public void setPostEdit(boolean postEdit) {
        this.postEdit = postEdit;
    }

    public boolean isShipmentAccess() {
        return shipmentAccess;
    }

    public void setShipmentAccess(boolean shipmentAccess) {
        this.shipmentAccess = shipmentAccess;
    }

    public boolean isShipmentEdit() {
        return shipmentEdit;
    }

    public void setShipmentEdit(boolean shipmentEdit) {
        this.shipmentEdit = shipmentEdit;
    }

    public boolean isShipperAccess() {
        return shipperAccess;
    }

    public void setShipperAccess(boolean shipperAccess) {
        this.shipperAccess = shipperAccess;
    }

    public boolean isShipperEdit() {
        return shipperEdit;
    }

    public void setShipperEdit(boolean shipperEdit) {
        this.shipperEdit = shipperEdit;
    }

    public boolean isStorageMapAccess() {
        return storageMapAccess;
    }

    public void setStorageMapAccess(boolean storageMapAccess) {
        this.storageMapAccess = storageMapAccess;
    }

    public boolean isStorageMapEdit() {
        return storageMapEdit;
    }

    public void setStorageMapEdit(boolean storageMapEdit) {
        this.storageMapEdit = storageMapEdit;
    }

    public boolean isStorageAccess() {
        return storageAccess;
    }

    public void setStorageAccess(boolean storageAccess) {
        this.storageAccess = storageAccess;
    }

    public boolean isStorageEdit() {
        return storageEdit;
    }

    public void setStorageEdit(boolean storageEdit) {
        this.storageEdit = storageEdit;
    }

    public boolean isTypeAccess() {
        return typeAccess;
    }

    public void setTypeAccess(boolean typeAccess) {
        this.typeAccess = typeAccess;
    }

    public boolean isTypeEdit() {
        return typeEdit;
    }

    public void setTypeEdit(boolean typeEdit) {
        this.typeEdit = typeEdit;
    }

    @Override
    public String toString()  {
        return getName();
    }
}
