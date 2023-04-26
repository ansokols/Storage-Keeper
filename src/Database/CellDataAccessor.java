package Database;

import Model.Cell;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class CellDataAccessor extends DataAccessor{

    public HashMap<Integer, Cell> getCellHashMap() {
        HashMap<Integer, Cell> cellHashMap = new HashMap<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM cell")
        ){
            while (resultSet.next()) {
                cellHashMap.put(resultSet.getInt("cell_id"), new Cell(
                        resultSet.getInt("cell_id"),
                        resultSet.getString("name"),
                        resultSet.getInt("capacity"),
                        resultSet.getInt("occupancy"),
                        resultSet.getInt("area_id"),
                        resultSet.getInt("type_id"),
                        resultSet.getInt("material_id")
                ));
            }
        } catch (SQLException throwables) {
                throwables.printStackTrace();
        }
        return cellHashMap;
    }
}

