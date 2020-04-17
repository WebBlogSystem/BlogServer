package domain;

public class LoginCheck {
    private boolean isLogin;
    private String info;

    public LoginCheck(boolean isLogin, String info) {
        this.isLogin = isLogin;
        this.info = info;
    }

    @Override
    public String toString() {
        return "LoginCheck{" +
                "isLogin=" + isLogin +
                ", info='" + info + '\'' +
                '}';
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
