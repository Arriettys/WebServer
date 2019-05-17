package servlet;

import java.io.File;

import com.arrietty.webserver.context.HttpContext;
import com.arrietty.webserver.http.HttpRequest;
import com.arrietty.webserver.http.HttpResponse;

public abstract class HttpServlet
{
	/*
	 * HttpServlet是所有Servlet的超类，规定了所有servlet都应具有的功能
	 */
	
	public abstract void service(HttpRequest request,HttpResponse respon);
	protected void forward(String url,HttpRequest request,HttpResponse response)
	{
		File file = new File(url);
		response.setStatusCode(HttpContext.STATUS_CODE_OK);
		response.setContentType(HttpContext.getMimeType("html"));
		response.setContentLength(file.length());
		response.setEntity(file);
		response.flush();
	}
}
