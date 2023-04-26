package Database;

import Model.Shipment;
import Model.ShipmentMaterial;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class ShipmentDataAccessor extends DataAccessor {

    public HashMap<Integer, Shipment> getSupplyHashMap() {
        HashMap<Integer, Shipment> supplyHashMap = new HashMap<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(
                        "SELECT * FROM supply")
        ){
            while (resultSet.next()) {
                supplyHashMap.put(resultSet.getInt("supply_id"), new Shipment(
                        resultSet.getInt("supply_id"),
                        resultSet.getTimestamp("date"),
                        resultSet.getString("status"),
                        resultSet.getInt("supplier_id"),
                        resultSet.getInt("employee_id")
                ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return supplyHashMap;
    }

    public HashMap<Integer, Shipment> getSendingHashMap() {
        HashMap<Integer, Shipment> sendingHashMap = new HashMap<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(
                        "SELECT * FROM sending")
        ){
            while (resultSet.next()) {
                sendingHashMap.put(resultSet.getInt("sending_id"), new Shipment(
                        resultSet.getInt("sending_id"),
                        resultSet.getTimestamp("date"),
                        resultSet.getString("status"),
                        resultSet.getInt("client_id"),
                        resultSet.getInt("employee_id")
                ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sendingHashMap;
    }

    public ArrayList<ShipmentMaterial> getSupplyMaterialArrayList(Integer shipmentId) {
        ArrayList<ShipmentMaterial> materialArrayList = new ArrayList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(
                        "SELECT supply_material.* FROM supply" +
                                " JOIN supply_material ON supply_material.supply_id = supply.supply_id" +
                                " WHERE supply_material.supply_id = '" + shipmentId + "'")
        ){
            while (resultSet.next()) {
                materialArrayList.add(new ShipmentMaterial(
                        resultSet.getInt("supply_material_id"),
                        resultSet.getInt("amount"),
                        resultSet.getInt("loaded_amount"),
                        resultSet.getDouble("unit_price"),
                        resultSet.getInt("supply_id"),
                        resultSet.getInt("material_id")
                ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return materialArrayList;
    }

    public ArrayList<ShipmentMaterial> getSendingMaterialArrayList(Integer shipmentId) {
        ArrayList<ShipmentMaterial> sendingMaterialArrayList = new ArrayList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(
                        "SELECT sending_material.* FROM sending" +
                                " JOIN sending_material ON sending_material.sending_id = sending.sending_id" +
                                " WHERE sending_material.sending_id = '" + shipmentId + "'")
        ){
            while (resultSet.next()) {
                sendingMaterialArrayList.add(new ShipmentMaterial(
                        resultSet.getInt("sending_material_id"),
                        resultSet.getInt("amount"),
                        resultSet.getInt("loaded_amount"),
                        resultSet.getDouble("unit_price"),
                        resultSet.getInt("sending_id"),
                        resultSet.getInt("material_id")
                ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sendingMaterialArrayList;
    }

    public void updateSupplyMaterial(ShipmentMaterial shipmentMaterial) {
        try (
                Statement statement = connection.createStatement()
        ) {
            String sql = "UPDATE `supply_material`" +
                    " SET `amount` = '" + shipmentMaterial.getAmount() +
                    "', `loaded_amount` = '" + shipmentMaterial.getLoadedAmount() +
                    "', `unit_price` = '" + shipmentMaterial.getUnitPrice() +
                    "', `supply_id` = '" + shipmentMaterial.getShipmentId() +
                    "', `material_id` = '" + shipmentMaterial.getMaterialId() +
                    "' WHERE supply_material_id = " + shipmentMaterial.getShipmentMaterialId();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateSendingMaterial(ShipmentMaterial shipmentMaterial) {
        try (
                Statement statement = connection.createStatement()
        ) {
            String sql = "UPDATE `sending_material`" +
                    " SET `amount` = '" + shipmentMaterial.getAmount() +
                    "', `loaded_amount` = '" + shipmentMaterial.getLoadedAmount() +
                    "', `unit_price` = '" + shipmentMaterial.getUnitPrice() +
                    "', `sending_id` = '" + shipmentMaterial.getShipmentId() +
                    "', `material_id` = '" + shipmentMaterial.getMaterialId() +
                    "' WHERE sending_material_id = " + shipmentMaterial.getShipmentMaterialId();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
