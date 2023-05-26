package DAO;

import DTO.Employee;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorizationDaoImpl extends ConnectionManager implements AuthorizationDao {

    @Override
    public Employee checkPassword(String login, String password) {
        Employee employee = null;

        try (
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM employee " +
                                "WHERE login = ? AND password = ?")
        ){
            statement.setString(1, String.valueOf(login));
            statement.setString(2, String.valueOf(password));
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                employee = new Employee(
                        resultSet.getInt("employee_id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone_number"),
                        resultSet.getTimestamp("enrollment_date"),
                        resultSet.getTimestamp("dismissal_date"),
                        resultSet.getString("login"),
                        resultSet.getString("password"),
                        resultSet.getInt("post_id")
                );

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return employee;
    }
}
