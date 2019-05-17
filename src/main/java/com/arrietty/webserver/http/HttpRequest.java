package com.arrietty.webserver.http;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.arrietty.webserver.context.HttpContext;

/**
 * 该类的每个实例用于表示一个HTTP请求内容
 * @author adminitartor
 *
 */
public class HttpRequest 
{
	//通过Socket获取输入流从而读取客户端的请求内容
	private Socket socket;
	
	//请求行相关信息
	//请求方式
	private String method;
	//请求资源路径
	private String url;
	//url中的请求部分
	private String requestURI;
	//url中的参数部分
	private String queryString;
	//参数对应部分
	private Map<String,String> parameters = new HashMap<String,String>();
	//请求使用的协议
	private String protocol;
	/**
	 * 实例化HttpRequest并通过给定的Socket解析请求
	 * @param socket
	 */
	private Map<String ,String> headers = new HashMap<String,String>();
	
	public HttpRequest(Socket socket)throws Exception
	{
		//System.out.println("初始化HttpRequest...");
		this.socket = socket;
		InputStream in = socket.getInputStream();
		//读请求行
		parseRequestLine(in);
		//读消息头
		parseHeaders(in);
		//读消息正文	
		//System.out.println("初始化HttpRequest完毕!");
	}
	/**
	 * 解析请求行
	 * @param in
	 */
	private void parseRequestLine(InputStream in)
	{
		//System.out.println("开始解析请求行...");
		String line = readLine(in);
		//System.out.println("请求行内容:"+line);
		
		/*
		 * GET /index.html HTTP/1.1
		 * 将请求行中的三部分内容分别设置到
		 * method,url,protocol属性上。
		 * 设置完毕后打桩查看,例如:
		 * method:GET
		 * url:/index.html
		 * protocol:HTTP/1.1
		 */
		//按照空格拆分
		String[] data = line.split("\\s");
		//System.out.println("len:"+data.length);
		
		this.method = data[0];
		this.url = data[1];
		this.protocol = data[2];
		//System.out.println("method:"+method);
		//System.out.println("url:"+url);
		//System.out.println("protocol:"+protocol);
		
		//进一步解析URL
		parseURL(url);
		
		//System.out.println("解析请求行完毕!");
	}
	
	/*
	 * 解析请求行中的URL部分
	 */
	private void parseURL(String url)
	{
		System.out.println("开始解析URL");
		/*
		 * 在Http协议中要求，地址栏中的内容只能是ISO8859-1中允许的字符，这个编码集是欧洲编码集，不含有中文这样的字符。所以所有这类字符都需要经过处理才可以传递过来
		 * 例如传递中文:姬野
		 * 那么浏览器会先将该字符以UTF-8的形式转换为对应的字节，然后将每个字节以16进制的形式用字符串表达，而前面使用%表示
		 * 例如：姬野会被转化为6个字节，其内容为：%E8%8C%83%E4%A5%87
		 */
		
		/*
		 * URLDecoder类用于对URL进行解码，其提供了一个静态方法decode方法可以将含有上述%XX这样的字符按照给定的字符集转换为实际字符
		 */
		try
		{
			url = URLDecoder.decode(url,"UTF-8");
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		/*
		 * 请求行中URL部分可能的两种情况
		 * 1./index.html
		 * 不含传参的形式
		 * 2./myweb/reg?name=...
		 * 含有传递的参数
		 * HTTP协议规定地址栏传参的格式以"?"分割请求与参数。为参数部分则为:参数名=参数值得形式
		 * 并且每个参数之间以"&"分割
		 * 
		 * 处理URL的方式
		 * 如果不含有参数，则直接将URL属性值内容赋值给属性requestURI
		 * 若含有参数则应当先按照?拆分url，将请求部分赋值给requestURI，将参数部分赋值给queryString
		 * 然后再将参数部分进行拆分，将每个参数都存入到属性parameters这个Map中，其中:key为参数的名字，value为参数的值
		 */
		if(url.contains("?"))
		{
			String[] data = url.split("\\?");
			requestURI = data[0];
			queryString = data[1];
			String[] name = queryString.split("&");
			for(String e : name)
			{
				String[] index = e.split("=");
				parameters.put(index[0],index[1]);
			}
		} else
		{
			requestURI = url;
			System.out.println("requsetURI:"+requestURI);
		}
		for(Entry<String,String> e : parameters.entrySet())
		{
			System.out.println("K:"+e.getKey()+"  "+"V:"+e.getValue());
		}
	}
	
	/*
	 * 解析消息头
	 */
	private void parseHeaders(InputStream in)
	{
		//System.out.println("开始解析消息头...");
		/*
		 * 解析思路：
		 * 循环读取一行内容(readLine方法)
		 * 然后按照":"截取出两项，左边一项应当是消息头的名字
		 * 右边应当是消息头的值。然后将他们存入属性header这个Map中保存
		 * 当读取一行内容返回的字符串是空字符串时说明单独读取到了一个CRLF，这就说明消息头都读取完毕了
		 * 那么应当停止循环读取工作
		 */
		String line;
		while(!"".equals((line=readLine(in))))
		{
			String[] data = line.split(":\\s");
			headers.put(data[0],data[data.length-1]);
			//System.out.println("len:"+data.length);
			//System.out.println(line);
			//for(Entry<String,String> e : headers.entrySet()) //System.out.println("K:"+e.getKey()+"V:"+e.getValue());
		}
		//System.out.println("消息解析完毕！！");
	}
	
	/**
	 * 读取一行字符串(以CRLF结尾)
	 * 从给定的流中读取若干字符，直到连续读取CRLF
	 * 然后将这些字符以字符串形式返回。
	 * @param in
	 * @return
	 */
	private String readLine(InputStream in)
	{
		StringBuilder builder = new StringBuilder();
		try 
		{
			char c1= 'a';//上次读取到的字符
			char c2= 'a';//本次读取到的字符
			int d = -1;
			while((d = in.read())!=-1)
			{
				c2 = (char)d;
				//若上次读取CR本次读取LF则停止读取
				if(c1==HttpContext.CR&&c2==HttpContext.LF)
				{
					break;
				}
				builder.append(c2);
				c1 = c2;
			}
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		//trim的目的是将字符串最后的CR去除
		return builder.toString().trim();
	}
	public String getMethod() 
	{
		return method;
	}
	public String getUrl() 
	{
		return url;
	}
	public String getProtocol() 
	{
		return protocol;
	}
	public String getRequestURI()
	{
		return requestURI;
	}
	public String getQueryString()
	{
		return queryString;
	}
	/*
	 * 根据给定的参数名获取对应的参数值
	 */
	public String getParameter(String name)
	{
		return parameters.get(name);
	}
}











