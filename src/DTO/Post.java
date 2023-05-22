package DTO;

public class Post {
    private Integer postId;
    private String name;

    public Post(Integer postId, String name) {
        this.postId = postId;
        this.name = name;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString()  {
        return getName();
    }
}
