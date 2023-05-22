package DAO;

import DTO.Shipper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDaoImpl extends ConnectionManager implements MainDao<Shipper> {
    @Override
    public Shipper get(int id) {
        Shipper client = null;
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM client" +
                                " WHERE client.client_id = ?"
                )
        ){
            statement.setString(1, String.valueOf(id));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                client = new Shipper(
                        resultSet.getInt("client_id"),
                        resultSet.getString("name"),
                        resultSet.getString("address"),
                        resultSet.getString("phone_number"),
                        resultSet.getString("contact_person")
                );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return client;
    }

    @Override
    public List<Shipper> getAll() {
        List<Shipper> clientList = new ArrayList<>();

        try (
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM client")
        ){
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                clientList.add(new Shipper(
                        resultSet.getInt("client_id"),
                        resultSet.getString("name"),
                        resultSet.getString("address"),
                        resultSet.getString("phone_number"),
                        resultSet.getString("contact_person")
                ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return clientList;
    }

    @Override
    public int save(Shipper client) {
        Integer id = null;
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO client (name, address, phone_number, contact_person) VALUES (?)")
        ) {
            statement.setString(1, client.getName());
            statement.setString(2, client.getAddress());
            statement.setString(3, client.getPhoneNumber());
            statement.setString(4, client.getContactPerson());

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
    public void update(Shipper shipper) {

    }

    @Override
    public void delete(Shipper shipper) {

    }
}
