package g.utils;

import g.bean.Stock;
import g.dao.CommonDao;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class StockBounsUtils {

	private static final String BURL = "http://vip.stock.finance.sina.com.cn/corp/go.php/vISSUE_ShareBonus/stockid/";

	private String assembleUrl(Stock so) {
		StringBuilder sb1 = new StringBuilder();
		sb1.append(BURL);
		sb1.append(so.getStockCode());
		sb1.append(".phtml");
		return sb1.toString();
	}

	public void setAllBonus(List<Stock> li) {
		for (int i = 0; i < li.size(); i++) {
			Stock stock = li.get(i);
			this.setBonus(stock);
		}
	}

	public boolean setBonus(Stock stock) {
		CloseableHttpClient hpc = null;
		CloseableHttpResponse response = null;
		HttpGet httpget = null;
		String bUrl = assembleUrl(stock);
		boolean flg1 = true;
		try {
			hpc = HttpClients.createDefault();
			httpget = new HttpGet(bUrl);
			response = hpc.execute(httpget);
			HttpEntity he = response.getEntity();
			String s1 = EntityUtils.toString(he, "GBK");
			List<String> li1 = new ArrayList<String>();
			this.handleData(s1, stock, li1);
			CommonDao cdao = new CommonDao();
			cdao.insertStockBonus(stock, li1);
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
		return true;
	}

	private boolean handleData(String s, Stock stock, List<String> li) {
		try {
			String s1 = s.replace(" ", "").replace("\t", "").replace("\n", "")
					.replace("\r", "");
			int idx = s1.indexOf("</td></tr></thead><tbody>");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (idx > -1) {
				String s2 = s1.substring(idx, s1.length());
				Pattern p = Pattern
						.compile("<tr><td>([^<]+)</td><td>([^<]+)</td><td>([^<]+)</td><td>([^<]+)</td><td>([^<]+)</td><td>([^<]+)</td><td>([^<]+)</td><td>([^<]+)</td><td><a[^>]+>[^<]+</a></td></tr>");
				Matcher m = p.matcher(s2);
				while (m.find()) {
					String status = m.group(5);
					if (status.equals("实施")) {
						String rdate = null;

						if (!m.group(6).equals("--")) {
							rdate = m.group(6);
						} else if (!m.group(7).equals("--")) {
							rdate = m.group(7);
						} else {
							rdate = m.group(1);
						}

						if (stock.getRecordDate1() == null
								|| rdate.compareTo(sdf.format(stock
										.getRecordDate1())) > 0) {
							// 时间，分紅，送股，配股，配股金额
							String ss = rdate + "," + m.group(4) + ","
									+ m.group(2) + "," + m.group(3) + ",0";
							li.add(ss);
							System.out.println(ss);
						} else {
							break;
						}
					}
				}
			}
			idx = s1.indexOf("</td></tr></thead><tbody>", idx + 100);
			if (idx > -1) {
				String s2 = s1.substring(idx, s1.length());
				Pattern p = Pattern
						.compile("<tr><td>([^<]+)</td><td>([^<]+)</td><td>([^<]+)</td><td>([^<]+)</td><td>([^<]+)</td><td>([^<]+)</td><td>([^<]+)</td><td>([^<]+)</td><td>([^<]+)</td><td>([^<]*)</td><td><a[^>]+>[^<]+</a></td></tr>");
				Matcher m = p.matcher(s2);
				while (m.find()) {
					String rdate = m.group(5);
					if (stock.getRecordDate1() == null
							|| rdate.compareTo(sdf.format(stock
									.getRecordDate1())) > 0) {
						// 时间，分紅，送股，配股，配股金额
						String ss = rdate + ",0,0," + m.group(2) + ","
								+ m.group(3);
						li.add(ss);
						// System.out.println(ss);
					} else {
						break;
					}
				}
			}
		} catch (Exception er) {
		}
		return true;
	}

}
