package dao;

import domain.EssayCate;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EssayCateDao {
    public void addEssayCate(@Param("essayid") int essayid,@Param("cateid") int cateid);
    public EssayCate getEssayCate(@Param("essayid") int essayid,@Param("cateid") int cateid);
    public void deleteEssayCate(@Param("essayid") int essayid,@Param("cateid") int cateid);
}
