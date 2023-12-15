package servlet;

import model.Goods;
import model.Order;
import service.GoodsService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "goods_buy",urlPatterns = "/goods_buy")
public class GoodsBuyServlet extends HttpServlet {
    private GoodsService gService = new GoodsService();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //声明购物车
        Order o = null;
        if(request.getSession().getAttribute("order") != null) {
            o = (Order) request.getSession().getAttribute("order");//有购物车 使用
        }else {
            o = new Order();
            request.getSession().setAttribute("order", o);//没有购物车 创建
        }
        //获取商品id
        int goodsid = Integer.parseInt(request.getParameter("goodsid"));
        Goods goods = gService.getGoodsById(goodsid);
        //加入购物车
        if(goods.getStock()>0) {
            o.addGoods(goods);
            response.getWriter().print("ok");
        }else {
            response.getWriter().print("fail");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
