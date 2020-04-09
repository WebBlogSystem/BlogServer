package domain;

public class UserNoRead {
    private int userId;
    private int nums;

    @Override
    public String toString() {
        return "UserNoRead{" +
                "userId=" + userId +
                ", nums=" + nums +
                '}';
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getNums() {
        return nums;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }
}
