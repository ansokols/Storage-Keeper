package Database;

import Model.Unit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class UnitDataAccessor extends DataAccessor {

    public HashMap<Integer, Unit> getUnitHashMap() {
        HashMap<Integer, Unit> unitHashMap = new HashMap<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(
                        "SELECT * FROM unit")
        ){
            while (resultSet.next()) {
                unitHashMap.put(resultSet.getInt("unit_id"), new Unit(
                        resultSet.getInt("unit_id"),
                        resultSet.getString("name")
                ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return unitHashMap;
    }
}
