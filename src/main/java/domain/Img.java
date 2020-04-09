package domain;

public class Img {
    private int id;
    private String location;
    @Override
    public String toString() {
        return "Img{" +
                "id=" + id +
                ", location='" + location + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


}
