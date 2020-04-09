package domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Reply {
    private int id;
    private int commentsId;
    private int fromUserId;
    private int toUserId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createtime;
    private String msg;
    private int flag;
    @Override
    public String toString() {
        return "Reply{" +
                "id=" + id +
                ", commentId=" + commentsId +
                ", fromUserId=" + fromUserId +
                ", toUserId=" + toUserId +
                ", createtime=" + createtime +
                ", msg='" + msg + '\'' +
                ", flag=" + flag +
                '}';
    }
    public Date getCreatetime() {
        return createtime;
    }
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCommentsId() {
        return commentsId;
    }

    public void setCommentsId(int commentsId) {
        this.commentsId = commentsId;
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

    public Date getTime() {
        return createtime;
    }

    public void setTime(Date createtime) {
        this.createtime = createtime;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
