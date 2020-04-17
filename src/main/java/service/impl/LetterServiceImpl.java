package service.impl;

import dao.LetterDao;
import domain.Letter;
import domain.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.LetterService;

import java.util.List;

@Service("letterService")
public class LetterServiceImpl implements LetterService {
    @Autowired
    private LetterDao letterDao;
    @Override
    public int queryAllChatroomByUserIdNum(int userId) {
        return letterDao.getAllChatroomByUserIdNum(userId);
    }

    @Override
    public List<Letter> queryAllChatroomUserByUserId(PageBean pb, int userId) {
        System.out.println((pb.getCurrentPage()-1)*10+","+pb.getPageSize()+","+userId);
        return letterDao.getAllChatroomByUserId((pb.getCurrentPage()-1)*10,pb.getPageSize(),userId);
    }

    @Override
    public boolean deleteChatroom(int chatroom) {
        try{
            letterDao.deleteChatroom(chatroom);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Letter queryLetterByUserId1AndUserId2(Letter letter) {
        return letterDao.getLetterByUserId1AndUserId2(letter);
    }

    @Override
    public boolean addLetter(Letter letter) {
        try{
            letterDao.addLetter(letter);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Letter queryLetterByLetterId(int letterId) {
        return letterDao.getLetterByLetterId(letterId);
    }
}
