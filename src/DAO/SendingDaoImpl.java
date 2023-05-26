package DAO;

import DTO.Shipment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SendingDaoImpl extends ConnectionManager implements MainDao<Shipment> {
    @Override
    public Shipment get(int id) {
        Shipment sending = null;

        try (
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM sending" +
                                " WHERE sending.sending_id = ?"
                )
        ){
            statement.setString(1, String.valueOf(id));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                sending = new Shipment(
                        resultSet.getInt("sending_id"),
                        resultSet.getString("name"),
                        resultSet.getTimestamp("date"),
                        resultSet.getString("status"),
                        resultSet.getInt("client_id"),
                        resultSet.getInt("employee_id")
                );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return sending;
    }

    @Override
    public List<Shipment> getAll() {
        List<Shipment> sendingList = new ArrayList<>();

        try (
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM sending")
        ){
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                sendingList.add(new Shipment(
                        resultSet.getInt("sending_id"),
                        resultSet.getString("name"),
                        resultSet.getTimestamp("date"),
                        resultSet.getString("status"),
                        resultSet.getInt("client_id"),
                        resultSet.getInt("employee_id")
                ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return sendingList;
    }

    @Override
    public int save(Shipment sending) {
        Integer id = null;

        try (
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO sending (name, date, status, client_id, employee_id)" +
                                "VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, sending.getName());
            statement.setTimestamp(2, sending.getDate());
            statement.setString(3, sending.getStatus());
            statement.setInt(4, sending.getShipperId());
            statement.setInt(5, sending.getEmployeeId());

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
    public void update(Shipment sending) {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE sending" +
                                " SET name = ?" +
                                ", date = ?" +
                                ", status = ?" +
                                ", client_id = ?" +
                                ", employee_id = ?" +
                                " WHERE sending_id = ?"
                )
        ) {
            statement.setString(1, sending.getName());
            statement.setTimestamp(2, sending.getDate());
            statement.setString(3, sending.getStatus());
            statement.setInt(4, sending.getShipperId());
            statement.setInt(5, sending.getEmployeeId());
            statement.setInt(6,sending.getShipmentId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Shipment sending) {
        try (
                PreparedStatement statement1 = connection.prepareStatement(
                        "DELETE FROM sending WHERE sending_id = ?"
                );
                PreparedStatement statement2 = connection.prepareStatement(
                        "DELETE FROM sending_material WHERE sending_id = ?"
                )
        ) {
            statement1.setInt(1, sending.getShipmentId());
            statement2.setInt(1, sending.getShipmentId());

            statement1.executeUpdate();
            statement2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
