package DAO;

import DTO.CellType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CellTypeDaoImpl extends ConnectionManager implements CellTypeDao<CellType>{

    @Override
    public CellType get(int cellId, int typeId) {
        CellType cellType = null;
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM cell_type" +
                                " WHERE cell_type.cell_id = ? AND cell_type.type_id = ?"
                )
        ){
            statement.setString(1, String.valueOf(cellId));
            statement.setString(2, String.valueOf(typeId));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                cellType = new CellType(
                        resultSet.getInt("cell_type_id"),
                        resultSet.getInt("capacity"),
                        resultSet.getInt("cell_id"),
                        resultSet.getInt("type_id")
                );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return cellType;
    }

    @Override
    public List<CellType> getAllByCell(int cellId) {
        List<CellType> cellTypeList = new ArrayList<>();

        try (
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT cell_type.* FROM type" +
                                " JOIN cell_type ON cell_type.type_id = type.type_id" +
                                " WHERE cell_type.cell_id = ?"
                )
        ){
            statement.setString(1, String.valueOf(cellId));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                cellTypeList.add(new CellType(
                        resultSet.getInt("cell_type_id"),
                        resultSet.getInt("capacity"),
                        resultSet.getInt("cell_id"),
                        resultSet.getInt("type_id")
                ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return cellTypeList;
    }

    @Override
    public List<CellType> getAllByType(int typeId) {
        List<CellType> cellTypeList = new ArrayList<>();

        try (
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT cell_type.* FROM cell" +
                                " JOIN cell_type ON cell_type.cell_id = cell.cell_id" +
                                " WHERE cell_type.type_id = ?"
                )
        ){
            statement.setString(1, String.valueOf(typeId));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                cellTypeList.add(new CellType(
                        resultSet.getInt("cell_type_id"),
                        resultSet.getInt("capacity"),
                        resultSet.getInt("cell_id"),
                        resultSet.getInt("type_id")
                ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return cellTypeList;
    }

    @Override
    public int save(CellType cellType) {
        Integer id = null;
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO cell_type (capacity, cell_id, type_id)" +
                                "VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setInt(1, cellType.getCapacity());
            statement.setInt(2, cellType.getCellId());
            statement.setDouble(3, cellType.getTypeId());

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
    public void update(CellType cellType) {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE cell_type" +
                                " SET capacity = ?" +
                                ", cell_id = ?" +
                                ", type_id = ?" +
                                " WHERE cell_type_id = ?"
                )
        ) {
            statement.setInt(1, cellType.getCapacity());
            statement.setInt(2, cellType.getCellId());
            statement.setInt(3, cellType.getTypeId());
            statement.setInt(4, cellType.getCellTypeId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(CellType cellType) {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "DELETE FROM cell_type WHERE cell_type_id = ?"
                )
        ) {
            statement.setInt(1, cellType.getCellTypeId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
