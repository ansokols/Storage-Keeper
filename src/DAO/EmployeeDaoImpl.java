package DAO;

import Model.Employee;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDaoImpl extends ConnectionManager implements Dao<Employee> {
    @Override
    public Employee get(int id) {
        return null;
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
        return 0;
    }

    @Override
    public void update(Employee employee) {

    }

    @Override
    public void delete(Employee employee) {

    }
}
