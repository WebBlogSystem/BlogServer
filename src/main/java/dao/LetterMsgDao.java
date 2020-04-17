package dao;

import domain.Letter;
import domain.LetterMsg;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LetterMsgDao {
    public int getAllChatroomMsgByChatroomIdNum(@Param("chatroomid") int chatroomid,@Param("search") String search);
    public List<LetterMsg> getAllChatroomMsgByChatroomId(@Param("start") int start, @Param("size") int size, @Param("letterid") int letterid, @Param("search") String search);
    public void addChatroomMsg(@Param("lettermsg") LetterMsg chatroomMsg);
    public void deleteChatroomMsg(@Param("chatroommsgid") int chatroommsgid);
    public LetterMsg getLatestLetterMsgByLetterId(@Param("letterid") int letterid);
    public LetterMsg getLetterMsgByLetterMsgId(@Param("lettermsgid") int letterMsgId);
    public void swtichLetterMsgFlag(@Param("lettermsgid") int lettermsgid);
    public int getNoReadLetterMsg(@Param("letterid") int letterid,@Param("userid") int userid);
    public int getNoReadLetterMsgByUserId(@Param("userid") int userid);
}
