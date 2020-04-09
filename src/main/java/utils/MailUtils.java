//package utils;
//
//import javax.mail.*;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//import java.util.Properties;
//
///**
// * 发邮件工具类
// */
//public final class MailUtils {
//    private static final String USER = "1306104920@qq.com"; // 发件人称号，同邮箱地址
//    private static final String PASSWORD = "arbvosrcvdtvhieb"; // 如果是qq邮箱可以使户端授权码，或者登录密码
//
//    /**
//     *
//     * @param to 收件人邮箱
//     * @param text 邮件正文
//     * @param title 标题
//     */
//    /* 发送验证信息的邮件 */
//    public static boolean sendMail(String to, String text, String title){
//        try {
//            final Properties props = new Properties();
//            props.put("mail.smtp.auth", "true");
//            props.put("mail.smtp.host", "smtp.qq.com");
//
//            // 发件人的账号
//            props.put("mail.user", USER);
//            //发件人的密码
//            props.put("mail.password", PASSWORD);
//
//            // 构建授权信息，用于进行SMTP进行身份验证
//            Authenticator authenticator = new Authenticator() {
//                @Override
//                protected PasswordAuthentication getPasswordAuthentication() {
//                    // 用户名、密码
//                    String userName = props.getProperty("mail.user");
//                    String password = props.getProperty("mail.password");
//                    return new PasswordAuthentication(userName, password);
//                }
//            };
//            // 使用环境属性和授权信息，创建邮件会话
//            Session mailSession = Session.getInstance(props, authenticator);
//            // 创建邮件消息
//            MimeMessage message = new MimeMessage(mailSession);
//            // 设置发件人
//            String username = props.getProperty("mail.user");
//            InternetAddress form = new InternetAddress(username);
//            message.setFrom(form);
//
//            // 设置收件人
//            InternetAddress toAddress = new InternetAddress(to);
//            message.setRecipient(Message.RecipientType.TO, toAddress);
//
//            // 设置邮件标题
//            message.setSubject(title);
//
//            // 设置邮件的内容体
//            message.setContent(text, "text/html;charset=UTF-8");
//            // 发送邮件
//            Transport.send(message);
//            return true;
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//
//
//
//}
package utils;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class MailUtils
{
    private static final String USER = "1306104920@qq.com"; // 发件人称号，同邮箱地址
    private static final String PASSWORD = "arbvosrcvdtvhieb"; // 如果是qq邮箱可以使户端授权码，或者登录密码
    public static void sendMail(String to, String text, String title)
    {
        // 获取系统属性
        final Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.qq.com");
        // 发件人的账号
        props.put("mail.user", USER);
        //发件人的密码
        props.put("mail.password", PASSWORD);
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };


        // 获取默认的 Session 对象。
        Session session = Session.getInstance(props,authenticator);

        try{
            // 创建默认的 MimeMessage 对象。
            MimeMessage message = new MimeMessage(session);

            // Set From: 头部头字段
            message.setFrom(new InternetAddress(USER));

            // Set To: 头部头字段
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));

            // Set Subject: 头字段
            message.setSubject(title);

            // 发送 HTML 消息, 可以插入html标签
            message.setContent(text,"text/html;charset=UTF-8");

            // 发送消息
            Transport.send(message);
        }catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}