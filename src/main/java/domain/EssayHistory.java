package domain;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.*;
public class EssayHistory {
    private int id;
    private int flag;
    private String checkmsg;
    private int userId;
    private String title;
    private String msg;
    private String htmlmsg;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date essaycreatetime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createtime;

    public void setMy(Essay essay){
        this.flag=essay.getFlag();
        this.checkmsg=essay.getCheckmsg();
        this.userId=essay.getUserId();
        this.title=essay.getTitle();
        this.msg=essay.getMsg();
        this.htmlmsg=essay.getHtmlmsg();
        this.essaycreatetime=essay.getCreatetime();
    }

    @Override
    public String toString() {
        return "EssayHistory{" +
                "id=" + id +
                ", flag=" + flag +
                ", checkmsg='" + checkmsg + '\'' +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", msg='" + msg + '\'' +
                ", htmlmsg='" + htmlmsg + '\'' +
                ", essaycreatetime=" + essaycreatetime +
                ", createtime=" + createtime +
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

    public String getHtmlmsg() {
        return htmlmsg;
    }

    public void setHtmlmsg(String htmlmsg) {
        this.htmlmsg = htmlmsg;
    }

    public Date getEssaycreatetime() {
        return essaycreatetime;
    }

    public void setEssaycreatetime(Date essaycreatetime) {
        this.essaycreatetime = essaycreatetime;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}
