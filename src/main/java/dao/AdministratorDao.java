package dao;

import domain.Administrator;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministratorDao {
    public Administrator getAdmin(@Param("admin") Administrator administrator);
    public void updatePassword(@Param("adminid") int adminid,@Param("newpwd") String newpwd);
    public Administrator getAdminById(@Param("adminid") int adminid);
}
