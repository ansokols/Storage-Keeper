package DAO;

import DTO.Unit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UnitDaoImpl extends ConnectionManager implements MainDao<Unit> {

    @Override
    public Unit get(int id) {
        Unit unit = null;

        try (
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM unit" +
                                " WHERE unit.unit_id = ?"
                )
        ){
            statement.setString(1, String.valueOf(id));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                unit = new Unit(
                        resultSet.getInt("unit_id"),
                        resultSet.getString("name")
                );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return unit;
    }

    @Override
    public List<Unit> getAll() {
        List<Unit> unitList = new ArrayList<>();

        try (
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM unit")
        ){
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                unitList.add(new Unit(
                        resultSet.getInt("unit_id"),
                        resultSet.getString("name")
                ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return unitList;
    }

    @Override
    public int save(Unit unit) {

        return 0;
    }

    @Override
    public void update(Unit unit) {

    }

    @Override
    public void delete(Unit unit) {

    }
}
