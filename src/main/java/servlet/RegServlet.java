package servlet;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;

import com.arrietty.webserver.context.HttpContext;
import com.arrietty.webserver.http.HttpRequest;
import com.arrietty.webserver.http.HttpResponse;

import dao.UserDAO;
import vo.UserInfo;

/*
 * 处理用户注册逻辑
 */
public class RegServlet extends HttpServlet
{
	public void service(HttpRequest request,HttpResponse response)
	{
		System.out.println("开始处理注册业务！");
		
		UserDAO dao = new UserDAO();
		
		//首先获取用户的注册信息
		//获取用户名
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String nickname = request.getParameter("nickname");
		System.out.println(username+":"+password+":"+nickname);
		
		//将该用户信息保存到文件中
		/*
		 * 数据保存在user.dat文件中
		 * 该文件每条记录定为90个字节
		 * 其中用户名，密码，昵称各占30个字节
		 */
		try
		{
			/*
			 * 首先判断该用户是否已经存在
			 * 存在则直接跳转错误页面：reg_fail.html
			 * 告知注册失败
			 * 否则才进行注册工作
			 * 首先读取user.dat文件中每条记录中的用户名
			 * 然后查看是否有重复。每条记录中前30个字节就是该用户的名字，读取出来时要去除空白字符
			 */
	
		if(dao.findUserByUserName(username)==null)
		{
			UserInfo userinfo = new UserInfo(username,password,nickname);
			dao.save(userinfo);
			//跳转注册成功页面
			//forward("webapps/myweb/reg_success.html",request,response);
			response.sendRediect("reg_success.html");
			//重定向另一个Servlet的功能
			//response.sendRediect("showUser");
		} else
		{
			forward("webapps/myweb/reg_fail.html",request,response);
		}
			
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		
		System.out.println("注册业务处理完毕！");
	}
	/*
	 * 跳转指定路径
	 */
}
