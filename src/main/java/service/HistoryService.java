package service;

import domain.CommentsHistory;
import domain.EssayHistory;
import domain.PageBean;

import java.util.*;

public interface HistoryService {
    public List<EssayHistory> queryEssayHistorys(PageBean pb,String search);
    public List<CommentsHistory> queryCommentsHistorys(PageBean pb,String search);
    public boolean addEssayHistory(EssayHistory essayHistory);
    public boolean addCommentsHistory(CommentsHistory commentsHistory);
    public int queryEssayHistorysNum(String search);
    public int queryCommentsHistorysNum(String search);
}
