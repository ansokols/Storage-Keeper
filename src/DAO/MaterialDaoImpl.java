package DAO;

import DTO.Material;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MaterialDaoImpl extends ConnectionManager implements MainDao<Material> {

    @Override
    public Material get(int id) {
        Material material = null;

        try (
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM material" +
                                " WHERE material.material_id = ?"
                )
        ){
            statement.setString(1, String.valueOf(id));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                material = new Material(
                        resultSet.getInt("material_id"),
                        resultSet.getString("name"),
                        resultSet.getString("manufacturer"),
                        resultSet.getDouble("unit_price"),
                        resultSet.getInt("amount"),
                        resultSet.getInt("type_id"),
                        resultSet.getInt("unit_id")
                );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return material;
    }

    @Override
    public List<Material> getAll() {
        List<Material> materialList = new ArrayList<>();

        try (
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM material")
        ){
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                materialList.add(new Material(
                        resultSet.getInt("material_id"),
                        resultSet.getString("name"),
                        resultSet.getString("manufacturer"),
                        resultSet.getDouble("unit_price"),
                        resultSet.getInt("amount"),
                        resultSet.getInt("type_id"),
                        resultSet.getInt("unit_id")
                ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return materialList;
    }

    @Override
    public int save(Material material) {
        Integer id = null;
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO material (name, manufacturer, unit_price, amount, type_id, unit_id)" +
                                "VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, material.getName());
            statement.setString(2, material.getManufacturer());
            statement.setDouble(3, material.getUnitPrice());
            statement.setInt(4, material.getAmount());
            statement.setInt(5, material.getTypeId());
            statement.setInt(6, material.getUnitId());

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
    public void update(Material material) {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE material" +
                                " SET name = ?" +
                                ", manufacturer = ?" +
                                ", unit_price = ?" +
                                ", amount = ?" +
                                ", type_id = ?" +
                                ", unit_id = ?" +
                                " WHERE material_id = ?"
                )
        ) {
            statement.setString(1, material.getName());
            statement.setString(2, material.getManufacturer());
            statement.setDouble(3, material.getUnitPrice());
            statement.setInt(4, material.getAmount());
            statement.setInt(5, material.getTypeId());
            statement.setInt(6, material.getUnitId());
            statement.setInt(7,material.getMaterialId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Material material) {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "DELETE FROM material WHERE material_id = ?"
                )
        ) {
            statement.setInt(1, material.getMaterialId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}