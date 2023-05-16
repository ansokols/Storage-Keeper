package DAO;

import Model.Area;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AreaDaoImpl extends ConnectionManager implements Dao<Area> {

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

        return 0;
    }

    @Override
    public void update(Area area) {

    }

    @Override
    public void delete(Area area) {

    }
}
