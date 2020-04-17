package dao;

import domain.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import javax.websocket.server.PathParam;
import java.util.List;

@Repository
public interface EssayDao {
    public List<Essay> getAllEssays(@Param("start")int start,@Param("size") int size,@Param("search") String search,@Param("list") int[] flags);
    public List<Essay> getAllEssaysByUserId(@Param("start")int start,@Param("size") int size,@Param("cateid") int cateid,@Param("userid") int id,@Param("search") String search,@Param("list") int[] flags,@Param("selectway")int selectway);
    public int getAllEssaysNum(@Param("search") String search, @Param("list") int[] flags);
    public int getAllEssaysByUserIdNum(@Param("cateid") int cateid,@Param("userid") int userid,@Param("search") String search,@Param("list") int[] flags,@Param("selectway") int selectway);
    public int getAllEssaysByCateIdNum(@Param("cateid") int cateid,@Param("userid") int userId,@Param("flag") int flag);
    public List<Essay> getAllEssaysByCateId(@Param("start")int start,@Param("size") int size,@Param("cateid") int cateid,@Param("userid") int userId,@Param("flag") int flag);
    public Essay getEssayByEssayId(@Param("essayid") int essayid);
    public User getUserByEssayId(@Param("essayid") int essayid);
    public void deleteEssay(@Param("essayid") int essayid);
    public void updateAllEssaysCateNumsByCateId(@Param("cateid") int cateid);
    public int getAllNotEssaysByCateIdNum(@Param("cateid") int cateid);
    public List<Essay> getAllNotEssaysByCateId(@Param("start")int start,@Param("size") int size,@Param("cateid") int cateid);
    public int getAllEssaysByCateNumsNum(@Param("catenums")int cateNums);
    public List<Essay> getAllEssaysByCateNums(@Param("start")int start,@Param("size") int size,@Param("catenums") int cateNums);
    public void updateEssayFlag(@Param("flag") int flag,@Param("essayid") int essayid);
    public void updateEssayFlagAndMsg(@Param("flag") int flag,@Param("msg") String msg,@Param("essayid") int essayid);
    public int getAllEssaysUnCheckNum(@Param("search") String msg);
    public List<Essay> getAllEssaysUnCheck(@Param("start") int start,@Param("size") int size,@Param("search") String search);
    public void addEssay(@Param("essay") Essay essay);
    public void updateEssayCateNums(@Param("essayid") int essayid,@Param("catenums") int catenums);
    public List<Essay> getAllWholeEssaysByCateId(@Param("cateid")int cateId,@Param("userid")int userId,@Param("flag")int flag);
    public void updateEssay(@Param("id")int id,@Param("title") String title,@Param("msg") String msg,@Param("htmlmsg") String htmlmsg);
    public int getAllEssaysByAdminNums(@Param("flag")int flag);
    public List<Essay> getAllEssaysByAdmin(@Param("start")int start,@Param("size")int size,@Param("flag")int flag);
}
