package g.dao;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

public class SetupDb {
	public static String getStrDriver() {
		return strDriver;
	}

	public static String getStrDburl() {
		return strDburl;
	}

	public static String getStrUsername() {
		return strUsername;
	}

	public static String getStrPassword() {
		return strPassword;
	}

	// 驱动名称
	private static String strDriver = null;

	// 数据库连接字符串
	private static String strDburl = null;
	// 数据库用户
	private static String strUsername = null;
	// 数据库密码
	private static String strPassword = null;

	static {
		Properties props = new Properties();
		try {

			InputStream in = SetupDb.class
					.getResourceAsStream("/setupdb.properties");
			props.load(in);
			strDriver = props.getProperty("driver");
			strDburl = props.getProperty("dburl");
			strUsername = props.getProperty("username");
			strPassword = props.getProperty("password");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
