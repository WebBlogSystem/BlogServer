package service.impl;

import dao.UpDao;
import domain.Up;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.UpService;

@Service("upService")
public class UpServiceImpl implements UpService {

    @Autowired
    private UpDao upDao;
    @Override
    public int getUps(int essayId, int flag) {
        return upDao.getUps(essayId,flag);
    }

    @Override
    public Up queryUpByUserIdAndEssayId(int userId, int essayId) {
        return upDao.getUpByUserIdAndEssayId(userId,essayId);
    }

    @Override
    public void addUp(int userId, int essayId, int flag) {
        upDao.addUp(userId,essayId,flag);
    }

    @Override
    public void updateUp(int id, int flag) {
        upDao.updateUp(id,flag);
    }
}
