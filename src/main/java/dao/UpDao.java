package dao;

import domain.Up;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UpDao {
    public int getUps(@Param("id") int essayId, @Param("flag") int flag);
    public Up getUpByUserIdAndEssayId(@Param("userid") int userid,@Param("essayid") int essayid);
    public void addUp(@Param("userid") int userId, @Param("essayid") int essayId,@Param("flag") int flag);
    public void updateUp(@Param("id")int id,@Param("flag") int flag);
}
