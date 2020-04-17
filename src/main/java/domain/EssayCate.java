package domain;

public class EssayCate {
    private int id;
    private int essayId;
    private int cateId;

    @Override
    public String toString() {
        return "TextCate{" +
                "id=" + id +
                ", essayId=" + essayId +
                ", cateId=" + cateId +
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

    public int getCateId() {
        return cateId;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }
}
