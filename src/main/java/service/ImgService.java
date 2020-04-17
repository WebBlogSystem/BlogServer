package service;

import domain.Img;

public interface ImgService {
    public String queryImgByImgId(int imgId);
    public int addImg(Img img);
    public int queryImgByLocation(String location);
}
