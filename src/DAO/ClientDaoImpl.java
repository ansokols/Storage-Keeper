package DAO;

import Model.Shipper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDaoImpl extends ConnectionManager implements Dao<Shipper> {
    @Override
    public Shipper get(int id) {
        return null;
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
