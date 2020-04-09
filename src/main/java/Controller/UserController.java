package Controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Img;
import domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import service.*;
import utils.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

@Controller
@RequestMapping(path="/user")
public class UserController {

    @Autowired
    private UpService upService;
    @Autowired
    private UserService userService;
    @Autowired
    private EssayService essayService;
    @Autowired
    private CateService cateService;
    @Autowired
    private CommentsService commentsService;
    @Autowired
    private ReplyService replyService;
    @Autowired
    private LetterService letterService;
    @Autowired
    private LetterMsgService letterMsgService;
    @Autowired
    private ImgService imgService;
    @Autowired
    private AttentionService attentionService;
    @Autowired
    private EssayCateService essayCateService;
    @Autowired
    private PointService pointService;
    @Autowired
    private ObjectMapper mapper;

    public boolean ConfirmCheckcode(String code, String session_code) {
        if (session_code.equals(code)) {
            return true;
        } else {
            return false;
        }
    }
    //登录用户仅使用账号(邮箱)和密码登录即可
    @RequestMapping(path = "/login")
    public @ResponseBody
    ResultInfo login(@RequestBody User user,HttpServletRequest request) throws IOException {
        ResultInfo rs = null;
//        String check_code=request.getParameter("checkcode");
//        String session_code=(request.getSession().getAttribute("checkcode")==null)?"":(String) request.getSession().getAttribute("checkcode");
//        request.getSession().removeAttribute("checkcode");
//        if(!ConfirmCheckcode(check_code,session_code)){
//            rs.setFlag(false);
//            rs.setInfo("验证码错误");
//            return rs;
//        }
//        String mail = request.getParameter("mail");
//        String password = request.getParameter("password");
//        User _user = new User();
//        _user.setMail(mail);
//        _user.setPassword(password);
        rs = new ResultInfo<User>();
        User _user = userService.queryUser(user);
        if (_user == null) {
            rs.setFlag(false);
            rs.setInfo("账号或密码错误");
        } else {
//            request.getSession().setAttribute("user", user);
            CurrentUser cuser=new CurrentUser(_user);
            if (SessionData.getUser_session().containsKey(cuser.getId())) {
                HttpSession httpSession = SessionData.getUser_session().get(cuser.getId());
                if (httpSession != null) {
                    httpSession.removeAttribute("user");
                    if(httpSession != request.getSession()) {
                        httpSession.invalidate();
                    }
                    WebSocketSession webSocketSession = SessionData.getUser_websocketSession().get(cuser.getId());
                    LoginCheck loginCheck = new LoginCheck(false, "当前账户已在别处登录");
                    if (webSocketSession != null) {
                        webSocketSession.sendMessage(new TextMessage(mapper.writeValueAsString(loginCheck)));
                        webSocketSession.close();
                    }
                    SessionData.getUser_websocketSession().remove(cuser.getId());
                }
            }
            request.getSession().setAttribute("user",cuser);
            SessionData.getUser_session().put(cuser.getId(),request.getSession());
            rs.setInfo("登录成功");
            rs.setFlag(true);
            rs.setRes(cuser);
            System.out.println("--------------------一次请求---------------");
        }
        return rs;
    }
    //注册用户,需要激活邮箱使用
    @RequestMapping(path = "/register")
    public @ResponseBody
    ResultInfo addUser(@RequestBody User user) {
        String mail=user.getMail();
        if(mail.length() <= 30) {
            boolean flag=PatternUtil.isPattern(mail,"^[a-zA-Z0-9]{5,20}@(qq|gmail|163)\\.com$");
            if(!flag) {
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setFlag(false);
                resultInfo.setInfo("邮箱格式错误");
                return resultInfo;
            }
        }else{
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setFlag(false);
            resultInfo.setInfo("邮箱长度过长");
            return resultInfo;
        }
        String username=user.getUsername();
        if(username.length() <= 15){
            boolean flag=PatternUtil.isPattern(username,"^[a-zA-Z0-9]{5,15}$");
            if(!flag){
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setFlag(false);
                resultInfo.setInfo("用户名格式错误");
                return resultInfo;
            }
        }else{
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setFlag(false);
            resultInfo.setInfo("用户名长度过长");
            return resultInfo;
        }
        String pwd=user.getPassword();
        if(pwd.length()<=20){
            boolean flag=PatternUtil.isPattern(pwd,"^[a-zA-Z0-9]{8,20}$");
            if(!flag){
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setFlag(false);
                resultInfo.setInfo("密码格式错误");
                return resultInfo;
            }
        }else{
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setFlag(false);
            resultInfo.setInfo("密码长度过长");
            return resultInfo;
        }
        String sex=user.getSex();
        if(sex.length()==1){
            boolean flag=PatternUtil.isPattern(sex,"^(1|2)$");
            if(!flag){
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setFlag(false);
                resultInfo.setInfo("性别格式错误");
                return resultInfo;
            }
        }else{
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setFlag(false);
            resultInfo.setInfo("性别长度过长");
            return resultInfo;
        }
        String intro=user.getIntro();
        intro=intro.trim();
        if(!(intro.length()<=200)){
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setFlag(false);
            resultInfo.setInfo("个人介绍长度过长");
            return resultInfo;
        }
        final String code = UuidUtil.getUuid();
        user.setCode(code);
        boolean res = userService.addUser(user);
        ResultInfo rs = new ResultInfo();
        if (res) {
            rs.setFlag(res);
            rs.setInfo("注册成功,请登录邮箱进行激活账号");
            MailUtils.sendMail(user.getMail(),"点击<a href='http://"+ IpUtils.getNativeIpAddress() +":8080/user/active?code=" + code + "'>这里</a>即可激活哦!!","小小博客邀请信");
        } else {
            rs.setFlag(res);
            rs.setInfo("注册失败");
        }
        return rs;
    }
    //激活邮箱
    @RequestMapping(path = "/active")
    public @ResponseBody ResultInfo active(HttpServletRequest request) {
        ResultInfo rs = new ResultInfo();
        String code=request.getParameter("code");
        if(code!=null){
            userService.activeUser(code);
            rs.setFlag(true);
            rs.setInfo("激活成功,恭喜加入小小博客大家园");
        }else{
            rs.setFlag(false);
            rs.setInfo("链接错误或失效,请重新注册");
        }
        return rs;
    }
    //获取用户信息
    @RequestMapping("/getUserByUserId")
    public @ResponseBody ResultInfo getUser(@RequestBody Map<String,String> request){
        int userid;
        ResultInfo<CurrentUser> rs=new ResultInfo();
        rs.setFlag(true);
        rs.setInfo("获取用户信息成功");
        if(request.containsKey("userId")){
            userid=Integer.parseInt(request.get("userId"));
        }else{
            rs.setFlag(false);
            rs.setInfo("getUserByUserId参数错误");
            return rs;
        }
        User user=userService.queryUserByUserId(userid);
        if(user==null){
            rs.setFlag(false);
            rs.setInfo("并没有当前用户");
            return rs;
        }
        CurrentUser _user=new CurrentUser(user);
        rs.setRes(_user);
        return rs;
    }
    @RequestMapping(path="/getAllEssaysByCateId")
    public @ResponseBody ResultInfo getAllEssaysByCateId(@RequestBody Map<String,String> request){
        int flag=1;
        if(request.containsKey("flag")){
            flag=Integer.parseInt(request.get("flag"));
        }
        int userId;
        if(request.containsKey("userId")) {
            userId = Integer.parseInt(request.get("userId"));
        }else{
            ResultInfo rs = new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("缺少参数");
            return rs;
        }
        int cateId;
        if(request.containsKey("cateId")){
            cateId = Integer.parseInt(request.get("cateId"));
        }else{
            ResultInfo rs = new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("缺少参数");
            return rs;
        }
        List<Essay> essays = essayService.queryAllWholeEssaysByCateId(cateId,userId,flag);
        ResultInfo rs = new ResultInfo();
        rs.setFlag(true);
        rs.setRes(essays);
        return rs;
    }
    //获取验证码功能
    @RequestMapping(path = "/checkcode")
    public void checkcode(HttpServletRequest request, HttpServletResponse response) {
        try {
            String res = CheckImage.createImage(response.getOutputStream());
            request.getSession().setAttribute("checkcode", res);
        } catch (IOException e) {
            System.out.println("error....");
        }
        return;
    }
    //更新用户
    @RequestMapping(path="/updateUser")
    public @ResponseBody ResultInfo updateUser(@RequestBody User _user){
        String username=_user.getUsername();
        if(username.length()<=15){
            boolean flag=PatternUtil.isPattern(username,"^[a-zA-Z0-9]{5,15}$");
            if(!flag){
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setFlag(false);
                resultInfo.setInfo("用户名格式错误");
                return resultInfo;
            }
        }else{
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setFlag(false);
            resultInfo.setInfo("用户名长度过长");
            return resultInfo;
        }
        String sex=_user.getSex();
        if(sex.length()==1){
            boolean flag=PatternUtil.isPattern(sex,"^(1|2)$");
            if(!flag){
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setFlag(false);
                resultInfo.setInfo("性别格式错误");
                return resultInfo;
            }
        }else{
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setFlag(false);
            resultInfo.setInfo("性别长度过长");
            return resultInfo;
        }
        String intro=_user.getIntro();
        intro=intro.trim();
        if(!(intro.length()<=200)){
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setFlag(false);
            resultInfo.setInfo("个人介绍长度过长");
            return resultInfo;
        }
        boolean flag=userService.updateUser(_user);
        ResultInfo rs=new ResultInfo();
        if(flag){
            rs.setFlag(true);
            rs.setInfo("更新成功");
            User user=userService.queryUserByUserId(_user.getId());
            rs.setRes(user);
        }else{
            rs.setFlag(false);
            rs.setInfo("更新失败");
        }
        return rs;
    }

    @RequestMapping(path="/upload")
    public @ResponseBody ResultInfo uploadImg(HttpServletRequest request, @RequestParam("upload")MultipartFile upload) throws IOException {
        // 先获取到要上传的文件目录
        String path = request.getSession().getServletContext().getRealPath("/images");
        // 创建File对象,一会向该路径下上传文件
        File file = new File(path);
        // 获取到上传文件的名称
        String filename = upload.getOriginalFilename();
        String[] names=filename.split("\\.");
        String fileSuffixName=names[names.length-1];
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        filename= uuid+"."+fileSuffixName;
        upload.transferTo(new File(file,filename));
        Img img =new Img();
        img.setLocation(filename);
        imgService.addImg(img);
//        System.out.println(img);
//        int imgid=imgService.queryImgByLocation(filename);
//        img.setId(imgid);
        ResultInfo rs=new ResultInfo();
        rs.setFlag(true);
        rs.setRes(img);
        return rs;
    }
    //在首页获取所有最新发布文章的用户和对应的一篇文章,根据时间排序
    @RequestMapping(path = "/getEssayListByTime")
    public @ResponseBody
    ResultInfo getRecommendEssayListByTime(@RequestBody Map<String,Object> requestmap) {
        //flag为1表示查询审核通过的文章 0表示查询审核中的文章 -1表示查询未通过的文章
        int page=1;
        List<Integer> integer_flag=new ArrayList<>();
        integer_flag.add(1);
        int userId=0;
        String search="";
        int cateId;
        if(requestmap.containsKey("cateId")){
            cateId=(Integer)requestmap.get("cateId");
        }else{
            ResultInfo rs=new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("getEssayListByTime参数有误");
            return rs;
        }
        if(requestmap.containsKey("userId")){
            userId=(Integer)requestmap.get("userId");
        }
        if(requestmap.containsKey("page")){
            page=(Integer)(requestmap.get("page"));
        }
        if(requestmap.containsKey("flag")){
            integer_flag=(ArrayList)(requestmap.get("flag"));
        }
        if(requestmap.containsKey("search")){
            search=((String)requestmap.get("search")).trim();
        }
        int[] flag=new int[integer_flag.size()];
        flag[0]=1;
        for(int i=0;i<integer_flag.size();i++){
            flag[i]=integer_flag.get(i);
        }
        PageBean<Map<String, Object>> essayPageBean = new PageBean<>();
        essayPageBean.setTotalCount(essayService.queryAllEssaysByUserIdNum(cateId,userId,search,flag));
        essayPageBean.setCurrentPage(page);
        essayPageBean.setPageSize(10);
        essayPageBean.setList(new ArrayList<Map<String, Object>>());
        boolean mod = (essayPageBean.getTotalCount() % essayPageBean.getPageSize()) == 0;
        essayPageBean.setTotalPage(mod ? essayPageBean.getTotalCount() / essayPageBean.getPageSize() : (essayPageBean.getTotalCount() / essayPageBean.getPageSize() + 1));
        List<Essay> essays = essayService.queryAllEssaysByUserId(essayPageBean,cateId,userId,search,flag);
        ResultInfo<PageBean> rs = new ResultInfo<PageBean>();
        rs.setFlag(true);
        rs.setRes(essayPageBean);
        for (Essay essay: essays) {
            int _userId = essay.getUserId();
            User user = userService.queryUserByUserId(_userId);
            CurrentUser _user=new CurrentUser(user);
            Map map = new HashMap();
            map.put("user", _user);
            map.put("essay", essay);
            rs.getRes().getList().add(map);
        }
        return rs;
    }

    @RequestMapping(path = "editEssay")
    public @ResponseBody ResultInfo editEssay(@RequestBody Map<String,String> request){
//        essayId: param.essayId,
//        title: param.title,
//        msg: param.msg,
//        htmlMsg: param.htmlMsg
        int essayId;
        String title;
        String msg;
        String htmlmsg;
        ResultInfo rs=new ResultInfo();
        if(request.containsKey("essayId")){
            essayId = Integer.parseInt(request.get("essayId"));
        }else{
            rs.setFlag(false);
            rs.setInfo("editEssay:缺少essayId参数");
            return rs;
        }
        if(request.containsKey("title")){
            title=request.get("title");
        }else{
            rs.setFlag(false);
            rs.setInfo("editEssay:缺少title参数");
            return rs;
        }
        if(request.containsKey("msg")){
            msg=request.get("msg");
        }else{
            rs.setFlag(false);
            rs.setInfo("editEssay:缺少msg参数");
            return rs;
        }
        if(request.containsKey("htmlmsg")){
            htmlmsg=request.get("htmlmsg");
        }else{
            rs.setFlag(false);
            rs.setInfo("editEssay:缺少htmlmsg参数");
            return rs;
        }
        title=title.trim();
        if(title.length()<=20){
            boolean flag=PatternUtil.isPattern(title,"^.{1,20}$");
            if(!flag){
                ResultInfo resultInfo=new ResultInfo();
                resultInfo.setFlag(false);
                resultInfo.setInfo("标题格式错误");
                return resultInfo;
            }
        }else{
            ResultInfo resultInfo=new ResultInfo();
            resultInfo.setFlag(false);
            resultInfo.setInfo("标题长度过长");
            return  resultInfo;
        }
        if(msg.length()>5000 || htmlmsg.length() > 5000){
            ResultInfo resultInfo=new ResultInfo();
            resultInfo.setFlag(false);
            resultInfo.setInfo("文章内容过长");
            return resultInfo;
        }
        boolean flag2=essayService.updateEssayFlag(0,essayId);
        boolean flag=essayService.updateEssay(essayId,title,msg,htmlmsg);
        if(flag && flag2){
            rs.setFlag(true);
            rs.setInfo("修改成功");
        }else{
            rs.setFlag(false);
            rs.setInfo("editEssay:修改失败");
        }
        return rs;
    }
    //根据用户获取相关的所有的文章,按照时间排序
//    @RequestMapping(path = "/getEssaysByUserIdByTime")
//    public @ResponseBody
//    ResultInfo getEssaysByUserId(@RequestBody Map<String,String> request) {
//        Set<Map.Entry<String, String>> entries = request.entrySet();
//        for(Map.Entry<String,String> entry:entries){
//            System.out.println(entry.getKey()+ " " + entry.getValue());
//        }
//        int page=1,userid,flag=1;
//        String search="";
//        // flag为1表示查询用户所有审核通过的文章 0代表查询所有文章(审核中\审核失败\审核通过)
//        if(request.containsKey("flag")){
//            flag=Integer.parseInt(request.get("flag"));
//        }
//        if(request.containsKey("page")){
//            page = Integer.parseInt(request.get("page"));
//        }
//        if(request.containsKey("userId")){
//            userid = Integer.parseInt(request.get("userId"));
//        }else{
//            ResultInfo rs = new ResultInfo();
//            rs.setFlag(false);
//            rs.setInfo("缺少参数");
//            return rs;
//        }
//        if(request.containsKey("search")){
//            search=request.get("search");
//        }
//        PageBean<Essay> essayPageBean = new PageBean<>();
//        essayPageBean.setTotalCount(essayService.queryAllEssaysByUserIdNum(userid,search,flag));
//        essayPageBean.setCurrentPage(page);
//        essayPageBean.setPageSize(10);
//        boolean mod = (essayPageBean.getTotalCount() % essayPageBean.getPageSize()) == 0;
//        essayPageBean.setTotalPage(mod ? essayPageBean.getTotalCount() / essayPageBean.getPageSize() : (essayPageBean.getTotalCount() / essayPageBean.getPageSize() + 1));
//        List<Essay> essays = essayService.queryAllEssaysByUserId(essayPageBean, userid,search,flag);
//        essayPageBean.setList(essays);
//        ResultInfo<PageBean> rs = new ResultInfo<PageBean>();
//        rs.setFlag(true);
//        rs.setRes(essayPageBean);
//        return rs;
//    }
    //根据用户获取相关的所有标签
    @RequestMapping(path = "/getCatesByUserId")
    public @ResponseBody
    ResultInfo getCatesByUserId(@RequestBody Map<String,String> request) {
        int userid;
        if(request.containsKey("userId")){
            userid = Integer.parseInt(request.get("userId"));
        }else{
            ResultInfo rs = new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("缺少参数");
            return rs;
        }
        List<Cate> cates = cateService.queryAllCatesByUserId(userid);
        ResultInfo rs = new ResultInfo();
        rs.setFlag(true);
        rs.setRes(cates);
        return rs;
    }
    //根据标签id删除标签及相应的文章
    @RequestMapping(path= "/deleteCatesByCateId")
    public @ResponseBody ResultInfo deleteCatesByCateId(@RequestBody Map<String,String> request){
        int cateId;
        if(request.containsKey("cateId")){
            cateId = Integer.parseInt(request.get("cateId"));
        }else{
            ResultInfo rs = new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("缺少参数");
            return rs;
        }
        ResultInfo rs=new ResultInfo();
        boolean update_flag=essayService.updateAllEssaysCateNumsByCateId(cateId);
        if(!update_flag){
            rs.setFlag(false);
            rs.setInfo("删除标签失败");
            return rs;
        }
        boolean delete_flag=cateService.deleteCate(cateId);
        if(delete_flag){
            rs.setFlag(true);
            rs.setInfo("删除标签成功");
        }else{
            rs.setFlag(false);
            rs.setInfo("删除标签失败");
        }
        return rs;
    }
    //更新一个标签
    @RequestMapping(path="/updateCateByCateId")
    public @ResponseBody ResultInfo updateCateByCateId(@RequestBody Map<String,String> request){
        int cateId;
        if(request.containsKey("cateId")){
            cateId = Integer.parseInt(request.get("cateId"));
        }else{
            ResultInfo rs = new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("缺少参数");
            return rs;
        }
        String cate_name;
        if(request.containsKey("catename")){
            cate_name = request.get("catename");
        }else{
            ResultInfo rs = new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("缺少参数");
            return rs;
        }
        ResultInfo rs=new ResultInfo();
        boolean flag=cateService.updateCate(cate_name,cateId);
        if(flag){
            rs.setFlag(true);
            rs.setInfo("更新标签成功");
        }else{
            rs.setFlag(false);
            rs.setInfo("更新标签失败");
        }
        return rs;
    }

    //根据文章id获取相关用户
    @RequestMapping(path="/getUserByEssayId")
    public @ResponseBody ResultInfo getUserByEssayId(@RequestBody Map<String,String> request){
        int essayid;
        if(request.containsKey("essayId")){
            essayid = Integer.parseInt(request.get("essayId"));
        }else{
            ResultInfo rs = new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("缺少参数");
            return rs;
        }
        ResultInfo rs=new ResultInfo();
        Essay essay=essayService.queryEssayByEssayId(essayid);
        int userid=essay.getUserId();
        CurrentUser user=new CurrentUser(userService.queryUserByUserId(userid));
        rs.setFlag(true);
        rs.setRes(user);
        return rs;
    }

    //根据相关标签获取所有相关文章,再按照时间排序
    @RequestMapping(path = "/getEssaysByCateId")
    public @ResponseBody
    ResultInfo getEssaysByCateIdByTime(@RequestBody Map<String,String> request) {
        int page=1,flag=1;
        if(request.containsKey("flag")){
            flag=Integer.parseInt(request.get("flag"));
        }
        if(request.containsKey("page")){
            page = Integer.parseInt(request.get("page"));
        }
        int userId;
        if(request.containsKey("userId")) {
            userId = Integer.parseInt(request.get("userId"));
        }else{
            ResultInfo rs = new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("缺少参数");
            return rs;
        }
        int cateId;
        if(request.containsKey("cateId")){
            cateId = Integer.parseInt(request.get("cateId"));
        }else{
            ResultInfo rs = new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("缺少参数");
            return rs;
        }
        PageBean<Essay> essayPageBean = new PageBean<>();
        essayPageBean.setTotalCount(essayService.queryAllEssaysByCateIdNum(cateId,userId,flag));
        essayPageBean.setCurrentPage(page);
        essayPageBean.setPageSize(10);
        boolean mod = (essayPageBean.getTotalCount() % essayPageBean.getPageSize()) == 0;
        essayPageBean.setTotalPage(mod ? essayPageBean.getTotalCount() / essayPageBean.getPageSize() : (essayPageBean.getTotalCount() /essayPageBean.getPageSize() + 1));
        List<Essay> essays = essayService.queryAllEssaysByCateId(essayPageBean, cateId,userId,flag);
        essayPageBean.setList(essays);
        ResultInfo<PageBean> rs = new ResultInfo<PageBean>();
        rs.setFlag(true);
        rs.setRes(essayPageBean);
        return rs;
    }
    //添加文章
    @RequestMapping(path= "/addEssay")
    public @ResponseBody ResultInfo addEssay(@RequestBody Map<String,Object> request){
        //需要userId   oldCateId为数组[]必须创建,可以为空数组但不能为null,newCateName新建标签名,msg纯文本数据,
        //htmlMsg带标签的文本数据,
        int userId;
        String title="";
        List<Integer> oldCateId=new ArrayList<>();
        String newCateName="";
        String msg="";
        String htmlMsg="";
        ResultInfo rs=new ResultInfo();
        rs.setFlag(false);
        rs.setInfo("addEssay参数错误");
        if(request.containsKey("userId")){
            userId=(Integer)request.get("userId");
        }else{
            return rs;
        }
        title=(String)(request.get("title"));
        title=title.trim();
        if(title.length()<=20){
            boolean flag=PatternUtil.isPattern(title,"^.{1,20}$");
            if(!flag){
                ResultInfo resultInfo=new ResultInfo();
                resultInfo.setFlag(false);
                resultInfo.setInfo("标题格式错误");
                return resultInfo;
            }
        }else{
            ResultInfo resultInfo=new ResultInfo();
            resultInfo.setFlag(false);
            resultInfo.setInfo("标题长度过长");
            return  resultInfo;
        }
        msg=(String)(request.get("msg"));
        htmlMsg=(String)(request.get("htmlMsg"));
        if((msg.length()>5000 || htmlMsg.length() > 5000) && (msg.length() == 0 || htmlMsg.length() == 0)){
            ResultInfo resultInfo=new ResultInfo();
            resultInfo.setFlag(false);
            resultInfo.setInfo("文章内容不能为空且长度在5000字以内");
            return resultInfo;
        }
        newCateName=(String) request.get("newCateName");
        newCateName=newCateName.trim();
        if(newCateName.length()>20 ){
            ResultInfo resultInfo=new ResultInfo();
            resultInfo.setFlag(false);
            resultInfo.setInfo("标签名称过长");
            return resultInfo;
        }
        Essay essay=new Essay();
        essay.setUserId(userId);
        essay.setTitle(title);
        essay.setMsg(msg);
        essay.setHtmlmsg(htmlMsg);
        boolean flag=essayService.addEssay(essay);
        if(flag) {
            oldCateId = (ArrayList<Integer>) request.get("oldCateId");
            int catenums=oldCateId.size();
            for (int cateId: oldCateId) {
                essayCateService.addEssayCate(essay.getId(),cateId);
            }
            if(! "".equals(newCateName)) {
                Cate cate = cateService.queryCateByCateName(userId, newCateName);
                if (cate == null) {
                    cate = new Cate();
                    cate.setName(newCateName);
                    cate.setUserId(userId);
                    cateService.addCate(cate);
                    catenums++;
                    essayCateService.addEssayCate(essay.getId(),cate.getId());
                }
            }
            essayService.updateEssayCateNums(essay.getId(),catenums);
            User user=userService.queryUserByUserId(userId);
            user.setPoint(user.getPoint()+5);
            user.setSumpoints(user.getSumpoints()+5);
            pointService.updateUser(user);
            rs.setFlag(true);
            rs.setInfo("添加博文成功");
            rs.setRes(new CurrentUser(user));
        }
        return rs;
    }
    //添加标签
    @RequestMapping(path = "/addCate")
    public @ResponseBody ResultInfo addCate(@RequestBody Cate cate){
        ResultInfo rs=new ResultInfo();
        if(!(cate.getName().length()>=1&&cate.getName().length()<=20)){
            rs.setFlag(false);
            rs.setInfo("标签内容不能为空且长度在20位以内");
            return rs;
        }
        boolean flag=cateService.addCate(cate);
        rs.setFlag(false);
        rs.setInfo("添加失败");
        if(flag){
            rs.setFlag(true);
            rs.setInfo("添加成功");
            rs.setRes(cate);
        }
        return rs;
    }
    //添加文章标签
    @RequestMapping(path="/addEssayCate")
    public @ResponseBody ResultInfo addEssayCateByEssayIdAndCateId(@RequestBody Map<String,String> request){
        int essayid,cateid;
        ResultInfo rs = new ResultInfo();
        rs.setFlag(false);
        rs.setInfo("缺少参数");
        if(request.containsKey("essayId")){
            essayid = Integer.parseInt(request.get("essayId"));
        }else{
            return rs;
        }
        if(request.containsKey("cateId")){
            cateid = Integer.parseInt(request.get("cateId"));
        }else{
            return rs;
        }
        EssayCate essayCate=essayCateService.queryEssayCate(essayid,cateid);
        if(essayCate==null) {
            boolean flag = essayCateService.addEssayCate(essayid, cateid);
            if (flag) {
                rs.setFlag(true);
                rs.setInfo("移动成功");
            }else{
                rs.setFlag(false);
                rs.setInfo("移动失败");
            }
        }else{
            rs.setFlag(false);
            rs.setInfo("已存在该标签,无需移动");
        }
        return rs;
    }
    @RequestMapping(path = "updateUp")
    public @ResponseBody ResultInfo updateUp(@RequestBody Map<String,String> request){
        int essayId;
        ResultInfo rs=new ResultInfo();
        if(request.containsKey("essayId")){
            essayId=Integer.parseInt(request.get("essayId"));
        }else{
            rs.setFlag(false);
            rs.setInfo("updateUp:essayId参数丢失");
            return rs;
        }
        int userId;
        if(request.containsKey("userId")){
            userId=Integer.parseInt(request.get("userId"));
        }else{
            rs.setFlag(false);
            rs.setInfo("updateUp:userId参数丢失");
            return rs;
        }
        int flag;
        if(request.containsKey("flag")){
            flag=Integer.parseInt(request.get("flag"));
        }else{
            rs.setFlag(false);
            rs.setInfo("updateUp:flag参数丢失");
            return rs;
        }
        Up up=upService.queryUpByUserIdAndEssayId(userId,essayId);
        if(up == null) {
            upService.addUp(userId,essayId,flag);
        }else{
            upService.updateUp(up.getId(),flag);
        }
        int ups=upService.getUps(essayId,1);
        int downs=upService.getUps(essayId,-1);
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("up",ups);
        map.put("down",downs);
        rs.setFlag(true);
        rs.setRes(map);
        return rs;
    }

    @RequestMapping(path = "getUpOrDown")
    public @ResponseBody ResultInfo getUpOrDown(@RequestBody Map<String,String> request) {
        int essayId;
        ResultInfo rs=new ResultInfo();
        if(request.containsKey("essayId")){
            essayId=Integer.parseInt(request.get("essayId"));
        }else{
            rs.setFlag(false);
            rs.setInfo("getUpOrDown:essayId参数丢失");
            return rs;
        }
        int userId;
        if(request.containsKey("userId")){
            userId=Integer.parseInt(request.get("userId"));
        }else{
            rs.setFlag(false);
            rs.setInfo("getUpOrDown:userId参数丢失");
            return rs;
        }
        Up up=upService.queryUpByUserIdAndEssayId(userId,essayId);
        rs.setFlag(true);
        rs.setRes(up);
        return rs;
    }

    @RequestMapping(path="getUps")
    public @ResponseBody ResultInfo getUps(@RequestBody Map<String,String> request){
        int essayId;
        ResultInfo rs=new ResultInfo();
        if(request.containsKey("essayId")){
            essayId=Integer.parseInt(request.get("essayId"));
        }else{
            rs.setFlag(false);
            rs.setInfo("getUps:essayId参数丢失");
            return rs;
        }
        int up=upService.getUps(essayId,1);
        int down=upService.getUps(essayId,-1);
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("up",up);
        map.put("down",down);
        rs.setFlag(true);
        rs.setRes(map);
        return rs;
    }


    //删除文章标签
    @RequestMapping(path="/deleteEssayCate")
    public @ResponseBody ResultInfo deleteEssayCateByEssayIdAndCateId(@RequestBody Map<String,String> request){
        int essayid,cateid;
        ResultInfo rs = new ResultInfo();
        rs.setFlag(false);
        rs.setInfo("缺少参数");
        if(request.containsKey("essayId")){
            essayid = Integer.parseInt(request.get("essayId"));
        }else{
            return rs;
        }
        if(request.containsKey("cateId")){
            cateid = Integer.parseInt(request.get("cateId"));
        }else{
            return rs;
        }
        EssayCate essayCate=essayCateService.queryEssayCate(essayid,cateid);
        if(essayCate!=null) {
            boolean flag = essayCateService.deleteEssayCate(essayid, cateid);
            if (flag) {
                essayService.updateEssayCateNums(essayid,essayService.queryEssayByEssayId(essayid).getCatenums()-1);
                rs.setFlag(true);
                rs.setInfo("移动成功");
            }else{
                rs.setFlag(false);
                rs.setInfo("移动失败");
            }
        }else{
            rs.setFlag(false);
            rs.setInfo("不存在标签对应的文章");
        }
        return rs;
    }
    //根据文章id删除一篇文章
    @RequestMapping(path="/deleteEssayByEssayId")
    public @ResponseBody ResultInfo deleteEssayByEssayId(@RequestBody Map<String,String> request){
        int essayId;
        if(request.containsKey("essayId")){
            essayId = Integer.parseInt(request.get("essayId"));
        }else{
            ResultInfo rs = new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("缺少参数");
            return rs;
        }
        ResultInfo rs=new ResultInfo();
        boolean flag=essayService.deleteEssay(essayId);
        if(flag){
            rs.setFlag(true);
            rs.setInfo("删除成功");
        }else{
            rs.setFlag(false);
            rs.setInfo("删除失败");
        }
        return rs;
    }
    //根据文章Id获取相关的文章
    @RequestMapping(path = "/getEssayByEssayId")
    public @ResponseBody
    ResultInfo getEssayByEssayId(@RequestBody Map<String,String> request) {
        int essayId;
        if(request.containsKey("essayId")){
            essayId = Integer.parseInt(request.get("essayId"));
        }else{
            ResultInfo rs = new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("缺少参数");
            return rs;
        }
        ResultInfo<Essay> rs = new ResultInfo<Essay>();
        rs.setFlag(true);
        Essay essay = essayService.queryEssayByEssayId(essayId);
        rs.setFlag(true);
        rs.setRes(essay);
        return rs;
    }
    //添加评论
    @RequestMapping(path="/addComments")
    public @ResponseBody ResultInfo addComments(@RequestBody Comments comments){
        boolean flag=commentsService.addComments(comments);
        ResultInfo<Map<String,Object>> rs=new ResultInfo<Map<String,Object>>();
        rs.setFlag(false);
        rs.setInfo("发表评论失败");
        if(flag){
            String msg=comments.getMsg();
            msg=msg.trim();
            if(!(msg.length() <= 50 && msg.length() >= 1)){
                ResultInfo resultInfo=new ResultInfo();
                resultInfo.setFlag(false);
                resultInfo.setInfo("评论内容不能为空且长度在1-50位");
            }
            User user=userService.queryUserByUserId(comments.getUserId());
            user.setPoint(user.getPoint()+2);
            user.setSumpoints(user.getSumpoints()+2);
            boolean flag2=pointService.updateUser(user);
            if(flag2){
                rs.setFlag(true);
                rs.setInfo("发表评论成功");
                Map<String,Object> map=new HashMap<String,Object>();
                comments=commentsService.queryCommentsByCommentsId(comments.getId());
                map.put("user",new CurrentUser(user));
                map.put("comment",comments);
                rs.setRes(map);
            }
        }
        return rs;
    }
    //根据文章Id获取所有评论
    @RequestMapping(path="/getCommentsByEssayId")
    public @ResponseBody ResultInfo getCommentsByEssayId(@RequestBody Map<String,String> request){
        int page=1;
        if(request.containsKey("page")){
            page = Integer.parseInt(request.get("page"));
        }
        int essayId;
        if(request.containsKey("essayId")){
            essayId = Integer.parseInt(request.get("essayId"));
        }else{
            ResultInfo rs = new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("getCommentsByEssayId缺少参数");
            return rs;
        }
        PageBean<Comments> commentsPageBean = new PageBean<>();
        commentsPageBean.setTotalCount(commentsService.queryAllCommentsByEssayIdNum(essayId));
        commentsPageBean.setCurrentPage(page);
        commentsPageBean.setPageSize(10);
        boolean mod = (commentsPageBean.getTotalCount() % commentsPageBean.getPageSize()) == 0;
        commentsPageBean.setTotalPage(mod ? commentsPageBean.getTotalCount() / commentsPageBean.getPageSize() : (commentsPageBean.getTotalCount() /commentsPageBean.getPageSize() + 1));
        List<Comments> comments = commentsService.queryAllCommentsByEssayId(commentsPageBean, essayId);
        commentsPageBean.setList(comments);
        ResultInfo<PageBean> rs = new ResultInfo<PageBean>();
        rs.setFlag(true);
        rs.setRes(commentsPageBean);
        return rs;
    }
    //根据文章获取相关的所有标签
    @RequestMapping(path= "/getCatesByEssayId")
    public @ResponseBody ResultInfo getCatesByEssayId(@RequestBody Map<String,String> request){
        int page=1;
        if(request.containsKey("page")){
            page = Integer.parseInt(request.get("page"));
        }
        int essayId;
        if(request.containsKey("essayId")){
            essayId = Integer.parseInt(request.get("essayId"));
        }else{
            ResultInfo rs = new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("getCatesByEssayId缺少参数");
            return rs;
        }
        List<Cate> cates = cateService.queryAllCatesByEssayId(essayId);
        ResultInfo rs = new ResultInfo();
        rs.setFlag(true);
        rs.setRes(cates);
        return rs;
    }
    //根据用户获取所有评论以及每条评论对应的文章和文章所属用户再按照时间排序
    @RequestMapping(path="/getCommentsByUserId")
    public @ResponseBody
    ResultInfo getCommentsByUserId(@RequestBody Map<String,String> request) {
        int page=1;
        if(request.containsKey("page")){
            page = Integer.parseInt(request.get("page"));
        }
        int userId;
        if(request.containsKey("userId")){
            userId = Integer.parseInt(request.get("userId"));
        }else{
            ResultInfo rs = new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("getCommentsByUserId缺少参数");
            return rs;
        }
        String search="";
        if(request.containsKey("search")){
            search=request.get("search");
        }
        PageBean<Map<String, Object>> commentsPageBean = new PageBean<>();
        commentsPageBean.setTotalCount(commentsService.queryAllCommentsByUserIdNum(userId,search));
        commentsPageBean.setCurrentPage(page);
        commentsPageBean.setPageSize(10);
        commentsPageBean.setList(new ArrayList<Map<String, Object>>());
        boolean mod = (commentsPageBean.getTotalCount() % commentsPageBean.getPageSize()) == 0;
        commentsPageBean.setTotalPage(mod ? commentsPageBean.getTotalCount() / commentsPageBean.getPageSize() : (commentsPageBean.getTotalCount() / commentsPageBean.getPageSize() + 1));
        List<Comments> comments = commentsService.queryAllCommentsByUserId(commentsPageBean, userId,search);
        ResultInfo<PageBean> rs = new ResultInfo<PageBean>();
        rs.setFlag(true);
        if (comments == null) {
            rs.setRes(null);
            return rs;
        }
        for (Comments comment: comments) {
            int essayId = comment.getEssayId();
            Map<String, Object> map = new HashMap<String, Object>();
            Essay essay = essayService.queryEssayByEssayId(essayId);
            CurrentUser user = new CurrentUser(essayService.queryUserByEssayId(essayId));
            map.put("comment", comment);
            map.put("essay", essay);
            commentsPageBean.getList().add(map);
        }
        rs.setRes(commentsPageBean);
        return rs;
    }
    //根据评论Id删除对应评论
    @RequestMapping(path="/deleteComment")
    public @ResponseBody ResultInfo deleteCommentsByCommentId(@RequestBody Map<String,String> request){
        int commentId;
        if(request.containsKey("commentId")){
            commentId = Integer.parseInt(request.get("commentId"));
        }else{
            ResultInfo rs = new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("deleteCommentsByCommentId缺少参数");
            return rs;
        }
        ResultInfo rs=new ResultInfo();
        boolean flag=commentsService.deleteComments(commentId);
        if(flag){
            rs.setFlag(true);
            rs.setInfo("删除成功");
        }else{
            rs.setFlag(false);
            rs.setInfo("删除失败");
        }
        return rs;
    }
    //根据评论获取所有回复,再按照时间排序
    @RequestMapping(path= "getReplyByCommentIdByTime")
    public @ResponseBody ResultInfo getReplyByCommentId(@RequestBody Map<String,String> request){
        int commentId;
        if(request.containsKey("commentsId")){
            commentId = Integer.parseInt(request.get("commentsId"));
        }else{
            ResultInfo rs = new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("getReplyByCommentIdByTime缺少参数");
            return rs;
        }
        List<Reply> replys = replyService.queryAllReplyByCommentId(commentId);
        ResultInfo rs = new ResultInfo<PageBean>();
        rs.setFlag(true);
        rs.setRes(replys);
        return rs;
    }
    //根据用户获取所有回复,再按照时间排序
    @RequestMapping(path="/getReplyByUserId")
    public @ResponseBody
    ResultInfo getReplyByUserId(@RequestBody Map<String,String> request) {
        int page=1;
        if(request.containsKey("page")){
            page = Integer.parseInt(request.get("page"));
        }
        int userId;
        if(request.containsKey("userId")){
            userId = Integer.parseInt(request.get("userId"));
        }else{
            ResultInfo rs = new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("getReplyByUserId缺少参数");
            return rs;
        }
        String search="";
        if(request.containsKey("search")){
            search=request.get("search");
        }
        PageBean<Map<String, Object>> replyPageBean = new PageBean<>();
        replyPageBean.setTotalCount(replyService.queryAllReplyByUserIdNum(userId,search));
        replyPageBean.setCurrentPage(page);
        replyPageBean.setPageSize(10);
        replyPageBean.setList(new ArrayList<Map<String,Object>>());
        boolean mod = (replyPageBean.getTotalCount() % replyPageBean.getPageSize()) == 0;
        replyPageBean.setTotalPage(mod ? replyPageBean.getTotalCount() / replyPageBean.getPageSize() : (replyPageBean.getTotalCount() / replyPageBean.getPageSize() + 1));
        List<Reply> replys = replyService.queryAllReplyByUserId(replyPageBean, userId,search);
        ResultInfo<PageBean> rs = new ResultInfo<PageBean>();
        rs.setFlag(true);
        if (replys == null) {
            rs.setRes(null);
            return rs;
        }
        for (Reply reply: replys) {
            int toUserId = reply.getToUserId();
            Map<String, Object> map = new HashMap<String, Object>();
            User touser = userService.queryUserByUserId(toUserId);
            Essay essay = essayService.queryEssayByEssayId(commentsService.queryCommentsByCommentsId(reply.getCommentsId()).getEssayId());
            map.put("reply", reply);
            map.put("touser", touser);
            map.put("essay", essay);
            replyPageBean.getList().add(map);
        }
        rs.setRes(replyPageBean);
        return rs;
    }
    //根据回复Id删除相关回复
    @RequestMapping(path = "/deleteReplyByReplyId")
    public @ResponseBody ResultInfo deleteReplyByReplyId(@RequestBody Map<String,String> request){
        int replyid;
        if(request.containsKey("replyId")){
            replyid = Integer.parseInt(request.get("replyId"));
        }else{
            ResultInfo rs = new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("deleteReplyByReplyId缺少参数");
            return rs;
        }
        ResultInfo rs=new ResultInfo();
        boolean flag=replyService.deleteReply(replyid);
        if(flag){
            rs.setFlag(true);
            rs.setInfo("删除成功");
        }else{
            rs.setFlag(false);
            rs.setInfo("删除失败");
        }
        return rs;
    }
    //添加回复
    @RequestMapping("/addReply")
    public @ResponseBody ResultInfo addReply(@RequestBody Reply reply){
        if(!(reply.getMsg().length()>=1&&reply.getMsg().length()<=50)){
            ResultInfo rs=new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("回复信息长度过长");
            return rs;
        }
        boolean flag=replyService.addReply(reply);
        ResultInfo rs=new ResultInfo();
        rs.setFlag(false);
        rs.setInfo("发表评论失败");
        if(flag){
            rs.setFlag(true);
            rs.setInfo("发表评论成功");
            reply=replyService.queryReplyByReplyId(reply.getId());
            rs.setRes(reply);
        }
        return rs;
    }
    //根据用户获取所有关注用户
    @RequestMapping(path="/getAttentionUserByUserId")
    public @ResponseBody
    ResultInfo getAttentionUserByUserId(@RequestBody Map<String,String> request) {
        int userId;
        if(request.containsKey("userId")){
            userId = Integer.parseInt(request.get("userId"));
        }else{
            ResultInfo rs = new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("getAttentionUserByUserId缺少参数");
            return rs;
        }
        List<User> users = userService.queryAllAttentionUserByUserId(userId);
        List<CurrentUser> _users = new ArrayList<CurrentUser>();
        for (User user: users) {
            CurrentUser c = new CurrentUser(user);
            _users.add(c);
        }
        ResultInfo rs = new ResultInfo();
        rs.setFlag(true);
        rs.setRes(_users);
        return rs;
    }
    @RequestMapping(path="/getAttention")
    public @ResponseBody ResultInfo getAttention(@RequestBody Attention attention){
        ResultInfo rs=new ResultInfo();
        Attention _attention=attentionService.getAttention(attention);
        rs.setFlag(true);
        rs.setInfo("查找成功");
        rs.setRes(_attention);
        return rs;
    }
    //删除一个用户关注
    @RequestMapping(path="/deleteAttention")
    public @ResponseBody ResultInfo deleteAttentionUserByUserId(@RequestBody Attention _attention){
        ResultInfo rs=new ResultInfo();
        boolean flag=attentionService.deleteAttention(_attention);
        if(!flag){
            rs.setFlag(false);
            rs.setInfo("删除失败");
        }else{
            rs.setFlag(true);
            rs.setInfo("删除成功");
        }
        return rs;
    }
    @RequestMapping(path="addAttention")
    public @ResponseBody ResultInfo addAttention(@RequestBody Attention attention){
        ResultInfo rs=new ResultInfo();
        rs.setFlag(false);
        rs.setInfo("addAttention添加关注失败");
        boolean flag=attentionService.addAttention(attention);
        if(flag){
            rs.setFlag(true);
            rs.setInfo("添加关注成功");
        }
        return rs;
    }
    //根据用户获取所有粉丝
    @RequestMapping(path="/getFanUserByUserId")
    public @ResponseBody
    ResultInfo getFanUserByUserId(@RequestBody Map<String,String> request) {
        int userId;
        if(request.containsKey("userId")){
            userId = Integer.parseInt(request.get("userId"));
        }else{
            ResultInfo rs = new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("getFanUserByUserId缺少参数");
            return rs;
        }
        List<User> users = userService.queryAllFanUserByUserId(userId);
        List<CurrentUser> _users = new ArrayList<CurrentUser>();
        for (User user: users) {
            CurrentUser c = new CurrentUser(user);
            _users.add(c);
        }
        ResultInfo rs = new ResultInfo();
        rs.setFlag(true);
        rs.setRes(_users);
        return rs;
    }
    @RequestMapping("addLetter")
    public @ResponseBody ResultInfo addLetter(@RequestBody Letter letter){
        User user=userService.queryUserByUserId(letter.getUserId1());
        if(user.getPoint()<10){
            ResultInfo rs=new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("对不起,私信需消耗10点活跃值");
            return rs;
        }
        ResultInfo<Map<String,Object>> rs=new ResultInfo<Map<String,Object>>();
        rs.setFlag(false);
        rs.setInfo("私信失败");
        boolean flag=letterService.addLetter(letter);
        if(flag){
            user.setPoint(user.getPoint()-10);
            pointService.updateUser(user);
            rs.setFlag(true);
            rs.setInfo("私信建立");
            Map<String,Object> map=new HashMap<String,Object>();
            map.put("user",new CurrentUser(user));
            map.put("letter",letterService.queryLetterByLetterId(letter.getId()));
            rs.setRes(map);
        }
        return rs;
    }
    //根据用户获取所有云信
    @RequestMapping(path="/getLetterByUserId")
    public @ResponseBody
    ResultInfo getChatroomByUserId(@RequestBody Map<String,String> request) {
        int page=1;
        if(request.containsKey("page")){
            page = Integer.parseInt(request.get("page"));
        }
        int userId;
        if(request.containsKey("userId")){
            userId = Integer.parseInt(request.get("userId"));
        }else{
            ResultInfo rs = new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("getLetterByUserId缺少参数");
            return rs;
        }
        PageBean<Map<String, Object>> chatroomPageBean = new PageBean<>();
        chatroomPageBean.setTotalCount(letterService.queryAllChatroomByUserIdNum(userId));
        chatroomPageBean.setCurrentPage(page);
        chatroomPageBean.setPageSize(10);
        chatroomPageBean.setList(new ArrayList<Map<String, Object>>());
        boolean mod = (chatroomPageBean.getTotalCount() % chatroomPageBean.getPageSize()) == 0;
        chatroomPageBean.setTotalPage(mod ? chatroomPageBean.getTotalCount() / chatroomPageBean.getPageSize() : (chatroomPageBean.getTotalCount() / chatroomPageBean.getPageSize() + 1));
        List<Letter> chatrooms = letterService.queryAllChatroomUserByUserId(chatroomPageBean, userId);
        for (Letter chatroom: chatrooms) {
            CurrentUser c = null;
            if (chatroom.getUserId1() == userId) {
                c = new CurrentUser(userService.queryUserByUserId(chatroom.getUserId2()));
            }else if(chatroom.getUserId2()==userId){
                c = new CurrentUser(userService.queryUserByUserId(chatroom.getUserId1()));
            }
            System.out.println(c);
            Map<String,Object> map=new HashMap<String, Object>();
            map.put("user",c);
            LetterMsg letterMsg=letterMsgService.queryLatestLetterMsgByLetterId(chatroom.getId());
            map.put("latestLetterMsg",letterMsg);
            map.put("letter", chatroom);
            int nums=letterMsgService.queryNoReadLetterMsg(chatroom.getId(),userId);
            map.put("noRead",nums);
            chatroomPageBean.getList().add(map);
        }
        ResultInfo<PageBean> rs = new ResultInfo<PageBean>();
        rs.setFlag(true);
        rs.setRes(chatroomPageBean);
        return rs;
    }
    @RequestMapping(path="/getNoReadLetterMsgNums")
    public @ResponseBody ResultInfo getNoReadLetterMsgNums(@RequestBody Map<String,String> request){
        int letterId;
        int userId;
        if(request.containsKey("letterId")){
            letterId = Integer.parseInt(request.get("letterId"));
        }else{
            ResultInfo rs=new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("getNoReadLetterMsgNums:缺少letterId参数");
            return rs;
        }
        if(request.containsKey("userId")){
            userId = Integer.parseInt(request.get("userId"));
        }else {
            ResultInfo rs=new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("getNoReadLetterMsgNums:缺少userId参数");
            return rs;
        }
        int nums=letterMsgService.queryNoReadLetterMsg(letterId,userId);
        ResultInfo rs=new ResultInfo();
        rs.setFlag(true);
        try {
            int allNums=letterMsgService.queryNoReadLetterMsgByUserId(userId);
            if(SessionData.getNoRead_user_socket().get(userId) != null) {
                SessionData.getNoRead_user_socket().get(userId).sendMessage(new TextMessage(allNums + ""));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        rs.setRes(nums);
        return rs;
    }
    //删除一个云信
    @RequestMapping(path="/deleteLetterByLetterId")
    public @ResponseBody ResultInfo deleteChatroomByUserId1AndUserId2(@RequestBody Map<String,String> request){
        int letterid;
        if(request.containsKey("letterId")){
            letterid = Integer.parseInt(request.get("letterId"));
        }else{
            ResultInfo rs = new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("deleteLetterByLetterId缺少参数");
            return rs;
        }
        ResultInfo rs=new ResultInfo();
        boolean flag=letterService.deleteChatroom(letterid);
        if(flag){
            rs.setFlag(true);
            rs.setInfo("删除成功");
        }else{
            rs.setFlag(false);
            rs.setInfo("删除失败");
        }
        return rs;
    }
    //获取云聊By userId1 and userId2
    @RequestMapping(path = "/getLetterByUserId1AndUserId2")
    public @ResponseBody ResultInfo getLetterByUserId1AndUserId2(@RequestBody Letter letter){
        Letter _letter=letterService.queryLetterByUserId1AndUserId2(letter);
        ResultInfo rs=new ResultInfo();
        rs.setFlag(true);
        rs.setRes(_letter);
        return rs;
    }
    //根据云聊Id获取相关所有云信
    @RequestMapping(path="/getLetterMsgByLetterId")
    public @ResponseBody ResultInfo getChatroomMsgByUserId(@RequestBody Map<String,String> request){
        int page=1;
        if(request.containsKey("page")){
            page = Integer.parseInt(request.get("page"));
        }
        int chatroomId;
        if(request.containsKey("letterId")){
            chatroomId = Integer.parseInt(request.get("letterId"));
        }else{
            ResultInfo rs = new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("getLetterMsgByLetterId缺少参数");
            return rs;
        }
        String search="";
        if(request.containsKey("search")){
            search=request.get("search");
        }
        PageBean<LetterMsg> chatroomMsgPageBean = new PageBean<>();
        chatroomMsgPageBean.setTotalCount(letterMsgService.queryAllChatroomMsgByChatroomIdNum(chatroomId,search));
        chatroomMsgPageBean.setCurrentPage(page);
        chatroomMsgPageBean.setPageSize(10);
        boolean mod = (chatroomMsgPageBean.getTotalCount() % chatroomMsgPageBean.getPageSize()) == 0;
        chatroomMsgPageBean.setTotalPage(mod ? chatroomMsgPageBean.getTotalCount() / chatroomMsgPageBean.getPageSize() : (chatroomMsgPageBean.getTotalCount() / chatroomMsgPageBean.getPageSize() + 1));
        List<LetterMsg> chatroomMsg = letterMsgService.queryAllChatroomMsgByChatroomId(chatroomMsgPageBean, chatroomId,search);
        chatroomMsgPageBean.setList(chatroomMsg);
        ResultInfo<PageBean> rs = new ResultInfo<PageBean>();
        rs.setFlag(true);
        rs.setRes(chatroomMsgPageBean);
        return rs;
    }
    @RequestMapping(path="switchLetterMsgFlag")
    public @ResponseBody ResultInfo switchLetterMsgFlag(@RequestBody Map<String,String> request) throws IOException {
        ResultInfo rs=new ResultInfo();
        rs.setFlag(false);
        rs.setInfo("switchLetterMsgFlag修改失败");
        int letterMsgId;
        if(request.containsKey("letterMsgId")){
            letterMsgId=Integer.parseInt(request.get("letterMsgId"));
        }else{
            rs.setInfo("switchLetterMsgFlag修改失败,参数有问题");
            return rs;
        }
        boolean flag=letterMsgService.switchLetterMsgFlag(letterMsgId);
        if(flag){
            LetterMsg letterMsg=letterMsgService.queryLetterMsgByLetterMsgId(letterMsgId);
            System.out.println(letterMsg);
            LetterMsg latestLetterMsg=letterMsgService.queryLatestLetterMsgByLetterId(letterMsg.getLetterId());
            int nums=letterMsgService.queryNoReadLetterMsg(letterMsg.getLetterId(),letterMsg.getToUserId());
            LetterNoRead res=new LetterNoRead(letterMsg.getLetterId(),nums,latestLetterMsg);
            SessionData.getNoRead_letter_socket().get(letterMsg.getLetterId()+"-"+letterMsg.getToUserId()).sendMessage(new TextMessage(mapper.writeValueAsString(res)));
            int allNums=letterMsgService.queryNoReadLetterMsgByUserId(letterMsg.getToUserId());
            SessionData.getNoRead_user_socket().get(letterMsg.getToUserId()).sendMessage(new TextMessage(allNums+""));
            rs.setFlag(true);
            rs.setInfo("修改成功");
        }
        return rs;
    }
    //添加云信通过云聊Id
    @RequestMapping(path="/addLetterMsg")
    public @ResponseBody ResultInfo addChatroomMsg(@RequestBody LetterMsg chatroomMsg) {
        ResultInfo rs = new ResultInfo();
        boolean flag = letterMsgService.addChatroomMsg(chatroomMsg);
        if (flag) {
            rs.setFlag(true);
            rs.setInfo("发送成功");
            rs.setRes(letterMsgService.queryLetterMsgByLetterMsgId(chatroomMsg.getId()));
        } else {
            rs.setFlag(false);
            rs.setInfo("发送失败");
        }
        return rs;
    }
    //根据图片Id获取相关图片路径(/images下的图片,img src='/images/...')
    @RequestMapping(path="/getImgByImgId")
    public @ResponseBody ResultInfo getImgByImgId(@RequestBody Map<String,String> request){
        int imgid;
        if(request.containsKey("imgId")){
            imgid = Integer.parseInt(request.get("imgId"));
        }else{
            ResultInfo rs = new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("getImgByImgId缺少参数");
            return rs;
        }
        String img = imgService.queryImgByImgId(imgid);
        ResultInfo rs=new ResultInfo();
        if(img==null){
            rs.setFlag(false);
            rs.setRes(null);
            return rs;
        }
        rs.setFlag(true);
        rs.setRes("/images/"+img);
        return rs;
    }

    //修改密码
    @RequestMapping(path="/setPassword")
    public @ResponseBody ResultInfo setPassworld(@RequestBody Map<String,String> request){
        String _oldpassword;
        if(request.containsKey("oldPassword")){
            _oldpassword = request.get("oldPassword");
        }else{
            ResultInfo rs = new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("setPassword缺少参数");
            return rs;
        }
        String _newpassword;
        if(request.containsKey("newPassword")){
            _newpassword = request.get("newPassword");
        }else{
            ResultInfo rs = new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("setPassword缺少参数");
            return rs;
        }
        if(_newpassword.length()<=20){
            boolean flag=PatternUtil.isPattern(_newpassword,"^[a-zA-Z0-9]{8,20}$");
            if(!flag){
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setFlag(false);
                resultInfo.setInfo("密码格式错误");
                return resultInfo;
            }
        }else{
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setFlag(false);
            resultInfo.setInfo("密码长度过长");
            return resultInfo;
        }
        int userid;
        if(request.containsKey("userId")){
            userid = Integer.parseInt(request.get("userId"));
        }else{
            ResultInfo rs = new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("setPassword缺少参数");
            return rs;
        }
        ResultInfo rs=new ResultInfo();
        User ad=userService.queryUserByUserId(userid);
        if(ad==null || !ad.getPassword().equals(_oldpassword)){
            rs.setFlag(false);
            rs.setInfo("原密码错误");
            return rs;
        }
        boolean flag=userService.updatePassword(userid,_newpassword);
        if(flag){
            rs.setFlag(true);
            rs.setInfo("设置密码成功");
        }else{
            rs.setFlag(false);
            rs.setInfo("设置密码失败");
        }
        return rs;
    }
}