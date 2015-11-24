package g;

import g.bean.Stock;
import g.dao.CommonDao;
import g.utils.StockBounsUtils;
import g.utils.StockDataUtils;

import java.util.List;

public class Program {

	/**
	 * @param args
	 */
	@SuppressWarnings("all")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CommonDao cdo = new CommonDao();
		StockDataUtils sdu = new StockDataUtils();
		StockBounsUtils sbu = new StockBounsUtils();
		List<Stock> li = cdo.getStocks();

		// sdu.setStockDateData(li, 3);
		// sbu.setAllBonus(li);

		cdo.cpyDat2Pre();
		System.out.println("cpyDat2Pre");
		cdo.preFixBonus();
		System.out.println("preFixBonus");
		cdo.setAllDzw4Pre();
		System.out.println("setAllDzw4Pre");

	}

}
