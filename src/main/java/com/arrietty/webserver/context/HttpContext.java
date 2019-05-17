package com.arrietty.webserver.context;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP协议相关定义
 * @author adminitartor
 *
 */
public class HttpContext {
	/**
	 * 回车符
	 */
	public static final int CR = 13;
	/**
	 * 换行符
	 */
	public static final int LF = 10;
	/*
	 * 头信息
	 */
	/*
	 * header:分隔符":"
	 */
	public static final String HEADER_SEPARATOR = ":";
	/*
	 * header:Content-Type
	 */
	public static final String HEADER_CINTENT_TYPE = "Content-Type";
	/*
	 * header:Content-length
	 */
	public static final String HEADER_CONTENT_LENGTH = "Content-length";
	
	/*
	 * 介质类型映射
	 * 资源后缀与Content-Type值得对应关系
	 */
	private static final Map<String,String> mimeMapping = new HashMap<String,String>();
	public static final Map<Integer,String> status_reason = new HashMap<Integer,String>();
	static
	{
		//初始化介质类型映射
		initMimeMapping();
		//初始化状态代码与原因的映射
		initStatusReasonMapping();
	}
	
	/*
	 * 状态代码定义
	 */
	public static final int STATUS_CODE_OK = 200;
	public static final int STATUS_CODE_REDIRECT = 302;
	public static final int STATUS_CODE_NOT_FOUND = 404;
	public static final int STATUS_CODE_ERROR = 500;
	/*
	 * 状态代码与描述的映射
	 */
	
	
	/*
	 * 初始化介质映射
	 */
	private static void initMimeMapping()
	{
		mimeMapping.put("html","text/html");
		mimeMapping.put("jpg","image/jpg");
		mimeMapping.put("png","image/png");
		mimeMapping.put("gif","image/gif");
		mimeMapping.put("css","text/css");
		mimeMapping.put("js","application/javascript");
	}
	
	/*
	 * 初始化状态码和原因的映射
	 */
	private static void initStatusReasonMapping()
	{
		status_reason.put(STATUS_CODE_OK,"ok");
		status_reason.put(STATUS_CODE_REDIRECT,"Found");
		status_reason.put(STATUS_CODE_NOT_FOUND,"Not Found");
		status_reason.put(STATUS_CODE_ERROR,"Internal Server Error");
	}
	
	public static String getMimeType(String ex)
	{
		return mimeMapping.get(ex);
	}
	
	/*
	 * 根据给定的状态代码获取对应的状态描述
	 */
	
	public static String getStatusReason(int code)
	{
		return status_reason.get(code);
	}
	public static void main(String[] args)
	{
		String type = getStatusReason(404);
		System.out.println(type);
	}
}





