package dao;

import domain.EssayHistory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EssayHistoryDao {
    public List<EssayHistory> getEssayHistorys(@Param("start") int start,@Param("size")int size,@Param("search")String search);

    public void addEssayHistory(@Param("essayhistory") EssayHistory essayHistory);

    public int getEssayHistorysNum(@Param("search") String search);
}
