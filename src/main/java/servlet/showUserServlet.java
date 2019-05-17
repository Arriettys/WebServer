package servlet;

import java.util.List;

import com.arrietty.webserver.http.HttpRequest;
import com.arrietty.webserver.http.HttpResponse;

import dao.UserDAO;
import vo.UserInfo;

/*
 * 显示用户信息
 */
public class showUserServlet extends HttpServlet
{
	public void service(HttpRequest request,HttpResponse response)
	{
		try
		{
			System.out.println("显示用户信息");
			/*
			 * 1.读取出所有用户信息
			 * 2.将用户信息拼接一个HTML页面代码
			 * 3.将拼好的数据转换为字节
			 * 4.设置response响应客户端
			 */
			UserDAO dao = new UserDAO();
			List<UserInfo> list = dao.fIndALL();
			
			StringBuilder builder = new StringBuilder();
			builder.append("<html>");
			builder.append("<head>");
			builder.append("<meta charset='UTF-8'>");
			builder.append("<title>用户列表</title>");
			builder.append("</head>");
			builder.append("<body>");
			builder.append("<h1 align='center'>用户信息</h1>");
			builder.append("<center>");
			builder.append("<table border='1' width='100%'>");
			builder.append("<tr><td>用户名</td><td>密码</td><td>昵称</td></tr>");
			for(UserInfo e : list)
			{
				builder.append("<tr><td>"+e.getUsername()+"</td><td>"+e.getPassword()+"</td><td>"+e.getNickname()+"</td></tr>");
			}
			builder.append("</table>");
			builder.append("</center>");
			builder.append("</body>");
			builder.append("</html>");
			byte[] data = builder.toString().getBytes("UTF-8");
			
			//响应客户端
			response.setContentType("text/html");
			response.setContentLength(data.length);
			response.setContext(data);
			response.flush();
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
