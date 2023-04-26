package Database;

import Model.Type;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class TypeDataAccessor extends DataAccessor{

    public HashMap<Integer, Type> getTypeHashMap() {
        HashMap<Integer, Type> typeHashMap = new HashMap<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(
                        "SELECT * FROM type")
        ){
            while (resultSet.next()) {
                typeHashMap.put(resultSet.getInt("type_id"), new Type(
                        resultSet.getInt("type_id"),
                        resultSet.getString("name")
                ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return typeHashMap;
    }
}
