package domain;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.*;
public class CommentsHistory {
    private int id;
    private int flag;
    private String checkmsg;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createtime;
    private int userId;
    private String msg;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date commentcreatetime;


    public void setMy(Comments comments){
        this.flag=comments.getFlag();
        this.checkmsg=comments.getCheckmsg();
        this.userId=comments.getUserId();
        this.msg=comments.getMsg();
        this.commentcreatetime=comments.getCreatetime();
    }

    @Override
    public String toString() {
        return "CommentsHistory{" +
                "id=" + id +
                ", flag=" + flag +
                ", checkmsg='" + checkmsg + '\'' +
                ", createtime=" + createtime +
                ", userId=" + userId +
                ", msg='" + msg + '\'' +
                ", commentcreatetime=" + commentcreatetime +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getCheckmsg() {
        return checkmsg;
    }

    public void setCheckmsg(String checkmsg) {
        this.checkmsg = checkmsg;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getCommentcreatetime() {
        return commentcreatetime;
    }

    public void setCommentcreatetime(Date commentcreatetime) {
        this.commentcreatetime = commentcreatetime;
    }
}