package dao;

import domain.CommentsHistory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentsHistoryDao {
    public void addCommentsHistory(@Param("commentsHistory") CommentsHistory commentsHistory);
    public List<CommentsHistory> getCommentsHistorys(@Param("start") int start,@Param("size")int size,@Param("search") String search);
    public int getCommentsHistorysNum(@Param("search") String search);

}
