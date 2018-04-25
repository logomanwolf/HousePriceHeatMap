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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import dao.HousePriceDao;
import bean.*;

public class LoadMap<E> extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public LoadMap() {
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
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HousePriceDao housePriceDao = new HousePriceDao();
		List<Position> usableHouses = new ArrayList<Position>();
		usableHouses = housePriceDao.selectUsableHouse();
		// String jsonString = JSONArray.toJSONString(usableHouses);
		// PrintWriter out=response.getWriter();
		// out.print("<html><h1>"+jsonString+"</h1></html>");
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

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
