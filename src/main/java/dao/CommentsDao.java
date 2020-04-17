package dao;

import domain.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentsDao {
    public int getAllCommentsByUserIdNum(@Param("userid") int userid,@Param("search") String search);
    public List<Comments> getAllCommentsByUserId(@Param("start") int start,@Param("size") int size,@Param("userid") int userid,@Param("search") String search);
    public Comments getCommentByCommentsId(@Param("commentsid") int commentsid);
    public int getAllCommentsByEssayIdNum(@Param("essayid") int essayid);
    public List<Comments> getAllCommentsByEssayId(@Param("start") int start,@Param("size") int size,@Param("essayid") int essayid);
    public void deleteComments(@Param("commentid") int commentid);
    public int getAllCommentsNum(@Param("search") String search);
    public List<Comments> getAllComments(@Param("start") int start,@Param("size") int size,@Param("search") String search);
    public int addComments(@Param("comment") Comments comments);
    public void updateCommentsFlag(@Param("flag") int flag,@Param("checkmsg") String checkmsg,@Param("commentid") int commentId);
    public int getAllCommentsCheckNum(@Param("flag") int flag);
    public List<Comments> getAllCommentsCheck(@Param("start")int start,@Param("size")int size,@Param("flag")int flag);
}
