package control;

import java.io.IOException;
import java.io.PrintWriter;
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

public class Dynamic extends HttpServlet {

	/**
	 * 用于实时更新滑块设置的区间
	 */
	public Dynamic() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int minPrice=Integer.parseInt(request.getParameter("minPrice"));
		System.out.println(minPrice);
		int maxPrice=Integer.parseInt(request.getParameter("maxPrice"));
		System.out.println(maxPrice);
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
		request.setAttribute("avg_lng", avg_lng);
		System.out.println("avg_lng"+avg_lng);
		request.getSession().setAttribute("avg_lat", avg_lat);
		request.getSession().setAttribute("maxCount", maxCount);
		response.setCharacterEncoding("UTF-8");
		PrintWriter out=response.getWriter();
        out.println(jsonString);
        out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
