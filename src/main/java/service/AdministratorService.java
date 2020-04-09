package service;

import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
import domain.Administrator;
import domain.Comments;
import domain.PageBean;

import java.util.List;

public interface AdministratorService {
    public Administrator queryAdmin(Administrator administrator);
    public boolean updatePassword(int adminid,String newpwd);
    public Administrator queryAdminById(int adminid);

}
