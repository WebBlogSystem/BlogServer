package dao;

import domain.Letter;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LetterDao {
    public int getAllChatroomByUserIdNum(@Param("userid") int userid);
    public List<Letter> getAllChatroomByUserId(@Param("start") int start, @Param("size") int size, @Param("userid") int userid);
    public void deleteChatroom(@Param("letterid") int chatroom);
    public Letter getLetterByUserId1AndUserId2(@Param("letter") Letter letter);
    public void addLetter(@Param("letter") Letter letter);
    public Letter getLetterByLetterId(@Param("letterid") int letterid);
}
