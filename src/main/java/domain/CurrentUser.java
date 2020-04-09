package domain;

public class CurrentUser {
    private int id;
    private int imgid;
    private String sex;
    private int point;
    private int sumpoint;

    @Override
    public String toString() {
        return "CurrentUser{" +
                "id=" + id +
                ", imgid=" + imgid +
                ", sex=" + sex +
                ", point=" + point +
                ", sumpoint=" + sumpoint +
                ", username='" + username + '\'' +
                ", mail='" + mail + '\'' +
                ", intro='" + intro + '\'' +
                '}';
    }

    public int getSumpoint() {
        return sumpoint;
    }

    public void setSumpoint(int sumpoint) {
        this.sumpoint = sumpoint;
    }

    private String username;
    private String mail;
    private String intro;
    public CurrentUser(){
    }

    public int getImgid() {
        return imgid;
    }

    public void setImgid(int imgid) {
        this.imgid = imgid;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public CurrentUser(User user){
        this.id=user.getId();
        this.username=user.getUsername();
        this.mail=user.getMail();
        this.intro=user.getIntro();
        this.sex=user.getSex();
        this.imgid=user.getImgid();
        this.point=user.getPoint();
        this.sumpoint=user.getSumpoints();
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
