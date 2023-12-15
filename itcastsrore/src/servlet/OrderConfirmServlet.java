package servlet;

import com.sun.mail.util.MailSSLSocketFactory;
import model.*;
import org.apache.commons.beanutils.BeanUtils;
import service.OrderService;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;


@WebServlet(name = "order_confirm",urlPatterns = "/order_confirm")
public class OrderConfirmServlet extends HttpServlet {
    private OrderService oService = new OrderService();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Order o = (Order) request.getSession().getAttribute("order");
        try {
            BeanUtils.copyProperties(o, request.getParameterMap());
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        o.setDatetime(new Date());
        o.setStatus(2);
        o.setUser((User) request.getSession().getAttribute("user"));
        oService.addOrder(o);
        request.getSession().removeAttribute("order");

        //创建一个配置文件并保存
        Properties properties = new Properties();
        properties.setProperty("mail.host", "smtp.qq.com");
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.port", "465");
        properties.setProperty("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");

        //QQ存在一个特性设置SSL加密
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);

        //创建一个session对象
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("sea_sand64@qq.com", "myaeamfhrndfdjcb");
            }
        });

        session.setDebug(true);//开启debug模式
        Transport transport = null;//获取连接对象
        try {
            transport = session.getTransport();

        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
        try {
            transport.connect("smtp.qq.com", "sea_sand64@qq.com", "myaeamfhrndfdjcb");//连接服务器
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        MimeMessage mimeMessage = new MimeMessage(session);//创建邮件对象
        try {
            mimeMessage.setFrom(new InternetAddress("sea_sand64@qq.com"));//邮件发送人
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        try {
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(o.getUser().getEmail()));//邮件接收人
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        try {
            mimeMessage.setSubject("感谢您的购买！");//邮件标题
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        try {
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date = new Date(System.currentTimeMillis());

            String m1 = o.getUser().getName() + "，您好！<br>";
            String m2 = "以下是您的订单信息：<br>";
            String m3 = "支付时间："+ formatter.format(date) + "<br>";
            String m4 = "支付金额："+ o.getTotal() + "¥<br>";
            String m5 = "订单已成功支付，我们会尽快为您发货，谢谢！<br>";
            mimeMessage.setContent(m1 + m2 + m3 + m4 + m5, "text/html;charset=UTF-8");//邮件内容
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        try {
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());//发送邮件
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        try {
            transport.close();//关闭连接
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }


        request.setAttribute("msg", "请查收邮件  订单支付成功！");
        request.getRequestDispatcher("/order_success.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
