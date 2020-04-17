package service;

import domain.*;

import java.util.List;

public interface CateService {
    public List<Cate> queryAllCatesByUserId(int userid);
    public int queryAllCatesByUserIdNum(int userid,String search);
    public int queryAllCatesByEssayIdNum(int essayId);
    public List<Cate> queryAllCatesByEssayId(int essayId);
    public boolean deleteCate(int cateId);
    public boolean updateCate(String cate_name,int cateId);
    public int queryAllNotCatesByEssayIdAndUserIdNum(int essayid,int userid);
    public List<Cate> queryAllNotCatesByEssayIdAndUserId(PageBean pb,int essayid,int userid);
    public boolean addCate(Cate cate);
    public Cate queryCateByCateName(int userId,String catename);
    public Cate queryCateByCateId(int cateId);
}
