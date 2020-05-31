package service;

import domain.*;

import java.util.List;

public interface CommentsService {
    public int queryAllCommentsByUserIdNum(int userId,String search);
    public List<Comments> queryAllCommentsByUserId(PageBean pb,int userId,String search);
    public Comments queryCommentsByCommentsId(int commentsId);
    public int queryAllCommentsByEssayIdNum(int essayId);
    public List<Comments> queryAllCommentsByEssayId(PageBean pb,int essayId);
    public boolean deleteComments(int commentId);
    public int queryAllCommentsNum(String search);
    public List<Comments> queryAllComments(PageBean pb,String search);
    public boolean addComments(Comments comments);
    public boolean updateCommentsFlag(int flag,String checkmsg,int commentId);
    public int queryAllCommentsCheckNum();
    public List<Comments> queryAllCommentsCheck(PageBean pb);
}
