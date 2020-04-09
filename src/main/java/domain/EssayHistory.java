package domain;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.*;
public class EssayHistory {
    private int id;
    private int essayId;
    private int flag;
    private String checkmsg;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createtime;

    @Override
    public String toString() {
        return "EssayHistory{" +
                "id=" + id +
                ", essayId=" + essayId +
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

    public int getEssayId() {
        return essayId;
    }

    public void setEssayId(int essayId) {
        this.essayId = essayId;
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
