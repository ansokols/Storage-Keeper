package DAO;

import DTO.Type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        Integer id = null;

        try (
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO type (name)" +
                                "VALUES (?)", Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, type.getName());

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
    public void update(Type type) {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE type" +
                                " SET name = ?" +
                                " WHERE type_id = ?"
                )
        ) {
            statement.setString(1, type.getName());
            statement.setInt(2, type.getTypeId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Type type) {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "DELETE FROM type WHERE type_id = ?"
                );
        ) {
            statement.setInt(1, type.getTypeId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
