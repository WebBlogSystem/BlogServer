package service.impl;

import dao.AttentionDao;
import domain.Attention;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.AttentionService;
@Service("attentionService")
public class AttentionServiceImpl implements AttentionService {
    @Autowired
    private AttentionDao attentionDao;
    @Override
    public boolean deleteAttention(Attention attention) {
        try {
            attentionDao.deleteAttention(attention);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean addAttention(Attention attention) {
        try{
            attentionDao.addAttention(attention);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Attention getAttention(Attention attention) {
        return attentionDao.getAttention(attention);
    }
}
