
package dao;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import vo.UserInfo;

public class UserDAO
{
	/*
	 * DAO Data Access Object 数据连接对象
	 * 
	 * UserDAO的职责是对用户数据进行存取工作，配合业务逻辑类进行数据操作
	 * 增删改查
	 */
	public UserInfo findUserByUserName(String username)
	{
		try
		{
			RandomAccessFile raf = new RandomAccessFile("user.dat","rw");
			for(int i=0;i<raf.length()/90;i++)
			{
				raf.seek(i*90);
				String name = readString(raf,30);
				if(name.equals(username))
				{
					String password = readString(raf,30);
					String nickname = readString(raf,30);
					UserInfo userInfo = new UserInfo(name,password,nickname);
					return userInfo;
				}
			}
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public String readString(RandomAccessFile raf,int len)
	{
		try
		{
			byte[] data = new byte[len];
			raf.read(data);
			String str = new String(data,"UTF-8").trim();
			return str;
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * 将给定的UserInfo对象表示的用户信息保存
	 */
	public boolean save(UserInfo userinfo)
	{
		try
		{
			RandomAccessFile raf = new RandomAccessFile("user.dat","rw");
			//将指针移动到文件末尾
			raf.seek(raf.length());
			writeString(raf,userinfo.getUsername(),30);
			writeString(raf,userinfo.getPassword(),30);
			writeString(raf,userinfo.getNickname(),30);
			return true;
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	private void writeString(RandomAccessFile raf,String str,int len)
	{
		try
		{
			byte[] data = str.getBytes();
			data = Arrays.copyOf(data,len);
			raf.write(data);
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	/*
	 * 查询所有用户信息
	 */
	public List<UserInfo> fIndALL()
	{
		List<UserInfo> list = new ArrayList<UserInfo>();
		try
		{
			RandomAccessFile raf = new RandomAccessFile("user.dat","rw");
			for(int i=0;i<raf.length()/90;i++)
			{
				raf.seek(i*90);
				String name = readString(raf,30);
				String pwd = readString(raf,30);
				String nick = readString(raf,30);
				list.add(new UserInfo(name,pwd,nick));
			}
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		return list;
	}
	
	public static void main(String[] args)
	{
		UserDAO dao = new UserDAO();
		UserInfo user = dao.findUserByUserName("wuzhen");
		System.out.println(user);
	}
}