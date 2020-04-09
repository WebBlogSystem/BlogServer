package service.impl;

import dao.*;
import domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.*;

import java.util.List;
@Service("cateService")
public class CateServiceImpl implements CateService {
    @Autowired
    private CateDao cateDao;

    @Override
    public List<Cate> queryAllCatesByUserId(int userid) {
        return cateDao.getCatesByUserId(userid);
    }
    @Override
    public int queryAllCatesByUserIdNum(int userid,String search) {
        return cateDao.getCatesByUserIdNum(userid,search);
    }

    @Override
    public int queryAllCatesByEssayIdNum(int essayId) {
        return cateDao.getAllCatesByEssayIdNum(essayId);
    }

    @Override
    public List<Cate> queryAllCatesByEssayId(int essayId) {
        return cateDao.getAllCatesByEssayId(essayId);
    }

    @Override
    public boolean deleteCate(int cateId) {
        try {
            cateDao.deleteCate(cateId);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateCate(String cate_name,int cateId) {
        try{
            cateDao.updateCate(cate_name,cateId);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public int queryAllNotCatesByEssayIdAndUserIdNum(int essayid, int userid) {
        return cateDao.getAllNotCatesByEssayIdAndUserIdNum(essayid,userid);
    }

    @Override
    public List<Cate> queryAllNotCatesByEssayIdAndUserId(PageBean pb, int essayid, int userid) {
        return cateDao.getAllNotCatesByEssayIdAndUserId((pb.getCurrentPage()-1)*10,pb.getPageSize(),essayid,userid);
    }

    @Override
    public boolean addCate(Cate cate) {
        try{
            cateDao.addCate(cate);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Cate queryCateByCateName(int userId,String catename) {
        return cateDao.getCateByCateName(userId,catename);
    }


}
