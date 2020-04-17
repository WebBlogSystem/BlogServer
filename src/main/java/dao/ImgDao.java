package dao;

import domain.Img;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ImgDao {
    public String getImgByImgId(@Param("imgid") int imgid);
    public int addImg(@Param("img") Img img);
    public int getImgByLocation(@Param("location") String location);
}
