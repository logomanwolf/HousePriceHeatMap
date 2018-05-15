package control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

//用于选择地铁附近的点坐标
public class SelectBySubway extends HttpServlet {
	public SelectBySubway() {
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
		double lngRange = 0.01, latRange = 0.004;

		List<Position> pos = new ArrayList<Position>();
		pos = new HousePriceDao().selectSubways();
		System.out.println(pos.size());
		String sql = "SELECT DISTINCT house.lng, house.lat,house.aver FROM house WHERE ";
		for (int i = 0; i < pos.size(); i++) {
			Position p = pos.get(i);
			double minLng = Double.parseDouble(p.getLng()) - lngRange;
			double maxLng = Double.parseDouble(p.getLng()) + lngRange;
			double minLat = Double.parseDouble(p.getLat()) - latRange;
			double maxLat = Double.parseDouble(p.getLat()) + latRange;
			String where = "(house.lng>" + String.valueOf(minLng)
					+ " and house.lng<" + String.valueOf(maxLng)
					+ " and house.lat>" + String.valueOf(minLat)
					+ " and house.lat<" + String.valueOf(maxLat) + ") or ";
			sql = sql + where;
		}
		if (pos.size() > 0)
			sql = sql + "house.lng<0;";
		else
			sql = "select * from myscheme.house where house.lng<0;";

		System.out.println(sql);

		HousePriceDao housePriceDao = new HousePriceDao();
		List<Position> usableHouses = new ArrayList<Position>();
		usableHouses = housePriceDao.selectHouseBySubway(sql);

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
		double lngRange = 0.01, latRange = 0.004;

		List<Position> pos = new ArrayList<Position>();
		pos = new HousePriceDao().selectSubways();
		System.out.println(pos.size());
		String sql = "SELECT DISTINCT house.lng, house.lat,house.aver FROM house WHERE ";
		for (int i = 0; i < pos.size(); i++) {
			Position p = pos.get(i);
			double minLng = Double.parseDouble(p.getLng()) - lngRange;
			double maxLng = Double.parseDouble(p.getLng()) + lngRange;
			double minLat = Double.parseDouble(p.getLat()) - latRange;
			double maxLat = Double.parseDouble(p.getLat()) + latRange;
			String where = "(house.lng>" + String.valueOf(minLng)
					+ " and house.lng<" + String.valueOf(maxLng)
					+ " and house.lat>" + String.valueOf(minLat)
					+ " and house.lat<" + String.valueOf(maxLat) + ") or ";
			sql = sql + where;
		}
		if (pos.size() > 0)
			sql = sql + "house.lng<0;";
		else
			sql = "select * from myscheme.house where house.lng<0;";

		System.out.println(sql);

		HousePriceDao housePriceDao = new HousePriceDao();
		List<Position> housesAroundSubway = new ArrayList<Position>();
		housesAroundSubway = housePriceDao.selectHouseBySubway(sql);
		System.out.println(housesAroundSubway.size());

		request.setCharacterEncoding("utf-8");
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(
					request.getInputStream(), "UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		String line = null;
		StringBuilder sb = new StringBuilder();
		try {
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 将资料解码
		String usableHousestr = sb.toString();

		System.out.println(usableHousestr);
		// 从前端获得的区间数据
		List<Position> usableHouses = (List<Position>) JSONArray.parseArray(
				usableHousestr, Position.class);
		// 拷贝区间数据
		List<Position> copyHousesAroundSubway = new ArrayList<Position>();
		for (Position copy : usableHouses) {
			boolean flag = false;
			for (Position po : housesAroundSubway) {
				if (copy.getLng().equals(po.getLng())
						&& copy.getLat().equals(po.getLat())) {
					flag = true;
	                copyHousesAroundSubway.add(copy);
					break;
				}
			}
		}
		String jsonString = JSONArray.toJSONString(copyHousesAroundSubway);
		System.out.println("地铁沿线点"+jsonString);
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.println(jsonString);
		out.close();

	}
}
