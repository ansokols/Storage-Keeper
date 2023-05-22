package DAO;

import DTO.ShipmentMaterial;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SupplyMaterialDaoImpl extends ConnectionManager implements ShipmentMaterialDao<ShipmentMaterial> {
    @Override
    public ShipmentMaterial get(int id) {
        return null;
    }

    @Override
    public List<ShipmentMaterial> getAllByShipment(int supplyId) {
        List<ShipmentMaterial> shipmentMaterialList = new ArrayList<>();

        try (
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT supply_material.* FROM supply" +
                                " JOIN supply_material ON supply_material.supply_id = supply.supply_id" +
                                " WHERE supply_material.supply_id = ?"
                )
        ){
            statement.setString(1, String.valueOf(supplyId));
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
    public int save(ShipmentMaterial supplyMaterial) {
        Integer id = null;
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO supply_material (loaded_amount, amount, unit_price, supply_id, material_id)" +
                                "VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setInt(1, supplyMaterial.getLoadedAmount());
            statement.setInt(2, supplyMaterial.getAmount());
            statement.setDouble(3, supplyMaterial.getUnitPrice());
            statement.setInt(4, supplyMaterial.getShipmentId());
            statement.setInt(5, supplyMaterial.getMaterialId());

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
    public void update(ShipmentMaterial supplyMaterial) {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE supply_material" +
                                " SET loaded_amount = ?" +
                                ", amount = ?" +
                                ", unit_price = ?" +
                                ", supply_id = ?" +
                                ", material_id = ?" +
                                " WHERE supply_material_id = ?"
                )
        ) {
            statement.setInt(1, supplyMaterial.getLoadedAmount());
            statement.setInt(2, supplyMaterial.getAmount());
            statement.setDouble(3, supplyMaterial.getUnitPrice());
            statement.setInt(4, supplyMaterial.getShipmentId());
            statement.setInt(5, supplyMaterial.getMaterialId());
            statement.setInt(6, supplyMaterial.getShipmentMaterialId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(ShipmentMaterial supplyMaterial) {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "DELETE FROM supply_material WHERE supply_material_id = ?"
                )
        ) {
            statement.setInt(1, supplyMaterial.getShipmentMaterialId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
