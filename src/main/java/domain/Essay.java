package domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Essay {
    private int id;
    private int userId;
    private String title;
    private String msg;
    private String htmlmsg;
    private int catenums;
    private int flag;
    private String checkmsg;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createtime;

    @Override
    public String toString() {
        return "Essay{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", msg='" + msg + '\'' +
                ", htmlmsg='" + htmlmsg + '\'' +
                ", catenums=" + catenums +
                ", flag=" + flag +
                ", checkmsg='" + checkmsg + '\'' +
                ", createtime=" + createtime +
                '}';
    }

    public String getHtmlmsg() {
        return htmlmsg;
    }

    public void setHtmlmsg(String htmlmsg) {
        this.htmlmsg = htmlmsg;
    }

    public int getCatenums() {
        return catenums;
    }

    public void setCatenums(int catenums) {
        this.catenums = catenums;
    }

    public String getCheckmsg() {
        return checkmsg;
    }

    public void setCheckmsg(String checkmsg) {
        this.checkmsg = checkmsg;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
