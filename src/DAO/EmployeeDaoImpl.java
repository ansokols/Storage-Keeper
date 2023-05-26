package DAO;

import DTO.Employee;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDaoImpl extends ConnectionManager implements MainDao<Employee> {
    @Override
    public Employee get(int id) {
        Employee employee = null;

        try (
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM employee" +
                                " WHERE employee.employee_id = ?"
                )
        ){
            statement.setString(1, String.valueOf(id));
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

    @Override
    public List<Employee> getAll() {
        List<Employee> employeeList = new ArrayList<>();

        try (
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM employee")
        ){
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                employeeList.add(new Employee(
                        resultSet.getInt("employee_id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone_number"),
                        resultSet.getTimestamp("enrollment_date"),
                        resultSet.getTimestamp("dismissal_date"),
                        resultSet.getString("login"),
                        resultSet.getString("password"),
                        resultSet.getInt("post_id")
                ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return employeeList;
    }

    @Override
    public int save(Employee employee) {
        Integer id = null;

        try (
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO employee (name, email, phone_number, enrollment_date," +
                                "dismissal_date, login, password, post_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, employee.getName());
            statement.setString(2, employee.getEmail());
            statement.setString(3, employee.getPhoneNumber());
            statement.setTimestamp(4, employee.getEnrollmentDate());
            statement.setTimestamp(5, employee.getDismissalDate());
            statement.setString(6, employee.getLogin());
            statement.setString(7, employee.getPassword());
            statement.setInt(8, employee.getPostId());

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
    public void update(Employee employee) {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE employee" +
                                " SET name = ?," +
                                " email = ?," +
                                " phone_number = ?," +
                                " enrollment_date = ?," +
                                " dismissal_date = ?," +
                                " login = ?," +
                                " password = ?," +
                                " post_id = ?" +
                                " WHERE employee_id = ?")
        ) {
            statement.setString(1, employee.getName());
            statement.setString(2, employee.getEmail());
            statement.setString(3, employee.getPhoneNumber());
            statement.setTimestamp(4, employee.getEnrollmentDate());
            statement.setTimestamp(5, employee.getDismissalDate());
            statement.setString(6, employee.getLogin());
            statement.setString(7, employee.getPassword());
            statement.setInt(8, employee.getPostId());
            statement.setInt(9, employee.getEmployeeId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Employee employee) {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "DELETE FROM employee WHERE employee_id = ?"
                )
        ) {
            statement.setInt(1, employee.getEmployeeId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
