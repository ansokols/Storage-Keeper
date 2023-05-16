package DAO;

import Model.Cell;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CellDaoImpl extends ConnectionManager implements Dao<Cell> {

    @Override
    public Cell get(int id) {
        Cell cell = null;

        try (
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM cell" +
                                " WHERE cell.cell_id = ?"
                )
        ){
            statement.setString(1, String.valueOf(id));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                cell = new Cell(
                        resultSet.getInt("cell_id"),
                        resultSet.getString("name"),
                        resultSet.getInt("capacity"),
                        resultSet.getInt("occupancy"),
                        resultSet.getInt("area_id"),
                        resultSet.getInt("type_id"),
                        resultSet.getInt("material_id")
                );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return cell;
    }

    @Override
    public List<Cell> getAll() {
        List<Cell> cellList = new ArrayList<>();

        try (
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM cell")
        ){
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                cellList.add(new Cell(
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

        return cellList;
    }

    @Override
    public int save(Cell cell) {

        return 0;
    }

    @Override
    public void update(Cell cell) {

    }

    @Override
    public void delete(Cell cell) {

    }
}

