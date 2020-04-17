package dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import domain.*;

@Repository
public interface PointDao {
    public void updateUser(@Param("user") User user);
}
