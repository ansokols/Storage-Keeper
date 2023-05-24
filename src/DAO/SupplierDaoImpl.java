package DAO;

import DTO.Shipper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SupplierDaoImpl extends ConnectionManager implements MainDao<Shipper> {
    @Override
    public Shipper get(int id) {
        Shipper supplier = null;

        try (
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM supplier" +
                                " WHERE supplier.supplier_id = ?"
                )
        ){
            statement.setString(1, String.valueOf(id));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                supplier = new Shipper(
                        resultSet.getInt("supplier_id"),
                        resultSet.getString("name"),
                        resultSet.getString("address"),
                        resultSet.getString("phone_number"),
                        resultSet.getString("contact_person")
                );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return supplier;
    }

    @Override
    public List<Shipper> getAll() {
        List<Shipper> supplierList = new ArrayList<>();

        try (
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM supplier")
        ){
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                supplierList.add(new Shipper(
                        resultSet.getInt("supplier_id"),
                        resultSet.getString("name"),
                        resultSet.getString("address"),
                        resultSet.getString("phone_number"),
                        resultSet.getString("contact_person")
                ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return supplierList;
    }

    @Override
    public int save(Shipper supplier) {
        Integer id = null;
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO supplier (name, address, phone_number, contact_person) VALUES (?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, String.valueOf(supplier.getName()));
            statement.setString(2, String.valueOf(supplier.getAddress()));
            statement.setString(3, String.valueOf(supplier.getPhoneNumber()));
            statement.setString(4, String.valueOf(supplier.getContactPerson()));

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
    public void update(Shipper supplier) {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE supplier" +
                                " SET name = ?" +
                                ", address = ?" +
                                ", phone_number = ?" +
                                ", contact_person = ?" +
                                " WHERE supplier_id = ?"
                )
        ) {
            statement.setString(1, supplier.getName());
            statement.setString(2, supplier.getAddress());
            statement.setString(3, supplier.getPhoneNumber());
            statement.setString(4, supplier.getContactPerson());
            statement.setInt(5, supplier.getShipperId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Shipper supplier) {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "DELETE FROM supplier WHERE supplier_id = ?"
                )
        ) {
            statement.setInt(1, supplier.getShipperId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
