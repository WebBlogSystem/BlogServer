package domain;

public class Cate {
    private int id;
    private int userId;
    private String name;

    @Override
    public String toString() {
        return "Cate{" +
                "id=" + id +
                ", userId=" + userId+
                ", name='" + name + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
