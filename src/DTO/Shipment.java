package DTO;

import java.sql.Timestamp;

public class Shipment {
    private Integer shipmentId;
    private String name;
    private Timestamp date;
    private String status;
    private Integer shipperId;
    private Integer employeeId;

    public Shipment(Integer shipmentId, String name, Timestamp date, String status, Integer shipperId, Integer employeeId) {
        this.shipmentId = shipmentId;
        this.name = name;
        this.date = date;
        this.status = status;
        this.shipperId = shipperId;
        this.employeeId = employeeId;
    }

    public Integer getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Integer shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getShipperId() {
        return shipperId;
    }

    public void setShipperId(Integer shipperId) {
        this.shipperId = shipperId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public String toString()  {
        return getName();
    }
}