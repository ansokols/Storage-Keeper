package Database;

import Model.Area;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class AreaDataAccessor extends DataAccessor{

    public HashMap<Integer, Area> getAreaHashMap() {
        HashMap<Integer, Area> areaHashMap = new HashMap<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(
                        "SELECT * FROM area")
        ){
            while (resultSet.next()) {
                areaHashMap.put(resultSet.getInt("area_id"), new Area(
                        resultSet.getInt("area_id"),
                        resultSet.getString("name")
                ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return areaHashMap;
    }
}
