package domain;

import java.util.List;

public class ResultInfo<E> {
    private boolean flag=false;//在公用的方法中用于表示获取结果的成败,在需要验证用户登录的方法中表示用户登录的成败
    private boolean islogin=true;

    @Override
    public String toString() {
        return "ResultInfo{" +
                "flag=" + flag +
                ", islogin=" + islogin +
                ", info='" + info + '\'' +
                ", res=" + res +
                '}';
    }

    public boolean isIslogin() {
        return islogin;
    }

    public void setIslogin(boolean islogin) {
        this.islogin = islogin;
    }

    private String info="";
    private E res;


    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public E getRes() {
        return res;
    }

    public void setRes(E res) {
        this.res = res;
    }
}
