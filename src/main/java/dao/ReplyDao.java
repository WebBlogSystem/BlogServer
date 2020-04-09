package dao;

import domain.Reply;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyDao {
    public int getAllReplyByUserIdNum(@Param("userid") int userid,@Param("search") String search);
    public List<Reply> getAllReplyByUserId(@Param("start") int start,@Param("size") int size,@Param("userid") int userid,@Param("search") String search);
    public int getAllReplyByCommentIdNum(@Param("commentsid") int commentId);
    public List<Reply> getAllReplyByCommentId(@Param("commentsid") int commentid);
    public void deleteReply(@Param("replyid") int replyid);
    public void addReply(@Param("reply") Reply reply);
    public Reply getReplyByReplyId(@Param("id") int id);
}
