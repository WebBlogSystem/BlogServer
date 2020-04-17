package service.impl;

import dao.*;
import domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("essayService")
public class EssayServiceImpl implements EssayService {
    @Autowired
    public EssayDao essayDao;
    @Override
    public List<Essay> queryAllEssays(PageBean pb,String search,int flag) {
        int[] params;
        // flag为1
        if(flag==0){
            params = new int[]{0};
        }else if(flag==-1){
            params = new int[]{1,-1};
        }else{
            params = new int[]{1};
        }
        System.out.println(params.length);
        return essayDao.getAllEssays((pb.getCurrentPage()-1)*10,pb.getPageSize(),search,params);
    }

    @Override
    public List<Essay> queryAllEssaysByUserId(PageBean pb,int cateId, int id,String search,int[] flag,int selectWay) {
        return essayDao.getAllEssaysByUserId((pb.getCurrentPage()-1)*10,pb.getPageSize(),cateId,id,search,flag,selectWay);
    }

    @Override
    public int queryAllEssaysNum(String search ,int flag) {
        int[] params;
        // flag为1
        if(flag==0){
            params = new int[]{0};
        }else if(flag==-1){
            params = new int[]{1,-1};
        }else{
            params = new int[]{1};
        }
        return essayDao.getAllEssaysNum(search,params);
    }
    @Override
    public int queryAllEssaysByUserIdNum(int cateId,int userid,String search,int[] flag,int selectWay) {
        return essayDao.getAllEssaysByUserIdNum(cateId,userid,search,flag,selectWay);
    }

    @Override
    public int queryAllEssaysByCateIdNum(int cateId,int userId,int flag) {
        return essayDao.getAllEssaysByCateIdNum(cateId,userId,flag);
    }

    @Override
    public List<Essay> queryAllEssaysByCateId(PageBean pb, int cateId,int userId,int flag) {
        return essayDao.getAllEssaysByCateId((pb.getCurrentPage()-1)*10,pb.getPageSize(),cateId,userId,flag);
    }

    @Override
    public Essay queryEssayByEssayId(int essayId) {
        return essayDao.getEssayByEssayId(essayId);
    }

    @Override
    public User queryUserByEssayId(int essayId) {
        return essayDao.getUserByEssayId(essayId);
    }

    @Override
    public boolean deleteEssay(int essayId) {
        try{
            essayDao.deleteEssay(essayId);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateAllEssaysCateNumsByCateId(int cateId) {
        try{
            essayDao.updateAllEssaysCateNumsByCateId(cateId);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public int queryAllNotEssaysByCateIdNum(int cateId) {
        return essayDao.getAllNotEssaysByCateIdNum(cateId);
    }

    @Override
    public List<Essay> queryAllNotEssaysByCateId(PageBean pb,int cateId) {
        return essayDao.getAllNotEssaysByCateId((pb.getCurrentPage()-1)*10,pb.getPageSize(),cateId);
    }

    @Override
    public int queryAllEssaysByCateNumsNum(int cateNums) {
        return essayDao.getAllEssaysByCateNumsNum(cateNums);
    }

    @Override
    public List<Essay> queryAllEssaysByCateNums(PageBean pb, int cateNums) {
        return essayDao.getAllEssaysByCateNums((pb.getCurrentPage()-1)*10,pb.getPageSize(),cateNums);
    }

    @Override
    public boolean updateEssayFlag(int flag, int essayId) {
        try {
            essayDao.updateEssayFlag(flag, essayId);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateEssayFlag(int flag, String msg, int essayId) {
        try {
            essayDao.updateEssayFlagAndMsg(flag,msg, essayId);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }    }

    @Override
    public int queryAllEssaysUnCheckNum(String msg) {
        return essayDao.getAllEssaysUnCheckNum(msg);
    }

    @Override
    public List<Essay> queryAllEssaysUnCheck(PageBean pb, String msg) {
        return essayDao.getAllEssaysUnCheck((pb.getCurrentPage()-1)*10,pb.getPageSize(),msg);
    }

    @Override
    public boolean addEssay(Essay essay) {
        try{
            essayDao.addEssay(essay);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public void updateEssayCateNums(int essayId,int catenums) {
        essayDao.updateEssayCateNums(essayId,catenums);
    }

    @Override
    public List<Essay> queryAllWholeEssaysByCateId(int cateId, int userId, int flag) {
        return essayDao.getAllWholeEssaysByCateId(cateId, userId, flag);
    }

    @Override
    public boolean updateEssay(int essayId, String title, String msg, String htmlmsg) {
        try{
            essayDao.updateEssay(essayId,title,msg,htmlmsg);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public int queryAllEssaysByAdminNums(int flag) {
        return essayDao.getAllEssaysByAdminNums(flag);
    }

    @Override
    public List<Essay> queryAllEssaysByAdmin(PageBean pb, int flag) {
        return essayDao.getAllEssaysByAdmin((pb.getCurrentPage()-1)*10,pb.getPageSize(),flag);
    }


}
