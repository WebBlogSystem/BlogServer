package service.impl;

import domain.Img;
import dao.ImgDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.ImgService;
@Service("imgService")
public class ImgServiceImpl implements ImgService {
    @Autowired
    private ImgDao imgDao;
    @Override
    public String queryImgByImgId(int imgId) {
        return imgDao.getImgByImgId(imgId);
    }

    @Override
    public int addImg(Img img) {
        return imgDao.addImg(img);
    }

    @Override
    public int queryImgByLocation(String location) {
        return imgDao.getImgByLocation(location);
    }
}
