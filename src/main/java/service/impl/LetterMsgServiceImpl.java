package service.impl;

import dao.LetterMsgDao;
import domain.LetterMsg;
import domain.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.LetterMsgService;

import java.util.List;
@Service("letterMsgService")
public class LetterMsgServiceImpl implements LetterMsgService {
    @Autowired
    private LetterMsgDao letterMsgDao;
//
//    @Override
//    public List<Chatroom> queryAllChatroomUserByUserId(PageBean pb, int chatroomId) {
//        return letterMsgDao.getAllChatroomUserByUserId(pb.getCurrentPage()-1)*10,pb.getPageSize(),chatroomId);
//    }

    @Override
    public int queryAllChatroomMsgByChatroomIdNum(int chatroomId,String search) {
        return letterMsgDao.getAllChatroomMsgByChatroomIdNum(chatroomId,search);
    }

    @Override
    public List<LetterMsg> queryAllChatroomMsgByChatroomId(PageBean pb, int chatroomId, String search) {
        return letterMsgDao.getAllChatroomMsgByChatroomId((pb.getCurrentPage()-1)*10,pb.getPageSize(),chatroomId,search);
    }

    @Override
    public boolean addChatroomMsg(LetterMsg chatroomMsg) {
        try{
            letterMsgDao.addChatroomMsg(chatroomMsg);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteChatroomMsg(int chatroomMsg) {
        try{
            letterMsgDao.deleteChatroomMsg(chatroomMsg);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public LetterMsg queryLatestLetterMsgByLetterId(int letterId) {
        try {
            System.out.println("letterid: "+letterId);
            LetterMsg letterMsg= letterMsgDao.getLatestLetterMsgByLetterId(letterId);
            System.out.println("56:"+letterMsg);
            return letterMsg;
        }catch (NullPointerException e){
            return null;
        }
    }

    @Override
    public LetterMsg queryLetterMsgByLetterMsgId(int letterMsgId) {
        return letterMsgDao.getLetterMsgByLetterMsgId(letterMsgId);
    }

    @Override
    public boolean switchLetterMsgFlag(int letterMsgId) {
        try{
            letterMsgDao.swtichLetterMsgFlag(letterMsgId);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public int queryNoReadLetterMsg(int letterId, int userId) {
        return letterMsgDao.getNoReadLetterMsg(letterId,userId);
    }

    @Override
    public int queryNoReadLetterMsgByUserId(int userId) {
        return letterMsgDao.getNoReadLetterMsgByUserId(userId);
    }
}
