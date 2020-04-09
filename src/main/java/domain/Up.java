package domain;

public class Up {
    private int id;
    private int essayId;
    private int userId;
    private int flag;

    @Override
    public String toString() {
        return "Up{" +
                "id=" + id +
                ", essayId=" + essayId +
                ", userId=" + userId +
                ", flag=" + flag +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEssayId() {
        return essayId;
    }

    public void setEssayId(int essayId) {
        this.essayId = essayId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
