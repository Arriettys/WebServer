package servlet;

import java.io.File;

import com.arrietty.webserver.context.HttpContext;
import com.arrietty.webserver.http.HttpRequest;
import com.arrietty.webserver.http.HttpResponse;

import dao.UserDAO;
import vo.UserInfo;

public class LoginServlet extends HttpServlet
{
	public void service(HttpRequest request,HttpResponse response)
	{
		System.out.println("开始处理登录业务");
		/*
		 * 登录业务
		 * 1.通过request获取用户输入的登录信息
		 * 2.实例化UserDAO
		 * 3.根据用户输入的用户名通过DAO查询该用户信息，若没有找到则直接跳转登录错误的页面
		 * 4.根据找到的用户信息匹配用户输入的密码是否正确，正确则跳转登录成功页面，否则跳转登录错误页面
		 */
		
		UserDAO dao= new UserDAO(); 
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		UserInfo userInfo;
		
		if((userInfo=dao.findUserByUserName(username))!=null)
		{
			if(password.equals(userInfo.getPassword()))
			{
				forward("webapps/myweb/login_success.html",request,response);
			}
		}
		forward("webapps/myweb/login_fail.html",request,response);
		System.out.println("登录完毕");
	}
}
