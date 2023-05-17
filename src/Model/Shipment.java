package Model;

import java.sql.Timestamp;

public class Shipment {
    private Integer shipmentId;
    private Timestamp date;
    private String status;
    private Integer shipperId;
    private Integer employeeId;

    public Shipment(Integer supplyId, Timestamp date, String status, Integer shipperId, Integer employuuId) {
        this.shipmentId = supplyId;
        this.date = date;
        this.status = status;
        this.shipperId = shipperId;
        this.employeeId = employuuId;
    }

    public Integer getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Integer shipmentId) {
        this.shipmentId = shipmentId;
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
        return getDate().toString();
    }
}