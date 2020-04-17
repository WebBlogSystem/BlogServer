package domain;

public class Letter {
    private int id;
    private int userId1;
    private int userId2;
    private int msgnums;

    @Override
    public String toString() {
        return "Letter{" +
                "id=" + id +
                ", userId1=" + userId1 +
                ", userId2=" + userId2 +
                ", msgnums=" + msgnums +
                '}';
    }

    public int getMsgnums() {
        return msgnums;
    }

    public void setMsgnums(int msgnums) {
        this.msgnums = msgnums;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId1() {
        return userId1;
    }

    public void setUserId1(int userId1) {
        this.userId1 = userId1;
    }

    public int getUserId2() {
        return userId2;
    }

    public void setUserId2(int userId2) {
        this.userId2 = userId2;
    }
}
