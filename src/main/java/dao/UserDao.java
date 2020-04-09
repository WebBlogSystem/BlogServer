package dao;

import domain.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {
    public User queryUser(@Param("user") User user);
    public void insertUser(@Param("user") User user);
    public void active(@Param("code") String code);
    public User queryUserByUserId(@Param("id") int id);
    public int getAllAttentionUserByUserIdNum(@Param("userid") int userid);
    public List<User> getAllAttentionUserByUserId(@Param("userid") int userid);
    public int getAllFanUserByUserIdNum(@Param("userid") int userid);
    public List<User> getAllFanUserByUserId(@Param("userid") int userid);
    public void updateUser(@Param("user") User user);
    public int getUsersNum(@Param("search") String search);
    public List<User> getUsers(@Param("start") int start,@Param("size") int size,@Param("search")String search);
    public void deleteUser(@Param("userid") int userid);
    public void updatePassword(@Param("userid") int userid,@Param("password") String password);
}
