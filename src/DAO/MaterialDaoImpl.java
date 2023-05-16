package DAO;

import Model.Material;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaterialDaoImpl extends ConnectionManager implements Dao<Material>  {

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

        return 0;
    }

    @Override
    public void update(Material material) {

    }

    @Override
    public void delete(Material material) {

    }
}