package service.impl;

import dao.*;
import domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.*;
import utils.Md5Util;
import utils.UuidUtil;

import java.util.List;

@Service("userService")
public class UserSericeImpl implements UserService {
    @Autowired
    public UserDao userDao;
    @Override
    public User queryUser(User user) {
        try {
//            user.setPassword(Md5Util.encodeByMd5(user.getPassword()));
            return userDao.queryUser(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean addUser(User user) {
        try {
            if(user.getUsername()==null){
                user.setUsername("");
            }
//            user.setPassword(Md5Util.encodeByMd5(user.getPassword()));
            System.out.println(user);
            userDao.insertUser(user);
            return true;
        }catch (Exception e){
            System.out.println("插入异常");
            return false;
        }
    }

    @Override
    public void activeUser(String code) {
        userDao.active(code);
    }

    @Override
    public User queryUserByUserId(int id) {
        User user=userDao.queryUserByUserId(id);
        return user;
    }

    @Override
    public List<User> queryAllAttentionUserByUserId(int userId) {
        return userDao.getAllAttentionUserByUserId(userId);
    }

    @Override
    public int queryAllAttentionUserByUserIdNum(int userId) {
        return userDao.getAllAttentionUserByUserIdNum(userId);
    }

    @Override
    public int queryAllFanUserByUserIdNum(int userId) {
        return userDao.getAllFanUserByUserIdNum(userId);
    }

    @Override
    public List<User> queryAllFanUserByUserId(int userId) {
        return userDao.getAllFanUserByUserId(userId);
    }

    @Override
    public boolean updateUser(User user) {
        try{
            userDao.updateUser(user);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }

    }

    @Override
    public int queryUsersNum(String search) {
        return userDao.getUsersNum(search);
    }

    @Override
    public List<User> queryUsers(PageBean pb,String search) {
        return userDao.getUsers((pb.getCurrentPage()-1)*10,pb.getPageSize(),search);
    }

    @Override
    public boolean deleteUser(int userId) {
        try{
            userDao.deleteUser(userId);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean updatePassword(int userid, String password) {
        try{
//            password=Md5Util.encodeByMd5(password);
            userDao.updatePassword(userid,password);
            return true;
        }catch (Exception e){
            return  false;
        }
    }


}
