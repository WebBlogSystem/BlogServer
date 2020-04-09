package domain;

public class CurrentAdmin {
    private int id;
    private String username;

    @Override
    public String toString() {
        return "CurrentAdmin{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }

    public CurrentAdmin(Administrator admin) {
        this.id = admin.getId();
        this.username = admin.getUsername();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
