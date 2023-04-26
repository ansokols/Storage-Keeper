package Database;

import Model.Material;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class MaterialDataAccessor extends DataAccessor {

    public HashMap<Integer, Material> getMaterialHashMap() {
        HashMap<Integer, Material> materialHashMap = new HashMap<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM material")
        ){
            while (resultSet.next()) {
                materialHashMap.put(resultSet.getInt("material_id"), new Material(
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
        return materialHashMap;
    }
}