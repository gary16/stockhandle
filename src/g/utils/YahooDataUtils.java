package g.utils;

import g.bean.Stock;
import g.dao.CommonDao;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class YahooDataUtils implements StockDataInterface {
	@Override
	public boolean setStockDateData(List<Stock> li) {
		return this.setStockDateDataFromYahoo(li);
	}

	private static final String YAHOOURL1 = "http://real-chart.finance.yahoo.com/table.csv?g=d";
	private static final String YAHOOURL2 = "http://table.finance.yahoo.com/table.csv?g=d";
	private static int currUrl = 1;

	private String assembleUrl(Stock so) {
		String bUrl = null;
		if (currUrl == 1) {
			bUrl = YAHOOURL1;
		} else if (currUrl == 2) {
			bUrl = YAHOOURL2;
		}
		System.out.println(so.getStockCode());
		StringBuilder sb1 = new StringBuilder();
		sb1.append(bUrl);
		sb1.append("&s=" + so.getStockCode() + "." + so.getJys());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");// 设置日期格式
		String ddate = sdf.format(so.getRecordDate());
		int mth = Integer.parseInt(ddate.substring(5, 7)) - 1;
		if (!so.getRecordDate().equals("0")) {
			sb1.append("&a=" + String.format("%02d", mth));
			sb1.append("&b=" + ddate.substring(8, 10));
			sb1.append("&c=" + ddate.substring(0, 4));
		}
		return sb1.toString();
	}

	public boolean getDataFromYahooUrl(Stock stock) {
		boolean flg = true;
		CloseableHttpClient hpc = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String bUrl = assembleUrl(stock);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			HttpGet httpget = new HttpGet(bUrl);
			response = hpc.execute(httpget);
			HttpEntity he = response.getEntity();
			String s1 = EntityUtils.toString(he);
			String sarr[] = s1.split("\n");
			int idx = sarr.length;
			if (!stock.getRecordDate().equals("0")) {
				idx = 0;
				for (int i = 0; i < sarr.length; i++) {
					if (sarr[i].substring(0, 10).compareTo(
							sdf.format(stock.getRecordDate())) <= 0) {
						break;
					}
					idx++;
				}
			}
			CommonDao cd = new CommonDao();
			cd.insertStockDateData(stock, sarr, idx);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(bUrl);
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

	public boolean setStockDateDataFromYahoo(List<Stock> li) {
		for (int i = 0; i < li.size(); i++) {
			Stock s = li.get(i);
			try {
				int cnt = 0;
				while (!getDataFromYahooUrl(s) && cnt < 4) {
					Thread.sleep(30000);
					currUrl = currUrl % 2 + 1;
					cnt++;
				}
			} catch (Exception er) {
			}

		}
		return true;
	}

}
