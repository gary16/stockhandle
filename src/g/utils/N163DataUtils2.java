package g.utils;

import g.bean.Stock;
import g.dao.CommonDao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class N163DataUtils2 implements StockDataInterface {
	private static final String N163URL1 = "http://quotes.money.163.com/trade/lsjysj_";
	private static int currUrl = 1;

	private String assembleUrl(Stock so, int yr, int ses) {
		String bUrl = null;
		if (currUrl == 1) {
			bUrl = N163URL1;
		}
		System.out.println(so.getStockCode());
		StringBuilder sb1 = new StringBuilder();
		sb1.append(bUrl);
		sb1.append(so.getStockCode() + ".html?");
		sb1.append("year=" + yr);
		sb1.append("&season=" + ses);
		return sb1.toString();
	}

	private boolean getDataFrom163Url2(Stock stock, String bUrl) {
		CloseableHttpClient hpc = null;
		CloseableHttpResponse response = null;
		HttpGet httpget = null;
		boolean flg1 = true;
		try {
			hpc = HttpClients.createDefault();
			httpget = new HttpGet(bUrl);
			System.out.println(bUrl);
			response = hpc.execute(httpget);
			HttpEntity he = response.getEntity();
			String s1 = EntityUtils.toString(he);
			List<String> li1 = new ArrayList<String>();
			flg1 = this.handleData(s1, stock, li1);
			CommonDao cd = new CommonDao();
			cd.insertStockDateData3(stock, li1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (httpget != null) {
					httpget.releaseConnection();
				}
				if (response != null) {
					response.close();
				}
				if (hpc != null) {
					hpc.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return flg1;
	}

	private boolean getDataFrom163Url(Stock stock) {
		boolean flg = true;
		CloseableHttpClient hpc = null;
		CloseableHttpResponse response = null;
		HttpGet httpget = null;
		Calendar a = Calendar.getInstance();
		int yr = a.get(Calendar.YEAR);
		int se = a.get(Calendar.MONTH) / 3 + 1;

		label: while (yr >= 2000) {
			while (se > 0) {
				String bUrl = assembleUrl(stock, yr, se);
				flg = this.getDataFrom163Url2(stock, bUrl);
				if (!flg)
					break label;
				se--;
			}
			se = 4;
			yr--;
		}
		return true;
	}

	public boolean setStockDateDataFrom163(List<Stock> li) {
		for (int i = 0; i < li.size(); i++) {
			try {
				for (int j = 0; j < 1; j++) {
					this.getDataFrom163Url(li.get(i));
				}
			} catch (Exception er) {
			}

		}
		return true;
	}

	private boolean handleData(String s, Stock stock, List<String> li) {
		try {
			String s1 = s.replace(" ", "").replace("\t", "").replace("\n", "")
					.replace("\r", "");
			int idx = s1.indexOf("</th></tr></thead>");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-hh");
			if (idx > -1) {
				String s2 = s1.substring(idx, s1.length());
				Pattern p = Pattern
						.compile("<tr[^>]+><td>([^<]+)</td><td[^>]+>([^<]+)</td><td[^>]+>([^<]+)</td><td[^>]+>([^<]+)</td><td[^>]+>([^<]+)</td><td[^>]+>([^<]+)</td><td[^>]+>([^<]+)</td><td>([^<]+)</td><td>([^<]+)</td><td>([^<]+)</td><td>([^<]+)</td></tr>");
				Matcher m = p.matcher(s2);
				String s3 = null;
				while (m.find()) {
					String ddate = m.group(1);
					if (stock.getRecordDate() == null
							|| ddate.compareTo(sdf.format(stock.getRecordDate())) > 0) {
						String ss = ddate + ",a,b," + m.group(2) + ","
								+ m.group(3) + "," + m.group(4) + ","
								+ m.group(5) + ","
								+ m.group(8).replace(",", "") + "00,"
								+ m.group(9).replace(",", "");
						li.add(ss);
						System.out.println(ss);
					} else {
						return false;
					}
				}
			}
		} catch (Exception er) {
			er.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean setStockDateData(List<Stock> li) {
		this.setStockDateDataFrom163(li);
		return false;
	}

}
