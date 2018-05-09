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

public class SelectBySubway extends HttpServlet{
	public SelectBySubway(){
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
		double lngRange=0.01,latRange=0.004;
		Position p1=new Position("120.22991","30.300329");
		Position p2=new Position("120.219526","30.297102");
		Position p3=new Position("120.214172","30.293251");
		Position p4=new Position("120.213777","30.282133");
		Position p5=new Position("120.214235","30.271997");
		Position p6=new Position("120.220361","30.261781");
		Position p7=new Position("120.221915","30.255776");
		Position p8=new Position("120.217828","30.250902");
		Position p9=new Position("120.213471","30.245653");
		Position p10=new Position("120.204425","30.236683");
		Position p11=new Position("120.196601","30.229475");
		Position p12=new Position("120.184249","30.223");
		Position p13=new Position("120.169715","30.215775");
		Position p14=new Position("120.15863","30.210376");
		Position p15=new Position("120.155369","30.17394");
		Position p16=new Position("120.159708","30.165939");
		Position p17=new Position("120.152822","30.181994");
		List<Position> pos=new ArrayList<Position>();
		pos.add(p1);
		pos.add(p2);
		pos.add(p3);
		pos.add(p4);
		pos.add(p5);
		pos.add(p6);
		pos.add(p7);
		pos.add(p8);
		pos.add(p9);
		pos.add(p10);
		pos.add(p11);
		pos.add(p12);
		pos.add(p13);
		pos.add(p14);
		pos.add(p15);
		pos.add(p16);
		pos.add(p17);
		String sql="SELECT DISTINCT house.lng, house.lat,house.aver FROM house WHERE ";
		for(int i=0;i<pos.size();i++){
			Position p=pos.get(i);
			double minLng=Double.parseDouble(p.getLng())-lngRange;
			double maxLng=Double.parseDouble(p.getLng())+lngRange;
			
			double minLat=Double.parseDouble(p.getLat())-latRange;
			double maxLat=Double.parseDouble(p.getLat())+latRange;
			
			String where="(house.lng>"+String.valueOf(minLng)+" and house.lng<"+String.valueOf(maxLng)
			            +" and house.lat>"+String.valueOf(minLat)+" and house.lat<"+String.valueOf(maxLat)+") or ";
			sql=sql+where;
		}
		if(pos.size()>0) sql=sql+"house.lng<0;";
		else sql="select * from myscheme.house where house.lng<0;";
		
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
		doGet(request, response);
	}
}
