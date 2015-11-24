package g.dao;

import g.bean.Stock;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommonDao {

	public List<Stock> getStocks() {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String QrySql = "select t.stockcode,t.stockname,t.jys ,v.tradedate rd,v1.bonusdate rd1 from stockinfo t left join ( select sdd.stockcode,max(sdd.tradedate) tradedate from stock_datedata sdd group by sdd.stockcode) v on t.stockcode = v.stockcode left join (select stb.stockcode, max(stb.BONUSDATE) BONUSDATE from stock_bonus stb group by stb.stockcode) v1 on t.stockcode = v1.stockcode";
		List<Stock> li = new ArrayList<Stock>();
		try {
			conn = C3p0ConnectionPool.getConnection();
			pst = conn.prepareStatement(QrySql);
			rs = pst.executeQuery();
			conn.commit();
			while (rs.next()) {
				Stock st = new Stock();
				st.setStockCode(rs.getString(1));
				st.setStockName(rs.getString(2));
				st.setJys(rs.getString(3));
				st.setRecordDate(rs.getDate(4));
				st.setRecordDate1(rs.getDate(5));
				li.add(st);
			}
		} catch (Exception ex2) {
			try {
				conn.rollback();
			} catch (Exception e) {
			}
			ex2.printStackTrace();
		} finally {
			try {
				rs.close();
				pst.close();
				C3p0ConnectionPool.closeConnection(conn);
			} catch (SQLException ex1) {
			}
		}
		return li;
	}

	public boolean insertStockBonus(Stock sto, List<String> li) {
		Connection conn = null;
		PreparedStatement pst = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String QrySql = "insert into stock_bonus(STOCKCODE,BONUSDATE,BONUS,SENDRIGHT,RIGHTOFFER,RIGHTOFFERPRI) values(?,?,?,?,?,?)";
		try {
			conn = C3p0ConnectionPool.getConnection();
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(QrySql);
			for (int i = 0; i < li.size(); i++) {
				String[] sArr = li.get(i).split(",");
				pst.setString(1, sto.getStockCode());
				Date d1 = sdf.parse(sArr[0]);
				pst.setDate(2, new java.sql.Date(d1.getTime()));
				pst.setDouble(3, Double.parseDouble(sArr[1]));
				pst.setDouble(4, Double.parseDouble(sArr[2]));
				pst.setDouble(5, Double.parseDouble(sArr[3]));
				pst.setDouble(6, Double.parseDouble(sArr[4]));
				pst.execute();
			}
		} catch (Exception ex2) {
			try {
				conn.rollback();
			} catch (Exception e) {
			}
			ex2.printStackTrace();
		} finally {
			try {
				conn.commit();
				pst.close();
				C3p0ConnectionPool.closeConnection(conn);
			} catch (SQLException ex1) {
			}
		}
		return true;
	}

	public boolean insertStockDateData(Stock sto, String li[], int idx) {
		Connection conn = null;
		PreparedStatement pst = null;
		String QrySql = "insert into stock_date_data(TID,STOCKCODE,TRADEDATE,OPRI,HPRI,LPRI,CPRI,VOLUMN) values(ids_seq.nextval,?,to_date(?,'yyyy-mm-dd'),?,?,?,?,?)";
		try {
			conn = C3p0ConnectionPool.getConnection();
			pst = conn.prepareStatement(QrySql);
			for (int i = 1; i < idx; i++) {
				String ss[] = li[i].split(",");
				pst.setString(1, sto.getStockCode());
				pst.setString(2, ss[0]);
				pst.setDouble(3, Double.parseDouble(ss[1]));
				pst.setDouble(4, Double.parseDouble(ss[2]));
				pst.setDouble(5, Double.parseDouble(ss[3]));
				pst.setDouble(6, Double.parseDouble(ss[4]));
				pst.setLong(7, Long.parseLong(ss[5]));
				pst.execute();
			}
			conn.commit();
		} catch (Exception ex2) {
			try {
				conn.rollback();
			} catch (Exception e) {
			}
			ex2.printStackTrace();
		} finally {
			try {
				pst.close();
				C3p0ConnectionPool.closeConnection(conn);
			} catch (SQLException ex1) {
			}
		}
		return true;
	}

	public boolean insertStockDateData2(Stock sto, List<String> li) {
		Connection conn = null;
		PreparedStatement pst = null;
		String QrySql = "insert into stock_date_data(TID,STOCKCODE,TRADEDATE,OPRI,HPRI,LPRI,CPRI,VOLUMN) values(ids_seq.nextval,?,to_date(?,'yyyy-mm-dd'),?,?,?,?,?)";
		try {
			conn = C3p0ConnectionPool.getConnection();
			pst = conn.prepareStatement(QrySql);
			for (int i = 0; i < li.size(); i++) {
				String ss[] = li.get(i).split(",");
				pst.setString(1, sto.getStockCode());
				pst.setString(2, ss[0]);
				pst.setDouble(3, Double.parseDouble(ss[1]));
				pst.setDouble(4, Double.parseDouble(ss[6]));
				pst.setDouble(5, Double.parseDouble(ss[5]));
				pst.setDouble(6, Double.parseDouble(ss[2]));
				pst.setLong(7, Long.parseLong(ss[7]));
				pst.execute();
			}
			conn.commit();
		} catch (Exception ex2) {
			try {
				conn.rollback();
			} catch (Exception e) {
			}
			ex2.printStackTrace();
		} finally {
			try {
				pst.close();
				C3p0ConnectionPool.closeConnection(conn);
			} catch (SQLException ex1) {
			}
		}
		return true;
	}

	public boolean insertStockDateData3(Stock sto, List<String> li) {
		Connection conn = null;
		PreparedStatement pst = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String QrySql = "insert into stock_datedata(STOCKCODE,TRADEDATE,OPRI,HPRI,LPRI,CPRI,VOLUMN,TURNOVER) values(?,?,?,?,?,?,?,?)";
		try {
			conn = C3p0ConnectionPool.getConnection();
			pst = conn.prepareStatement(QrySql);
			for (int i = 0; i < li.size(); i++) {
				String ss[] = li.get(i).split(",");
				pst.setString(1, sto.getStockCode());
				// DATE
				Date d1 = sdf.parse(ss[0]);
				pst.setDate(2, new java.sql.Date(d1.getTime()));
				// O
				pst.setDouble(3, Double.parseDouble(ss[3]));
				// H
				pst.setDouble(4, Double.parseDouble(ss[4]));
				// L
				pst.setDouble(5, Double.parseDouble(ss[5]));
				// C
				pst.setDouble(6, Double.parseDouble(ss[6]));
				// V
				pst.setDouble(7, Double.parseDouble(ss[7]));
				pst.setDouble(8, Double.parseDouble(ss[8]));
				pst.execute();
			}
			conn.commit();
		} catch (Exception ex2) {
			try {
				conn.rollback();
			} catch (Exception e) {
			}
			ex2.printStackTrace();
		} finally {
			try {
				pst.close();
				C3p0ConnectionPool.closeConnection(conn);
			} catch (SQLException ex1) {
			}
		}
		return true;
	}

	public boolean setAllDzw4Pre() {
		Connection conn = null;
		try {
			conn = C3p0ConnectionPool.getConnection();
			CallableStatement proc = null;
			proc = conn.prepareCall("{ call setalldzw4pre() }");
			proc.execute();
			conn.commit();
		} catch (Exception ex2) {
			try {
				conn.rollback();
			} catch (Exception e) {
			}
			ex2.printStackTrace();
		} finally {
			try {
				C3p0ConnectionPool.closeConnection(conn);
			} catch (Exception e) {
			}
		}
		return true;
	}

	public boolean cpyDat2Pre() {
		Connection conn = null;
		try {
			conn = C3p0ConnectionPool.getConnection();
			CallableStatement proc = null;
			proc = conn.prepareCall("{ call cpydat2pre() }");
			proc.execute();
			conn.commit();
		} catch (Exception ex2) {
			try {
				conn.rollback();
			} catch (Exception e) {
			}
			ex2.printStackTrace();
		} finally {
			try {
				C3p0ConnectionPool.closeConnection(conn);
			} catch (Exception e) {
			}
		}
		return true;
	}
	
	
	public boolean preFixBonus() {
		Connection conn = null;
		try {
			conn = C3p0ConnectionPool.getConnection();
			CallableStatement proc = null;
			proc = conn.prepareCall("{ call prefixbonus() }");
			proc.execute();
			conn.commit();
		} catch (Exception ex2) {
			try {
				conn.rollback();
			} catch (Exception e) {
			}
			ex2.printStackTrace();
		} finally {
			try {
				C3p0ConnectionPool.closeConnection(conn);
			} catch (Exception e) {
			}
		}
		return true;
	}
}
