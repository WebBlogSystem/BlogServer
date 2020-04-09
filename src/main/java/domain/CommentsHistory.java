package domain;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.*;
public class CommentsHistory {
    private int id;
    private int commentid;
    private int flag;
    private String checkmsg;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createtime;

    @Override
    public String toString() {
        return "CommentsHistory{" +
                "id=" + id +
                ", commentid=" + commentid +
                ", flag=" + flag +
                ", checkmsg='" + checkmsg + '\'' +
                ", createtime=" + createtime +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCommentid() {
        return commentid;
    }

    public void setCommentid(int commentid) {
        this.commentid = commentid;
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
}
