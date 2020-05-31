package service.impl;

import dao.*;

import domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.*;

import java.util.List;

@Service("commentsService")
public class CommentsServiceImpl implements CommentsService {
    @Autowired
    private CommentsDao commentsDao;
    @Override
    public int queryAllCommentsByUserIdNum(int userId,String search) {
        return commentsDao.getAllCommentsByUserIdNum(userId,search);
    }

    @Override
    public List<Comments> queryAllCommentsByUserId(PageBean pb,int userId,String search) {
        return commentsDao.getAllCommentsByUserId((pb.getCurrentPage()-1)*10,pb.getPageSize(),userId,search);
    }

    @Override
    public Comments queryCommentsByCommentsId(int commentsId) {
        return commentsDao.getCommentByCommentsId(commentsId);
    }

    @Override
    public int queryAllCommentsByEssayIdNum(int essayId) {
        return commentsDao.getAllCommentsByEssayIdNum(essayId);
    }

    @Override
    public List<Comments> queryAllCommentsByEssayId(PageBean pb, int essayId) {
        return commentsDao.getAllCommentsByEssayId((pb.getCurrentPage()-1)*10,pb.getPageSize(),essayId);
    }

    @Override
    public boolean deleteComments(int commentId) {
        try{
            commentsDao.deleteComments(commentId);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public int queryAllCommentsNum(String search) {
        return commentsDao.getAllCommentsNum(search);
    }

    @Override
    public List<Comments> queryAllComments(PageBean pb,String search) {
        return commentsDao.getAllComments((pb.getCurrentPage()-1)*10,pb.getPageSize(),search);
    }

    @Override
    public boolean addComments(Comments comments) {
        try{
            commentsDao.addComments(comments);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateCommentsFlag(int flag, String checkmsg, int commentId) {
        try{
            commentsDao.updateCommentsFlag(flag,checkmsg,commentId);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public int queryAllCommentsCheckNum() {
        return commentsDao.getAllCommentsCheckNum();
    }

    @Override
    public List<Comments> queryAllCommentsCheck(PageBean pb) {
        return commentsDao.getAllCommentsCheck((pb.getCurrentPage()-1)*10,pb.getPageSize());
    }
}
