package service.impl;

import dao.ReplyDao;
import domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.*;

import java.util.List;

@Service("replyService")
public class ReplyServiceImpl implements ReplyService {
    @Autowired
    private ReplyDao replyDao;
    @Override
    public int queryAllReplyByUserIdNum(int userId,String search,int flag) {
        return replyDao.getAllReplyByUserIdNum(userId,search,flag);
    }

    @Override
    public List<Reply> queryAllReplyByUserId(PageBean pb, int userId,String search,int flag) {
        return replyDao.getAllReplyByUserId((pb.getCurrentPage()-1)*10,pb.getPageSize(),userId,search,flag);
    }

    @Override
    public int queryAllReplyByCommentIdNum(int commentId) {
        return replyDao.getAllReplyByCommentIdNum(commentId);
    }

    @Override
    public List<Reply> queryAllReplyByCommentId(int commentId) {
        return replyDao.getAllReplyByCommentId(commentId);
    }

    @Override
    public boolean deleteReply(int replyId) {
        try{
            replyDao.deleteReply(replyId);
            return true;
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean addReply(Reply reply) {
        try{
            replyDao.addReply(reply);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Reply queryReplyByReplyId(int id) {
        return replyDao.getReplyByReplyId(id);
    }
}
