package service;

import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
import domain.*;

import java.util.List;

public interface UserService {
    public User queryUser(User user);
    public boolean addUser(User user);
    public void activeUser(String code);
    public User queryUserByUserId(int id);
    public List<User> queryAllAttentionUserByUserId(int userId);
    public int queryAllAttentionUserByUserIdNum(int userId);
    public int queryAllFanUserByUserIdNum(int userId);
    public List<User> queryAllFanUserByUserId(int userId);

    public boolean updateUser(User user);

    public int queryUsersNum(String search);
    public List<User> queryUsers(PageBean pb,String search);
    public boolean deleteUser(int userId);
    public boolean updatePassword(int userid,String password);
}
