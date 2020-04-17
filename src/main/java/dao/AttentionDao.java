package dao;

import domain.Attention;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AttentionDao {
    public void deleteAttention(@Param("attention") Attention attention);
    public void addAttention(@Param("attention") Attention attention);
    public Attention getAttention(@Param("attention") Attention attention);
}
