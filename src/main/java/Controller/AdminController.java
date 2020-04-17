package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xpath.internal.operations.Bool;
import domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import service.*;
import utils.PatternUtil;
import utils.SessionData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping(path="/admin")
public class AdminController {
    @Autowired
    private ObjectMapper mapper;

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
    private LetterService chatroomService;
    @Autowired
    private LetterMsgService chatroomMsgService;
    @Autowired
    private ImgService imgService;
    @Autowired
    private AttentionService attentionService;
    @Autowired
    private EssayCateService essayCateService;
    @Autowired
    private AdministratorService administratorService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private PointService pointService;
    //登录用户仅使用账号(邮箱)和密码登录即可
    @RequestMapping(path = "/login")
    public @ResponseBody
    ResultInfo login(@RequestBody Administrator _administrator,HttpServletRequest request) throws IOException {
        ResultInfo rs = new ResultInfo();
        rs = new ResultInfo<User>();
        Administrator administrator = administratorService.queryAdmin(_administrator);
        if (administrator == null) {
            rs.setFlag(false);
            rs.setInfo("账号或密码错误");
        } else {
            Set<Integer> integers = SessionData.getUser_session().keySet();
            for(Integer key:integers){
                if(SessionData.getAdmin_session().get(key)==request.getSession()){
                    HttpSession adminKey=SessionData.getAdmin_session().get(key);
                    adminKey.invalidate();
                }
            }
            if (SessionData.getAdmin_session().containsKey(administrator.getId())) {
                HttpSession httpSession = SessionData.getAdmin_session().get(administrator.getId());
                if (httpSession != null) {
                    httpSession.removeAttribute("admin");
                    if(httpSession != request.getSession()) {
                        httpSession.invalidate();
                    }
                }
            }
            request.getSession().setAttribute("admin",administrator);
            SessionData.getAdmin_session().put(administrator.getId(),request.getSession());
            rs.setInfo("登录成功");
            rs.setFlag(true);
            rs.setRes(new CurrentAdmin(administrator));
        }
        return rs;
    }
    // 查询未审核或已经审核的文章
    @RequestMapping("/getEssays")
    public @ResponseBody ResultInfo getEssays(@RequestBody Map<String,String> request) {
        //0 表示查询未审核的文章,1 表示查询已经审过的文章
        int flag=0;
        if(request.containsKey("flag")){
            flag=Integer.parseInt(request.get("flag"));
        }
        int page=1;
        if(request.containsKey("page")){
            page=Integer.parseInt(request.get("page"));
        }
        PageBean<Map<String,Object>> essayPageBean=new PageBean<>();
        essayPageBean.setTotalCount(essayService.queryAllEssaysByAdminNums(flag));
        essayPageBean.setCurrentPage(page);
        essayPageBean.setPageSize(10);
        essayPageBean.setList(new ArrayList<Map<String, Object>>());
        boolean mod = (essayPageBean.getTotalCount() % essayPageBean.getPageSize()) == 0;
        essayPageBean.setTotalPage(mod ? essayPageBean.getTotalCount() / essayPageBean.getPageSize() : (essayPageBean.getTotalCount() / essayPageBean.getPageSize() + 1));
        List<Essay> essays = essayService.queryAllEssaysByAdmin(essayPageBean,flag);
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
    //更改密码
    @RequestMapping("/setPassword")
    public @ResponseBody ResultInfo setPassword(@RequestBody Map<String,String> request) {
        String _oldpassword;
        if(request.containsKey("oldPassword")){
            _oldpassword = request.get("oldPassword");
        }else{
            ResultInfo rs = new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("setPassword缺少参数oldPassword");
            return rs;
        }
        String _newpassword;
        if(request.containsKey("newPassword")){
            _newpassword = request.get("newPassword");
        }else{
            ResultInfo rs = new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("setPassword缺少参数newPassword");
            return rs;
        }
        if(_newpassword.length()<=20){
            boolean flag= PatternUtil.isPattern(_newpassword,"^[a-zA-Z0-9]{8,20}$");
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
        int adminid;
        if(request.containsKey("adminId")){
            adminid = Integer.parseInt(request.get("adminId"));
        }else{
            ResultInfo rs = new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("setPassword缺少参数adminId");
            return rs;
        }
        ResultInfo rs=new ResultInfo();
        Administrator ad=administratorService.queryAdminById(adminid);
        if(ad==null || !ad.getPassword().equals(_oldpassword)){
            rs.setFlag(false);
            rs.setInfo("原密码错误");
            return rs;
        }
        boolean flag=administratorService.updatePassword(adminid,_newpassword);
        if(flag){
            rs.setFlag(true);
            rs.setInfo("设置密码成功");
        }else{
            rs.setFlag(false);
            rs.setInfo("设置密码失败");
        }
        return rs;
    }
    // 审核文章
    @RequestMapping("/setEssayFlag")
    public @ResponseBody ResultInfo setEssayFlag(@RequestBody Map<String,String> request){
        int essayId,flag=0;
        if(request.containsKey("essayId")){
            essayId = Integer.parseInt(request.get("essayId"));
        }else{
            ResultInfo rs=new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("setEssayFlag参数错误");
            return rs;
        }
        if(request.containsKey("flag")){
            flag=Integer.parseInt(request.get("flag"));
        }else{
            ResultInfo rs=new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("setEssayFlag参数错误");
            return rs;
        }
        String checkmsg="";
        if(flag==-1){
            checkmsg=request.get("checkmsg");
            checkmsg=checkmsg.trim();
            if(checkmsg.length()>=1 && checkmsg.length()<=10){
                boolean checkmsgFlag=PatternUtil.isPattern(checkmsg,"^.{1,10}$");
                if(!checkmsgFlag){
                    ResultInfo resultInfo=new ResultInfo();
                    resultInfo.setFlag(false);
                    resultInfo.setInfo("checkmsg格式错误");
                    return resultInfo;
                }
            }else{
                ResultInfo resultInfo=new ResultInfo();
                resultInfo.setFlag(false);
                resultInfo.setInfo("输入的审核信息长度过长");
                return resultInfo;
            }
        }
        Boolean res=essayService.updateEssayFlag(flag,checkmsg,essayId);
        EssayHistory essayHistory=new EssayHistory();
        essayHistory.setCheckmsg(checkmsg);
        essayHistory.setEssayId(essayId);
        essayHistory.setFlag(flag);
        Boolean res2=historyService.addEssayHistory(essayHistory);
        ResultInfo rs=new ResultInfo();
        rs.setFlag(false);
        rs.setInfo("操作失败");
        if(res && res2){
            if(flag==1) {
                User user = userService.queryUserByUserId(essayService.queryEssayByEssayId(essayId).getUserId());
                user.setPoint(user.getPoint() + 5);
                user.setSumpoints(user.getSumpoints() + 5);
                pointService.updateUser(user);
            }
            rs.setFlag(true);
            rs.setInfo("操作成功");
        }
        return rs;
    }
    // 审核文章
    @RequestMapping("/setCommentFlag")
    public @ResponseBody ResultInfo setCommentFlag(@RequestBody Map<String,String> request){
        int commentId,flag=0;
        if(request.containsKey("commentId")){
            commentId = Integer.parseInt(request.get("commentId"));
        }else{
            ResultInfo rs=new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("setCommentFlag参数错误");
            return rs;
        }
        if(request.containsKey("flag")){
            flag=Integer.parseInt(request.get("flag"));
        }else{
            ResultInfo rs=new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("setCommentFlag参数错误");
            return rs;
        }
        String checkmsg="";
        if(flag==-1){
            checkmsg=request.get("checkmsg");
            checkmsg=checkmsg.trim();
            if(checkmsg.length()>=1 && checkmsg.length()<=10){
                boolean checkmsgFlag=PatternUtil.isPattern(checkmsg,"^.{1,10}$");
                if(!checkmsgFlag){
                    ResultInfo resultInfo=new ResultInfo();
                    resultInfo.setFlag(false);
                    resultInfo.setInfo("checkmsg格式错误");
                    return resultInfo;
                }
            }else{
                ResultInfo resultInfo=new ResultInfo();
                resultInfo.setFlag(false);
                resultInfo.setInfo("输入的审核信息长度过长");
                return resultInfo;
            }
        }
        Boolean res=commentsService.updateCommentsFlag(flag,checkmsg,commentId);
        CommentsHistory commentsHistory=new CommentsHistory();
        commentsHistory.setCheckmsg(checkmsg);
        commentsHistory.setCommentid(commentId);
        commentsHistory.setFlag(flag);
        Boolean res2=historyService.addCommentsHistory(commentsHistory);
        ResultInfo rs=new ResultInfo();
        rs.setFlag(false);
        rs.setInfo("操作失败");
        if(res && res2){
            if(flag==1){
                User user=userService.queryUserByUserId(commentsService.queryCommentsByCommentsId(commentId).getUserId());
                user.setPoint(user.getPoint()+2);
                user.setSumpoints(user.getSumpoints()+2);
                pointService.updateUser(user);
            }
            rs.setFlag(true);
            rs.setInfo("操作成功");
        }
        return rs;
    }
    @RequestMapping("/getComments")
    public @ResponseBody ResultInfo getComments(@RequestBody Map<String,String> request){
        int flag,page=1;
        if(request.containsKey("flag")){
            flag=Integer.parseInt(request.get("flag"));
        }else{
            ResultInfo rs=new ResultInfo();
            rs.setFlag(false);
            rs.setInfo("getComments参数错误");
            return rs;
        }
        if(request.containsKey("page")) {
            page = Integer.parseInt(request.get("page"));
        }
        PageBean<Comments> commentsPageBean = new PageBean<>();
        commentsPageBean.setTotalCount(commentsService.queryAllCommentsCheckNum(flag));
        commentsPageBean.setCurrentPage(page);
        commentsPageBean.setPageSize(10);
        boolean mod = (commentsPageBean.getTotalCount() % commentsPageBean.getPageSize()) == 0;
        commentsPageBean.setTotalPage(mod ? commentsPageBean.getTotalCount() / commentsPageBean.getPageSize() : (commentsPageBean.getTotalCount() /commentsPageBean.getPageSize() + 1));
        List<Comments> comments = commentsService.queryAllCommentsCheck(commentsPageBean, flag);
        commentsPageBean.setList(comments);
        ResultInfo<PageBean> rs = new ResultInfo<PageBean>();
        rs.setFlag(true);
        rs.setRes(commentsPageBean);
        return rs;
    }
    @RequestMapping("/getEssayHistorys")
    public @ResponseBody ResultInfo getEssayHistorys(@RequestBody Map<String,String> request){
        int page=1;
        if(request.containsKey("page")){
            page=Integer.parseInt(request.get("page"));
        }
        String search="";
        if(request.containsKey("search")) {
            search = request.get("search");
        }
        PageBean<Map<String,Object>> essayPageBean = new PageBean<>();
        essayPageBean.setTotalCount(historyService.queryEssayHistorysNum(search));
        essayPageBean.setCurrentPage(page);
        essayPageBean.setPageSize(10);
        essayPageBean.setList(new ArrayList<Map<String, Object>>());
        boolean mod = (essayPageBean.getTotalCount() % essayPageBean.getPageSize()) == 0;
        essayPageBean.setTotalPage(mod ? essayPageBean.getTotalCount() / essayPageBean.getPageSize() : (essayPageBean.getTotalCount() / essayPageBean.getPageSize() + 1));
        List<EssayHistory> essayHistorys=historyService.queryEssayHistorys(essayPageBean,search);
        ResultInfo<PageBean> rs = new ResultInfo<PageBean>();
        rs.setFlag(true);
        rs.setRes(essayPageBean);
        for (EssayHistory essayHistory: essayHistorys) {
            Essay essay=essayService.queryEssayByEssayId(essayHistory.getEssayId());
            CurrentUser user=new CurrentUser(userService.queryUserByUserId(essay.getUserId()));
            Map map = new HashMap();
            map.put("history", essayHistory);
            map.put("essay", essay);
            map.put("user",user);
            rs.getRes().getList().add(map);
        }
        return rs;
    }
    @RequestMapping("/getCommentsHistorys")
    public @ResponseBody ResultInfo getCommentsHistorys(@RequestBody Map<String,String> request){
        int page=1;
        if(request.containsKey("page")){
            page=Integer.parseInt(request.get("page"));
        }
        String search="";
        if(request.containsKey("search")) {
            search=request.get("search");
        }
        PageBean<Map<String,Object>> commentsPageBean = new PageBean<>();
        commentsPageBean.setTotalCount(historyService.queryCommentsHistorysNum(search));
        commentsPageBean.setCurrentPage(page);
        commentsPageBean.setPageSize(10);
        commentsPageBean.setList(new ArrayList<Map<String, Object>>());
        boolean mod = (commentsPageBean.getTotalCount() % commentsPageBean.getPageSize()) == 0;
        commentsPageBean.setTotalPage(mod ? commentsPageBean.getTotalCount() / commentsPageBean.getPageSize() : (commentsPageBean.getTotalCount() / commentsPageBean.getPageSize() + 1));
        List<CommentsHistory> commentsHistorys=historyService.queryCommentsHistorys(commentsPageBean,search);
        ResultInfo<PageBean> rs = new ResultInfo<PageBean>();
        rs.setFlag(true);
        rs.setRes(commentsPageBean);
        for (CommentsHistory commentsHistory: commentsHistorys) {
            Comments comments=commentsService.queryCommentsByCommentsId(commentsHistory.getCommentid());
            CurrentUser user=new CurrentUser(userService.queryUserByUserId(comments.getUserId()));
            Map map = new HashMap();
            map.put("history", commentsHistory);
            map.put("comment", comments);
            map.put("user",user);
            rs.getRes().getList().add(map);
        }
        return rs;
    }
//    //查询所有用户
//    @RequestMapping(path="/getUsers")
//    public @ResponseBody ResultInfo getUsers(HttpServletRequest request){
//        int page;
//        String res = request.getParameter("page");
//        if (res == null || Integer.parseInt(res) <= 0) {
//            page = 1;
//        }else{
//            page = Integer.parseInt(res);
//        }
//        String search="";
//        String _search=request.getParameter("search");
//        if(_search!=null){
//            search=_search;
//        }
//        PageBean<User> userPageBean = new PageBean<>();
//        userPageBean.setTotalCount(userService.queryUsersNum(search));
//        userPageBean.setCurrentPage(page);
//        userPageBean.setPageSize(10);
//        boolean mod = (userPageBean.getTotalCount() % userPageBean.getPageSize()) == 0;
//        userPageBean.setTotalPage(mod ? userPageBean.getTotalCount() / userPageBean.getPageSize() : (userPageBean.getTotalCount() / userPageBean.getPageSize() + 1));
//        List<User> users = userService.queryUsers(userPageBean,search);
//        ResultInfo<PageBean> rs = new ResultInfo<PageBean>();
//        rs.setFlag(true);
//        rs.setRes(userPageBean);
//        return rs;
//    }
//    //退出
//    @RequestMapping("/exit")
//    public @ResponseBody ResultInfo exit(HttpServletRequest request){
//        request.getSession().removeAttribute("admin");
//        ResultInfo rs=new ResultInfo();
//        rs.setFlag(true);
//        rs.setInfo("已退出");
//        return rs;
//    }
//    //根据用户id删除用户
//    @RequestMapping("/deleteUserByUserId")
//    public @ResponseBody ResultInfo deleteUser(HttpServletRequest request){
//        ResultInfo rs=new ResultInfo();
//        String _userid=request.getParameter("userid");
//        if(_userid==null){
//            rs.setFlag(false);
//            rs.setInfo("参数错误");
//            return rs;
//        }
//        boolean flag=userService.deleteUser(Integer.parseInt(_userid));
//        if(flag){
//            rs.setFlag(true);
//            rs.setInfo("删除用户成功");
//        }else{
//            rs.setFlag(false);
//            rs.setInfo("删除用户失败");
//        }
//        return rs;
//    }
//    //获取所有博文
//    @RequestMapping(path = "/getAllEssaysListByTime")
//    public @ResponseBody
//    ResultInfo getRecommendEssayListByTime(HttpServletRequest request) {
//        int page;
//        String res = request.getParameter("page");
//        if (res == null || Integer.parseInt(res) <= 0) {
//            page = 1;
//        }else{
//            page = Integer.parseInt(res);
//        }
//        String search="";
//        String _search=request.getParameter("search");
//        if(_search!=null){
//            search=_search;
//        }
//        PageBean<Map<String, Object>> essayPageBean = new PageBean<>();
//        essayPageBean.setTotalCount(essayService.queryAllEssaysNum(search,1));
//        essayPageBean.setCurrentPage(page);
//        essayPageBean.setPageSize(10);
//        essayPageBean.setList(new ArrayList<Map<String, Object>>());
//        boolean mod = (essayPageBean.getTotalCount() % essayPageBean.getPageSize()) == 0;
//        essayPageBean.setTotalPage(mod ? essayPageBean.getTotalCount() / essayPageBean.getPageSize() : (essayPageBean.getTotalCount() / essayPageBean.getPageSize() + 1));
//        List<Essay> essays = essayService.queryAllEssays(essayPageBean,search,1);
//        ResultInfo<PageBean> rs = new ResultInfo<PageBean>();
//        rs.setFlag(true);
//        rs.setRes(essayPageBean);
//        for (Essay essay: essays) {
//            System.out.println(essay.getUserId());
//            int userId = essay.getUserId();
//            System.out.println(userId);
//            CurrentUser user = new CurrentUser(userService.queryUserByUserId(userId));
//            System.out.println(user);
//            System.out.println(essay);
//            Map map = new HashMap();
//            map.put("user", user);
//            map.put("essay", essay);
//            rs.getRes().getList().add(map);
//        }
//        return rs;
//    }
//    //获取所有评论
//    @RequestMapping(path = "/getCommentsByTime")
//    public @ResponseBody ResultInfo getCommentsByTime(HttpServletRequest request){
//        int page;
//        String res = request.getParameter("page");
//        if (res == null || Integer.parseInt(res) <= 0) {
//            page = 1;
//        }else{
//            page = Integer.parseInt(res);
//        }
//        String search="";
//        String _search=request.getParameter("search");
//        if(_search!=null){
//            search=_search;
//        }
//        PageBean<Map<String, Object>> commentsPageBean = new PageBean<>();
//        commentsPageBean.setTotalCount(commentsService.queryAllCommentsNum(search));
//        commentsPageBean.setCurrentPage(page);
//        commentsPageBean.setPageSize(10);
//        commentsPageBean.setList(new ArrayList<Map<String,Object>>());
//        boolean mod = (commentsPageBean.getTotalCount() % commentsPageBean.getPageSize()) == 0;
//        commentsPageBean.setTotalPage(mod ? commentsPageBean.getTotalCount() / commentsPageBean.getPageSize() : (commentsPageBean.getTotalCount() / commentsPageBean.getPageSize() + 1));
//        List<Comments> comments = commentsService.queryAllComments(commentsPageBean,search);
//        ResultInfo<PageBean> rs = new ResultInfo<PageBean>();
//        rs.setFlag(true);
//        if (comments == null) {
//            rs.setRes(null);
//            return rs;
//        }
//        for (Comments comment: comments) {
//            int essayId = comment.getEssayId();
//            Map<String, Object> map = new HashMap<String, Object>();
//            User fromuser= userService.queryUserByUserId(comment.getUserId());
//            Essay essay = essayService.queryEssayByEssayId(essayId);
//            User user = essayService.queryUserByEssayId(essayId);
//            map.put("fromuser",fromuser);
//            map.put("comment", comment);
//            map.put("touser", user);
//            map.put("essay", essay);
//            commentsPageBean.getList().add(map);
//        }
//        rs.setRes(commentsPageBean);
//        return rs;
//    }
//    //根据用户获取相关的所有的文章,按照时间排序
////    @RequestMapping(path = "/getEssaysByUserIdByTime")
////    public @ResponseBody
////    ResultInfo getEssaysByUserId(@RequestBody Map<String,String> request) {
////        int page=1,flag=1;
////        String search="";
////        if(request.containsKey("page")){
////            page=Integer.parseInt(request.get("page"));
////        }
////        if(request.containsKey("flag")){
////            flag=Integer.parseInt(request.get("flag"));
////        }
////        if(request.containsKey("search")){
////            search=request.get("search");
////        }
////
////        int userid = Integer.parseInt(request.get("userid"));
////        PageBean<Essay> essayPageBean = new PageBean<>();
////        essayPageBean.setTotalCount(essayService.queryAllEssaysByUserIdNum(userid,search,flag));
////        essayPageBean.setCurrentPage(page);
////        essayPageBean.setPageSize(10);
////        boolean mod = (essayPageBean.getTotalCount() % essayPageBean.getPageSize()) == 0;
////        essayPageBean.setTotalPage(mod ? essayPageBean.getTotalCount() / essayPageBean.getPageSize() : (essayPageBean.getTotalCount() / essayPageBean.getPageSize() + 1));
////        List<Essay> essays = essayService.queryAllEssaysByUserId(essayPageBean, userid,search,flag);
////        System.out.println(essays.get(0));
////        essayPageBean.setList(essays);
////        ResultInfo<PageBean> rs = new ResultInfo<PageBean>();
////        rs.setFlag(true);
////        rs.setRes(essayPageBean);
////        return rs;
////    }
////    //根据用户获取相关的所有标签
//    @RequestMapping(path = "/getCatesByUserId")
//    public @ResponseBody
//    ResultInfo getCatesByUserId(HttpServletRequest request) {
//        int page;
//        String res = request.getParameter("page");
//        if (res == null || Integer.parseInt(res) <= 0) {
//            page = 1;
//        }else{
//            page = Integer.parseInt(res);
//        }
//        String search="";
//        String _search=request.getParameter("search");
//        if(_search!=null){
//            search=_search;
//        }
//        int userid = Integer.parseInt(request.getParameter("userid"));
//        PageBean<Cate> catePageBean = new PageBean<>();
//        catePageBean.setTotalCount(cateService.queryAllCatesByUserIdNum(userid,search));
//        catePageBean.setCurrentPage(page);
//        catePageBean.setPageSize(10);
//        boolean mod = (catePageBean.getTotalCount() % catePageBean.getPageSize()) == 0;
//        catePageBean.setTotalPage(mod ? catePageBean.getTotalCount() / catePageBean.getPageSize() : (catePageBean.getTotalCount() / catePageBean.getPageSize() + 1));
//        List<Cate> cates = cateService.queryAllCatesByUserId(catePageBean, userid,search);
//        System.out.println(cates.get(0));
//        catePageBean.setList(cates);
//        ResultInfo<PageBean> rs = new ResultInfo<PageBean>();
//        rs.setFlag(true);
//        rs.setRes(catePageBean);
//        return rs;
//    }
//    //审核文章
//    @RequestMapping(path="checkEssayByEssayId")
//    public @ResponseBody ResultInfo checkEssay(HttpServletRequest request){
//        String essayId=request.getParameter("essayid");
//        String _res=request.getParameter("res");
//        ResultInfo rs=new ResultInfo();
//        if(essayId==null || _res==null){
//            rs.setFlag(false);
//            rs.setInfo("参数错误");
//            return rs;
//        }
//        int res=Integer.parseInt(_res);
//        if(res==1){
//            boolean flag=essayService.updateEssayFlag(1,Integer.parseInt(essayId));
//            if(flag){
//                rs.setFlag(true);
//                rs.setInfo("审核文章通过");
//            }else{
//                rs.setFlag(false);
//                rs.setInfo("服务器正在抢修");
//            }
//        }else if(res==-1){
//            String msg=request.getParameter("msg");
//            boolean flag=essayService.updateEssayFlag(-1,msg,Integer.parseInt(essayId));
//            if(flag){
//                rs.setFlag(true);
//                rs.setInfo("审核文章通过");
//            }else{
//                rs.setFlag(false);
//                rs.setInfo("服务器正在抢修");
//            }
//        }else{
//            rs.setFlag(false);
//            rs.setInfo("参数错误");
//        }
//        return rs;
//    }
//    //根据标签id删除标签及相应的文章
//    @RequestMapping(path= "/deleteCatesByCateId")
//    public @ResponseBody ResultInfo deleteCatesByCateId(HttpServletRequest request){
//        String _cateId=request.getParameter("cateid");
//        ResultInfo rs=new ResultInfo();
//        if(_cateId==null || Integer.parseInt(_cateId)==0){
//            rs.setFlag(false);
//            rs.setInfo("删除标签失败");
//            return rs;
//        }
//        int cateId=Integer.parseInt(_cateId);
//        boolean update_flag=essayService.updateAllEssaysCateNumsByCateId(cateId);
//        if(!update_flag){
//            rs.setFlag(false);
//            rs.setInfo("删除标签失败");
//            return rs;
//        }
//        boolean delete_flag=cateService.deleteCate(cateId);
//        if(delete_flag){
//            rs.setFlag(true);
//            rs.setInfo("删除标签成功");
//        }else{
//            rs.setFlag(false);
//            rs.setInfo("删除标签失败");
//        }
//        return rs;
//    }
//    //根据相关分类获取所有相关文章,再按照时间排序
//    @RequestMapping(path = "/getEssaysByCateId")
//    public @ResponseBody
//    ResultInfo getEssaysByCateIdByTime(HttpServletRequest request) {
//        int page;
//        String res = request.getParameter("page");
//        if (res == null || Integer.parseInt(res) <= 0) {
//            page = 1;
//        }else{
//            page = Integer.parseInt(res);
//        }
//        int cateId = Integer.parseInt(request.getParameter("cateid"));
//        PageBean<Essay> essayPageBean = new PageBean<>();
//        essayPageBean.setTotalCount(essayService.queryAllEssaysByCateIdNum(cateId));
//        essayPageBean.setCurrentPage(page);
//        essayPageBean.setPageSize(10);
//        boolean mod = (essayPageBean.getTotalCount() % essayPageBean.getPageSize()) == 0;
//        essayPageBean.setTotalPage(mod ? essayPageBean.getTotalCount() / essayPageBean.getPageSize() : (essayPageBean.getTotalCount() /essayPageBean.getPageSize() + 1));
//        List<Essay> essays = essayService.queryAllEssaysByCateId(essayPageBean, cateId);
//        System.out.println(essays.get(0));
//        essayPageBean.setList(essays);
//        ResultInfo<PageBean> rs = new ResultInfo<PageBean>();
//        rs.setFlag(true);
//        rs.setRes(essayPageBean);
//        return rs;
//    }
//    //根据文章id删除一篇文章
//    @RequestMapping(path="/deleteEssayByEssayId")
//    public @ResponseBody ResultInfo deleteEssayByEssayId(HttpServletRequest request){
//        String essayId=request.getParameter("essayid");
//        ResultInfo rs=new ResultInfo();
//        if(essayId==null){
//            rs.setFlag(false);
//            rs.setInfo("参数错误");
//            return rs;
//        }
//        boolean flag=essayService.deleteEssay(Integer.parseInt(essayId));
//        if(flag){
//            rs.setFlag(true);
//            rs.setInfo("删除成功");
//        }else{
//            rs.setFlag(false);
//            rs.setInfo("删除失败");
//        }
//        return rs;
//    }
//    //根据文章Id获取相关的文章
//    @RequestMapping(path = "/getEssayByEssayId")
//    public @ResponseBody
//    ResultInfo getEssayByEssayId(HttpServletRequest request) {
//        String _essayId = request.getParameter("essayid");
//        ResultInfo<Essay> rs = new ResultInfo<Essay>();
//        rs.setFlag(true);
//        if (_essayId == null) {
//            rs.setRes(null);
//            System.out.println(rs);
//            return rs;
//        }
//        int essayId = Integer.parseInt(request.getParameter("essayid"));
//        Essay essay = essayService.queryEssayByEssayId(essayId);
//        System.out.println(essay);
//        rs.setFlag(true);
//        rs.setRes(essay);
//        return rs;
//    }
//    //根据文章Id获取所有评论
//    @RequestMapping(path="/getCommentsByEssayId")
//    public @ResponseBody ResultInfo getCommentsByEssayId(HttpServletRequest request){
//        int page;
//        String res = request.getParameter("page");
//        if (res == null || Integer.parseInt(res) <= 0) {
//            page = 1;
//        }else{
//            page = Integer.parseInt(res);
//        }
//        int essayId = Integer.parseInt(request.getParameter("essayid"));
//        PageBean<Comments> commentsPageBean = new PageBean<>();
//        commentsPageBean.setTotalCount(commentsService.queryAllCommentsByEssayIdNum(essayId));
//        commentsPageBean.setCurrentPage(page);
//        commentsPageBean.setPageSize(10);
//        boolean mod = (commentsPageBean.getTotalCount() % commentsPageBean.getPageSize()) == 0;
//        commentsPageBean.setTotalPage(mod ? commentsPageBean.getTotalCount() / commentsPageBean.getPageSize() : (commentsPageBean.getTotalCount() /commentsPageBean.getPageSize() + 1));
//        List<Comments> comments = commentsService.queryAllCommentsByEssayId(commentsPageBean, essayId);
//        commentsPageBean.setList(comments);
//        ResultInfo<PageBean> rs = new ResultInfo<PageBean>();
//        rs.setFlag(true);
//        rs.setRes(commentsPageBean);
//        return rs;
//    }
//    //根据文章获取相关的所有分类
//    @RequestMapping(path= "/getCatesByEssayId")
//    public @ResponseBody ResultInfo getCatesByEssayId(HttpServletRequest request){
//        int page;
//        String res = request.getParameter("page");
//        if (res == null || Integer.parseInt(res) <= 0) {
//            page = 1;
//        }else{
//            page = Integer.parseInt(res);
//        }
//        int essayId = Integer.parseInt(request.getParameter("essayid"));
//        PageBean<Cate> catePageBean = new PageBean<>();
//        catePageBean.setTotalCount(cateService.queryAllCatesByEssayIdNum(essayId));
//        catePageBean.setCurrentPage(page);
//        catePageBean.setPageSize(10);
//        boolean mod = (catePageBean.getTotalCount() % catePageBean.getPageSize()) == 0;
//        catePageBean.setTotalPage(mod ? catePageBean.getTotalCount() / catePageBean.getPageSize() : (catePageBean.getTotalCount() / catePageBean.getPageSize() + 1));
//        List<Cate> cates = cateService.queryAllCatesByEssayId(catePageBean, essayId,0);
//        catePageBean.setList(cates);
//        ResultInfo<PageBean> rs = new ResultInfo<PageBean>();
//        rs.setFlag(true);
//        rs.setRes(catePageBean);
//        return rs;
//    }
//    //获取所有未核实的评论和博文
//    @RequestMapping(path="/getUnCheckEssay")
//    public @ResponseBody ResultInfo getUnCheckEssayAndComments(HttpServletRequest request){
//        int page;
//        String res = request.getParameter("page");
//        if (res == null || Integer.parseInt(res) <= 0) {
//            page = 1;
//        }else{
//            page = Integer.parseInt(res);
//        }
//        String search="";
//        String _search=request.getParameter("search");
//        if(_search!=null){
//            search=_search;
//        }
//        PageBean<Map<String, Object>> essayPageBean = new PageBean<>();
//        essayPageBean.setTotalCount(essayService.queryAllEssaysUnCheckNum(search));
//        essayPageBean.setCurrentPage(page);
//        essayPageBean.setPageSize(10);
//        essayPageBean.setList(new ArrayList<Map<String, Object>>());
//        boolean mod = (essayPageBean.getTotalCount() % essayPageBean.getPageSize()) == 0;
//        essayPageBean.setTotalPage(mod ? essayPageBean.getTotalCount() / essayPageBean.getPageSize() : (essayPageBean.getTotalCount() / essayPageBean.getPageSize() + 1));
//        List<Essay> essays = essayService.queryAllEssaysUnCheck(essayPageBean,search);
//        ResultInfo<PageBean> rs = new ResultInfo<PageBean>();
//        rs.setFlag(true);
//        rs.setRes(essayPageBean);
//        for (Essay essay: essays) {
//            System.out.println(essay.getUserId());
//            int userId = essay.getUserId();
//            System.out.println(userId);
//            CurrentUser user = new CurrentUser(userService.queryUserByUserId(userId));
//            System.out.println(user);
//            System.out.println(essay);
//            Map map = new HashMap();
//            map.put("user", user);
//            map.put("essay", essay);
//            rs.getRes().getList().add(map);
//        }
//        return rs;
//
//    }
//    //根据用户获取所有评论以及每条评论对应的文章和文章所属用户再按照时间排序
//    @RequestMapping(path="/getCommentsByUserId")
//    public @ResponseBody
//    ResultInfo getCommentsByUserId(HttpServletRequest request) {
//        int page;
//        String res = request.getParameter("page");
//        if (res == null || Integer.parseInt(res) <= 0) {
//            page = 1;
//        }else{
//            page = Integer.parseInt(res);
//        }
//        String search="";
//        String _search=request.getParameter("search");
//        if(_search!=null){
//            search=_search;
//        }
//        int userId = Integer.parseInt(request.getParameter("userid"));
//        PageBean<Map<String, Object>> commentsPageBean = new PageBean<>();
//        commentsPageBean.setTotalCount(commentsService.queryAllCommentsByUserIdNum(userId,search));
//        commentsPageBean.setCurrentPage(page);
//        commentsPageBean.setPageSize(10);
//        commentsPageBean.setList(new ArrayList<Map<String,Object>>());
//        boolean mod = (commentsPageBean.getTotalCount() % commentsPageBean.getPageSize()) == 0;
//        commentsPageBean.setTotalPage(mod ? commentsPageBean.getTotalCount() / commentsPageBean.getPageSize() : (commentsPageBean.getTotalCount() / commentsPageBean.getPageSize() + 1));
//        List<Comments> comments = commentsService.queryAllCommentsByUserId(commentsPageBean, userId,search);
//        ResultInfo<PageBean> rs = new ResultInfo<PageBean>();
//        rs.setFlag(true);
//        if (comments == null) {
//            rs.setRes(null);
//            return rs;
//        }
//        for (Comments comment: comments) {
//            int essayId = comment.getEssayId();
//            Map<String, Object> map = new HashMap<String, Object>();
//            Essay essay = essayService.queryEssayByEssayId(essayId);
//            User user = essayService.queryUserByEssayId(essayId);
//            map.put("comment", comment);
//            map.put("touser", user);
//            map.put("essay", essay);
//            commentsPageBean.getList().add(map);
//        }
//        rs.setRes(commentsPageBean);
//        return rs;
//    }
//    //根据评论Id删除对应评论
//    @RequestMapping(path="/deleteCommentsByCommentId")
//    public @ResponseBody ResultInfo deleteCommentsByCommentId(HttpServletRequest request){
//        String commentId = request.getParameter("commentid");
//        ResultInfo rs=new ResultInfo();
//        if(commentId==null) {
//            rs.setFlag(false);
//            rs.setInfo("删除失败");
//            return rs;
//        }
//        boolean flag=commentsService.deleteComments(Integer.parseInt(commentId));
//        if(flag){
//            rs.setFlag(false);
//            rs.setInfo("删除失败");
//        }else{
//            rs.setFlag(true);
//            rs.setInfo("删除成功");
//        }
//        return rs;
//    }
//    //根据评论获取所有留言,再按照时间排序
//    @RequestMapping(path= "getReplyByCommentIdByTime")
//    public @ResponseBody ResultInfo getReplyByCommentId(HttpServletRequest request){
//        int page;
//        String res = request.getParameter("page");
//        if (res == null || Integer.parseInt(res) <= 0) {
//            page = 1;
//        }else{
//            page = Integer.parseInt(res);
//        }
//        int commentId = Integer.parseInt(request.getParameter("commentid"));
//        PageBean<Reply> replyPageBean = new PageBean<>();
//        replyPageBean.setTotalCount(replyService.queryAllReplyByCommentIdNum(commentId));
//        replyPageBean.setCurrentPage(page);
//        replyPageBean.setPageSize(10);
//        boolean mod = (replyPageBean.getTotalCount() % replyPageBean.getPageSize()) == 0;
//        replyPageBean.setTotalPage(mod ? replyPageBean.getTotalCount() / replyPageBean.getPageSize() : (replyPageBean.getTotalCount() / replyPageBean.getPageSize() + 1));
//        List<Reply> replys = replyService.queryAllReplyByCommentId(replyPageBean, commentId);
//        replyPageBean.setList(replys);
//        ResultInfo<PageBean> rs = new ResultInfo<PageBean>();
//        rs.setFlag(true);
//        rs.setRes(replyPageBean);
//        return rs;
//    }
//    //根据用户获取所有回复,再按照时间排序
//    @RequestMapping(path="/getReplyByUserId")
//    public @ResponseBody
//    ResultInfo getReplyByUserId(HttpServletRequest request) {
//        int page;
//        String res = request.getParameter("page");
//        if (res == null || Integer.parseInt(res) <= 0) {
//            page = 1;
//        }else{
//            page = Integer.parseInt(res);
//        }
//        String search="";
//        String _search=request.getParameter("search");
//        if(_search!=null){
//            search=_search;
//        }
//        int userId = Integer.parseInt(request.getParameter("userid"));
//        PageBean<List<Map<String, Object>>> replyPageBean = new PageBean<>();
//        replyPageBean.setTotalCount(replyService.queryAllReplyByUserIdNum(userId,search));
//        replyPageBean.setCurrentPage(page);
//        replyPageBean.setPageSize(10);
//        boolean mod = (replyPageBean.getTotalCount() % replyPageBean.getPageSize()) == 0;
//        replyPageBean.setTotalPage(mod ? replyPageBean.getTotalCount() / replyPageBean.getPageSize() : (replyPageBean.getTotalCount() / replyPageBean.getPageSize() + 1));
//        List<Reply> replys = replyService.queryAllReplyByUserId(replyPageBean, userId,search);
//        ResultInfo<PageBean> rs = new ResultInfo<PageBean>();
//        rs.setFlag(true);
//        if (replys == null) {
//            rs.setRes(null);
//            return rs;
//        }
//        for (Reply reply: replys) {
//            int toUserId = reply.getToUserId();
//            Map<String, Object> map = new HashMap<String, Object>();
//            User touser = userService.queryUserByUserId(toUserId);
//            Essay essay = essayService.queryEssayByEssayId(commentsService.queryCommentsByCommentsId(reply.getCommentId()).getEssayId());
//            map.put("reply", reply);
//            map.put("touser", touser);
//            map.put("essay", essay);
//        }
//        rs.setRes(replyPageBean);
//        return rs;
//    }
//    //根据回复Id删除相关回复
//    @RequestMapping(path = "/deleteReplyByReplyId")
//    public @ResponseBody ResultInfo deleteReplyByReplyId(HttpServletRequest request){
//        String replyid = request.getParameter("replyid");
//        ResultInfo rs=new ResultInfo();
//        if(replyid==null) {
//            rs.setFlag(false);
//            rs.setInfo("删除失败");
//            return rs;
//        }
//        boolean flag=replyService.deleteReply(Integer.parseInt(replyid));
//        if(flag){
//            rs.setFlag(false);
//            rs.setInfo("删除失败");
//        }else{
//            rs.setFlag(true);
//            rs.setInfo("删除成功");
//        }
//        return rs;
//    }
//    //根据用户获取所有关注用户
//    @RequestMapping(path="/getAttentionUserByUserId")
//    public @ResponseBody
//    ResultInfo getAttentionUserByUserId(HttpServletRequest request) {
//        int page;
//        String res = request.getParameter("page");
//        if (res == null || Integer.parseInt(res) <= 0) {
//            page = 1;
//        }else{
//            page = Integer.parseInt(res);
//        }
//        int userId = Integer.parseInt(request.getParameter("userid"));
//        PageBean<CurrentUser> attentionPageBean = new PageBean<>();
//        attentionPageBean.setTotalCount(userService.queryAllAttentionUserByUserIdNum(userId));
//        attentionPageBean.setCurrentPage(page);
//        attentionPageBean.setPageSize(10);
//        boolean mod = (attentionPageBean.getTotalCount() % attentionPageBean.getPageSize()) == 0;
//        attentionPageBean.setTotalPage(mod ? attentionPageBean.getTotalCount() / attentionPageBean.getPageSize() : (attentionPageBean.getTotalCount() / attentionPageBean.getPageSize() + 1));
//        List<User> users = userService.queryAllAttentionUserByUserId(attentionPageBean, userId);
//        List<CurrentUser> _users = new ArrayList<CurrentUser>();
//        for (User user: users) {
//            CurrentUser c = new CurrentUser();
//            c.setId(user.getId());
//            c.setUsername(user.getUsername());
//            c.setMail(user.getMail());
//            _users.add(c);
//        }
//        attentionPageBean.setList(_users);
//        ResultInfo<PageBean> rs = new ResultInfo<PageBean>();
//        rs.setFlag(true);
//        rs.setRes(attentionPageBean);
//        return rs;
//    }
//    //根据用户获取所有粉丝
//    @RequestMapping(path="/getFanUserByUserId")
//    public @ResponseBody
//    ResultInfo getFanUserByUserId(HttpServletRequest request) {
//        int page;
//        String res = request.getParameter("page");
//        if (res == null || Integer.parseInt(res) <= 0) {
//            page = 1;
//        }else{
//            page = Integer.parseInt(res);
//        }
//        int userId = Integer.parseInt(request.getParameter("userid"));
//        PageBean<CurrentUser> fanPageBean = new PageBean<>();
//        fanPageBean.setTotalCount(userService.queryAllFanUserByUserIdNum(userId));
//        fanPageBean.setCurrentPage(page);
//        fanPageBean.setPageSize(10);
//        boolean mod = (fanPageBean.getTotalCount() % fanPageBean.getPageSize()) == 0;
//        fanPageBean.setTotalPage(mod ? fanPageBean.getTotalCount() / fanPageBean.getPageSize() : (fanPageBean.getTotalCount() / fanPageBean.getPageSize() + 1));
//        List<User> users = userService.queryAllFanUserByUserId(fanPageBean, userId);
//        List<CurrentUser> _users = new ArrayList<CurrentUser>();
//        for (User user: users) {
//            CurrentUser c = new CurrentUser(user);
//            _users.add(c);
//        }
//        fanPageBean.setList(_users);
//        ResultInfo<PageBean> rs = new ResultInfo<PageBean>();
//        rs.setFlag(true);
//        rs.setRes(fanPageBean);
//        return rs;
//    }
//    //根据用户获取所有云聊
//    @RequestMapping(path="/getChatroomByUserId")
//    public @ResponseBody
//    ResultInfo getChatroomByUserId(HttpServletRequest request) {
//        int page;
//        String res = request.getParameter("page");
//        if (res == null || Integer.parseInt(res) <= 0) {
//            page = 1;
//        }else{
//            page = Integer.parseInt(res);
//        }
//        int userId = Integer.parseInt(request.getParameter("userid"));
//        PageBean<CurrentUser> chatroomPageBean = new PageBean<>();
//        chatroomPageBean.setTotalCount(chatroomService.queryAllChatroomByUserIdNum(userId));
//        chatroomPageBean.setCurrentPage(page);
//        chatroomPageBean.setPageSize(10);
//        boolean mod = (chatroomPageBean.getTotalCount() % chatroomPageBean.getPageSize()) == 0;
//        chatroomPageBean.setTotalPage(mod ? chatroomPageBean.getTotalCount() / chatroomPageBean.getPageSize() : (chatroomPageBean.getTotalCount() / chatroomPageBean.getPageSize() + 1));
//        List<Letter> chatrooms = chatroomService.queryAllChatroomUserByUserId(chatroomPageBean, userId);
//        List<CurrentUser> _users = new ArrayList<>();
//        for (Letter chatroom: chatrooms) {
//            if (chatroom.getUserId1() == userId) {
//                CurrentUser c = new CurrentUser();
//                User user = userService.queryUserByUserId(chatroom.getUserId2());
//                c.setId(user.getId());
//                c.setUsername(user.getUsername());
//                c.setMail(user.getMail());
//                _users.add(c);
//            }else if(chatroom.getUserId2()==userId){
//                CurrentUser c = new CurrentUser();
//                User user = userService.queryUserByUserId(chatroom.getUserId1());
//                c.setId(user.getId());
//                c.setUsername(user.getUsername());
//                c.setMail(user.getMail());
//                _users.add(c);
//            }
//        }
//        chatroomPageBean.setList(_users);
//        ResultInfo<PageBean> rs = new ResultInfo<PageBean>();
//        rs.setFlag(true);
//        rs.setRes(chatroomPageBean);
//        return rs;
//    }
//    //删除一个云聊
//    @RequestMapping(path="/deleteChatroomByUserId1AndUserId2")
//    public @ResponseBody ResultInfo deleteChatroomByUserId1AndUserId2(@RequestBody Letter chatroom){
//        ResultInfo rs=new ResultInfo();
//        boolean flag=chatroomService.deleteChatroom(12);
//        if(flag){
//            rs.setFlag(true);
//            rs.setInfo("删除成功");
//        }else{
//            rs.setFlag(false);
//            rs.setInfo("删除失败");
//        }
//        return rs;
//    }
//    //根据云聊Id获取相关所有云信
//    @RequestMapping(path="/getChatroomMsgByChatroomId")
//    public @ResponseBody ResultInfo getChatroomMsgByUserId(HttpServletRequest request){
//        int page;
//        String res = request.getParameter("page");
//        if (res == null || Integer.parseInt(res) <= 0) {
//            page = 1;
//        }else{
//            page = Integer.parseInt(res);
//        }
//        String search="";
//        String _search=request.getParameter("search");
//        if(_search!=null){
//            search=_search;
//        }
//        int chatroomId = Integer.parseInt(request.getParameter("chatroomid"));
//        PageBean<LetterMsg> chatroomMsgPageBean = new PageBean<>();
//        chatroomMsgPageBean.setTotalCount(chatroomMsgService.queryAllChatroomMsgByChatroomIdNum(chatroomId,search));
//        chatroomMsgPageBean.setCurrentPage(page);
//        chatroomMsgPageBean.setPageSize(10);
//        boolean mod = (chatroomMsgPageBean.getTotalCount() % chatroomMsgPageBean.getPageSize()) == 0;
//        chatroomMsgPageBean.setTotalPage(mod ? chatroomMsgPageBean.getTotalCount() / chatroomMsgPageBean.getPageSize() : (chatroomMsgPageBean.getTotalCount() / chatroomMsgPageBean.getPageSize() + 1));
//        List<LetterMsg> chatroomMsg = chatroomMsgService.queryAllChatroomMsgByChatroomId(chatroomMsgPageBean, chatroomId,search);
//        chatroomMsgPageBean.setList(chatroomMsg);
//        ResultInfo<PageBean> rs = new ResultInfo<PageBean>();
//        rs.setFlag(true);
//        rs.setRes(chatroomMsgPageBean);
//        return rs;
//    }
//    //添加云信通过云聊Id
//    @RequestMapping(path="/addChatroomMsg")
//    public @ResponseBody ResultInfo addChatroomMsg(@RequestBody LetterMsg chatroomMsg){
//        ResultInfo rs=new ResultInfo();
//        boolean flag=chatroomMsgService.addChatroomMsg(chatroomMsg);
//        if(flag){
//            rs.setFlag(true);
//            rs.setInfo("发送成功");
//        }else{
//            rs.setFlag(false);
//            rs.setInfo("发送失败");
//        }
//        return rs;
//    }
//    //删除一条云信
//    @RequestMapping(path="deleteChatroomMsg")
//    public @ResponseBody ResultInfo deleteChatroomMsg(HttpServletRequest request){
//        String chatroommsgid=request.getParameter("chatroommsgid");
//        ResultInfo rs=new ResultInfo();
//        if(chatroommsgid==null){
//            rs.setFlag(false);
//            rs.setInfo("参数错误");
//            return rs;
//        }
//        boolean flag=chatroomMsgService.deleteChatroomMsg(Integer.parseInt(chatroommsgid));
//        if(flag){
//            rs.setFlag(true);
//            rs.setInfo("删除成功");
//        }else{
//            rs.setFlag(false);
//            rs.setInfo("删除失败");
//        }
//        return rs;
//    }
//
//    //根据用户Id获取相关用户
//    @RequestMapping(path="/getUserByUserId")
//    public @ResponseBody ResultInfo getUserByUserId(HttpServletRequest request){
//        int userid=Integer.parseInt(request.getParameter("userid"));
//        User user = userService.queryUserByUserId(userid);
//        ResultInfo rs=new ResultInfo();
//        rs.setFlag(true);
//        rs.setRes(user);
//        return rs;
//    }
//    //根据图片Id获取相关图片路径(/images下的图片,img src='/images/...')
//    @RequestMapping(path="/getImgByImgId")
//    public @ResponseBody ResultInfo getImgByImgId(HttpServletRequest request){
//        int imgid=Integer.parseInt(request.getParameter("imgig"));
//        String img = imgService.queryImgByImgId(imgid);
//        ResultInfo rs=new ResultInfo();
//        if(img==null){
//            rs.setFlag(false);
//            rs.setRes(null);
//            return rs;
//        }
//        rs.setFlag(true);
//        rs.setRes("/images/"+img);
//        return rs;
//    }
//    //修改密码
//    @RequestMapping(path="/setPassword")
//    public @ResponseBody ResultInfo setPassworld(HttpServletRequest request){
//        String _oldpassword=request.getParameter("oldpassword");
//        String _newpassword=request.getParameter("newpassword");
//        String _adminid=request.getParameter("adminid");
//        ResultInfo rs=new ResultInfo();
//        if(_oldpassword==null || _newpassword ==null || _adminid==null){
//            rs.setFlag(false);
//            rs.setInfo("参数有误");
//            return rs;
//        }
//        int adminid=Integer.parseInt(_adminid);
//        Administrator ad=administratorService.queryAdminById(adminid);
//        if(ad==null || !ad.getPassword().equals(_oldpassword)){//需要经过md5util
//            rs.setFlag(false);
//            rs.setInfo("原密码错误");
//            return rs;
//        }
//        boolean flag=administratorService.updatePassword(adminid,_newpassword);
//        if(flag){
//            rs.setFlag(true);
//            rs.setInfo("设置密码成功");
//        }else{
//            rs.setFlag(false);
//            rs.setInfo("设置密码失败");
//        }
//        return rs;
//
//    }
}
