package service.impl;

import dao.AdministratorDao;
import domain.Administrator;
import domain.Comments;
import domain.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import service.AdministratorService;

import java.util.List;

@Service("administratorService")
public class AdministratorServiceImpl implements AdministratorService {
    @Autowired
    private AdministratorDao administratorDao;
    @Override
    public Administrator queryAdmin(Administrator administrator) {
        return administratorDao.getAdmin(administrator);
    }

    @Override
    public boolean updatePassword(int adminid, String newpwd) {
        try{
            administratorDao.updatePassword(adminid,newpwd);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Administrator queryAdminById(int adminid) {
        return administratorDao.getAdminById(adminid);
    }

}
