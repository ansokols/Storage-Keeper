package DAO;

import DTO.Cell;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CellDaoImpl extends ConnectionManager implements CellDao<Cell> {

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
    public List<Cell> getAllByArea(int areaId) {
        List<Cell> cellList = new ArrayList<>();

        try (
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT cell.* FROM area" +
                                " JOIN cell ON cell.area_id = area.area_id" +
                                " WHERE cell.area_id = ?"
                )
        ){
            statement.setString(1, String.valueOf(areaId));
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
                        "INSERT INTO cell (name, occupancy, area_id, material_id) VALUES (?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, cell.getName());
            statement.setInt(2, cell.getOccupancy());
            statement.setInt(3, cell.getAreaId());
            if (cell.getMaterialId() == null) {
                statement.setNull(4, Types.INTEGER);
            } else {
                statement.setInt(4, cell.getMaterialId());
            }

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
            if (cell.getMaterialId() == null) {
                statement.setNull(4, Types.INTEGER);
            } else {
                statement.setInt(4, cell.getMaterialId());
            }
            statement.setInt(5, cell.getCellId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Cell cell) {
        try (
                PreparedStatement statement1 = connection.prepareStatement(
                        "DELETE FROM cell WHERE cell_id = ?"
                );
                PreparedStatement statement2 = connection.prepareStatement(
                        "DELETE FROM cell_type WHERE cell_id = ?"
                )
        ) {
            statement1.setInt(1, cell.getCellId());
            statement2.setInt(1, cell.getCellId());

            statement1.executeUpdate();
            statement2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

