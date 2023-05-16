package DAO;

import Model.ShipmentMaterial;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SendingMaterialDaoImpl extends ConnectionManager implements ShipmentMaterialDao<ShipmentMaterial> {
    @Override
    public ShipmentMaterial get(int id) {
        return null;
    }

    @Override
    public List<ShipmentMaterial> getAll(int shipmentId) {
        List<ShipmentMaterial> shipmentMaterialList = new ArrayList<>();

        try (
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT sending_material.* FROM sending" +
                                " JOIN sending_material ON sending_material.sending_id = sending.sending_id" +
                                " WHERE sending_material.sending_id = ?"
                )
        ){
            statement.setString(1, String.valueOf(shipmentId));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                shipmentMaterialList.add(new ShipmentMaterial(
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
                        "UPDATE `sending_material`" +
                                " SET `amount` = '" + shipmentMaterial.getAmount() +
                                "', `loaded_amount` = '" + shipmentMaterial.getLoadedAmount() +
                                "', `unit_price` = '" + shipmentMaterial.getUnitPrice() +
                                "', `sending_id` = '" + shipmentMaterial.getShipmentId() +
                                "', `material_id` = '" + shipmentMaterial.getMaterialId() +
                                "' WHERE sending_material_id = " + shipmentMaterial.getShipmentMaterialId()
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
