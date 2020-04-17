package service.impl;

import dao.PointDao;
import domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.PointService;
@Service("pointService")
public class PointServiceImpl implements PointService {
    @Autowired
    private PointDao pointDao;

    @Override
    public boolean updateUser(User user) {
        try {
            pointDao.updateUser(user);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
}
