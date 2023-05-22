package DAO;

import DTO.Post;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                        resultSet.getString("name")
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
                        resultSet.getString("name")
                ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return postList;
    }

    @Override
    public int save(Post post) {
        return 0;
    }

    @Override
    public void update(Post post) {

    }

    @Override
    public void delete(Post post) {

    }
}
