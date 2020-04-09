package service;

import domain.PageBean;
import domain.Reply;

import java.util.List;

public interface ReplyService {
    public int queryAllReplyByUserIdNum(int userId,String search);
    public List<Reply> queryAllReplyByUserId(PageBean pb,int userId,String search);
    public int queryAllReplyByCommentIdNum(int commentId);
    public List<Reply> queryAllReplyByCommentId(int commentId);
    public boolean deleteReply(int replyId);
    public boolean addReply(Reply reply);
    public Reply queryReplyByReplyId(int id);
}
