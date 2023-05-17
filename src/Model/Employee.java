package Model;

import java.sql.Timestamp;

public class Employee {
    private Integer employeeId;
    private String name;
    private String email;
    private String phoneNumber;
    private Timestamp enrollmentDate;
    private Timestamp dismissalDate;
    private String login;
    private String password;
    private Integer postId;

    public Employee(Integer employeeId, String name, String email, String phoneNumber, Timestamp enrollmentDate, Timestamp dismissalDate, String login, String password, Integer postId) {
        this.employeeId = employeeId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.enrollmentDate = enrollmentDate;
        this.dismissalDate = dismissalDate;
        this.login = login;
        this.password = password;
        this.postId = postId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Timestamp getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(Timestamp enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public Timestamp getDismissalDate() {
        return dismissalDate;
    }

    public void setDismissalDate(Timestamp dismissalDate) {
        this.dismissalDate = dismissalDate;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }
}
