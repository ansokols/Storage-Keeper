package DAO;

import Model.ShipmentMaterial;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplyMaterialDaoImpl extends ConnectionManager implements ShipmentMaterialDao<ShipmentMaterial> {
    @Override
    public ShipmentMaterial get(int id) {
        return null;
    }

    @Override
    public List<ShipmentMaterial> getAll(int shipmentId) {
        List<ShipmentMaterial> shipmentMaterialList = new ArrayList<>();

        try (
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT supply_material.* FROM supply" +
                                " JOIN supply_material ON supply_material.supply_id = supply.supply_id" +
                                " WHERE supply_material.supply_id = ?"
                )
        ){
            statement.setString(1, String.valueOf(shipmentId));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                shipmentMaterialList.add(new ShipmentMaterial(
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

        return shipmentMaterialList;
    }

    @Override
    public int save(ShipmentMaterial shipmentMaterial) {

        return 0;
    }

    @Override
    public void update(ShipmentMaterial shipmentMaterial) {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE `supply_material`" +
                                " SET `amount` = '" + shipmentMaterial.getAmount() +
                                "', `loaded_amount` = '" + shipmentMaterial.getLoadedAmount() +
                                "', `unit_price` = '" + shipmentMaterial.getUnitPrice() +
                                "', `supply_id` = '" + shipmentMaterial.getShipmentId() +
                                "', `material_id` = '" + shipmentMaterial.getMaterialId() +
                                "' WHERE supply_material_id = " + shipmentMaterial.getShipmentMaterialId()
                )
        ) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(ShipmentMaterial shipmentMaterial) {

    }
}
