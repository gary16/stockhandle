package g.utils;

import g.bean.Stock;
import g.dao.CommonDao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class SohuDataUtils implements StockDataInterface {
	@Override
	public boolean setStockDateData(List<Stock> li) {
		return this.setStockDateDataFromSohu(li);
	}

	private static final String SOHUURL1 = "http://q.stock.sohu.com/hisHq?stat=0&order=D&period=d&rt=jsonp";
	private static int currUrl = 1;

	private String assembleUrl(Stock so) {
		String bUrl = null;
		if (currUrl == 1) {
			bUrl = SOHUURL1;
		} else if (currUrl == 2) {
		}
		System.out.println(so.getStockCode());
		StringBuilder sb1 = new StringBuilder();
		sb1.append(bUrl);
		sb1.append("&code=cn_" + so.getStockCode());
		// sb1.append("&code=cn_600318");
		if (so.getRecordDate() != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");// 设置日期格式
			sb1.append("&start=" + df.format(so.getRecordDate()));
			String endstr = df.format(new Date());
			sb1.append("&end=" + endstr);
		} else {
			sb1.append("&start=19900101");
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");// 设置日期格式
			String endstr = df.format(new Date());
			sb1.append("&end=" + endstr);
		}
		return sb1.toString();
	}

	private boolean getDataFromSohuUrl(Stock stock) {
		boolean flg = true;
		CloseableHttpClient hpc = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String bUrl = assembleUrl(stock);
		String ss = null;
		try {

			HttpGet httpget = new HttpGet(bUrl);
			response = hpc.execute(httpget);
			HttpEntity he = response.getEntity();
			String s1 = EntityUtils.toString(he);
			List<String> li1 = new ArrayList<String>();
			this.handleData(s1, stock, li1);
			CommonDao cd = new CommonDao();
			cd.insertStockDateData2(stock, li1);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(ss);
			flg = false;
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				if (hpc != null) {
					hpc.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				flg = false;
			}
		}
		return flg;
	}

	public boolean setStockDateDataFromSohu(List<Stock> li) {
		for (int i = 0; i < li.size(); i++) {
			Stock s = li.get(i);
			try {
				int cnt = 0;
				while (!getDataFromSohuUrl(s) && cnt < 4) {
					Thread.sleep(30000);
					cnt++;
				}
			} catch (Exception er) {
			}

		}
		return true;
	}

	private boolean handleData(String s, Stock stock, List<String> li) {
		int idx1 = s.indexOf(":[");
		idx1 += 3;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			while (true) {

				int idx2 = s.indexOf("]", idx1);
				String s1 = s.substring(idx1, idx2);
				idx2 += 1;
				String s2 = s1.replace("\"", "");
				if (s2.substring(0, 10).compareTo(
						sdf.format(stock.getRecordDate())) <= 0) {
					break;
				}
				li.add(s2);
				if (s.substring(idx2, idx2 + 1).equals("]")) {
					break;
				} else {
					idx1 = idx2 + 2;
				}
			}
		} catch (Exception er) {
			er.printStackTrace();
		}
		return true;
	}
}
