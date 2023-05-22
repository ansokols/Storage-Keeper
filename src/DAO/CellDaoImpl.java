package DAO;

import DTO.Cell;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CellDaoImpl extends ConnectionManager implements MainDao<Cell> {

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
                        resultSet.getInt("occupancy"),
                        resultSet.getInt("area_id"),
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
                        resultSet.getInt("occupancy"),
                        resultSet.getInt("area_id"),
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
        Integer id = null;
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO cell (name, occupancy, area_id, material_id) VALUES (?)")
        ) {
            statement.setString(1, cell.getName());
            statement.setInt(2, cell.getOccupancy());
            statement.setInt(3, cell.getAreaId());
            statement.setInt(4, cell.getMaterialId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    id = generatedKeys.getInt(1);
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public void update(Cell cell) {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE cell" +
                                " SET name = ?" +
                                ", occupancy = ?" +
                                ", area_id = ?" +
                                ", material_id = ?" +
                                " WHERE cell_id = ?"
                )
        ) {
            statement.setString(1, cell.getName());
            statement.setInt(2, cell.getOccupancy());
            statement.setInt(3, cell.getAreaId());
            statement.setInt(4, cell.getMaterialId());
            statement.setInt(5, cell.getCellId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Cell cell) {

    }
}

