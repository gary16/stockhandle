package g.dao;

import java.sql.Connection;

import javax.sql.DataSource;
import com.mchange.v2.c3p0.DataSources;

public class C3p0ConnectionPool {

	private static DataSource pooled;

	static {
		try {
			Class.forName(SetupDb.getStrDriver());
			DataSource unpooled = DataSources.unpooledDataSource(
					SetupDb.getStrDburl(), SetupDb.getStrUsername(),
					SetupDb.getStrPassword());
			pooled = DataSources.pooledDataSource(unpooled);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		if (pooled != null) {
			try {
				return pooled.getConnection();
			} catch (Exception er) {
				er.printStackTrace();
				return null;
			}
		} else
			return null;
	}

	public static void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception er) {
				er.printStackTrace();
			}
		}
	}

}
