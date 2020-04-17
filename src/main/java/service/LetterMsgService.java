package service;

import domain.Letter;
import domain.LetterMsg;
import domain.PageBean;

import java.util.List;

public interface LetterMsgService {
    public int queryAllChatroomMsgByChatroomIdNum(int chatroomId,String search);
    public List<LetterMsg> queryAllChatroomMsgByChatroomId(PageBean pb, int chatroomId, String search);
    public boolean addChatroomMsg(LetterMsg chatroomMsg);
    public boolean deleteChatroomMsg(int chatroomMsg);
    public LetterMsg queryLatestLetterMsgByLetterId(int letterId);
    public LetterMsg queryLetterMsgByLetterMsgId(int letterMsgId);
    public boolean switchLetterMsgFlag(int letterMsgId);
    public int queryNoReadLetterMsg(int letterId,int userId);
    public int queryNoReadLetterMsgByUserId(int userId);
}
