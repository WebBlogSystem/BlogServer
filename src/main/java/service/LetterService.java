package service;

import domain.Letter;
import domain.PageBean;

import java.util.List;

public interface LetterService {
    public int queryAllChatroomByUserIdNum(int userId);
    public List<Letter> queryAllChatroomUserByUserId(PageBean pb, int userId);
    public boolean deleteChatroom(int chatroomid);
    public Letter queryLetterByUserId1AndUserId2(Letter letter);
    public boolean addLetter(Letter letter);
    public Letter queryLetterByLetterId(int letterId);
}
