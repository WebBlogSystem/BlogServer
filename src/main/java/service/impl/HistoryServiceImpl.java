package service.impl;

import domain.CommentsHistory;
import dao.CommentsHistoryDao;
import domain.EssayHistory;
import dao.EssayHistoryDao;
import domain.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.HistoryService;

import java.util.List;
@Service("historyServiceImpl")
public class HistoryServiceImpl implements HistoryService {
    @Autowired
    private EssayHistoryDao essayHistoryDao;
    @Autowired
    private CommentsHistoryDao commentsHistoryDao;
    @Override
    public List<EssayHistory> queryEssayHistorys(PageBean pb,String search) {
        return essayHistoryDao.getEssayHistorys((pb.getCurrentPage()-1)*10,pb.getPageSize(),search);
    }

    @Override
    public List<CommentsHistory> queryCommentsHistorys(PageBean pb,String search) {
        return commentsHistoryDao.getCommentsHistorys((pb.getCurrentPage()-1)*10,pb.getPageSize(),search);
    }

    @Override
    public boolean addEssayHistory(EssayHistory essayHistory) {
        try{
            essayHistoryDao.addEssayHistory(essayHistory);
            return true;
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean addCommentsHistory(CommentsHistory commentsHistory) {
        try{
            commentsHistoryDao.addCommentsHistory(commentsHistory);
            return true;
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public int queryEssayHistorysNum(String search) {
        return essayHistoryDao.getEssayHistorysNum(search);
    }

    @Override
    public int queryCommentsHistorysNum(String search) {
        return commentsHistoryDao.getCommentsHistorysNum(search);
    }
}
