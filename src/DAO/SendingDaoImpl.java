package DAO;

import Model.Shipment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SendingDaoImpl extends ConnectionManager implements Dao<Shipment> {
    @Override
    public Shipment get(int id) {
        return null;
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
    public int save(Shipment shipment) {

        return 0;
    }

    @Override
    public void update(Shipment shipment) {

    }

    @Override
    public void delete(Shipment shipment) {

    }
}