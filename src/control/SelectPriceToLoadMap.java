package control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Position;

import com.alibaba.fastjson.JSONArray;

import dao.HousePriceDao;

public class SelectPriceToLoadMap extends HttpServlet{
	public SelectPriceToLoadMap(){
		super();
	}
	
	public void init() throws ServletException {
		// Put your code here
	}
	
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int minPrice=Integer.parseInt(request.getParameter("minPrice"));
		int maxPrice=Integer.parseInt(request.getParameter("maxPrice"));
		HousePriceDao housePriceDao = new HousePriceDao();
		List<Position> usableHouses = new ArrayList<Position>();
		usableHouses = housePriceDao.selectSuitableHouse(minPrice, maxPrice);

		int num = usableHouses.size();
		System.out.println(usableHouses.size());
		Double lngSum = (double) 0;
		Double latSum = (double) 0;
		Double maxCount = 0d;
		for (Position po : usableHouses) {
			if (Double.parseDouble(po.getCount()) > maxCount)
				maxCount = Double.parseDouble(po.getCount());
			lngSum += Double.parseDouble(po.getLng());
			latSum += Double.parseDouble(po.getLat());
		}
		Double avg_lng = lngSum / num;
		Double avg_lat = latSum / num;
		String jsonString = JSONArray.toJSONString(usableHouses);
		request.getSession().setAttribute("usableHouses", jsonString);
		request.getSession().setAttribute("avg_lng", avg_lng);
		request.getSession().setAttribute("avg_lat", avg_lat);
		request.getSession().setAttribute("maxCount", maxCount);
		RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
		rd.forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
