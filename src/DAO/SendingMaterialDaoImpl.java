package DAO;

import DTO.ShipmentMaterial;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SendingMaterialDaoImpl extends ConnectionManager implements ShipmentMaterialDao<ShipmentMaterial> {
    @Override
    public ShipmentMaterial get(int id) {
        return null;
    }

    @Override
    public List<ShipmentMaterial> getAllByShipment(int sendingId) {
        List<ShipmentMaterial> shipmentMaterialList = new ArrayList<>();

        try (
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT sending_material.* FROM sending" +
                                " JOIN sending_material ON sending_material.sending_id = sending.sending_id" +
                                " WHERE sending_material.sending_id = ?"
                )
        ){
            statement.setString(1, String.valueOf(sendingId));
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
    public int save(ShipmentMaterial sendingMaterial) {
        Integer id = null;
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO sending_material (loaded_amount, amount, unit_price, sending_id, material_id)" +
                                "VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setInt(1, sendingMaterial.getLoadedAmount());
            statement.setInt(2, sendingMaterial.getAmount());
            statement.setDouble(3, sendingMaterial.getUnitPrice());
            statement.setInt(4, sendingMaterial.getShipmentId());
            statement.setInt(5, sendingMaterial.getMaterialId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating failed, no rows affected");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    id = generatedKeys.getInt(1);
                }
                else {
                    throw new SQLException("Creating failed, no ID obtained");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public void update(ShipmentMaterial sendingMaterial) {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE sending_material" +
                                " SET amount = ?" +
                                ", loaded_amount = ?" +
                                ", unit_price = ?" +
                                ", sending_id = ?" +
                                ", material_id = ?" +
                                " WHERE sending_material_id = ?"
                )
        ) {
            statement.setInt(1, sendingMaterial.getAmount());
            statement.setInt(2, sendingMaterial.getLoadedAmount());
            statement.setDouble(3, sendingMaterial.getUnitPrice());
            statement.setInt(4, sendingMaterial.getShipmentId());
            statement.setInt(5, sendingMaterial.getMaterialId());
            statement.setInt(6, sendingMaterial.getShipmentMaterialId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(ShipmentMaterial sendingMaterial) {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "DELETE FROM sending_material WHERE sending_material_id = ?"
                )
        ) {
            statement.setInt(1, sendingMaterial.getShipmentMaterialId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
