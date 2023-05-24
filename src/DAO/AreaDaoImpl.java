package DAO;

import DTO.Area;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AreaDaoImpl extends ConnectionManager implements MainDao<Area> {

    @Override
    public Area get(int id) {
        Area area = null;

        try (
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM area" +
                                " WHERE area.area_id = ?"
                )
        ){
            statement.setString(1, String.valueOf(id));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                area = new Area(
                        resultSet.getInt("area_id"),
                        resultSet.getString("name")
                );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return area;
    }

    @Override
    public List<Area> getAll() {
        List<Area> areaList = new ArrayList<>();

        try (
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM area")
        ){
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                areaList.add(new Area(
                        resultSet.getInt("area_id"),
                        resultSet.getString("name")
                ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return areaList;
    }

    @Override
    public int save(Area area) {
        Integer id = null;
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO area (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, area.getName());

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
    public void update(Area area) {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE area" +
                                " SET name = ?" +
                                " WHERE area_id = ?"
                )
        ) {
            statement.setString(1, area.getName());
            statement.setInt(2, area.getAreaId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Area area) {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "DELETE FROM area WHERE area_id = ?"
                )
        ) {
            statement.setInt(1, area.getAreaId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
