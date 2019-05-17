package com.arrietty.webserver;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

import com.arrietty.webserver.context.HttpContext;
import com.arrietty.webserver.context.ServletContext;
import com.arrietty.webserver.http.HttpRequest;
import com.arrietty.webserver.http.HttpResponse;

import servlet.HttpServlet;
import servlet.LoginServlet;
import servlet.RegServlet;

/**
 * 处理客户端请求的处理类
 * @author adminitartor
 *
 */
public class ClientHandler implements Runnable
{
	private Socket socket;
	public ClientHandler(Socket socket)
	{
		this.socket = socket;
	}
	public void run()
	{
		try 
		{
			//解析请求
			HttpRequest request = new HttpRequest(socket);
			//生成响应对象
			HttpResponse response= new HttpResponse(socket);
		
			//获取请求路径
			String requestURI = request.getRequestURI();
			String servletName = ServletContext.getServletNameByUrl(requestURI);
			if(servletName!=null)
			{
				//请求注册业务
				Class cls = Class.forName(servletName);
				HttpServlet servlet = (HttpServlet)cls.newInstance();
				servlet.service(request,response);
			} else
			{
				File file = new File("webapps"+requestURI);
				if(file.exists())
				{
					System.out.println("webapps"+requestURI+":该文件存在!");
					//回复客户端(发送HTTP响应)
					//设置响应头
					//先截取资源文件后缀名
					String ex = file.getName().substring(file.getName().lastIndexOf(".")+1);
					//设置状态代码:200
					response.setStatusCode(HttpContext.STATUS_CODE_OK);
					//根据资源后缀的名字获取对应的ContentType的值并设置响应头
					response.setContentType(HttpContext.getMimeType(ex));
					response.setContentLength((int)file.length());
					response.setEntity(file);
					response.flush();
				}else
				{
					System.out.println("webapps"+requestURI+":该文件不存在!");
				}	
			}
		} catch (Exception e) 
		{
			e.printStackTrace();
		} finally
		{
			try 
			{
				/*
				 * Socket关闭后，通过它获取的输入流与
				 * 输出流就关闭了。
				 */
				socket.close();
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}	
}




