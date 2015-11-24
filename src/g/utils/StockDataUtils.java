package g.utils;

import g.bean.Stock;

import java.util.List;

public class StockDataUtils {

	public boolean setStockDateData(List<Stock> li, int xianlu) {
		StockDataInterface sd1 = null;
		if (xianlu == 1) {
			sd1 = new YahooDataUtils();
		} else if (xianlu == 2) {
			sd1 = new SohuDataUtils();
		} else if (xianlu == 3) {
			sd1 = new N163DataUtils();
		} else if (xianlu == 4) {
			sd1 = new N163DataUtils2();
		}
		sd1.setStockDateData(li);
		return true;
	}

}
