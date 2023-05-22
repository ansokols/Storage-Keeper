package DAO;

import DTO.Shipment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SupplyDaoImpl extends ConnectionManager implements MainDao<Shipment> {
    @Override
    public Shipment get(int id) {
        Shipment supply = null;

        try (
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM supply" +
                                " WHERE supply.supply_id = ?"
                )
        ){
            statement.setString(1, String.valueOf(id));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                supply = new Shipment(
                        resultSet.getInt("supply_id"),
                        resultSet.getString("name"),
                        resultSet.getTimestamp("date"),
                        resultSet.getString("status"),
                        resultSet.getInt("supplier_id"),
                        resultSet.getInt("employee_id")
                );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return supply;
    }

    @Override
    public List<Shipment> getAll() {
        List<Shipment> supplyList = new ArrayList<>();

        try (
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM supply")
        ){
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                supplyList.add(new Shipment(
                        resultSet.getInt("supply_id"),
                        resultSet.getString("name"),
                        resultSet.getTimestamp("date"),
                        resultSet.getString("status"),
                        resultSet.getInt("supplier_id"),
                        resultSet.getInt("employee_id")
                ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return supplyList;
    }

    @Override
    public int save(Shipment supply) {
        Integer id = null;
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO supply (name, date, status, supplier_id, employee_id)" +
                                "VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, supply.getName());
            statement.setTimestamp(2, supply.getDate());
            statement.setString(3, supply.getStatus());
            statement.setInt(4, supply.getShipperId());
            statement.setInt(5, supply.getEmployeeId());

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
    public void update(Shipment supply) {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE supply" +
                                " SET name = ?" +
                                ", date = ?" +
                                ", status = ?" +
                                ", supplier_id = ?" +
                                ", employee_id = ?" +
                                " WHERE supply_id = ?"
                )
        ) {
            statement.setString(1, supply.getName());
            statement.setTimestamp(2, supply.getDate());
            statement.setString(3, supply.getStatus());
            statement.setInt(4, supply.getShipperId());
            statement.setInt(5, supply.getEmployeeId());
            statement.setInt(6,supply.getShipmentId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Shipment supply) {
        try (
                PreparedStatement statement1 = connection.prepareStatement(
                        "DELETE FROM supply WHERE supply_id = ?"
                );
                PreparedStatement statement2 = connection.prepareStatement(
                        "DELETE FROM supply_material WHERE supply_id = ?"
        )
        ) {
            statement1.setInt(1, supply.getShipmentId());
            statement2.setInt(1, supply.getShipmentId());

            statement1.executeUpdate();
            statement2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
