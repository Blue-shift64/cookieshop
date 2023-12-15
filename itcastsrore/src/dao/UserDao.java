package dao;

import model.User;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import utils.DBUtils;

import java.sql.SQLException;
import java.util.List;

public class UserDao {
    //add方法
    public void addUser(User user) throws SQLException
    {
        //拿到执行者对象
        QueryRunner r =new QueryRunner(DBUtils.getDataSource());
        //sql
        String sql = "insert into user(username,email,password,name,phone,address,isadmin,isvalidate) values(?,?,?,?,?,?,?,?)";
        //执行
        r.update(sql,user.getUsername(),user.getEmail(),user.getPassword(),user.getName(),user.getPhone(),user.getAddress(),user.isIsadmin(),user.isIsvalidate());
    }
    //判断用户是否存在
    public boolean isUsernameExit(String username) throws SQLException
    {
        QueryRunner r =new  QueryRunner(DBUtils.getDataSource());
        String sql = "select * from user where username = ?";
        User u = r.query(sql, new BeanHandler<User>(User.class),username);
        if(u == null) {
            return false;//用户不存在 可以注册
        }else {
            return true;//用户存在 不能注册
        }
    }
    //判断邮箱是否存在
    public boolean isEmailExist(String email) throws SQLException
    {
        QueryRunner r = new QueryRunner(DBUtils.getDataSource());
        String sql = "select * from user where email = ?";
        User u = r.query(sql, new BeanHandler<User>(User.class),email);
        if(u == null) {
            return false;
        }else {
            return true;
        }
    }
    //用户名密码登录
    public User selectByUsernamePassword(String username,String password) throws SQLException
    {
        QueryRunner r = new QueryRunner(DBUtils.getDataSource());
        String sql = "select * from user where username=? and password=?";
        return r.query(sql, new BeanHandler<User>(User.class),username,password);
    }
    //邮箱密码登录
    public User selectByEmailPassword(String email,String password) throws SQLException {
        QueryRunner r = new QueryRunner(DBUtils.getDataSource());
        String sql = "select * from user where email=? and password=?";
        return r.query(sql, new BeanHandler<User>(User.class),email,password);
    }
    public User selectById(int id) throws SQLException {
        QueryRunner r = new QueryRunner(DBUtils.getDataSource());
        String sql = "select * from user where id=?";
        return r.query(sql, new BeanHandler<User>(User.class),id);
    }

    public void updateUserAddress(User user) throws SQLException {
        QueryRunner r = new QueryRunner(DBUtils.getDataSource());
        String sql ="update user set name = ?,phone=?,address=? where id = ?";
        r.update(sql,user.getName(),user.getPhone(),user.getAddress(),user.getId());
    }
    public void updateUserEmail(User user) throws SQLException {
        QueryRunner r = new QueryRunner(DBUtils.getDataSource());
        String sql ="update user set name = ?,phone=?,email=? where id = ?";
        r.update(sql,user.getName(),user.getPhone(),user.getEmail(),user.getId());
    }
    public void updatePwd(User user) throws SQLException {
        QueryRunner r = new QueryRunner(DBUtils.getDataSource());
        String sql ="update user set password = ? where id = ?";
        r.update(sql,user.getPassword(),user.getId());
    }
    public int selectUserCount() throws SQLException {
        QueryRunner r = new QueryRunner(DBUtils.getDataSource());
        String sql = "select count(*) from user";
        return r.query(sql, new ScalarHandler<Long>()).intValue();
    }
    public List selectUserList(int pageNo, int pageSize) throws SQLException {
        QueryRunner r = new QueryRunner(DBUtils.getDataSource());
        String sql = "select * from user limit ?,?";
        return r.query(sql, new BeanListHandler<User>(User.class), (pageNo-1)*pageSize,pageSize );
    }
    public void delete(int id) throws SQLException {
        QueryRunner r = new QueryRunner(DBUtils.getDataSource());
        String sql = "delete from user where id = ?";
        r.update(sql,id);
    }
}
