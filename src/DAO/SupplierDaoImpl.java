package DAO;

import Model.Shipper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierDaoImpl extends ConnectionManager implements Dao<Shipper> {
    @Override
    public Shipper get(int id) {
        return null;
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
    public int save(Shipper shipper) {
        return 0;
    }

    @Override
    public void update(Shipper shipper) {

    }

    @Override
    public void delete(Shipper shipper) {

    }
}
