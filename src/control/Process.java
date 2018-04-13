package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.House;
import dao.HousePriceDao;

public class Process extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public Process() {
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

		doPost(request, response);

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

		response.setContentType("text/html");
		HousePriceDao housePriceDao = new HousePriceDao();
		String lng = request.getParameter("lng");
		String lat = request.getParameter("lat");
		String[] lngs = lng.split("#");
		String[] lats = lat.split("#");
		List<House> houses = (ArrayList<House>)request.getSession().getAttribute("houses");
		int i=0;
		System.out.println(houses.size());
		System.out.println(lngs.length);
		for (House house:houses) {	
			house.setLng(Double.parseDouble(lngs[i]));
			house.setLat(Double.parseDouble(lats[i]));
			i++;
		}
		Integer updateNum=housePriceDao.updateHouses(houses);
		PrintWriter out=response.getWriter();
		out.print(updateNum);
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
