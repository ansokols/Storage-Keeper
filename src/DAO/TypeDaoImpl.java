package DAO;

import DTO.Type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TypeDaoImpl extends ConnectionManager implements MainDao<Type> {

    @Override
    public Type get(int id) {
        Type type = null;

        try (
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM type" +
                                " WHERE type.type_id = ?"
                )
        ){
            statement.setString(1, String.valueOf(id));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                type = new Type(
                        resultSet.getInt("type_id"),
                        resultSet.getString("name")
                );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return type;
    }

    @Override
    public List<Type> getAll() {
        List<Type> typeList = new ArrayList<>();

        try (
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM type")
        ){
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                typeList.add(new Type(
                        resultSet.getInt("type_id"),
                        resultSet.getString("name")
                ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return typeList;
    }

    @Override
    public int save(Type type) {

        return 0;
    }

    @Override
    public void update(Type type) {

    }

    @Override
    public void delete(Type type) {

    }
}
