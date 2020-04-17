package domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class LetterMsg {
    private int id;
    private int letterId;
    private int fromUserId;
    private int toUserId;
    private String msg;
    private int flag;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createtime;

    @Override
    public String toString() {
        return "LetterMsg{" +
                "id=" + id +
                ", letterId=" + letterId +
                ", fromUserId=" + fromUserId +
                ", toUserId=" + toUserId +
                ", msg='" + msg + '\'' +
                ", flag=" + flag +
                ", createtime=" + createtime +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLetterId() {
        return letterId;
    }

    public void setLetterId(int letterId) {
        this.letterId = letterId;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}
