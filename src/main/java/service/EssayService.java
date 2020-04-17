package service;

import domain.*;

import java.util.List;




public interface EssayService {
    public List<Essay> queryAllEssays(PageBean pb,String search,int flag);
    public List<Essay> queryAllEssaysByUserId(PageBean pb,int cateId,int id,String search,int[] flag,int selectWay);
    public int queryAllEssaysNum(String search,int flag);
    public int queryAllEssaysByUserIdNum(int cateId,int userId,String search,int[] flag,int selectWay);
    public int queryAllEssaysByCateIdNum(int cateId, int userId,int flag);
    public List<Essay> queryAllEssaysByCateId(PageBean pb,int cateId,int userId,int flag);
    public Essay queryEssayByEssayId(int essayId);
    public User queryUserByEssayId(int essayId);
    public boolean deleteEssay(int essayId);
    public boolean updateAllEssaysCateNumsByCateId(int cateId);
    public int queryAllNotEssaysByCateIdNum(int cateId);
    public List<Essay> queryAllNotEssaysByCateId(PageBean pb,int cateId);
    public int queryAllEssaysByCateNumsNum(int cateNums);
    public List<Essay> queryAllEssaysByCateNums(PageBean pb,int cateNums);
    public boolean updateEssayFlag(int flag,int essayId);
    public boolean updateEssayFlag(int flag,String msg,int essayId);
    public int queryAllEssaysUnCheckNum(String msg);
    public List<Essay> queryAllEssaysUnCheck(PageBean pb,String msg);
    public boolean addEssay(Essay essay);
    public void updateEssayCateNums(int essayId,int catenums);
    public List<Essay> queryAllWholeEssaysByCateId(int cateId,int userId,int flag);
    public boolean updateEssay(int essayId,String title,String msg,String htmlmsg);
    public int queryAllEssaysByAdminNums(int flag);
    public List<Essay> queryAllEssaysByAdmin(PageBean pb,int flag);
}
