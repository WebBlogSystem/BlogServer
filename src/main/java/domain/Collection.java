package domain;

public class Collection {
    private int id;
    private int fromUserId;
    private int textId;
    private int toUserId;

    @Override
    public String toString() {
        return "Collection{" +
                "id=" + id +
                ", fromUserId=" + fromUserId +
                ", textId=" + textId +
                ", toUserId=" + toUserId +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public int getTextId() {
        return textId;
    }

    public void setTextId(int textId) {
        this.textId = textId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }
}
