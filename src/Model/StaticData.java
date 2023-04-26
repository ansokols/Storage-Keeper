package Model;

import Database.*;

import java.util.HashMap;

public class StaticData {
    private static final AreaDataAccessor areaDataAccessor = new AreaDataAccessor();
    private static final CellDataAccessor cellDataAccessor = new CellDataAccessor();
    private static final MaterialDataAccessor materialDataAccessor = new MaterialDataAccessor();
    private static final TypeDataAccessor typeDataAccessor = new TypeDataAccessor();
    private static final UnitDataAccessor unitDataAccessor = new UnitDataAccessor();
    private static final ShipmentDataAccessor shipmentDataAccessor = new ShipmentDataAccessor();

    private static final HashMap<Integer, Area> areaHashMap = areaDataAccessor.getAreaHashMap();
    private static final HashMap<Integer, Cell> cellHashMap = cellDataAccessor.getCellHashMap();
    private static final HashMap<Integer, Material> materialHashMap = materialDataAccessor.getMaterialHashMap();
    private static final HashMap<Integer, Type> typeHashMap = typeDataAccessor.getTypeHashMap();
    private static final HashMap<Integer, Unit> unitHashMap = unitDataAccessor.getUnitHashMap();
    private static final HashMap<Integer, Shipment> supplyHashMap = shipmentDataAccessor.getSupplyHashMap();
    private static final HashMap<Integer, Shipment> sendingHashMap = shipmentDataAccessor.getSendingHashMap();


    public static HashMap<Integer, Area> getAreaHashMap() {
        return areaHashMap;
    }
    public static void setArea(Area area) {
        StaticData.areaHashMap.put(area.getAreaId(), area);
    }

    public static HashMap<Integer, Cell> getCellHashMap() {
        return cellHashMap;
    }
    public static void setCell(Cell cell) {
        StaticData.cellHashMap.put(cell.getCellId(), cell);
    }

    public static HashMap<Integer, Material> getMaterialHashMap() {
        return materialHashMap;
    }
    public static void setMaterial(Material material) {
        StaticData.materialHashMap.put(material.getMaterialId(), material);
    }

    public static HashMap<Integer, Type> getTypeHashMap() {
        return typeHashMap;
    }
    public static void setType(Type type) {
        StaticData.typeHashMap.put(type.getTypeId(), type);
    }

    public static HashMap<Integer, Unit> getUnitHashMap() {
        return unitHashMap;
    }
    public static void setUnit(Unit unit) {
        StaticData.unitHashMap.put(unit.getUnitId(), unit);
    }

    public static HashMap<Integer, Shipment> getSupplyHashMap() {
        return supplyHashMap;
    }
    public static void setSupply(Shipment shipment) {
        StaticData.supplyHashMap.put(shipment.getShipmentId(), shipment);
    }

    public static HashMap<Integer, Shipment> getSendingHashMap() {
        return sendingHashMap;
    }
    public static void setSending(Shipment shipment) {
        StaticData.sendingHashMap.put(shipment.getShipmentId(), shipment);
    }
}
