package service.impl;


import dao.EssayCateDao;
import domain.EssayCate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.EssayCateService;
@Service("essayCateService")
public class EssayCateServiceImpl implements EssayCateService {
    @Autowired
    private EssayCateDao essayCateDao;
    @Override
    public boolean addEssayCate(int essayId, int cateId) {
        try{
            essayCateDao.addEssayCate(essayId,cateId);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public EssayCate queryEssayCate(int essayId, int cateId) {
        return essayCateDao.getEssayCate(essayId,cateId);
    }

    @Override
    public boolean deleteEssayCate(int essayId, int cateId) {
        try{
            essayCateDao.deleteEssayCate(essayId,cateId);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    // http://192.168.2.104:8080/user/login
}
