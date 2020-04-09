package dao;

import domain.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CateDao {
    public int getCatesByUserIdNum(@Param("userid")int userid,@Param("search") String search);
    public List<Cate> getCatesByUserId(@Param("userid") int userid);
    public int getAllCatesByEssayIdNum(@Param("essayid")int essayid);
    public List<Cate> getAllCatesByEssayId(@Param("essayid") int essayid);
    public void deleteCate(@Param("cateid") int cateid);
    public void updateCate(@Param("name") String cate_name,@Param("cateid") int cateid);
    public int getAllNotCatesByEssayIdAndUserIdNum(@Param("essayid") int essayid,@Param("userid") int userid);
    public List<Cate> getAllNotCatesByEssayIdAndUserId(@Param("start") int start,@Param("size") int size,@Param("essayid") int essayid,@Param("userid") int userid);
    public void addCate(@Param("cate") Cate cate);
    public Cate getCateByCateName(@Param("userid") int userid,@Param("catename") String catename);
}
