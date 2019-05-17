package com.arrietty.webserver.context;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/*
 * 服务器相关配置信息
 */

public class ServletContext
{
	/*
	 * Servlet映射
	 * key:请求url
	 * value:处理该请求的Servlet名字
	 */
	private static Map<String,String> servletMapping = new HashMap<String,String>();
	static
	{
		initServletMapping();
	}
	private static void initServletMapping()
	{
		try
		{
			SAXReader reader = new SAXReader();
			Document doc = reader.read(new FileInputStream("conf/servlets.xml"));
			Element root = doc.getRootElement();
			List<Element> list = root.elements();
			for(Element e : list)
			{
				servletMapping.put(e.attribute("url").getValue(),e.attribute("class").getValue());
			}
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/*
	 * 根据给定的url获取对应的Servlet名字
	 */
	public static String getServletNameByUrl(String url)
	{
		return servletMapping.get(url);
	}
	public static void main(String[] args)
	{
		String name = ServletContext.getServletNameByUrl("/myweb/reg");
		System.out.println(name);
	}
}
