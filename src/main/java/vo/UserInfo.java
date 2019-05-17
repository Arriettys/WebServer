package vo;

import java.io.RandomAccessFile;

/*
 * vo value object 值对象
 * vo的每个实例用于表示一条具体的记录，不含有逻辑功能
 * 
 * UserInfo的每一个实例用于表示一个用户信息
 */
public class UserInfo
{
	private String username;
	private String password;
	private String nickname;
	
	/*
	 * 根据给定的用户名查找指定用户
	 * 没有用户时返回null
	 */
	
	public UserInfo(String name,String pwd,String nick)
	{
		username = name;
		password = pwd;
		nickname = nick;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}
	public String toString()
	{
		return username+","+password+","+nickname;
	}
}
