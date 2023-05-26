package DAO;

import DTO.Employee;

public interface AuthorizationDao {

    Employee checkPassword(String login, String password);
}
