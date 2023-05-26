package DAO;

import DTO.Post;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PostDaoImpl extends ConnectionManager implements MainDao<Post> {
    @Override
    public Post get(int id) {
        Post post = null;

        try (
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM post" +
                                " WHERE post.post_id = ?"
                )
        ){
            statement.setString(1, String.valueOf(id));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                post = new Post(
                        resultSet.getInt("post_id"),
                        resultSet.getString("name"),
                        resultSet.getBoolean("employee_access"),
                        resultSet.getBoolean("employee_edit"),
                        resultSet.getBoolean("material_access"),
                        resultSet.getBoolean("material_edit"),
                        resultSet.getBoolean("post_access"),
                        resultSet.getBoolean("post_edit"),
                        resultSet.getBoolean("shipment_access"),
                        resultSet.getBoolean("shipment_edit"),
                        resultSet.getBoolean("shipper_access"),
                        resultSet.getBoolean("shipper_edit"),
                        resultSet.getBoolean("storage_map_access"),
                        resultSet.getBoolean("storage_map_edit"),
                        resultSet.getBoolean("storage_access"),
                        resultSet.getBoolean("storage_edit"),
                        resultSet.getBoolean("type_access"),
                        resultSet.getBoolean("type_edit")
                );

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return post;
    }

    @Override
    public List<Post> getAll() {
        List<Post> postList = new ArrayList<>();

        try (
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM post")
        ){
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                postList.add(new Post(
                        resultSet.getInt("post_id"),
                        resultSet.getString("name"),
                        resultSet.getBoolean("employee_access"),
                        resultSet.getBoolean("employee_edit"),
                        resultSet.getBoolean("material_access"),
                        resultSet.getBoolean("material_edit"),
                        resultSet.getBoolean("post_access"),
                        resultSet.getBoolean("post_edit"),
                        resultSet.getBoolean("shipment_access"),
                        resultSet.getBoolean("shipment_edit"),
                        resultSet.getBoolean("shipper_access"),
                        resultSet.getBoolean("shipper_edit"),
                        resultSet.getBoolean("storage_map_access"),
                        resultSet.getBoolean("storage_map_edit"),
                        resultSet.getBoolean("storage_access"),
                        resultSet.getBoolean("storage_edit"),
                        resultSet.getBoolean("type_access"),
                        resultSet.getBoolean("type_edit")
                ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return postList;
    }

    @Override
    public int save(Post post) {
        Integer id = null;

        try (
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO post (name, employee_access, employee_edit, material_access," +
                                "material_edit, post_access, post_edit, shipment_access," +
                                "shipment_edit, shipper_access, shipper_edit, storage_map_access," +
                                "storage_map_edit, storage_access, storage_edit, type_access, type_edit)" +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, post.getName());
            statement.setBoolean(2, post.isEmployeeAccess());
            statement.setBoolean(3, post.isEmployeeEdit());
            statement.setBoolean(4, post.isMaterialAccess());
            statement.setBoolean(5, post.isMaterialEdit());
            statement.setBoolean(6, post.isPostAccess());
            statement.setBoolean(7, post.isPostEdit());
            statement.setBoolean(8, post.isShipmentAccess());
            statement.setBoolean(9, post.isShipmentEdit());
            statement.setBoolean(10, post.isShipperAccess());
            statement.setBoolean(11, post.isShipperEdit());
            statement.setBoolean(12, post.isStorageMapAccess());
            statement.setBoolean(13, post.isStorageMapEdit());
            statement.setBoolean(14, post.isStorageAccess());
            statement.setBoolean(15, post.isStorageEdit());
            statement.setBoolean(16, post.isTypeAccess());
            statement.setBoolean(17, post.isTypeEdit());

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
    public void update(Post post) {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE post" +
                                " SET name = ?" +
                                ", employee_access = ?" +
                                ", employee_edit = ?" +
                                ", material_access = ?" +
                                ", material_edit = ?" +
                                ", post_access = ?" +
                                ", post_edit = ?" +
                                ", shipment_access = ?" +
                                ", shipment_edit = ?" +
                                ", shipper_access = ?" +
                                ", shipper_edit = ?" +
                                ", storage_map_access = ?" +
                                ", storage_map_edit = ?" +
                                ", storage_access = ?" +
                                ", storage_edit = ?" +
                                ", type_access = ?" +
                                ", type_edit = ?" +
                                " WHERE post_id = ?"
                )
        ) {
            statement.setString(1, post.getName());
            statement.setBoolean(2, post.isEmployeeAccess());
            statement.setBoolean(3, post.isEmployeeEdit());
            statement.setBoolean(4, post.isMaterialAccess());
            statement.setBoolean(5, post.isMaterialEdit());
            statement.setBoolean(6, post.isPostAccess());
            statement.setBoolean(7, post.isPostEdit());
            statement.setBoolean(8, post.isShipmentAccess());
            statement.setBoolean(9, post.isShipmentEdit());
            statement.setBoolean(10, post.isShipperAccess());
            statement.setBoolean(11, post.isShipperEdit());
            statement.setBoolean(12, post.isStorageMapAccess());
            statement.setBoolean(13, post.isStorageMapEdit());
            statement.setBoolean(14, post.isStorageAccess());
            statement.setBoolean(15, post.isStorageEdit());
            statement.setBoolean(16, post.isTypeAccess());
            statement.setBoolean(17, post.isTypeEdit());
            statement.setInt(18, post.getPostId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Post post) {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "DELETE FROM post WHERE post_id = ?"
                );
        ) {
            statement.setInt(1, post.getPostId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
