package domain;

import service.LetterMsgService;

public class LetterNoRead {
    private int letterId;
    private int nums;
    private LetterMsg latestLetterMsg;
    public LetterMsg getLatestLetterMsg() {
        return latestLetterMsg;
    }

    public LetterNoRead(int letterId, int nums) {
        this.letterId = letterId;
        this.nums = nums;
    }

    public void setLatestLetterMsg(LetterMsg latestLetterMsg) {
        this.latestLetterMsg = latestLetterMsg;
    }

    public LetterNoRead(int letterId, int nums, LetterMsg latestLetterMsg) {
        this.letterId = letterId;
        this.nums = nums;
        this.latestLetterMsg = latestLetterMsg;
    }

    @Override
    public String toString() {
        return "NoRead{" +
                "letterId=" + letterId +
                ", nums=" + nums +
                ", latestLetterMsg=" + latestLetterMsg +
                '}';
    }

    public int getLetterId() {
        return letterId;
    }

    public void setLetterId(int letterId) {
        this.letterId = letterId;
    }

    public int getNums() {
        return nums;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }
}
