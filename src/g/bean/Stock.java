package g.bean;

import java.util.Date;

public class Stock {

	private String stockCode;
	private String stockName;
	private String jys;
	private Date recordDate;
	private Date recordDate1;

	public String getJys() {
		return jys;
	}

	public void setJys(String jys) {
		this.jys = jys;
	}

	public String getStockCode() {
		return stockCode;
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public Date getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	public Date getRecordDate1() {
		return recordDate1;
	}

	public void setRecordDate1(Date recordDate1) {
		this.recordDate1 = recordDate1;
	}

}
